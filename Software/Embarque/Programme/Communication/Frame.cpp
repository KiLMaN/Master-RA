#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>

#include "../Def.h"
#include "Frame.h"
#include "../Tools/Tools.h"

#define DEBUG 1


Frame::Frame(char *receiveFrame, int sizeRead)
{
    //ctor
    this->_receiveFrame = receiveFrame;
    this->_message = NULL;
    this->_sizeRead = sizeRead;
}

Frame::~Frame()
{
    free(this->_receiveFrame);
}

bool Frame::verifyFormat(){

    // Verify frame
    if(_sizeRead < 3 ){
        #ifdef DEBUG
            printf("Error on read frame\n");
            fflush(stdout);
        #endif
        return false;
    }
    unsigned short CRC = calculateCRC16((char*) this->_receiveFrame, _sizeRead-2);
    unsigned short CRCRead = (unsigned char) this->_receiveFrame[_sizeRead-2]<<8 | (unsigned char) this->_receiveFrame[_sizeRead-1];
    if(CRC != CRCRead){
        #ifdef DEBUG
            printf("CRC error on read frame\n");
            fflush(stdout);
        #endif
        return false;
    }
    
    // Calculate cry
    // Remove Start and stop code
    if( this->_message != NULL)
        free(this->_message);

    _message = (char * ) malloc(_sizeRead);
    memcpy(_message,&_receiveFrame,_sizeRead-1);
    
    return true;
}

netMessage* Frame::prepareMessage(char message[],int size){
    if(strlen(message) == 0)
        return NULL;

    netMessage *m = (netMessage *) malloc(sizeof(netMessage));
    m->size = size+2;

    // Initialise buffer
    m->msg = (char *) malloc(size+3);
    bzero(m->msg,size+3);

    memcpy(m->msg,message,size);

    // Set CRC16
   // m->msg[0] = TCP_START_CODE;
   // m->msg[size+1] = TCP_STOP_CODE;
    m->msg[size] = '\0';

    return m;
}


netMessage* Frame::decodeFrame(bool *authorizedClient,int* numberConnectedclient, AccelStepper *stepper, char *ip,  bool* activeVideo){
    switch(_receiveFrame[0]){
        case (char) 0xCC:{
            #ifdef DEBUG
                printf("Connect\n");
                fflush(stdout);
            #endif
            *(authorizedClient) = true;
            char answer[] = {(char) 0xCD};
            return prepareMessage(answer, 1);
            break;
        }
       
        case (char) 0xDC:{
            #ifdef DEBUG
                printf("Disonnect\n");
                fflush(stdout);
            #endif
            *(authorizedClient) = false;
            if( *(activeVideo) == true ){
              	  system((char *) "killall -9 raspivid");
			  	  char answer[] = {(char) 0x91};
			  	  *(activeVideo) = false;
			 
			}
            char answer[] = {(char) 0xDD};
            return prepareMessage(answer, 1);
            break;
        }
        
        case (char) 0x80:{
            if(*authorizedClient == true){
                #ifdef DEBUG
                    printf("Open video\n");
                    fflush(stdout);
                #endif

				if ( *activeVideo == false ){
				
                	char command[200];
                	
                	sprintf(command,"raspivid -t 0 -h %i -w %i -fps 25 -hf -n  -o - | gst-launch-0.10 -v fdsrc ! h264parse ! rtph264pay config-interval=1 pt=96 ! gdppay ! tcpserversink host=%s port=%i &", GST_HEIGHT, GST_WIDTH,ip ,GST_PORT);
                	system(command);
                	char answer[] = {(char) 0x81};
                	printf("%s\n",command);
                	*(activeVideo) = true;
                	return prepareMessage(answer, 1);
                }
            }
            char answer[] = {(char) 0x81,0x00};
            return prepareMessage(answer, 2);
            
            break;
        }
        
        case (char) 0x90:{
            if( *(authorizedClient) == true){
                #ifdef DEBUG
                    printf("Close video\n");
                    fflush(stdout);
                #endif
                
                if( *(activeVideo) == true ){
              	  system((char *) "killall -9 raspivid");
			  	  char answer[] = {(char) 0x91};
			  	  *(activeVideo) = false;
			  	  return prepareMessage(answer, 1);
			  	}
			  	
            }
            char answer[] = {(char) 0x91, 0x00};
            return prepareMessage(answer, 2);
            break;
        }

        case (char) 0xA0:{
            if( *authorizedClient == true ){
		long moveSize = (_receiveFrame[2] == 0x00)?_receiveFrame[3]:-_receiveFrame[3];

                #ifdef DEBUG
                    printf("Move motor %i Size: %d\n",_receiveFrame[1],moveSize);
                    fflush(stdout);
                #endif
                
                if( _receiveFrame[1] == 0x00){
                	if ( moveSize < SERVO_MIN_POS)
                		moveSize = SERVO_MIN_POS;
                	else if( moveSize > SERVO_MAX_POS)
                		moveSize = SERVO_MAX_POS;
                		
                	char command[120];
					sprintf(command,"echo %i=%i%% > /dev/servoblaster",SERVO_BLASTER_ID,moveSize);
					printf("%s\n",command);
					fflush(stdout);
					system(command);
					}
                else if (_receiveFrame[1] == 0x80 ){
                	stepper->move(moveSize*STEPPER_STEPS);
                }
            }
        }
        default:{
            char answer[] = {0x00, 0x00};
            return prepareMessage(answer,2);
            break;
        }
    }
    return NULL;
}
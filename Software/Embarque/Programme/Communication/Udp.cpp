#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <pthread.h>
#include <sys/ioctl.h>

#include "Udp.h"

#define BUFFER_SIZE 256
#define DEBUG 1

#define UDP_PORT 6783

int _continueUdp = 0;
//bool _isWorkingUdp = false;
int sockfdU = 0;
pthread_t ReceiveUdpMsg;


void openUdpSocket();
void *receiveUdpMessage(void* temp);


int isUdpReceiving() { return _continueUdp; }


int startUdpReception(){
    _continueUdp = 1;
    pthread_create(&ReceiveUdpMsg,NULL,receiveUdpMessage, NULL );
    return 1;
}

int stopUdpReception(){
    if(_continueUdp == 0) // Udp server not running
        return 1;

    _continueUdp = 0; // Set the continue flag to 0 for indeicate end

	#ifdef DEBUG
		printf("Udp: Stop server \n");
        fflush(stdout);
    #endif
   // while ( _isWorkingUdp == true ) sleep(1);
    pthread_cancel(ReceiveUdpMsg);
    return 1;
}

void openUdpSocket(){
    struct sockaddr_in serv_addr;

	/*** Network configuration ***/
    sockfdU = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sockfdU == 0)
       return;

    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(UDP_PORT);

    if (bind((int)sockfdU, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0){
        #ifdef DEBUG
            printf("Udp: ERROR on binding port: %i \n", UDP_PORT);
            fflush(stdout);
        #endif
        sockfdU = 0;
       return;
    }
    #ifdef DEBUG
        printf("Udp: Open successful port: %i\n",UDP_PORT);
        fflush(stdout);
    #endif
}

void * receiveUdpMessage(void * temp){
    do{
        openUdpSocket();
        sleep(1);
    }while(sockfdU == 0);


    char buff[BUFFER_SIZE];
	bzero(buff,BUFFER_SIZE);

	struct sockaddr_in cli_addr;
	socklen_t clilen;
	clilen = sizeof(cli_addr);
	listen((int)sockfdU,5);

   // unsigned short CRCRead = calculateCRC16((char*) 0xD0, 1);
   // unsigned short CRCSend = calculateCRC16((char*) 0xD1, 1);
	while(_continueUdp == 1){
	//    _isWorkingUdp = false;
	    int n = recvfrom(sockfdU,buff,sizeof(buff),0,(sockaddr *)&cli_addr,&clilen);
	    if(n == 3 ){
	//        _isWorkingUdp = true;
	        if( buff[0] == (char) 0xD0 /*&& buff[1] == CRCRead>>8 && buff[2]==CRCRead & 0xFF */ ){ // Auto detect frame
	            	#ifdef DEBUG
                        printf("Auto detect frame\n");
                        fflush(stdout);
                    #endif
                char answer[] = {(char) 0xD1, 0x00, 0x00}; // Remplace 0x22 et 0x33 par crc
                int aa = sendto(sockfdU, answer, sizeof(answer), 0, (struct sockaddr*)&cli_addr, clilen); // Send the answer
                printf("Send to: %i\n",aa);
                fflush(stdout);
            }
        }
    }
	close(sockfdU);
	return NULL;
}

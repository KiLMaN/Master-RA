#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <net/if.h>
#include <arpa/inet.h>

#include <pthread.h>
#include "Frame.h"
#include "../Def.h"
#include "Tcp.h"


#define DEBUG 1

struct connectHandler{
    int* connectedClient;
    AccelStepper *stepper;
    int  sock;
};

int	sockfd;


int _continueTcp = 0;
bool isWorking = false;
pthread_t ReceiveTcpMsg;

void openTcpSocket();
void *receiveTcpMessage(void *temp);
void *connection_handler(void *socket_desc);


/// Open closed functions
int isTcpReceiving() {
    return _continueTcp;
}

int startTcpReception(AccelStepper *stepper){
    _continueTcp = 1;

    // Create thread for TCP reception
    pthread_create(&ReceiveTcpMsg,NULL,receiveTcpMessage,(void *)stepper);
    return 1;
}

int stopTcpReception(){
    if(_continueTcp == 0) // TCP server not running
        return 1;
    _continueTcp = 0; // Set the continue flag to 0 for indicate end


    while( isWorking == true ) usleep(100000);

    pthread_cancel(ReceiveTcpMsg);

    #ifdef DEBUG
        printf("TCP: Stop server \n");
        fflush(stdout);
    #endif
    return 1;
}

void openTcpSocket(){
    int port = TCP_PORT_DEFAULT;
	struct sockaddr_in	serv_addr;


	// Open a TCP socket
	if ( (sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
	    #ifdef DEBUG
            printf("Tcp: Can't open stream socket\n");
            fflush(stdout);
        #endif
	}
	// Bind our local address so that the clien can send to us
	bzero((char *) &serv_addr, sizeof(serv_addr));
	serv_addr.sin_family		= AF_INET;
	serv_addr.sin_addr.s_addr	= htonl(INADDR_ANY);
	serv_addr.sin_port		= htons(port);

	if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0){
        #ifdef DEBUG
            printf("Tcp: Can't bind local address: Port:%i \n",port);
            fflush(stdout);
        #endif
        close(sockfd);
        sockfd = 0;
    }
	#ifdef DEBUG
        printf("Tcp: Open socket OK: Port: %i\n",port);
        fflush(stdout);
    #endif
}



/// Recieve functions
void *receiveTcpMessage(void* temp){
	AccelStepper *stepper = (AccelStepper *) temp;
	if ( stepper == NULL )
		return NULL;


    openTcpSocket(); // Open socket for Tcp connection
    while( sockfd == 0){
        sleep(1);
        openTcpSocket();
    }

    if( sockfd == 0) // Error on open socket
        return NULL;

    int newsockfd;
    struct sockaddr_in	client;
	listen(sockfd, 5);

	int _connectedClient = 0;

	int c = sizeof(struct sockaddr_in);


	while(_continueTcp){

		while( _continueTcp == 1 && (newsockfd = accept(sockfd, (struct sockaddr *)&client, (socklen_t*)&c)) ){
		    connectHandler *ch =  (connectHandler *) malloc(sizeof(connectHandler));
            ch->connectedClient = &_connectedClient;
            ch->sock = newsockfd;
            ch->stepper = stepper;
            connection_handler((void *) ch);
		}
	}
	return NULL;
}

char* getIp(int newfd) {
    struct sockaddr_in addr;
    socklen_t addr_size = sizeof(struct sockaddr_in);
    int res = getpeername(newfd, (struct sockaddr *)&addr, &addr_size);
    char *clientip = new char[20];
    strcpy(clientip, inet_ntoa(addr.sin_addr));
    
    return clientip;
}

void *connection_handler(void *tCh)
{
	bool authorizedClient = false;
	bool activeVideo = false;
	
	#ifdef DEBUG
		printf("Tcp: Client connection\n");
		fflush(stdout);
	#endif


    //Get the socket descriptor
    connectHandler *ch = (connectHandler *) tCh;
    char *ip = getIp(ch->sock);
   
    
    fflush(stdout);
    int sock = ch->sock;
    int read_size;
    char client_message[2000];

    //Receive a message from client
    while( _continueTcp && (read_size = (int)read(sock , client_message , 2000)) > 0 ){
		 if(strlen(client_message) > 0){                        // If data recieve
             isWorking = true;
            Frame *f = new Frame(client_message, read_size);
            if(f->verifyFormat() == true){
                netMessage* m = f->decodeFrame(&authorizedClient, ch->connectedClient, ch->stepper, ip , &activeVideo);
                if(m != NULL && m->size > 0){
                     if(write(sock,m->msg,m->size) < 0){
						#ifdef DEBUG
							printf("Error on send message: %s\n",m->msg);
							fflush(stdout);
						#endif
					}
				}
			}

		//clear the message buffer
		memset(client_message, 0, 2000);
        isWorking = false;
        }
    }

    // On TCP connexion closed
    if( authorizedClient == true && *(ch->connectedClient) > 0){
        *ch->connectedClient = *ch->connectedClient - 1;
    }
    if ( activeVideo == true ){
    	system((char *) "killall -9 raspivid");
    }
    isWorking = false;
    
    return NULL;
}

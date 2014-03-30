#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <net/if.h>

#include <pthread.h>

#include "Frame.h"

#include "Def.h"
#include "Tcp.h"

#define TIME_OUT 10
#define DEBUG 1


struct connectHandler{
    int* connectedClient;
    int  sock;
};

int _continueTcp = 0;
pthread_t ReceiveTcpMsg;

int openTcpSocket();
void *receiveTcpMessage(void *temp);
void *connection_handler(void *socket_desc);


/// Open closed functions
int isTcpReceiving() {
    return _continueTcp;
}

int startTcpReception(){
    _continueTcp = 1;
    
    // Create thread for TCP reception
    pthread_create(&ReceiveTcpMsg,NULL,receiveTcpMessage,NULL);
    return 1;
}

int stopTcpReception(){
    if(_continueTcp == 0) // TCP server not running
        return 1;
    _continueTcp = 0; // Set the continue flag to 0 for indicate end

	#ifdef DEBUG
		printf("TCP: Stop server \n");
        	fflush(stdout);
    	#endif
    void * ret;
    pthread_join(ReceiveTcpMsg,&ret); // Wait the end of this thread
    return 1;
}

int openTcpSocket(){
    int	sockfd, port = TCP_PORT_DEFAULT;
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
    }
	#ifdef DEBUG
        printf("Tcp: Open socket OK: Port: %i\n",port);
        fflush(stdout);
    #endif
	return sockfd;
}



/// Recieve functions
void *receiveTcpMessage(void* temp){
    int sockfd = openTcpSocket(); // Open socket for Tcp connection

    if( sockfd == 0) // Error on open socket
        return NULL;

    int newsockfd;
    struct sockaddr_in	client;
	listen(sockfd, 5);

	int _connectedClient = 0;

//	pthread_t thread_id;
	int c = sizeof(struct sockaddr_in);


	while(_continueTcp){

		while( _continueTcp == 1 && (newsockfd = accept(sockfd, (struct sockaddr *)&client, (socklen_t*)&c)) ){
		    connectHandler *ch =  (connectHandler *) malloc(sizeof(connectHandler));
            ch->connectedClient = &_connectedClient;
            ch->sock = newsockfd;
            connection_handler((void *) ch);
			//pthread_create( &thread_id , NULL ,  connection_handler , (void*)ch);
		}
	}
	return NULL;
}

void *connection_handler(void *tCh)
{
    // time out definition
    struct timeval tv;
    tv.tv_sec = TIME_OUT;
    tv.tv_usec = 0;

	bool authorizedClient = false;

	#ifdef DEBUG
		printf("Tcp: Client connection\n");
		fflush(stdout);
	#endif


    //Get the socket descriptor
    connectHandler *ch = (connectHandler *) tCh;
    int sock = ch->sock;
    int read_size;
    char client_message[2000];


	setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, (char *)&tv,sizeof(struct timeval) ); // SET Time OUT


    //Receive a message from client
    while( _continueTcp && (read_size = (int)read(sock , client_message , 2000)) > 0 )
    {
		 if(strlen(client_message) > 0){                        // If data recieve
            Frame *f = new Frame(client_message, read_size);
            if(f->verifyFormat() == true){
                netMessage* m = f->decodeFrame(&authorizedClient, ch->connectedClient);
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
        }
    }

    // On TCP connexion closed
    if( authorizedClient == true && *(ch->connectedClient) > 0){
        *ch->connectedClient = *ch->connectedClient - 1;
    }

    return NULL;
}

#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>

#include "Tcp.h"
//#include "Udp.h"
#include "Network.h"

#define DEBUG 1

Network::Network()
{

}

Network::~Network()
{
   // stopUdpServeur();
    stopTcpServeur();

}

// UDP managment
/*int Network::startUdpServeur(){
	return startUdpReception(this);
}
int Network::stopUdpServeur(){
	return stopUdpReception();
}
int Network::isUdpRunning(){
	return isUdpReceiving() ;
}*/


// TCP managment
int Network::startTcpServeur(){
    return startTcpReception();
}
int Network::stopTcpServeur(){
    return stopTcpReception();
}
int Network::isTcpRunning(){
    return isTcpReceiving();
}
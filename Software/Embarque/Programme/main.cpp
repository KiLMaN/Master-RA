//
//  main.cpp
//  Main
//
//  Created by Maxime Leblanc on 02/04/2014.
//  Copyright (c) 2014 Master 1 RA. All rights reserved.
//

#include <iostream>
#include <time.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <math.h>
#include <wiringPi.h>
#include <inttypes.h>
#include <stdbool.h>

#include <pthread.h>


#include "Communication/Network.h"
#include "Tools/Tools.h"
#include "Stepper/AccelStepper.h"

#include "Def.h"

#define DEBUG 1

/** Prototypes **/
bool initializeStepper();
void* stepperThread(void *data);

int main(int argc, const char * argv[]){

    // Initialisation
    system("echo 10 > /proc/sys/net/ipv4/tcp_fin_timeout"); // Reset time out Tcp Verifier fichier

    // Init Stepper
    initializeStepper();
    AccelStepper *stepper = new AccelStepper( AccelStepper::FULL4WIRE, IN1, IN2, IN3, IN4, true);
    
    stepper->setMaxSpeed(5000);

    stepper->setAcceleration(2000);
    pthread_t t_stepperThread;
    pthread_create(&t_stepperThread,NULL,stepperThread,(void *)stepper);

    Network *network = new Network();

    // Start Tcp Server
    network->startTcpServeur(stepper);
    network->startUdpServeur();

	int _continue = 1;
    while( _continue == 1){
        sleep(2);
        char a[10];
        scanf("%s",&a);
        _continue = 0;
    }
    
    stepper->disableOutputs();

    network->stopTcpServeur();
    network->stopUdpServeur();

    return 0;
}

void *stepperThread(void *data){
	AccelStepper *stepper = (AccelStepper *) data;
	if ( stepper == NULL)
		return NULL;

	while(1){
		stepper->run();
		usleep(STEPPER_RUN_SLEEP);
	}
	return NULL;
}
bool initializeStepper(){
    if ( wiringPiSetup () == -1 ){
        #ifdef DEBUG
            fprintf (stdout, "oops: %s\n", strerror (errno)) ;
        #endif
		return false;
	}
    pinMode(IN1,OUTPUT);
    pinMode(IN2,OUTPUT);
    pinMode(IN3,OUTPUT);
    pinMode(IN4,OUTPUT);

    return true;
}



//
//  main.cpp
//  RaspberryComm
//
//  Created by Maxime Leblanc on 26/03/2014.
//  Copyright (c) 2014 Maxime Leblanc. All rights reserved.
//

#include <time.h>
#include <iostream>
#include <string>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include "Network.h"

int main(int argc, const char * argv[])
{

    Network *network = new Network();
    network->startTcpServeur();
    
    while(1 == 1)
        usleep(20000);
    
    // insert code here...
    std::cout << "Hello, World!\n";
    return 0;
}


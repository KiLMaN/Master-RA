#ifndef TCP_H
#define TCP_H

#include "../Stepper/AccelStepper.h"

int startTcpReception(AccelStepper* stepper);
int stopTcpReception();
int isTcpReceiving();

int loadTcpPort();
int saveTcpPort(int port);

#endif // TCP_H

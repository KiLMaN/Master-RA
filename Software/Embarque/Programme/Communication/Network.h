#ifndef NETWORK_H
#define NETWORK_H
#include "../Stepper/AccelStepper.h"

class Network
{
    public:
        Network();
        virtual ~Network();

        // UDP
        int startUdpServeur();
        int stopUdpServeur();
        int isUdpRunning();

        //Tcp
        int startTcpServeur(AccelStepper *stepper);
        int stopTcpServeur();
        int isTcpRunning();

    protected:
    private:

};



#endif // NETWORK_H

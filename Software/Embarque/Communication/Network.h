#ifndef NETWORK_H
#define NETWORK_H

class Network
{
    public:
        Network();
        virtual ~Network();

        // UDP
       /* int startUdpServeur();
        int stopUdpServeur();
        int isUdpRunning();
        */
        //Tcp
        int startTcpServeur();
        int stopTcpServeur();
        int isTcpRunning();

    protected:
    private:

};



#endif // NETWORK_H

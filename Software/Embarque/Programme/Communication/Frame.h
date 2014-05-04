#ifndef FRAME_H
#define FRAME_H

#include "../Stepper/AccelStepper.h"

struct netMessage{
    char* msg;
    int   size;
    char IP_ADDR[20];
};

class Frame
{
    public:
        Frame(char * receiveFrame, int sizeRead );
        virtual ~Frame();

        bool verifyFormat();

        netMessage* decodeFrame(bool *authorizedClient, int* numberConnectedclient, AccelStepper *stepper, char *ip, bool* activeVideo);
    protected:
    private:
        netMessage* prepareMessage(char message[], int size);
        int   _sizeRead;
        char  *_receiveFrame;
        char  *_message;
};
#endif // FRAME_H

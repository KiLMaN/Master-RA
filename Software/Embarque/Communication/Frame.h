#ifndef FRAME_H
#define FRAME_H

struct netMessage{
    char* msg;
    int   size;
};

class Frame
{
    public:
        Frame(char * receiveFrame, int sizeRead );
        virtual ~Frame();

        bool verifyFormat();

        netMessage* decodeFrame(bool *authorizedClient, int* numberConnectedclient);
    protected:
    private:
        netMessage* prepareMessage(char message[], int size);
        int   _sizeRead;
        char  *_receiveFrame;
        char  *_message;
};
#endif // FRAME_H

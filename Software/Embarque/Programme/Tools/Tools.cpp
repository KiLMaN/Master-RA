//
//  Tools.cpp
//  RaspberryComm
//
//  Created by Maxime Leblanc on 26/03/2014.
//  Copyright (c) 2014 Maxime Leblanc. All rights reserved.
//

#include "Tools.h"
#include <stdint.h>

unsigned short calculateCRC16(char *data, unsigned short length)
{
    uint16_t CRC = 0x0000;
    uint16_t A = 0x0000, B = 0x0000;
    uint8_t index = 0, Counter = 0;
    
    for( index = 0; index < length; index++ )
    {
        uint8_t _byte = data[index];
        
        A = CRC / 256;
        A = A ^ _byte;
        A = A * 256;
        
        B = CRC & 0xFF;
        CRC = A | B;
        
        for( Counter = 0; Counter < 8; Counter++ ){
            if( (CRC & 0x8000) > 0)
            {
                CRC = CRC * 2;
                CRC = CRC ^ 0x8005;
            }
            else
            {
                CRC = CRC * 2;
            }
        }
    }
    return CRC;
}
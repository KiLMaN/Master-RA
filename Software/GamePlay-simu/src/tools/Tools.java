package tools;

public class Tools {
	
    
	public static char calc_crc16(byte[] data, int length)
    {
	  char CRC = 0x0000;
	  char A = 0, B = 0;
	  char index = 0, Counter = 0;

      for( index = 0; index < length; index++ )
      {
        byte _byte = data[index];

        A = (char)(CRC / 256);
        A = (char)(A ^ _byte);
        A = (char)(A * 256);

        B = (char)(CRC & 0xFF);
        CRC = (char)(A | B);

        for( Counter = 0; Counter < 8; Counter++ )
        {
          if ((char)(CRC & 0x8000) > (short)(0x0))
          {
            CRC = (char)(CRC * 2);
            CRC = (char)(CRC ^ 0x8005);
          }
          else
          {
              CRC = (char)(CRC * 2);
          }
        }
      }
      return CRC;
    }
}

INSTALLATION ON A RASPBERRY PI
==============================


1. Go to gpio/PiBits-master/ServoBlaster/user
2. Install Servoblaster using : make install
3. Copy servoblaster into /etc/init.d/ folder
4. Go to gpio/WiringPi-master/wiringPi
5. Install wiringPi using : make install

Now you can use the source code to build your own version of the software : make clean all
and then you can launch the software using : sudo bin/main
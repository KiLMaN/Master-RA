/*
 * servo.c:
 *	Test of the softServo code.
 *	Do not use this code - use the servoBlaster kernel module instead
 *
 * Copyright (c) 2012-2013 Gordon Henderson. <projects@drogon.net>
 ***********************************************************************
 * This file is part of wiringPi:
 *	https://projects.drogon.net/raspberry-pi/wiringpi/
 *
 *    wiringPi is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    wiringPi is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with wiringPi.  If not, see <http://www.gnu.org/licenses/>.
 ***********************************************************************
 */
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>

#include <wiringPi.h>

#define IN1			2
#define IN2			3
#define IN3			4	
#define IN4			5
#define SERVO_BLASTER_ID	0

#define TIME			1000	

#define STEPPER_STEPS 		48	

#define STEP_Bn 		0x08
#define STEP_Bp 		0x04
#define STEP_An 		0x02
#define STEP_Ap 		0x01


void writeServo(int ServoValue)
{
	char command[120];

	sprintf(command,"echo %i=%i%% > /dev/servoblaster",SERVO_BLASTER_ID,ServoValue);
	system(command);
}
void afficheBinaire(unsigned n,int taille)
{
	unsigned bit = 0;
	unsigned mask = 1;
	int i =0;	
	for(i = 0 ; i < taille ; i++)
	{
		bit = (n& mask) >> i;
		printf("%d",bit);
		mask <<= 1;
	}

}
void writeStepper(int StepValue)
{
	digitalWrite(IN1,(StepValue & 0x08));
	digitalWrite(IN2,(StepValue & 0x04));
	digitalWrite(IN3,(StepValue & 0x02));
	digitalWrite(IN4,(StepValue & 0x01));
	afficheBinaire(StepValue,4);
	printf("\r\n");
}



int main ()
{
  	if (wiringPiSetup () == -1)
  	{
    		fprintf (stdout, "oops: %s\n", strerror (errno)) ;
    		return 1 ;
  	}

	pinMode(IN1,OUTPUT);
	pinMode(IN2,OUTPUT);
	pinMode(IN3,OUTPUT);
	pinMode(IN4,OUTPUT);
	

	int i = 0;

	for(i = 10; i < STEPPER_STEPS ;i++)
	{
		writeServo(i);

		writeStepper(STEP_Ap);
		delay(TIME);

//		writeStepper(STEP_Ap |  STEP_Bp);
//		delay(TIME);

		writeStepper(STEP_Bp);
		delay(TIME);


//		writeStepper(STEP_Bp | STEP_An);
//		delay(TIME);

		writeStepper(STEP_An);
		delay(TIME);

//		writeStepper(STEP_An | STEP_Bn);
//		delay(TIME);

		writeStepper(STEP_Bn);
		delay(TIME);

//		writeStepper(STEP_Bn | STEP_Ap);
//		delay(TIME);

	}
	writeStepper(0);

	writeServo(0);



	return 0;
}

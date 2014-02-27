#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <math.h>

#include <ncurses.h>

#include <wiringPi.h>

#define IN1                     2
#define IN2                     3
#define IN3                     4
#define IN4                     5
#define SERVO_BLASTER_ID        0

#define SERVO_MAX_POS			90
#define SERVO_MIN_POS			10

#define TIME                    100

#define STEPPER_STEPS           48

#define STEP_Bn                 0x08
#define STEP_Bp                 0x04
#define STEP_An                 0x02
#define STEP_Ap                 0x01

void writeServo(int ServoValue)
{
    char command[120];

    sprintf(command,"echo %i=%i%% > /dev/servoblaster",SERVO_BLASTER_ID,ServoValue);
    system(command);
}
void writeStepper(int StepValue)
{
        digitalWrite(IN1,(StepValue & 0x08));
        digitalWrite(IN2,(StepValue & 0x04));
        digitalWrite(IN3,(StepValue & 0x02));
        digitalWrite(IN4,(StepValue & 0x01));
        //afficheBinaire(StepValue,4);
        //printf("\r\n");
}

int servoPosition = 50;
int moveServo(int direction)
{
	int ret = 1;
	if(direction == 1)
		servoPosition++;
	else
		servoPosition--;
		
	if(servoPosition > SERVO_MAX_POS)
	{
		ret = 0;
		servoPosition = SERVO_MAX_POS;
	}
	else if(servoPosition < SERVO_MIN_POS)
	{
		ret = 0;
		servoPosition = SERVO_MIN_POS;
	}
	
	move(2,0);
	printw("Servo position : %i\n",servoPosition);	
		
	writeServo(servoPosition);	
	
	return ret;
}

int stepsStepper = 0;
int moveStepper(int direction)
{
	int ret = 1;
	if(direction == 1)
		stepsStepper++;
	else
		stepsStepper--;
		
	
	move(3,0);
	printw("Stepper position : %i\n",stepsStepper);	
		
	writeStepper(stepsStepper);
	
	return ret;
}

int main()
{

	if (wiringPiSetup () == -1)
	{
		//fprintf (stdout, "oops: %s\n", strerror (errno)) ;
		printw("oops: %s\n", strerror (errno)) ;
		return 1 ;
	}

	int ch;
	/* Curses Initialisations */
	initscr();
	raw();
	keypad(stdscr, TRUE);
	noecho();

	pinMode(IN1,OUTPUT);
	pinMode(IN2,OUTPUT);
	pinMode(IN3,OUTPUT);
	pinMode(IN4,OUTPUT);
	
	printw("Press E to Exit\n");
	
	
	writeServo(servoPosition);
	writeStepper(0);

	while((ch = getch()) != 'E')
	{
		move(1,0);
		printw("Last command : ");
		switch(ch)
		{
			case KEY_UP:     
				printw("Up\n");
				moveServo(1);
				break;
			case KEY_DOWN:
				printw("Down\n");
				moveServo(0);	
				break;
			case KEY_LEFT:	
				printw("Left\n");
				break;
			case KEY_RIGHT:
				printw("Right\n");
				break;
			default:    
				printw("The pressed key is %c\n",ch);

		}
		
	}

	printw("Exiting Now\n");
	endwin();
	return 0;
}

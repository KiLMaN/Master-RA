#ifndef DEF_H
#define DEF_H

/// Network definition
#define TCP_PORT_DEFAULT 6786
#define UDP_PORT 6783

/// Stepper definition
#define IN1                     2
#define IN2                     3
#define IN3                     4
#define IN4                     5
#define SERVO_BLASTER_ID        0

#define SERVO_MAX_POS			60
#define SERVO_MIN_POS			40

#define STEPPER_RUN_SLEEP       15000 // Time in us

#define STEPPER_STEPS           (666/360)

#define STEP_Bn                 0x08
#define STEP_Bp                 0x04
#define STEP_An                 0x02
#define STEP_Ap                 0x01

// GSTREAMER
#define GST_PORT				5000
#define GST_WIDTH				640
#define GST_HEIGHT				480


#endif // DEF_H_INCLUDED

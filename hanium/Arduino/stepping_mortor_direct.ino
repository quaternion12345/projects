#include <Stepper.h>

const int stepsPerRev = 200;  // change this to fit the number of steps per revolution
// for your motor

// initialize the stepper library on pins 8 through 11:
Stepper stepper(stepsPerRev, 8, 9, 10, 11);
int btn1 = 7;
int btn2 = 6;
int a = 1;
void setup() {
  // set the speed at 60 rpm:
  stepper.setSpeed(60);
  // initialize the serial port:
  Serial.begin(9600);
  pinMode(btn1, INPUT_PULLUP);
  pinMode(btn2, INPUT_PULLUP);
}

void loop() {
    boolean btn1HL = digitalRead(btn1);
  boolean btn2HL = digitalRead(btn2);
  if (btn1HL == HIGH ) {
    stepper.step(1);  // 한 바퀴 회전 명령
   
  }
  if (btn2HL == HIGH) {
    stepper.step(-1);  // 반대 방향으로 한 바퀴 회전
  }
}
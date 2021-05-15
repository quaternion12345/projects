#include <Stepper.h>
int pushButton = 8;
int ENB=2; int IN4=3; int IN3=4;
int IN2=5; int IN1=6; 
int ENA=7;
char serialInput;
const int stepsPerRev = 200; 
Stepper stepper_down(stepsPerRev, IN2, IN1, IN3, IN4);
void setup()
{
 Serial.begin(9600);
 pinMode(IN1,OUTPUT); pinMode(IN2,OUTPUT);
 pinMode(IN3,OUTPUT); pinMode(IN4,OUTPUT);
 pinMode(ENB,OUTPUT); digitalWrite(ENB,HIGH);
 pinMode(ENA,OUTPUT); digitalWrite(ENA,HIGH);

 pinMode(pushButton, INPUT_PULLUP); 
 stepper_down.setSpeed(30);
}
void loop()
{
  int switchValue=digitalRead(pushButton);

  if(switchValue == 0){
    stepper_down.step(1);
    //Serial.println(switchValue);
  }
  else{
    stepper_down.step(-1);
    //Serial.println(switchValue);
  }
}
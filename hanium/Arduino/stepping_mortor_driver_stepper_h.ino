#include <Stepper.h>

int pushButton = 1;

int ENB=2; int IN4=3; int IN3=4;
int IN2=5; int IN1=6; 
//int ENA=7;
//int ENB_2=8;  

int IN4_2=9;  int IN3_2=10;
int IN2_2=11; int IN1_2=12; int ENA_2=13;

const int stepsPerRev = 200; 
Stepper stepper_up(stepsPerRev, IN2_2, IN1_2, IN3_2, IN4_2); // 위 모터
Stepper stepper_down(stepsPerRev, IN2, IN1, IN3, IN4); // 아래 모터

void setup()
{
 Serial.begin(9600);
 pinMode(IN1,OUTPUT); pinMode(IN2,OUTPUT);
 pinMode(IN3,OUTPUT); pinMode(IN4,OUTPUT);
 pinMode(ENB,OUTPUT);
 digitalWrite(ENB,HIGH);
 // pinMode(ENA,OUTPUT); 
 // digitalWrite(ENA,HIGH);
 pinMode(IN1_2,OUTPUT); pinMode(IN2_2,OUTPUT);
 pinMode(IN3_2,OUTPUT); pinMode(IN4_2,OUTPUT);
 pinMode(ENA_2,OUTPUT); 
 digitalWrite(ENA_2,HIGH);
// pinMode(ENB_2,OUTPUT);
// digitalWrite(ENB_2,HIGH);
 pinMode(pushButton, INPUT_PULLUP); 
 stepper_up.setSpeed(30);
 stepper_down.setSpeed(30);
}

void loop()
{
  char switchValue=digitalRead(pushButton);
  //Serial.println(switchValue);
  if(switchValue == 0){
    stepper_up.step(1);
    stepper_down.step(1);
  }
  else{
    stepper_up.step(-1);
    stepper_down.step(-1);
  }
  //delay(100);
}
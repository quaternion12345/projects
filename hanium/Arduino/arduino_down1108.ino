#include <Stepper.h>
#include <SoftwareSerial.h> 

SoftwareSerial BTSerial(10,11); 

byte qqq = 1;
int high = 1;
int b = 0;
int c = 0;
int down = 0;

byte pre = 0;
byte sig = 0;

int ble_flag = 0 ;
int d_flag = 0;


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
 stepper_down.setSpeed(60);

 
  BTSerial.begin(9600);
 
}

void loop()
{
  //BTSerial.flush();
  if (BTSerial.available()){
    
    pre = BTSerial.read();
    Serial.print("pre:");
    Serial.println(pre);
    
    if(pre == 85){
      Serial.println("up");
      sig = 1;
    }
    if(pre == 68){
      Serial.println("down");
      sig = 2;
    }
  }
  if(sig==1){
    //up
    if((pre != 85)&& (d_flag==0)){
      high = pre;
      d_flag = 1;
      Serial.println(high);
    }
    while((high>30)&&(d_flag==1)){
      stepper_down.setSpeed(60);
      stepper_down.step(-100);
      high=high-qqq;
      Serial.println(high);
    }

    if((high==30)&&(d_flag==1)){
      stepper_down.step(0);
      BTSerial.write('r');
      sig =4;
      d_flag=2;
      Serial.println(high);
    }
  }
  if(sig==2){
    //down
   if((pre != 68)&& (d_flag==2)){
      high = pre;
      d_flag = 3;
      Serial.println(high);
    }
    while((high>30)&&(d_flag==3)){
      stepper_down.setSpeed(60);
      stepper_down.step(100);
      high=high-qqq;
      Serial.println(high);
    }

    if((high==30)&&(d_flag==3)){
      stepper_down.step(0);
      BTSerial.write('q');
      sig=4;
      d_flag=0;
      Serial.println(high);
    }
  }
}
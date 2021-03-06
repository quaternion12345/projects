#include <SoftwareSerial.h> 
#include <Stepper.h>

SoftwareSerial BTSerial(8, 7); 

 byte qqq = 1;
 int check = 20;
 int load = 1;
 byte data = 150;
 int sig = 0;
 byte tmp = 10;

 int q_flag = 0;

 int d_flag = 0;

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


 
  BTSerial.begin(9600);


  
}





void loop()
{
  /*
  if(Serial.available()){
    BTSerial.write(Serial.read());
  }
  */
  //BTSerial.flush();
  if(BTSerial.available()){
      data = BTSerial.read();
      Serial.write(data);
      Serial.println();
      if (data==63){ //data == '?' 전체 시작
        sig = 1;
      }
      if( data == 123){
        //data == '{' 윗 라인 돌아오기
        sig = 2;
      }
      if(data == 126){
        //data =='~' 아래라인 시작
        sig = 3;
        BTSerial.write('~');
        Serial.println("sig:");
        Serial.println(3);
      }
      if(data == 125){
        //data == '}' 아래라인 돌아오기
        sig=4;
        BTSerial.write('}');
        Serial.println("sig:");
        Serial.println(sig);
        Serial.println(d_flag);
      }
   }
   
  if ((sig == 1)&&(check<=77)){
    stepper_up.step(-60);
    check=check+1;
    if (data == 'A' ) // data=='A' 위치 확인409
    { 
      //stepper_up.step(0);
      BTSerial.write(check); //이동 거리 정보 보내기
      Serial.println(check);
      data='?';
      
    }
  }

  if((sig ==2)||(check>=77)){ // 윗 라인 돌아오기...간 만큼
    while(check>20){
        stepper_up.setSpeed(90);
        stepper_up.step(60);
        check=check-1;  
    }
    if(check==20){
        delay(1000);
        BTSerial.write('Q');
        Serial.print("qq");
        delay(1000);
        check=19;

    }
  }

  if(sig == 3){
     //Serial.println("down_line_start");
    if((data != 126)&&(d_flag==0)){
      load = data+qqq; //이동할 거리 받기
      /*
      Serial.print(load);
      Serial.println();
      */
      d_flag=2;
    }
    while((load>20)&&(d_flag==2)){
      stepper_down.step(-60); // 이동
      load = load - qqq;
      Serial.print(load);
      Serial.println();
    }
    if((load==20)&&(d_flag==2)){
      stepper_down.step(0);
      BTSerial.write("s"); //115
      Serial.println("s");
      d_flag=1;
    }
  }
  
  if(sig == 4){
    Serial.print("flag");
    Serial.print(d_flag);
    if((data != 125)&&(d_flag==1)){
      load = data+qqq; //이동할 거리 받기
      Serial.println(load);
      d_flag=2;
    }
     while((load>20)&&(d_flag==2)){
      stepper_down.step(60); // 이동
      load = load - qqq;
      Serial.print(load);
      Serial.println();
    }
    if((load==20)&&(d_flag==2)){
      stepper_down.step(0);
      BTSerial.write("z"); //122
      d_flag=0;
    }
  }
}
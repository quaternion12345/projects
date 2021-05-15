int pushButton = 1;

int ENB=2;
int IN4=3;
int IN3=4;
int IN2=5;
int IN1=6;
int ENA=7;

int ENB_2=8;
int IN4_2=9;
int IN3_2=10;
int IN2_2=11;
int IN1_2=12;
int ENA_2=13;


void forward_sync(int speed){
 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);
 
 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);

 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);

 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);
}

void reverse_sync(int speed){
 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);

 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);

 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);

 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);
}

void forward(int speed){
 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);
 delay(speed);

 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);
 delay(speed);

 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);
 delay(speed);

 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);
 delay(speed);    
}

void reverse1(int speed){
 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);
 delay(speed);

 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);
 delay(speed);

 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,LOW);
 digitalWrite(IN4,HIGH);
 delay(speed);

 digitalWrite(IN1,LOW);
 digitalWrite(IN2,HIGH);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW);
 delay(speed);
}

void forward2(int speed){
 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);

 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);    
}

void reverse2(int speed){
 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);

 digitalWrite(IN1_2,HIGH);
 digitalWrite(IN2_2,LOW);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,LOW);
 digitalWrite(IN4_2,HIGH);
 delay(speed);

 digitalWrite(IN1_2,LOW);
 digitalWrite(IN2_2,HIGH);
 digitalWrite(IN3_2,HIGH);
 digitalWrite(IN4_2,LOW);
 delay(speed);
}
void setup()
{
 Serial.begin(9600);
 pinMode(IN1,OUTPUT); pinMode(IN2,OUTPUT);
 pinMode(IN3,OUTPUT); pinMode(IN4,OUTPUT);
 pinMode(pushButton, INPUT_PULLUP); 
 pinMode(ENA,OUTPUT); pinMode(ENB,OUTPUT);
 digitalWrite(ENA,HIGH); digitalWrite(ENB,HIGH);
 
 pinMode(IN1_2,OUTPUT); pinMode(IN2_2,OUTPUT);
 pinMode(IN3_2,OUTPUT); pinMode(IN4_2,OUTPUT);
 pinMode(ENA_2,OUTPUT); pinMode(ENB_2,OUTPUT);
 digitalWrite(ENA_2,HIGH); digitalWrite(ENB_2,HIGH);
}

void loop()
{
  
  int switchValue=digitalRead(pushButton);
  Serial.println(switchValue);
  if(switchValue == 0){
    reverse_sync(5);
    //reverse1(5);
    //reverse2(30);
  }
  else{
    forward_sync(5);
    //forward1(30);
    //forward2(30);
  }
}
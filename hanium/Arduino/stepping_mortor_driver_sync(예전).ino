int pushButton = 1;

int ENB=2;
int IN4=3;
int IN3=4;
int IN2=5;
int IN1=6;
int ENA=7;



void forward_sync(int speed){
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

void reverse_sync(int speed){
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


void setup()
{
 Serial.begin(9600);
 pinMode(IN1,OUTPUT); pinMode(IN2,OUTPUT);
 pinMode(IN3,OUTPUT); pinMode(IN4,OUTPUT);
 pinMode(pushButton, INPUT_PULLUP); 
 pinMode(ENA,OUTPUT); pinMode(ENB,OUTPUT);
 digitalWrite(ENA,HIGH); digitalWrite(ENB,HIGH);
}

void loop()
{
  int switchValue=digitalRead(pushButton);
  Serial.println(switchValue);
  if(switchValue == 0){
    reverse_sync(5);
  }
  else{
    forward_sync(5);
  }
}
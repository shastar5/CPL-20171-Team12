#include <Stepper.h>

#define dustLimit 0.8
#define vibLimit 350
#define smokeLimit 401

char buffer[20];     
int val, vib, smoke; //값
int bufferIndex =0;
int LED = 0;
const int stepsPerRevolution = 2048 * 6; //360도 * 6바퀴
//pin 연결 설정.
const int vibPin = 1;
const int stpperIN1_Pin = 8;
const int stpperIN2_Pin = 9;
const int stpperIN3_Pin = 10;
const int stpperIN4_Pin = 11;
const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
const int gasPin = 0;
const int dustPin = 4; 
const int buzzerPin = 3;

unsigned long duration;
unsigned long starttime;
unsigned long sampletime_ms = 3000;//sampe 30s ;
unsigned long lowpulseoccupancy = 0;
float ratio = 0;
float dust = 0;
int Down = 0; //커튼
Stepper myStepper(stepsPerRevolution,stpperIN4_Pin,stpperIN2_Pin,stpperIN3_Pin,stpperIN1_Pin);

int value = 0;

void setup() {
  Serial.begin(9600);
  //삼색 LED
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT); 
  myStepper.setSpeed(14); //모터 속도 설정
  pinMode(dustPin, INPUT);
  pinMode(buzzerPin, OUTPUT);
  starttime = millis();//get the current time;
}
void loop()
{
  vib = analogRead(vibPin); //진동센서 값 가져오기
    smoke = analogRead(gasPin);
  
    if ((value & 1) == 1) //dust
    {
      LED = 3;
      if (Down == 0)
      {
        myStepper.step(stepsPerRevolution);
        Down = 1;
        starttime = millis();
      }
    }
    else if ((Down == 1) && (value & 1) != 1)
    {
      if ((millis() - starttime) > sampletime_ms)
      {
        Down = 0;
        LED = 0;
        myStepper.step(-stepsPerRevolution);
      }
    }
    if ((value & 1 << 1) == 1 << 1) //SMOKE
    {
      LED = 1;
    }
    else
      LED = 0;
    if ((value & 1 << 2) == 1 << 2) //Vibration
    {
      LED = 2;
    }
    else
      LED = 0;
    if ((value & 1 << 3) == 1 << 3) //fingerPrint
    {

    }
    duration = pulseIn(dustPin, LOW);
    lowpulseoccupancy = lowpulseoccupancy + duration;
    //if ((millis() - starttime) > sampletime_ms) //if the sampel time == 30s
    //{
    ratio = lowpulseoccupancy / (sampletime_ms * 10.0); // Integer percentage 0=>100
    dust = 1.1 * pow(ratio, 3) - 3.8 * pow(ratio, 2) + 520 * ratio + 0.62; //입자 규모
    lowpulseoccupancy = 0;
    //starttime = millis();
    //}
   
  if(dust > dustLimit)
    if((~value & 1) == 1)
        value += 1;
  if(dust <= dustLimit)
     if((value & 1) == 1)
        value -= 1;
  if(smoke > smokeLimit)
    if((~value & 1<<1) == 1<<1)
        value += 1<<1;
  if(smoke <= smokeLimit)
     if((value & 1<<1) == 1<<1)
        value -= 1<<1;
  if(vib > vibLimit)
    if((~value & 1<<2) == 1<<2)
        value += 1<<2;
  if(vib <= vibLimit)
     if((value & 1<<2) == 1<<2)
        value -= 1<<2;
    Serial.print("vib : ");
    Serial.print(vib);
    Serial.print(" smoke : ");
    Serial.print(smoke);
    Serial.print(" dust : ");
    Serial.print(dust);
    Serial.print("  ");
    Serial.print(value);
    Serial.println(Down);
   switch (LED)
    {
    case 1: //화재 경보일때 빨간색
      setColor(255, 0, 0);
      beep(50);
      delay(100);
      beep(50);
      delay(100);
      LED = 0;
      break;
    case 2: //진동센서 일정수준이상일때 초록색
      setColor(0, 255, 0);
      beep(50);
      delay(100);
      beep(50);
      delay(100);
      LED = 0;
      break;
    case 3: //미세먼지 일정수준이상일때 파란색
      setColor(0, 0, 255);
      LED = 0;
      break;
    default:
      setColor(0, 0, 0);
    }
    delay(100);
}
// RGB 값을 받아 analogWrite를 통해 각 핀에 연결된 LED에 전달 함수
void setColor(int red, int green, int blue)
{
  analogWrite(redPin, red);
  analogWrite(greenPin, green);
  analogWrite(bluePin, blue);
}
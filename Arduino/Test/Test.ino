#include <Servo.h>
#include <SoftwareSerial.h>
#include <Console.h>
#include <Stepper.h>

int VibrationSensor;

int DustSensorPin = 4;
unsigned long duration;
unsigned long starttime;
unsigned long sampletime_ms = 100;//sampe 1s ;
unsigned long lowpulseoccupancy = 0;
float ratio = 0;
float concentration = 0;

Servo servo;
int servopos = 0;
Stepper stepper(2048, 11, 9, 10, 8);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  while(!Console.available());
  Console.println("Sueccessfully connected");
  Serial.println("Successfully Connected");
  pinMode(DustSensorPin, INPUT);
  starttime = millis();
  VibrationSensor = analogRead(0);
  //servo.attach(5);
  stepper.setSpeed(100);
  Serial.println("센서 연결 완료");  
}

void loop() {
  Serial.print("Vibration Sensor: ");
  Serial.println(VibrationSensor, DEC);

  duration = pulseIn(DustSensorPin, LOW);
  lowpulseoccupancy = lowpulseoccupancy+duration;

  if ((millis()-starttime) > sampletime_ms)//if the sampel time == 1s
  {
    ratio = lowpulseoccupancy/(sampletime_ms*10.0);  // Integer percentage 0=>100
    concentration = 1.1*pow(ratio,3)-3.8*pow(ratio,2)+520*ratio+0.62; // using spec sheet curve
    Serial.print(lowpulseoccupancy);
    Serial.print(",");
    Serial.print(ratio);
    Serial.print(",");
    Serial.println(concentration);
    lowpulseoccupancy = 0;
    starttime = millis();
  }

  if(concentration + ratio + lowpulseoccupancy > 1000.0)
  {
    Serial.println("모터 돌림");
    for(int i = 0 ;i<32;i++)
    {
      stepper.step(64);
    }

    delay(150);
    for(int i=0;i<32;i++)
    {
      stepper.step(-64);
    }

  }
  delay(100);
}

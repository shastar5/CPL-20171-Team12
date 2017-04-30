#include <SoftwareSerial.h>
#include <MECHA_UARTFingerprint.h>
SoftwareSerial sofSeri(2,3);
MECHA_UARTFinger fin = MECHA_UARTFinger(&sofSeri);
void setup() {
  Serial.begin(9600);
  fin.begin();
  fin.DelUser();
  fin.GetRespon();
}

void loop() {
}

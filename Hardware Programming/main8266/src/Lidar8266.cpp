#include "Lidar8266.h"



Lidar8266::Lidar8266(int rx, int tx){

  tally = 0;
  startMillis = millis();

  softwareSerial = new SoftwareSerial(rx, tx);  // RX, TX

  softwareSerial->begin(115200);
  software = true;

}

Lidar8266::Lidar8266(){
  tally = 0;
  startMillis = millis();
  software = false;
}

String Lidar8266::read(){

    currMillis = millis();
    byte data[9];
    tally++;
    updateRate();

    if(software && softwareSerial->available() && softwareSerial->available() >= 9){

        for (int i = 0; i < 9; i++) {
        data[i] = softwareSerial->read();
        }

        clearSerialBuffer();

        String distance = String(data[2]);
        return (distance);
    }

    else if (!software && Serial.available() && Serial.available() >= 9){
        for (int i = 0; i < 9; i++) {
        data[i] = Serial.read();
        }

        clearSerialBuffer();

        String distance = String(data[2]);
        return (distance);
    }

    tally--;
    return "-1";

}

void Lidar8266::clearSerialBuffer() {
    if(software){
        while (softwareSerial->available() > 0) {
        softwareSerial->read();  
        }
    }
    else{
        while (Serial.available() > 0) {
        Serial.read();  
        }
    }
}

void Lidar8266::updateRate(){
    rate = (float)tally * 1000 / (currMillis - startMillis); 

  if (currMillis-startMillis > 1000){
    startMillis = currMillis;
    tally = 0;
  }

}

double Lidar8266::getRate(){
    return rate;
}
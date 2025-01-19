#ifndef Lidar8266_h
#define Lidar8266_h


#include <Arduino.h>
#include <SoftwareSerial.h>


class Lidar8266 {
  public:

    Lidar8266(int rx, int tx);
    Lidar8266();

    String read();
    double getRate();

  private:

    void clearSerialBuffer();
    void updateRate();

    boolean software;

    int tally;
    unsigned long startMillis;
    unsigned long currMillis;
    double rate;

    SoftwareSerial* softwareSerial;

};


#endif
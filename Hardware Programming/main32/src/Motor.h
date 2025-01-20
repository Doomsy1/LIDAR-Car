#include <Arduino.h>

#ifndef MOTOR_H
#define MOTOR_H

class Motor {
  public:
    Motor(int pin1, int pin2, int e1, int e2, int encoderTicks);

    void setP1(int p);
    void setP2(int p);

    void updateEncoder();
    int getPos();

    void run(int direction, int strength);
    void stop();

    static const int CLOCKWISE = -1;
    static const int COUNTERCLOCKWISE = 1;

  private:
    int p1;
    int p2;
    int e1;
    int e2;

    int currPos;

    int changePerTick;
};

#endif
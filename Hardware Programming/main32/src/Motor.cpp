#include "Motor.h"

Motor::Motor(int pin1, int pin2, int e1, int e2, int encoderTicks) {
    this->p1 = pin1;
    this->p2 = pin2;
    this->e1 = e1;
    this->e2 = e2;

    pinMode(p1, OUTPUT);
    pinMode(p2, OUTPUT);
    pinMode(e1, INPUT);
    pinMode(e2, INPUT);

    
    currPos = 0;
    changePerTick = 360 / encoderTicks;
}

void Motor::setP1(int p) {
    this->p1 = p;
    pinMode(p1, OUTPUT);
}

void Motor::setP2(int p) {
    this->p2 = p;
    pinMode(p2, OUTPUT);
}

void Motor::updateEncoder() {
    int a = digitalRead(e1);
    int b = digitalRead(e2);
    
    static int lastA = LOW;
    static int lastB = LOW;

    if (a != lastA || b != lastB) {
        if (a == HIGH && lastB == LOW) {
            currPos+changePerTick;
        } else if (a == LOW && lastB == HIGH) {
            currPos-changePerTick;
        }
        lastA = a;
        lastB = b;
    }
}

void Motor::run(int direction, int strength) {
    if (direction == CLOCKWISE) {
        digitalWrite(p1, HIGH);
        digitalWrite(p2, LOW);
    } else if (direction == COUNTERCLOCKWISE) {
        digitalWrite(p1, LOW);
        digitalWrite(p2, HIGH);
    }
}

void Motor::stop(){
  digitalWrite(p1, LOW);
  digitalWrite(p2, LOW);
}   


int Motor::getPos(){
    return currPos;
}
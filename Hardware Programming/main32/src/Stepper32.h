#include <Arduino.h>

#ifndef Stepper32_H
#define Stepper32_H

class Stepper32{

    public:

        Stepper32(int dirPin, int stepPin, int stepsPerRevolution);
        void turnAngle(int angle);
        void turnSteps(int steps);
        void setDelayPerStep(int microseconds);
        int getSteps();
        double getAngle();
        void setSpeed(int rpm);
    private:
        
        int dirPin;
        int stepPin;
        int stepsPerRevolution;
        float anglePerStep;
        int delayPerStep;

        int currSteps;

};



#endif
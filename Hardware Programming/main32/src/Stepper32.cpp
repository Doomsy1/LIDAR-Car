#include "Stepper32.h"


Stepper32::Stepper32(int dirPin, int stepPin, int stepsPerRevolution){
	this->dirPin = dirPin;
	this->stepPin = stepPin;
	this->stepsPerRevolution = stepsPerRevolution;
	this->anglePerStep = 360.0 / stepsPerRevolution;
	this->delayPerStep = 1000;
	this->currSteps = 0;

	pinMode(dirPin, OUTPUT);
	pinMode(stepPin, OUTPUT);

}

void Stepper32::turnAngle(int angle){
	
	int numSteps = round(abs(angle)/anglePerStep);

	if (angle > 0){
		digitalWrite(dirPin, HIGH);
		currSteps += numSteps;
	}
	else{
		digitalWrite(dirPin, LOW);
		currSteps -= numSteps;
	}

	for(int x = 0; x < numSteps; x++)
	{
		digitalWrite(stepPin, HIGH);
		delayMicroseconds(delayPerStep);
		digitalWrite(stepPin, LOW);
		delayMicroseconds(delayPerStep);
	}

}

void Stepper32::turnSteps(int steps){

	int numSteps = abs(steps);

	if (steps > 0){
		digitalWrite(dirPin, HIGH);
		currSteps += numSteps;
	}
	else{
		digitalWrite(dirPin, LOW);
		currSteps -= numSteps;
	}
	
	currSteps = currSteps % stepsPerRevolution;


	for(int x = 0; x < numSteps; x++){

		digitalWrite(stepPin, HIGH);
		delayMicroseconds(delayPerStep);
		digitalWrite(stepPin, LOW);
		delayMicroseconds(delayPerStep);
		
	}
}

int Stepper32::getSteps(){
	return currSteps;

}

double Stepper32::getAngle(){
	return currSteps*anglePerStep;
}

void Stepper32::setDelayPerStep(int microseconds){
	delayPerStep = microseconds;
}

void Stepper32::setSpeed(int rpm) {
    delayPerStep = 60L * 1000L * 1000L / (rpm * stepsPerRevolution);
}

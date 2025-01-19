#include <Arduino.h>
#include "Socket8266.h"
#include "Motor.h"
#include "Stepper32.h"

const char* ssid = "Virgin AIG_EXT";      
const char* password = "ginggiang0";      
const int port = 1024;              

// const char* ssid = "Ario";
// const char* password = "ArioBario@0";

String motorMovement = "$";

unsigned long universalMillis;
unsigned long ESPPrevmillis;
unsigned long ESPCurrmillis;

const int LForward = 18;
const int LBackward = 19;

const int RForward = 4;
const int RBackward = 16;

const int REncoderA = 34;
const int REncoderB = 35;

const int LEncoderA = 32;
const int LEncoderB = 33;

const int EncoderTicks = 20;

const int dirPin = 25;
const int stepPin = 26;
const int stepsperRev = 200;

const int lidarResolution = 100;
const int sps = 50;

const int SECOND = 1000;

int lidarTally = 0;
int receiveTally = 0;

int encoderSent = 0;
int encoderPacketInterval = 1000;

int stepperSent = 0;
int stepperPacketInterval = 250;

int receiveMillis = 20;

int motorGetMillis = 500;
int motorGetTally = 0;

boolean driving = false;

Socket8266 socket;

Motor leftMotor = Motor(LForward, LBackward, LEncoderA, LEncoderB, EncoderTicks);
Motor rightMotor = Motor(RForward, RBackward, REncoderA, REncoderB, EncoderTicks);

Stepper32 stepper = Stepper32(dirPin, stepPin, stepsperRev);

void setup() {

  Serial.begin(115200);

  socket.connectWifi(ssid, password);
  // socket.begin();

  ESPCurrmillis = millis();
  ESPPrevmillis = millis();


  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);

  universalMillis = 0;
  stepper.setDelayPerStep(2000);

}



void rotateLeft(){
  leftMotor.run(Motor::CLOCKWISE, 200);
  rightMotor.run(Motor::CLOCKWISE, 200);
}
void rotateRight(){
  leftMotor.run(Motor::COUNTERCLOCKWISE, 200);
  rightMotor.run(Motor::COUNTERCLOCKWISE, 200);
}

void moveForward(){
  leftMotor.run(Motor::COUNTERCLOCKWISE, 200);
  rightMotor.run(Motor::CLOCKWISE, 200);
}
void moveBackward(){
  leftMotor.run(Motor::CLOCKWISE, 200);
  rightMotor.run(Motor::COUNTERCLOCKWISE, 200);
}

void stop(){
  leftMotor.stop();
  rightMotor.stop();
}

void processInput(String input) {

  if (input.isEmpty()){
    return;
  }

  if (input == "*") {
    universalMillis = input.substring(1).toInt();
  }

  if (input == "w"){
    driving = true;
    moveForward();
    Serial.println("Moving Forward");
  }
  else if(input== "s"){
    driving = true;
    moveBackward();
    Serial.println("Moving Backward");

  }
  else if(input == "d"){
    driving = true;
    rotateRight();
    Serial.println("Rotating Right");

  }
  else if(input== "a"){
    driving = true;
    rotateLeft();
    Serial.println("Rotating Left");
  }
  else if (input == "x"){
    driving = false;
    stop();
  }
}


void loop() {
  
  if (!socket.isConnected()){
    digitalWrite(LED_BUILTIN, LOW);
    stop();

    socket.begin();

    universalMillis = 0;
    lidarTally = 0;
    encoderSent = 0;
    stepperSent = 0;
    receiveTally = 0;
    motorGetTally = 0;
  }



  digitalWrite(LED_BUILTIN, HIGH);

  // // leftMotor.updateEncoder();
  // // rightMotor.updateEncoder();

  ESPCurrmillis = millis();
  universalMillis += (ESPCurrmillis-ESPPrevmillis);

  if (universalMillis/receiveMillis > receiveTally) {
            String data = socket.receive();
            if (!data.isEmpty()) {
                Serial.println(data + " | " + String(universalMillis));
                processInput(data);
            
        }
        receiveTally++;
    }



  if ((universalMillis / (SECOND/sps) > lidarTally) && !driving) {
    stepper.turnSteps(stepsperRev / lidarResolution);
    lidarTally=universalMillis/(SECOND/sps);
  }
  

  if ((universalMillis / stepperPacketInterval) > stepperSent){
    socket.send("%"+String(stepper.getAngle()));
    stepperSent=universalMillis/stepperPacketInterval;
    digitalWrite(LED_BUILTIN, LOW);
  }


  ESPPrevmillis = ESPCurrmillis;

}



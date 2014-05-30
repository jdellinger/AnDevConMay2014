#include "motor_control_complete.h"

const int STBY = 2;
const int AIN1 = 3;
const int AIN2 = 4;
const int PWMA = 5;
const int BIN1 = 6;
const int BIN2 = 7;
const int PWMB = 9;
const int MOTOR_LEFT = 1;
const int MOTOR_RIGHT = 2;

const int fwdPin = 12;
const int backPin = 11;
const int ledPin = 13;

const int pi0 = A0;
const int pi1 = A1;
const int pi2 = A2;
const int pi3 = A3;

const int SPEED = 200;

void setup(){
  Serial.begin(9600);
  //motor control
  pinMode(STBY, OUTPUT);
  pinMode(PWMA, OUTPUT);
  pinMode(AIN1, OUTPUT);
  pinMode(AIN2, OUTPUT);
  pinMode(PWMB, OUTPUT);
  pinMode(BIN1, OUTPUT);
  pinMode(BIN2, OUTPUT);
  
  //button inputs
  pinMode(fwdPin, INPUT_PULLUP);
  pinMode(backPin, INPUT_PULLUP);
  pinMode(ledPin, OUTPUT);
  
  //raspberry pi comms
  pinMode(pi0, INPUT);
  pinMode(pi1, INPUT);
  pinMode(pi2, INPUT);
  pinMode(pi3, INPUT);
}



void loop(){
  DriveState state = getStateFromButtons();
  if(state == none){
    state = getStateFromPi();
  }
  
  switch(state){
    case forward:
      goForward(SPEED);
      break;
    case backward:
      goBackward(SPEED);
      break;
    case left:
      turnLeft(SPEED);
      break;
    case right:
      turnRight(SPEED);
      break;
    default:
      stopMotor();
  }
}

DriveState getStateFromPi(){
  int p0 = digitalRead(pi0);
  int p1 = digitalRead(pi1);
  int p2 = digitalRead(pi2);
  int p3 = digitalRead(pi3);
  
  if(p3 == HIGH){
    digitalWrite(ledPin, HIGH);
    if(p0 == HIGH && p1 == LOW && p2 == LOW){
      return forward;
    }else if(p0 == LOW && p1 == HIGH && p2 == LOW){
      return backward;
    }else if(p0 == HIGH && p1 == HIGH && p2 == LOW){
      return left;
    }else if(p0 == LOW && p1 == LOW && p2 == HIGH){
      return right;
    }else{
      return none;
    }
  }else{
    digitalWrite(ledPin, LOW);
  }
}

DriveState getStateFromButtons(){
  int fwd = digitalRead(fwdPin);
  int bwd = digitalRead(backPin);
  
  DriveState state = none;
  if(fwd == LOW && bwd == LOW){
    state = left;
  }else if(fwd == LOW){
    state = forward;
  }else if(bwd == LOW){
    state = backward;
  }else{
    state = none;
  }
  return state;
}

void goForward(int speed){
    driveMotor(MOTOR_LEFT, speed, 1);
    driveMotor(MOTOR_RIGHT, speed, 1);
    Serial.println("forward");
}

void goBackward(int speed){
    driveMotor(MOTOR_LEFT, speed, 0);
    driveMotor(MOTOR_RIGHT, speed, 0);
    Serial.println("backward");
}

void turnLeft(int speed){
    driveMotor(MOTOR_LEFT, speed, 0);
    driveMotor(MOTOR_RIGHT, speed, 1);
    Serial.println("left");
}

void turnRight(int speed){
    driveMotor(MOTOR_LEFT, speed, 1);
    driveMotor(MOTOR_RIGHT, speed, 0);
    Serial.println("right");
}

//motor control
void driveMotor(int motor, int speed, int direction){
  digitalWrite(STBY, HIGH);
  
  boolean inPin1 = LOW;
  boolean inPin2 = HIGH;
  if(direction == 1){
    inPin1 = HIGH;
    inPin2 = LOW;
  }
  if(motor == MOTOR_LEFT){
    digitalWrite(AIN1, inPin1);
    digitalWrite(AIN2, inPin2);
    analogWrite(PWMA, constrain(speed, 0, 255)); 
  }else if(motor == MOTOR_RIGHT){
    digitalWrite(BIN1, inPin1);
    digitalWrite(BIN2, inPin2);
    analogWrite(PWMB, constrain(speed, 0, 255)); 
  }
}

void stopMotor(){
  digitalWrite(STBY, LOW);
}

// IOLibrary.ino
// I/O function
// Created by Simon Schneider, 01. October 2024

#include "Arduino.h"
#include "IOLibrary.h"

// ---------------------------------------------------
// Class TOF
//
// An OFF delay timer that disables the "q" output 
// a specified number of milliseconds the trig inpu is disabled.
TOF::TOF(unsigned long interval) : q(false), time(interval){};

TOF::TOF() : q(false), time(1000){};

bool TOF::run(bool trig) {
  if(trig) {
    startTime = millis();
    q = true;
  } else {
    if(startTime + time < millis()) q = false;
  }
  return q;
}

bool TOF::getQ() {
  return q;
}

// ---------------------------------------------------
// Class TON
//
// An ON delay timer that enables the "q" output
// a specific number of milliseconds after the trig input is enabled.
TON::TON(unsigned long interval) : q(false), time(interval){};

TON::TON() : q(false), time(1000){};

bool TON::run(bool trig) {
  if (!trig) {
    startTime = millis();
    q = false;
  } else {
    if (startTime + time < millis()) {
      q = true;
    }
  }
  return q;
}

void TON::reset() {
  startTime = millis();
  q = false;
}

bool TON::getQ() {
  return q;
}

// ---------------------------------------------------
// Class Debounce
//
// Makes sure that the code is only triggered once per user input.
Debounce::Debounce(unsigned long interval) : tonClk(interval), tofClk(interval){};

bool Debounce::run(bool clk) {
  return tofClk.run(tonClk.run(clk));
}

// ---------------------------------------------------
// R_TRIG
//
// Detect a rising edge
void R_TRIG::run(bool clk) {
  q = (clk > clkOld);
  clkOld = clk;
}

bool R_TRIG::getQ() {
  return q;
}

// ---------------------------------------------------
// F_TRIG
//
// Detect a falling edge
void F_TRIG::run(bool clk) {
  q = (clk < clkOld);
  clkOld = clk;
}

bool F_TRIG::getQ() {
  return q;
}

// ---------------------------------------------------
// CTU
//
// Countup
void CTU::run(bool cu, bool reset, unsigned long pv) {
  rtCu.run(cu);
  if(rtCu.getQ()) cv++;
  if(reset) cv = 0;
  q = (cv >= pv); // The assignment is calculated and the Boolean value is assigned.
}

bool CTU::getQ() {
  return q;
}

// ---------------------------------------------------
// CTD
//
// Countdown
void CTD::run(bool cd, bool load, unsigned long pv) {
  rtCu.run(cd);
  if(rtCu.getQ() && cd > 0) cv--;
  if(load) cv = pv;
  q = (cv == 0);  // The assignment is calculated and the Boolean value is assigned.
}

bool CTD::getQ() {
  return q;
}

// ---------------------------------------------------
// class Blink
//
// Without discription
Blink::Blink(unsigned long newTimeHigh, unsigned long newTimeLow)
  : timeHigh(newTimeHigh), timeLow(newTimeLow), time(newTimeLow){};

bool Blink::run(bool enable) {
  if(!enable) return q;
  time = q ? timeHigh : timeLow;  // variable = condition ? value_if_true : value_if_false;
  if (startTime + time < millis()) {
    q = !q;
    startTime = time;
  }
  return q;
}

bool Blink::getQ() {
  return q;
}

// ---------------------------------------------------
// class Key
//
// Includes functions for pressed, released, long pressed, short pressed and double click
Key::Key(unsigned long time)
  : tofShort(time), tofLong(time), timer(time){};

/*
bool Key::isPressed(bool key) {
  return q;
}

bool Key::isReleased(bool key) {
  return q;
}
*/

bool Key::longPressed(bool key) {
  rtLong.run(key);
  tofLong.run(rtLong.getQ() || ftLong.getQ() && key);
  ftLong.run(tofLong.getQ());
  return ftLong.getQ() && key;
}

bool Key::shortPressed(bool key) {
  rtShort.run(key);
  ftShort.run(key);
  tofShort.run(rtShort.getQ());
  return ftShort.getQ() && tofShort.getQ();
}

bool Key::doubleClick(bool key) {
  rtDoubleClick.run(key);
  ftDoubleClick.run(key);
  if(ftDoubleClick.getQ() && triggerDoubleClick) startTime = millis();
  if(ftDoubleClick.getQ() && !triggerDoubleClick) triggerDoubleClick = true;
  if(startTime != 0) {
    if(rtDoubleClick.getQ() && startTime + timer > millis()) {
      triggerDoubleClick = false;
      startTime = 0;
      return true;
    }
  }else return false;
  return false;
}
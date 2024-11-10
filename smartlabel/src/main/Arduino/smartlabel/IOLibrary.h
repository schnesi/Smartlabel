
// IOLibrary.h
// I/O function
// Created by Simon Schneider, 01. October 2024

#ifndef IOLibrary_h
#define IOLibrary_h

#include "Arduino.h"

// ---------------------------------------------------
// Class TOF
//
// An OFF delay timer that disables the "q" output 
// a specified number of milliseconds the trig inpu is disabled.
class TOF {
public:
  TOF(unsigned long interval);
  TOF();
  bool run(bool trig);
  bool getQ();
private:
  bool q;
  unsigned long startTime, time;
};

// ---------------------------------------------------
// Class TON
//
// An ON delay timer that enables the "q" output
// a specific number of milliseconds after the trig input is enabled.
class TON {
public:
  TON(unsigned long interval);
  TON();
  bool run(bool trig);
  void reset();
  bool getQ();
private:
  bool q;
  unsigned long startTime, time;
};

// ---------------------------------------------------
// Class Debounce
//
// Makes sure that the code is only triggered once per user input.
class Debounce {
public:
  Debounce(unsigned long interval);
  bool run(bool clk);
private:
  TOF tofClk;
  TON tonClk;
};

// ---------------------------------------------------
// R_TRIG
//
// Detect a rising edge
class R_TRIG {
public:
  void run(bool clk);
  bool getQ();
private:
  bool q;
  bool clkOld = false;
};

// ---------------------------------------------------
// F_TRIG
//
// Detect a falling edge
class F_TRIG {
public:
  void run(bool clk);
  bool getQ();
private:
  bool q;
  bool clkOld = false;
};

// ---------------------------------------------------
// class CTU
//
// Countup
class CTU {
public:
  unsigned long cv = 0; 
  unsigned long pv;
  void run(bool cu, bool reset, unsigned long pv);
  bool getQ();
private:
  bool q;
  R_TRIG rtCu;
};

// ---------------------------------------------------
// class CTD
//
// Countdown
class CTD {
public:
  unsigned long cv = 0;
  unsigned long pv;
  void run(bool cd, bool load, unsigned long pv);
  bool getQ();
private:
  bool q;
  R_TRIG rtCu;
};

// ---------------------------------------------------
// class Blink
//
// Without discription
class Blink {
public:
  Blink(unsigned long newTimeHigh, unsigned long newTimeLow);
  bool run(bool enable);
  bool getQ();
private:
  bool q;
  unsigned long startTime = millis(); 
  unsigned long timeHigh, timeLow, time;
};

// ---------------------------------------------------
// class Key
//
// Includes functions for pressed, released, long pressed, short pressed and double click
class Key {
public:
  Key(unsigned long time);
  /* to be done
  bool isPressed(bool key);
  bool isReleased(bool key);
  */
  bool longPressed(bool key);
  bool shortPressed(bool key);
  bool doubleClick(bool key);
private:
  bool q;
  TOF tofShort, tofLong;
  R_TRIG rtShort, rtLong, rtDoubleClick;
  F_TRIG ftShort, ftLong, ftDoubleClick;
  unsigned long startTime = 0;
  unsigned long timer;
  bool triggerDoubleClick = true;
};

#endif

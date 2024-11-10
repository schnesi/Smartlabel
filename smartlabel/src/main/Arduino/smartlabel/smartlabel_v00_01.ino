// ---------------------------------------------------
// Smartlabel
// ==========
// Simon Schneider
//
// ---------------------------------------------------

#include "IOLibrary.h"
#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <StreamUtils.h>
#include <Adafruit_GFX.h>    // Core graphics library
#include <Adafruit_ST7735.h> // Hardware-specific library for ST7735

// ---------------------------------------------------
// WiFi data
// Copy from Editor
const char* ssid= "your-ssid";
const char* password = "your-password";

// ---------------------------------------------------
// I/O-Mapping
#define TFT_CS 17
#define TFT_RST 21 
#define TFT_DC 20  // --> A0 on Display
#define SPI_MOSI 19
#define SPI_MISO 12
#define SPI_SCK 18
#define BUTTON_PIN 15

// ---------------------------------------------------
// Variables
const char* broker = "test.mosquitto.org";
unsigned int eepromAddress = 0;
unsigned int eepromSize = 512;
unsigned int serialNo = 38;
bool button;

// ---------------------------------------------------
// Instances
TON debbuging(200);
Debounce debouncer(20);
Key key(2000);
WiFiClient wifiClient;
PubSubClient mqtt(wifiClient);
Adafruit_ST7735 tft = Adafruit_ST7735(TFT_CS, TFT_DC, TFT_RST);

// ---------------------------------------------------
// Functions

// ---------------------------------------------------
// When message arrived from MQTT
void onReceive(char* topic, byte* payload, unsigned int length) {  
  Serial.print("Nachricht empfangen von Topic: ");
  Serial.println(topic);

  // Change payload to string
  String message;
  for (unsigned int i = 0; i < length; i++) {
    message += (char)payload[i];
  }

  Serial.print("Rohdaten der Nachricht: ");
  Serial.println(message);
  
  // Generrate an StaticJsonDocument
  StaticJsonDocument<512> doc;  // Change datasize for your project
  DeserializationError error = deserializeJson(doc, message);

  if (error) {
    Serial.print("JSON Parsing-Fehler: ");
    Serial.println(error.c_str());
    return; // Ends function if an error occurres
  }

  JsonObject data = doc["data"]["0"];
  displayData(data);
}

// ---------------------------------------------------
// Sends data request
void sendRequest() {
  JsonDocument doc;
  JsonObject object = doc.to<JsonObject>();

  object["header"] = "request";
  JsonObject data = object["data"].to<JsonObject>();
  data["smartlabelSN"] = serialNo;
  data["item"] = "dataSmartlabel";

  String jsonString;
  serializeJson(doc, jsonString);
  Serial.println(jsonString);

  const char* topic = "/smartlabel/smartlabel";
  mqtt.publish(topic, jsonString.c_str());
}

// ---------------------------------------------------
// Send order
void sendOrder() {
  JsonDocument doc;
  JsonObject object = doc.to<JsonObject>();

  object["header"] = "orderSmartlabel";
  JsonObject data = object["data"].to<JsonObject>();
  data["smartlabelSN"] = serialNo;
  data["item"] = "order";

  String jsonString;
  serializeJson(doc, jsonString);
  Serial.println(jsonString);

  const char* topic = "/smartlabel/smartlabel";
  mqtt.publish(topic, jsonString.c_str());
}

// ---------------------------------------------------
// Display Data
void displayData(JsonObject data) {

  JsonObject objectZero = data;

  tft.fillScreen(ST7735_BLACK);
  tft.setCursor(0, 0);
  
  Serial.println("Daten aus Objekt '0':");
  tft.println("Daten aus Objekt '0':");
  displayField("storageproduction", objectZero["storageproduction"].as<const char*>());
  displayField("materialname", objectZero["materialname"].as<const char*>());
  displayField("quantity", String(objectZero["quantity"].as<int>()).c_str());
  displayField("materialno", objectZero["materialno"].as<const char*>());
  displayField("smartlabelsn", objectZero["smartlabelsn"].as<const char*>());
  displayField("active", objectZero["active"].as<bool>() ? "true" : "false");
  displayField("measurementperiode", objectZero["measurementperiode"].as<const char*>()); // kann `null` sein
  displayField("sensor", objectZero["sensor"].as<bool>() ? "true" : "false");
}

// ---------------------------------------------------
// Function to Display the data
void displayField(const char* label, const char* value) {
  Serial.print(label);
  Serial.print(": ");
  Serial.println(value);

  tft.print(label);
  tft.print(": ");
  tft.println(value);
}

// ---------------------------------------------------
// Setup
void setup() {
  Serial.begin(115200);
  while(!Serial); // Wait for connection
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  WiFi.begin(ssid, password);
  while(WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(500);
  }

  Serial.print("Connected! IP address: ");
  Serial.println(WiFi.localIP());

  mqtt.setServer(broker, 1883);
  mqtt.connect("SmartlabelSN");
  mqtt.setCallback(onReceive);
  mqtt.subscribe("/smartlabel/smartlabel/38");

  tft.initR(INITR_144GREENTAB);
  tft.setRotation(1);
  tft.fillScreen(ST7735_BLACK);
  tft.setTextColor(ST7735_WHITE);
  tft.setTextSize(1);

  sendRequest();
}

// ---------------------------------------------------
// Loop
void loop() {
  // Input
  button = !digitalRead(BUTTON_PIN);
  bool dbButton = debouncer.run(button);

  // Process
  if(key.shortPressed(dbButton)) sendOrder();
  if(key.longPressed(dbButton)) sendRequest();
  mqtt.loop();

  // Output

  // Debug
  if(debbuging.run(true)) {
    debbuging.reset();
    Serial.print((String)millis() + ": ");
    Serial.println(dbButton);
  }
}


#ifndef Socket8266_h
#define Socket8266_h

#include <Arduino.h>
#include <ESP8266WiFi.h>

class Socket8266 {
  public:

    Socket8266();

    void connectWifi(String ssid, String password);
    void begin();
    void end();

    bool isConnected();
    bool send(String data);
    String receive();

  private:

    WiFiServer server = WiFiServer(80);
    WiFiClient client;
    
};

#endif

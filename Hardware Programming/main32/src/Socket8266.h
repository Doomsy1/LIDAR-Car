#ifndef Socket8266_h
#define Socket8266_h

#include <Arduino.h>
#include <WIfi.h>

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

    WiFiServer server = WiFiServer(8080);
    WiFiClient client;
    
};

#endif

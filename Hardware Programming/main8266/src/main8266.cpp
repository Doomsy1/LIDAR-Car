#include "Socket8266.h"
#include "Lidar8266.h"

// String ssid = "Ario";
// String password = "ArioBario@0";

String ssid = "Virgin AIG_EXT";
String password = "ginggiang0";

int port = 80;
int PACKET_SIZE = 10;
int SPS = 30;

unsigned long currMillis;
unsigned long prevMillis;
long universalMillis = 0;


int SECOND = 1000;

int millisPerScan = (SECOND/SPS);
int millisPerPacket = millisPerScan*PACKET_SIZE;


int receiveMillis = 100;



int lidarTally = 0;
int receiveTally = 0;
int sendTally = 0;

String lidarReadings = "&";

Socket8266 socket;
Lidar8266 lidar;

void setup() {
  

  Serial.begin(115200);
  socket.connectWifi(ssid, password);
  socket.begin();

  currMillis = millis();
  prevMillis = millis();
  universalMillis = 0;

}




void loop() {
  if (!socket.isConnected()) {
    Serial.println("Client disconnected.");
    socket.begin();

    lidarTally = 0;
    receiveTally = 0;
    sendTally = 0;
    universalMillis = 0;
  }

  digitalWrite(2, HIGH);
  currMillis = millis();

  universalMillis += (currMillis-prevMillis);

  if(universalMillis/millisPerScan > lidarTally){
    lidarReadings+=lidar.read()+"|";
    lidarTally++;
  }


  if(universalMillis/millisPerPacket > sendTally){
    digitalWrite(2, LOW);
    socket.send(lidarReadings);
    Serial.println(lidarReadings);
    lidarReadings = "&";
    sendTally=universalMillis/millisPerPacket;
  }
  // socket.send("test");
  // delay(1000);


  // Serial.println(universalMillis);
  prevMillis = currMillis;

}



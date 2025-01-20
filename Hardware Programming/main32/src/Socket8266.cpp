#include "Socket8266.h"





Socket8266::Socket8266(){} 


void Socket8266::connectWifi(String ssid, String password){

  WiFi.begin(ssid, password);
  pinMode(2, OUTPUT);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Serial.println("Connected to the WiFi network");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());

  server.begin();
}

void Socket8266::begin(){
  while (!client){
  client = server.accept();
  digitalWrite(2, HIGH);
  delay(200);
  digitalWrite(2, LOW);
  delay(200);
  }
  if (client){
    Serial.println("Client connected.");
  }
  else{
    Serial.println("Client did not begin.");
  }

}

bool Socket8266::send(String data){
  if(client){
    client.println(data);
    return true;
  }
  else{
    Serial.println("Client not connected.");
    return false;
  }
}

String Socket8266::receive() {
    if (client.available()) {
        char c = client.read(); 


        return String(c); 
    
    return "";  
}
}

void Socket8266::end(){
  if (client ){
    client.stop();
    Serial.println("Client disconnected.");
  }
}

bool Socket8266::isConnected(){
  return client;
}





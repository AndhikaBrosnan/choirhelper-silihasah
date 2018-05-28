#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>

#define WIFI_SSID "choirhelper"
#define WIFI_PASSWORD "choirhelper"

#define FIREBASE_HOST "choirhelperfix.firebaseio.com"
#define FIREBASE_AUTH "dyXffyXBaBbWaMjr9OjeFV06xs6VdgwDijUn7WT8"

#include "complex.h" //the magic happens here, keep out!
#include <ESP8266WiFi.h>

WiFiClient client;

int INPUT_PIN = D0;    //input pin


int NSAMPLES = 64;    // number of readings of A0 for each loop execution, coba ubah jadi 32 or 16
int SAMPLINGFREQ = 768;    // number of readings per second (default 704)
// pins of the lights, pins with PWM were chosen in case you want to use different brightness levels
int led_1 = D1;
int led_2 = D2;
int led_3 = D3;
int led_4 = D4;
int led_5 = D5;
int led_6 = D6;

WiFiServer server(80);

void setup() {
  Serial.begin(9600); 
  WiFi.begin(WIFI_SSID,WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }  

  server.begin();
  WiFiClient client = server.available();
  if(client){
    Serial.println("Client connected");
  }
  
  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
  Firebase.setInt("frequency",0);
  
  pinMode(led_1,OUTPUT);
  pinMode(led_2,OUTPUT);
  pinMode(led_3,OUTPUT);
  pinMode(led_4,OUTPUT);
  pinMode(led_5,OUTPUT);
  pinMode(led_6,OUTPUT);
}

void loop() {
  Complex* inputSignal = (Complex*)malloc(sizeof(Complex)*NSAMPLES);
  double ti=millis();
  for(int k = 0; k<NSAMPLES; k++){
    inputSignal[k] = Complex((double)analogRead(INPUT_PIN), 0);
    delayMicroseconds(1296);    // wait to reach the frequency of 704 samples per second approx.
  }
  double tf=millis();
  Serial.print("Sampling Frequency : ");    // print on the monitor the actual frequency with which the sample was taken
  Serial.println(NSAMPLES/((tf-ti)/1000.0));
  
  normalize(inputSignal,NSAMPLES);   // subtract the average of the data to each data
  double maxFreq = maxFrequency(inputSignal, NSAMPLES);    // you get the frequency of greatest amplitude
  free(inputSignal);    //the sample memory is freed
  
  Serial.print("Frecuencia maxima : ");   //print the maximum frequency on the monitor
  Serial.println(maxFreq);

//  Firebase.setInt("frequency",maxFreq);
  
  
  //The logic to determine frequencies, the range is still customize which don't determine chord, yet  
  if (maxFreq < 100){
    digitalWrite(led_1,LOW);
    digitalWrite(led_2,LOW);
    digitalWrite(led_3,LOW);
    digitalWrite(led_4,LOW);
    digitalWrite(led_5,LOW);    
    digitalWrite(led_6,LOW);
    delay(500);
  }else if(maxFreq>=100 && maxFreq< 200){
//    Firebase.setInt("frequency",100);
    digitalWrite(led_3,HIGH);
    delay(500);
  }else if(maxFreq>=200 && maxFreq < 300){
//    Firebase.setInt("frequency",200);  
    digitalWrite(led_4,HIGH);
    delay(500);
  }else if(maxFreq>=300 && maxFreq < 400){
//    Firebase.setInt("frequency",300);
    digitalWrite(led_5,HIGH);
    delay(500);
  }else if(maxFreq>=400 ){
//    Firebase.setInt("frequency",400);  
    digitalWrite(led_6,HIGH);
    delay(500);
  }
  
  delay(1000);
  
//  lightByTargetFrequency(198, maxFreq, SAMPLINGFREQ/NSAMPLES);    // se espera recibir 198hz, prender/apagar luces segun corresponda
}  

void _fft(Complex* buf, Complex* out, int n, int s){   
  if(s<n){
    _fft(out, buf, n, s*2);
    _fft(out+s, buf+s, n, s*2);
    for (int i = 0; i<n; i+=2*s) {
      double aux = -3.1416*(double)i/(double)n;
      Complex t = Complex(cos(aux),sin(aux))*out[i+s];
      buf[i/2] = out[i]+t;
      buf[(i+n)/2] = out[i]-t;
    }
  }
}
 
void fft(Complex* buf, int n){
  Complex* out = (Complex*)malloc(sizeof(Complex)*n);
  for (int i = 0; i < n; i++) 
    out[i] = buf[i];
  _fft(buf, out, n, 1);
  free(out);
}

double maxFrequency(Complex* buf, int n){
  fft(buf, n);
  double maxAmp = 0;
  int maxAmpIndex=0;
  for(int k = 0; k<=NSAMPLES/2; k++){    //the second half of the arrangement obtained in the FFT does not provide info ...
    double amp = 2*sqrt(pow(buf[k].real()/(double)NSAMPLES,2)+pow(buf[k].imag()/(double)NSAMPLES,2));    //get the amplitude
    if(amp>maxAmp){
      maxAmp = amp;
      maxAmpIndex = k;
    }
  }
  double maxfrec = maxAmpIndex*(SAMPLINGFREQ/2)/(NSAMPLES/2);
  return maxfrec;
}

void normalize(Complex* buf, int n){  //subtract the average of the data to eliminate the DC component of the signal
  double sum=0;
  for(int k=0; k<n; k++){
    sum=sum+buf[k].real();
  }
  double mean=sum/(double)n;
  for(int k=0; k<n; k++){
    buf[k]=Complex(buf[k].real()-mean,0);
  }
}

void lightByTargetFrequency(int frecuenciaObjetivo, int frecuenciaLeida, int rangoError){
  int resta = frecuenciaObjetivo-frecuenciaLeida;
  if(resta<0){    // Received frequency is higher than expected
    if(resta==-rangoError){
      setLights(0,0,0,255,0,0);
    }
    else if(resta==-2*rangoError){
      setLights(0,0,0,0,255,0);
    }
    else{
      setLights(0,0,0,0,0,255);
    } 
  }
  else if(resta==0){    // received frequency is equal to the expected
    setLights(0,0,255,255,0,0);
  }
  else{    // received frequency is lower than expected
    if(resta==rangoError){
      setLights(0,0,255,0,0,0);
    }
    else if(resta==2*rangoError){
      setLights(0,255,0,0,0,0);
    }
    else{
      setLights(255,0,0,0,0,0);
    }
  }
}

void setLights(byte rdBrightness, byte riBrightness, byte viBrightness, byte vdBrightness, byte aiBrightness, byte adBrightness ){
  analogWrite(led_1, rdBrightness);
  analogWrite(led_2, riBrightness);
  analogWrite(led_3, viBrightness);
  analogWrite(led_4, vdBrightness);
  analogWrite(led_5, aiBrightness);
  analogWrite(led_6, adBrightness);
}

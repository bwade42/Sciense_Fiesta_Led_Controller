//Written by Brandon Wade and David Di Donato
//Recieves values in the format, <Hello,R,G,B,Strength,Command>
//Recieves values in the format, <String,int(0-255),int(0-255),int(0-255),int(0-24),int(1-2)>

#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
#include <avr/power.h>
#endif

#define PIN 13
Adafruit_NeoPixel strip = Adafruit_NeoPixel(24, PIN, NEO_GRB + NEO_KHZ800);

 
//Define variables that are going to be used by the program  
  int R = 0;
  int G = 0;
  int B = 0;
  int Strength = 0;
  int Command = 0;
  
//These are variables used for reading the arrays
const byte numChars = 32;
char receivedChars[numChars];
char tempChars[numChars];        // temporary array for use when parsing

// variables to hold the parsed data
char messageFromPC[numChars] = {0};
boolean newData = false;


//---------------------------------------------------------------------------------------------------
//Do all of the main loop things and the setup--------------------------------------------------------
void setup() 
{
  // put your setup code here, to run once:
  //Begin serial communication at 9600 baud
  Serial.begin(9600); 

  //Initialize that strip and whatnot
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'
  
}

void loop() 
{
  // Put your main code here, to run repeatedly:

  // Run the program
  getsome();
  colormaker();

  //Debug (can comment out)
  //showParsedData();
}

//-----------------------------------------------------------------------------------------------------
//Recieve the message, with some end markers---------------------------------------------------------

void recvWithStartEndMarkers() {
    static boolean recvInProgress = false;
    static byte ndx = 0;
    char startMarker = '<';
    char endMarker = '>';
    char rc;

    while (Serial.available() > 0 && newData == false) {
        rc = Serial.read();

        if (recvInProgress == true) {
            if (rc != endMarker) {
                receivedChars[ndx] = rc;
                ndx++;
                if (ndx >= numChars) {
                    ndx = numChars - 1;
                }
            }
            else {
                receivedChars[ndx] = '\0'; // terminate the string
                recvInProgress = false;
                ndx = 0;
                newData = true;
            }
        }

        else if (rc == startMarker) {
            recvInProgress = true;
        }
    }
}

//----------------------------------------------------------------------------------------------------
// split the data into its parts----------------------------------------------------------------------

void parseData() {      

    char * strtokIndx; // this is used by strtok() as an index

    strtokIndx = strtok(tempChars,",");      // get the first part - the string
    strcpy(messageFromPC, strtokIndx); // copy it to messageFromPC
 
    strtokIndx = strtok(NULL, ","); // this continues where the previous call left off
    R = atoi(strtokIndx);     // convert this part to an integer

    strtokIndx = strtok(NULL, ","); // this continues where the previous call left off
    G = atoi(strtokIndx);     // convert this part to an integer

    strtokIndx = strtok(NULL, ","); // this continues where the previous call left off
    B = atoi(strtokIndx);     // convert this part to an integer

    strtokIndx = strtok(NULL, ","); // this continues where the previous call left off
    Strength = atoi(strtokIndx);     // convert this part to an integer
    
    strtokIndx = strtok(NULL, ","); // this continues where the previous call left off
    Command = atoi(strtokIndx);     // convert this part to an integer

    
}

//--------------------------------------------------------------------------------------------------
//Get some data from the serial buffer-------------------------------------------------------------
void getsome()
{
    recvWithStartEndMarkers();
    if (newData == true) 
    {
        strcpy(tempChars, receivedChars);
            // this temporary copy is necessary to protect the original data
            //   because strtok() used in parseData() replaces the commas with \0
        parseData();

        newData = false;
    }
}


//----------------------------------------------------------------------------------------------------
//Changes based of the the strength of the shaking of the tablet--------------------------------------

void strengthWipe(uint32_t c, uint8_t wait) 
{
  for(int j=0; j<256*5; j++) 
  { // 5 cycles of all colors on wheel
    for(int i=0; i< Strength; i++) 
    {
      strip.setPixelColor(i, Wheel(((i * 256 / Strength) + j) & 255));
      //delay(10);
    }
    strip.show();
    //delay(10);
  
  }  
}

//---------------------------------------------------------------------------------------------------
// Fill the dots one after the other with a color---------------------------------------------------
void colorWipe(uint32_t c, uint8_t wait) 
{  
        
   
  for(uint16_t i=0; i<24; i++) 
  {
    strip.setPixelColor(i, c);
    strip.show();
   
    delay(wait);
  }
   
}

//----------------------------------------------------------------------------------------------------
//makes the values in the format that the library likes
void colormaker()
{
  // For basic color
        if(Command == 1)
        {
        colorWipe(strip.Color(R, G, B), 50); 
        colorWipe(strip.Color(0, 0, 0), 50);
        }
  // For the strength
        if(Command == 2)
        {
        strengthWipe(strip.Color(R, G, B), 200);
        delay(2000);
        strengthWipe(strip.Color(0, 0, 0), 100);
        }
        if(Command == 3)
        {
          saturnWipe(strip.Color(255, 255, 255), 100);
          saturnWipe(strip.Color(255, 255, 0), 100);
          saturnWipe(strip.Color(255, 0, 0), 100);
        }
        
  //For Neither
        if(Command !=1 || Command !=2 || Command != 3)
        {
          //Some sort of debug lighting pattern can go here ***************************************
          strengthWipe(strip.Color(255,255,255), 100);
        }
}

//---------------------------------------------------------------------------------------------------
//Print out what we got------------------------------------------------------------------------------
void showParsedData() 
{
    Serial.println("=============");
    Serial.print("Note:    ");
    Serial.println(messageFromPC);  
    Serial.print("Red:     ");
    Serial.println(R);
    Serial.print("Green:   ");
    Serial.println(G);
    Serial.print("Blue:    ");
    Serial.println(B);
    Serial.print("Strength:");
    Serial.println(Strength);
    Serial.print("Command: ");
    Serial.println(Command);
    Serial.println("=============");    
}
//-------------------------------------------------------------------------------------------------------
//Rainbow Animation--------------------------------------------------------------------------------------

  uint32_t Wheel(byte WheelPos) 
  {
    
    WheelPos = 255 - WheelPos;
   
    if(WheelPos < 85) {
      return strip.Color(255 - WheelPos * 3, 0, WheelPos * 3);
      
    }
    if(WheelPos < 170) {
      WheelPos -= 85;
      return strip.Color(0, WheelPos * 3, 255 - WheelPos * 3);
    }
  }

//-------------------------------------------------------------------------------------------------------
//Saturn Animation Command "3" white, red, orange, brown, and yellow---------------------------------------

void saturnWipe(uint32_t c, uint8_t wait) 
  {  
          
    for(uint16_t i=0; i<Strength; i++) 
    {
      strip.setPixelColor(i, c);
      strip.show();
     
      delay(wait);
    }
     
  }



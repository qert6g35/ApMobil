#define gatePin 10
#define clockPin 11
#define dataPin 12
//                 define rows pins numbers
#define R1 7
#define R2 6
#define R3 5
#define R4 4
#define R5 3
#define R6 2
#define R7 13
#define R8 9
//                 define columns pins numbers in shift register
#define C1 1
#define C2 2
#define C3 4
#define C4 8
#define C5 16 
#define C6 32
#define C7 64
#define C8 128
//                 define buttons
#define Up    A4
#define Down  A5
#define Left  A2
#define Right A3

//                                    helper
bool   DispMatrix[8][8] = {//       1.[] col   2.[]row
  {0,0,0,1,1,0,0,0},
  {0,0,1,1,1,1,0,0},
  {0,0,1,1,1,1,0,0},
  {0,0,0,1,1,0,0,0},
  {0,0,0,1,1,0,0,0},
  {0,1,0,1,1,0,1,0},
  {1,0,1,1,1,1,0,1},
  {1,0,1,1,1,1,0,1}
  };

byte   CMatrix[8] = {
  1,2,4,8,16,32,64,128
};
//                               contains last postion of snake


byte getColumn(byte row){
  byte numberForColumnDisplay = 0;
  for(int i = 0; i < 8; i++){
    numberForColumnDisplay = numberForColumnDisplay +  DispMatrix[i][row] * CMatrix[i];
  }
  return numberForColumnDisplay;
}


void SelectRow(byte row){
  digitalWrite(R1,HIGH);
  digitalWrite(R2,HIGH);
  digitalWrite(R3,HIGH);
  digitalWrite(R4,HIGH);
  digitalWrite(R5,HIGH);
  digitalWrite(R6,HIGH);
  digitalWrite(R7,HIGH);
  digitalWrite(R8,HIGH); 
  switch(row){
  case 0:
    digitalWrite(R1,LOW);
    break;
  case 1:
    digitalWrite(R2,LOW);
    break;
  case 2:
    digitalWrite(R3,LOW);
    break;
  case 3:
    digitalWrite(R4,LOW);
    break;
  case 4:
    digitalWrite(R5,LOW);
    break;
  case 5:
    digitalWrite(R6,LOW);
    break;
  case 6:
    digitalWrite(R7,LOW);
    break;
  case 7:
    digitalWrite(R8,LOW);
    break;
  default:
    break;
    
  }
  
}


void SelectColumns(byte data){
  digitalWrite(gatePin,LOW);
  shiftOut(dataPin, clockPin ,MSBFIRST, data);
  digitalWrite(gatePin,HIGH);
}

byte checkButtons(){
   if(digitalRead(Up)){
      return 1;
   }
   if(digitalRead(Down)){
      return 2;
   }
   if(digitalRead(Left)){
      return 3;
   }
   if(digitalRead(Right)){
      return 4;
   }
   return 0;
}
// tutaj działa na skret lewo prawo 1 i 2 jest nie używane z tego co patrzyłem 

//         Up 1
//  Left <- 3   4 -> Right    
//       Down 2





class Snake{//                                                     snake class
  private:
    byte body[2][64];//                            0 for->col->x, 1 for->row->y
    byte Candy[2][2];//          1.[] which 2.[0] for x 2.[1]for y
    byte leng;
    byte WayToGO;
  public:
    Snake();
    bool moveSnake(byte where);
    void makeDisplay();
    void addPart(byte x, byte y);
    bool HitSnake(byte col,byte row);
    bool AteCandy(byte col, byte row);
    byte whereToGo(byte lastButton);
};


Snake::Snake(){
  body[0][0] = 4;//                  its for column x                       
  body[1][0] = 4;//                  its for row y
  body[0][1] = 4;                       
  body[1][1] = 5;
  Candy[0][0] = 2;
  Candy[0][1] = 1;
  Candy[1][0] = 4;
  Candy[1][1] = 6;
  leng = 2;
  WayToGO = 1;
}

void Snake::makeDisplay(){
 for(int i = 0; i<8 ; i++)
  for(int j = 0; j<8 ; j++)
    DispMatrix[i][j] = 0;
 for(int len = leng; len > 0; len--){
   DispMatrix[body[0][len-1]][body[1][len-1]] = 1;
 }
 DispMatrix[Candy[0][0]][Candy[0][1]] = 1;
 DispMatrix[Candy[1][0]][Candy[1][1]] = 1;
}

bool Snake::moveSnake(byte where){//                      MOVE SNAKE
  //Serial.println(where);
  bool didAte;
  byte NewPart[2];
  switch(where){
    case 1://                                             GO UP
      if(body[0][0]<1) return 1;
      if(HitSnake(body[0][0] - 1,body[1][0])) return 1;
      didAte = AteCandy(body[0][0] - 1,body[1][0]);
      NewPart[0] =  body[0][leng - 1];
      NewPart[1] =  body[1][leng - 1];
      for(int len = leng - 1; len > 0; len--){
        body[0][len] = body[0][len - 1];
        body[1][len] = body[1][len - 1];
      }
      if(didAte){
        addPart(NewPart[0],NewPart[1]);
      }
      body[0][0]--;
    return 0;



    
    case 2://                                              GO DOWN
      if(body[0][0]>6) return 1;
      if(HitSnake(body[0][0] + 1,body[1][0])) return 1;
      didAte = AteCandy(body[0][0] + 1,body[1][0]);
      NewPart[0] =  body[0][leng - 1];
      NewPart[1] =  body[1][leng - 1];
      for(int len = leng - 1; len > 0; len--){
        body[0][len] = body[0][len - 1];
        body[1][len] = body[1][len - 1];
      }
      if(didAte){
        addPart(NewPart[0],NewPart[1]);
      }
      body[0][0]++;
    return 0;


    
    case 4://                                          GO LEFT
      if(body[1][0]<1) return 1;
      if(HitSnake(body[0][0],body[1][0]-1)) return 1;
      didAte = AteCandy(body[0][0],body[1][0] -1);
      NewPart[0] =  body[0][leng - 1];
      NewPart[1] =  body[1][leng - 1];
      for(int len = leng - 1; len > 0; len--){
        body[0][len] = body[0][len - 1];
        body[1][len] = body[1][len - 1];
      }
      if(didAte){
        addPart(NewPart[0],NewPart[1]);
      }
      body[1][0]--;
    return 0;

    
    case 3://                                             GO RIGHT
      if(body[1][0]>6) return 1;
      if(HitSnake(body[0][0],body[1][0] + 1)) return 1;
      didAte = AteCandy(body[0][0],body[1][0]+1);
      NewPart[0] =  body[0][leng - 1];
      NewPart[1] =  body[1][leng - 1];
      for(int len = leng - 1; len > 0; len--){
        body[0][len] = body[0][len - 1];
        body[1][len] = body[1][len - 1];
      }
      if(didAte){
        addPart(NewPart[0],NewPart[1]);

      }
      body[1][0]++;
    return 0;

    
    default:
    return 0;
  }
  
}

void Snake::addPart(byte x, byte y){
  if(leng < 62){
    body[0][leng] = x;
    body[1][leng] = y;
    leng++; 
  }
}

bool Snake::HitSnake(byte col,byte row){
  for(int len = leng - 1; len > 0; len -- ){
    if((body[0][len] == col)&&(body[1][len] == row)) return 1;
  }
  return 0;
}

bool Snake::AteCandy(byte col, byte row){
  byte newX = 0,newY = 0;
  randomSeed(analogRead(0));
  byte randomCandyPoze = random(64-leng-4);
  if((Candy[0][0] == col)&&(Candy[0][1] == row)){//[0][x/y]
    while(randomCandyPoze > 0){
      if(newX >= 7){
        newY++;
        newX = 0;
      }else{
        newX++;
      }
     if(DispMatrix[newY][newX] == 0)randomCandyPoze--;
    }
    Candy[0][0] = newX;
    Candy[0][1] = newY;

    Serial.print(newX);
    Serial.println(newY);
    
    return 1;
  }
    if((Candy[1][0] == col)&&(Candy[1][1] == row)){//[1][x/y]
      while(randomCandyPoze > 0){
      if(newX >= 7){
        newY++;
        newX = 0;
      }else{
        newX++;
      }
      if(DispMatrix[newY][newX] == 0) randomCandyPoze--;
    }
    Candy[1][1] = newX;
    Candy[1][0] = newY;
    
    Serial.print(newX);
    Serial.println(newY);
    
    return 1;
  }
  return 0;
}

byte Snake::whereToGo(byte lastButton){
  switch(lastButton){
    case 3:
    if(WayToGO == 1){
      WayToGO = 3;
      return WayToGO;
      }
    if(WayToGO == 2){
      WayToGO = 4;
      return WayToGO;
      }
    if(WayToGO == 3){
      WayToGO = 2;
      return WayToGO;
      }
    if(WayToGO == 4){
      WayToGO = 1;
      return WayToGO;
      }
    case 4:
    if(WayToGO == 1){
      WayToGO = 4;
      return WayToGO;
      }
    if(WayToGO == 2){
      WayToGO = 3;
      return WayToGO;
      }
    if(WayToGO == 3){
      WayToGO = 1;
      return WayToGO;
      }
    if(WayToGO == 4){
      WayToGO = 2; 
      return WayToGO;
      }
    default:
    return WayToGO;
  }
}

Snake * snake = new Snake();
unsigned long Time;

//                                                                        SetUp 

void setup() {
//                       setting pinMode's for 74HC595 ShiftRegister 
  pinMode(gatePin,OUTPUT);
  pinMode(clockPin,OUTPUT);
  pinMode(dataPin,OUTPUT);
//                       setting pinMode's for 74HC595 ShiftRegister 
  pinMode(R1,OUTPUT);
  pinMode(R2,OUTPUT);
  pinMode(R3,OUTPUT);
  pinMode(R4,OUTPUT);
  pinMode(R5,OUTPUT);
  pinMode(R6,OUTPUT);
  pinMode(R7,OUTPUT);
  pinMode(R8,OUTPUT);
//                       setting buttons

  pinMode(Up, INPUT);
  pinMode(Down, INPUT);
  pinMode(Left, INPUT);
  pinMode(Right, INPUT);
//                       turning off all matrix

//                       set all Columns on LOW
  SelectColumns(255);
//                       set all Rows on High
  digitalWrite(R1,LOW);
  digitalWrite(R2,LOW);
  digitalWrite(R3,LOW);
  digitalWrite(R4,LOW);
  digitalWrite(R5,LOW);
  digitalWrite(R6,LOW);
  digitalWrite(R7,LOW);
  digitalWrite(R8,LOW);

  delay(3000);

//                       set all Columns on LOW
  SelectColumns(0);
//                       set all Rows on High
  digitalWrite(R1,HIGH);
  digitalWrite(R2,HIGH);
  digitalWrite(R3,HIGH);
  digitalWrite(R4,HIGH);
  digitalWrite(R5,HIGH);
  digitalWrite(R6,HIGH);
  digitalWrite(R7,HIGH);
  digitalWrite(R8,HIGH);

  Serial.begin(9600);
  Time = millis();
}

void Show(){
  for(int i = 0;i<8;i++){
     SelectColumns(0);
     SelectRow(i);
     SelectColumns(getColumn(i));
  }
}

byte lastButton = 0;
byte pom;
byte ifCandyCollected;

void loop() {
  pom = checkButtons();
      
  if(pom){
    lastButton = pom;// system of cheacking button (works)
  }
  if(Time + 1000 < millis()){
    if(snake->moveSnake(snake->whereToGo(lastButton))){
      delete snake;
      snake = new Snake();
    }
    snake->makeDisplay();
    Time = millis();
    lastButton = 0;
  }
  Show();// system of showing on board (works)
}
// end of file

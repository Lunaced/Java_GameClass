/**
  *
  * Beschreibung: Implementierung von den Attributen der Klasse "The Game" und den dazugehoerigen Methoden usw usw.
  *
  * @version 1.2 vom 14.12.2016
  * @author  Dennis und Jan
  */
  


public class TheGame {
  // Anfang Attribute
  private boolean ende;
  private int spielerAnzahl;
  private final int MAX_SPIELERANZAHL = 4;
  private boolean istGestartet;
  private int minKartenAblegen;
  private int mussAblegen;
  private int maxHandkarten;
  private Queue<Spieler> spieler = new Queue();
  private Queue<Spieler> spielerFertig = new Queue();
  private Stack<Karte> aufwaerts1 = new Stack();
  private Stack<Karte> aufwaerts2 = new Stack();
  private Stack<Karte> abwaerts1 = new Stack();
  private Stack<Karte> abwaerts2 = new Stack();
  private Deck deck = new Deck();
  // Ende Attribute
  
  // Anfang Methoden
  public boolean zugDarfBeendetWerden(){
    if (!spieler.isEmpty()) {
      Spieler player = spieler.front() ;
      if (player.getAbgelegteKarten() >= minKartenAblegen)  {
        return true;  
      }
    } // end of if
    
    return false;
  }
  
  public TheGame() {
    spielerAnzahl = 0;
    aufwaerts1.push(new Karte(1));
    aufwaerts2.push(new Karte(1));
    abwaerts1.push(new Karte(100));
    abwaerts2.push(new Karte(100));
  }
  
  public int getspielerAnzahl() {
    return spielerAnzahl;
  }
  
  public int getMAX_SPIELERANZAHL() {
    return MAX_SPIELERANZAHL;
  }
  
  public void setspielerAnzahl(int spielerAnzahl) {
    this.spielerAnzahl = spielerAnzahl;
  }
  
  public boolean getIstGestartet() {
    return istGestartet;
  }
  
  public int getMinKartenAblegen() {
    return minKartenAblegen;
  }
  
  public void setMinKartenAblegen(int pminKartenAblegen) {
    minKartenAblegen = pminKartenAblegen;
  }
  
  public int getMussAblegen() {
    return mussAblegen;
  }
  
  public int getMaxHandkarten() {
    return maxHandkarten;
  }
  
  
  public boolean spielerHinzufuegen(Spieler pSpieler){
    
    if(pSpieler != null){
      spieler.enqueue(pSpieler);
      spielerAnzahl++;
      return true;
    }
    return false;
    
  }
  
  
  /*
      Loescht oberste Karte vom deck und fuellt die Hand voll auf
      @param anzahlHandkarten Anzahl der aktuellen Handkarten des Spielers
  @param handkarten Liste der Handkarten des Spielers 
  @author Jan Michnia & Dennis Guenther
  @version 28.11.2016  
      */
      
  public Karte ziehen(){
    if (!deck.isEmpty()) {
      Karte pKarte = deck.ziehen();
      return pKarte;
    } // end of if
    Karte oKarte = new Karte(0);
    return oKarte;   
  }
  
  
  /*
      setzt die benoetigte maxAnzahl an Handkarten
  @param spielerAnzahl Die Anzahl der spielenden Spieler
      @author Jan Michnia & Dennis Guenther
      @version 28.11.2016
      */
      
  private void setMaxHandkarten(){
    switch (spielerAnzahl) {
      case 1 : 
        this.maxHandkarten = 8; 
        break;
      case 2 : 
        this.maxHandkarten = 7;
        break;
      case 3 : case 4 : case 5 : 
        this.maxHandkarten = 6;
        break;      
      default: 
        this.maxHandkarten = -1;
    } // end of switch
  }
      //  
      /** @version 1.0 vom 28.11.16
      @author Justus Overbeck
      @return Ausgabe : Name des austretenden Spielers
      Die Methode prueft ob ein Spieler im Spiel ist, loescht ihn aus der spieler falls dies der Fall ist, 
      gibt die uebrigen Handkarten zum Einmischen in den Nachdeck 
      (und reduziert die Spielerzahl um 1).
      */
  public void austreten (Spieler pSpieler){
    spieler.dequeue();
    System.out.println(pSpieler.getName()+ "hat das Spiel verlassen");
  }
      
      /**
      *
      * Die Starten Methode wird ausgefuehrt nachdem die gewuenschte Anzahl Spieler beigetreten ist. Sie bereitet die 
      * Ausfuehrung des Spiels vor und legt alle noch nicht festgelegten, noetigen Parameter fest.
      *                          
      * @param spielerAnzahl ist die Anzahl der angemeldeten Clients, die Handkartenanzahl ist davon abhaengig
      * @return -
      */
  public void starten() { 
    setMaxHandkarten();
    austeilen();
    printAllPlayersDEBUG();
  }
      
      /**
      * Der vorderste Spieler wird hinten in die Queue eingereiht und dann vorne aus
      * der Queue geloescht.
      *
      * @version 1.0 vom 28.11.2016
      * @author Finn Kemmling
      */
      
  public void naechsterSpieler(){
    spieler.enqueue(spieler.front());
    spieler.dequeue();
  }
      
  public boolean verloren(){         // Am Anfang vom Zug ueberpruefen
    if (!spieler.isEmpty()) {
      Spieler player = spieler.front();
      int gueltige = 0;
      int fehlende = getMinKartenAblegen() - player.getAbgelegteKarten();
      if (player.gibAnzahlHandkarten()>0) {
        
        for (int i=0;i<player.gibAnzahlHandkarten();i++) {
          for ( int stapelNr = 0 ; stapelNr < 4 ; stapelNr++ ) {
            if (zugGueltig(stapelNr, i)){
              gueltige++;              
            }
          }
        }
        
        if (gueltige<fehlende) {
          return true;
        } // end of if
      } // end of if
    } // end of if
    
    return false;
  }
      
  public void ablegen(int pStapelNr, int pKartenNr){
    Spieler player = spieler.front();
    if( pStapelNr != 0 && pKartenNr != 0 ){          // eigentlich nicht noetig, weil auch in Spielablauf kontrolliert
      switch (pStapelNr) {
        case  1: 
          aufwaerts1.push(player.ablegen(pKartenNr));
          break;
        case  2: 
          aufwaerts2.push(player.ablegen(pKartenNr));
          break;
        case  3: 
          abwaerts1.push(player.ablegen(pKartenNr));
          break;
        case  4: 
          abwaerts2.push(player.ablegen(pKartenNr));
          break;
        default: 
        
      } // end of switch  
    }
  }
      
  public boolean zugGueltig(int pStapelNr, int pKartenNr){
    if (!spieler.isEmpty()) {
      Spieler player = spieler.front();
      switch (pStapelNr) {
        case 1 : 
          if (player.gibKartenWert(pKartenNr) > aufwaerts1.top().getWert()) {
            return true;
          }else {
            return false;
          }
        case 2 : 
          if (player.gibKartenWert(pKartenNr) > aufwaerts2.top().getWert()) {
            return true;
          }else{
            return false;
          }
        case 3 : 
          if (player.gibKartenWert(pKartenNr) < abwaerts1.top().getWert()) {
            return true;
          }else{
            return false;
          }
        case 4 :         
          if (player.gibKartenWert(pKartenNr) < abwaerts2.top().getWert()) {
            return true;
          }else{
            return false;
          }
        default: 
          return false;
      } // end of switch
    } // end of if
    
    return false;
  }
      
  public String zeigeHandkarten(){
    if (!spieler.isEmpty()) {
      return spieler.front().zeigeHandkarten();
    } // end of if
    return "Noch keine Spieler da";    
  }
      
      
      
  public String zeigeStapel(){
    
    String stapelAusgabe = "";
    
    if (!aufwaerts1.isEmpty()) {
      stapelAusgabe.concat(String.valueOf(aufwaerts1.top().getWert()));
    } else {
      stapelAusgabe.concat("0");
    } // end of if-else
    stapelAusgabe.concat(", ");
    if (!aufwaerts2.isEmpty()) {
      stapelAusgabe.concat(String.valueOf(aufwaerts2.top().getWert()));
    } else {
      stapelAusgabe.concat("0");
    } // end of if-else
    stapelAusgabe.concat(", ");
    if (!abwaerts1.isEmpty()) {
      stapelAusgabe.concat(String.valueOf(abwaerts1.top().getWert()));
    } else {
      stapelAusgabe.concat("0");
    } // end of if-else
    stapelAusgabe.concat(", ");
    if (!abwaerts2.isEmpty()) {
      stapelAusgabe.concat(String.valueOf(abwaerts2.top().getWert()));
    } else {
      stapelAusgabe.concat("0");
    } // end of if-else
    
    return stapelAusgabe;
    
  }
      
  public boolean pruefeGewonnen(){
    if (deck.istLeer()&&spieler.isEmpty()) {
      return true;
    } // end of if
    return false;
  }
      
  public void printAllPlayersDEBUG() {              
    Queue<Spieler> nebenQ = new Queue();
    while (!spieler.isEmpty()) {
      Spieler player = spieler.front();
      System.out.println(player.zeigeHandkarten());
      nebenQ.enqueue(player);
      spieler.dequeue();
    } // end of while
    while (!nebenQ.isEmpty()) { 
      Spieler player = nebenQ.front();
      spieler.enqueue(player);
      nebenQ.dequeue();
    } // end of while
  }
      
      /**
      * Verteilt die Karten vom Kartenstapel an die verschiedenen
      * Spieler in der spieler, indem er beim 1. anfaengt,
      * dann dem 2.,3.,4. [...] auch jeweils eine Karte gibt bis
      * er allen eine Karte gegeben hat und wieder von vorne anfaengt,
      * bis jeder Spieler die durch 'maxHandkarten' festgelegte
      * Handkartenzahl hat.
      * @author Johannes Gundlach ueberarbeitet von Dennis und Jan
      * @Version: 1.12.16
      **/
  public void austeilen() {
    for (int i = 0; i < maxHandkarten; i++) {
      for (int j = 0; j < spielerAnzahl; j++) {
        Spieler player = spieler.front();
        player.ziehen(deck.ziehen());
        deck.pop();
      } // end of for
    } // end of for
  }
      
  public void zugBeenden(){
    naechsterSpieler();
  }
      
      // Ende Methoden
      
} // end of class TheGame
    
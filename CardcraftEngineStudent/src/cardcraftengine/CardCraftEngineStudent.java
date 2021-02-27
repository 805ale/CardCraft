/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;

import cardcraftengine.presentation.HTMLTableOutput;
import cardcraftengine.presentation.TablePresentation;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Alexandra Vaida @w18001218
 */
public class CardCraftEngineStudent implements CardCraftInterface
{
     private String studentID ; 
    TablePresentation tableGenerator ; 
    private Map<String, Player> allPlayers ; // people who have played sorted by sc @w18001218
    private Set<Match>  allMatches ; //@w18001218
    private Map<String, Card>   allKnownCards ; //@w18001218
    private Map<String, Deck>   allKnownDecks; //@w18001218
 
    //--------------------------------------------------------------------------
    /**
     * 
     * @param studentID 
     */
    public CardCraftEngineStudent( String studentID  )
    { 
        super(); 
     
        this.studentID =  studentID ; 
        this.tableGenerator = new HTMLTableOutput(); 
         
        this.allPlayers    = new HashMap<>(); // people who have played sorted by sc //@w18001218
        this.allMatches    = new HashSet<>() ; //@w18001218
        this.allKnownCards = new HashMap<>(); //@w18001218
        this.allKnownDecks = new HashMap<>(); //@w18001218
    }
   
    //---------------------------------------------------------------------------
    /** this is used to save indivdual html files to named file.*/
    
    boolean saveStringTofile( String contents , String filename )
    { 
        return false ; 
    }
    //---------------------------------------------------------------------------
   public static  int compareByPlayerWins( Player b, Player a )
   { 
    if( a.getExperiance()==0 && b.getExperiance()==0 ) return 0 ; 
    if(  a.getExperiance()==0 ||  b.getExperiance()==0)
    { 
       return a.getExperiance()-b.getExperiance();
    }
    float aScore = a.getWinRatio(); 
    float bScore = b.getWinRatio(); 
    
    if( aScore==bScore )return a.getWins()- b.getWins(); 
    if(  (aScore-bScore) > 0) return 1 ; 
    
    return -1 ;
   }
   //---------------------------------------------------------------------------
   /**
    * sorts.
    * @param plyrs 
    */
    public void sortPlayersByWinRatio(   ArrayList<Player> plyrs )
   { 
       Collections.sort(plyrs, (Player b, Player a) -> compareByPlayerWins(b, a));
   }
    //--------------------------------------------------------------------------
    /**
     *  create an HTML VIEW showing top players. Computes Ranking.  
     * @param howMany
     * @return 
     */
    public  String topPlayers( int howMany ) 
    { 
       ArrayList<Player> plyrs = getAllPlayersAsList() ;  
       sortPlayersByWinRatio( plyrs );
       
       //@w18001218: Stringbuilder
       StringBuilder top = new StringBuilder(); 
       String items[] = {   "PlayerName        ", 
                            "  Wins  ", 
                            "Losses   ", 
                            "Experance",  
                            " Ranking ", 
                            "  Score  " } ;
      
       //@w18001218: append
       top.append( tableGenerator.startTable(items,
                    "Top " + howMany +" players" ,
                    this.getStudentID()) ); 
       // out.println(tableGenerator.startTable(catagorynames ,));
       
       int c = 0 ; 
       float lastRato = -1f;
       int  lastWins = 0 ; 
       for( Player p : plyrs )
       {
           if( !( lastRato == p.getWinRatio() && lastWins == p.getWins())  )c++ ; 
           p.setRanking(c);
           lastRato = p.getWinRatio() ;
           lastWins = p.getWins();
       }
       
       c = 0 ; 
       for( Player p : plyrs )
       { 
           items[0 ] =  String.format( "%-18s" , p.getName()) ; 
           items[1] =   String.format( "  %d   " , p.getWins()) ;
                                       
           items[2] =   String.format( "    %d   " , p.getLossess()); 
           items[3] =   String.format( " %d  " , p.getExperiance()); 
           items[4] =   String.format( " %d  " , p.getRanking()); 
           float f = -1  ; 
           if( p.getExperiance() > 0 ) f = p.getWins()/(float)p.getExperiance(); 
           items[5] = String.format(" %2.2f " ,f ); 
           //@w18001218: append
           top.append(this.tableGenerator.convertRow(items)); 
           if( c++ > howMany) break ; 
       }
       //@w18001218: append
       top.append(this.tableGenerator.endTable()); 
       
       //@w18001218: return toString
       return top.toString();
   
    }
    //--------------------------------------------------------------------------
    /**
     *   checking method defined in interface. 
     * @param pres
     * @return 
     */
  
    public String getDecksAsString( )
    { 
        ArrayList<Deck> allDecks = copyOfAllKnownDecksAsList(); 
        
        return allDecks.toString() ; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param cdName - must be not null. 
     * @return 
     */
    public Card getOrMakeCard( String cdName )
    { 
        assert cdName != null; 
        Card cd = findCard(cdName ); 
        if( cd != null)return cd; 
        cd = new Card( cdName ); assert cd != null ; 
        addCard( cd); 
        return cd; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param lineOfNodes
     * @return 
     */
    public Deck readDeck( String player1Deck )
    { 
        assert player1Deck != null ; 
        //@w18001218: substring is faster than replace
        //player1Deck = player1Deck.replace("DeckdeckName=,",""); 
        player1Deck = player1Deck.substring(14, player1Deck.length());
        
        String deckOne[] = player1Deck.split(","); 
        boolean first = true ;
        Deck d = new Deck(  deckOne[0] ); 
        for( String s: deckOne )
        { 
            if( first == true ){ first = false ; continue ; } 
            Card cd =  getOrMakeCard( s.trim() );
            d.addCard(cd);//System.out.format("%d %s %n", c++, s);
        }
        addDeckNoDuplicates(d); 
        return d ;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param p1cardsUsed
     * @param dck
     * @return 
     */
    public List<Card> processCardsUsed(  String  p1cardsUsed , Deck dck )
    {   
        //@w18001218: substring is faster than replace
        p1cardsUsed = p1cardsUsed.substring(5, p1cardsUsed.length());
        String cardsUsed[]  = p1cardsUsed.split(",");
        List<Card> cds = new ArrayList<Card>(cardsUsed.length);
        
        for( String s: cardsUsed )
        { 
            assert s!= null; 
            Card cd =  dck.findCard( s.trim()  );  
            if( cd == null )
            { 
                System.out.println( " MISSING '" + s.trim()+ "'" );
                System.out.println( dck ) ; 
            }
            assert cd != null ;
            cds.add( cd ); 
            //System.out.format( "--%s-- \n", s );
        }
        return cds;
    }
    //--------------------------------------------------------------------------
    /**
     * Read a single match - this reads the 7 line format from CCraftGenerator.
     * 
     * @param reader
     * @return
     * @throws IOException 
     */
    public  Match  readMatch( BufferedReader reader) throws IOException 
    { 
        String Player1Str, Player2Str, winner, id, when, player1Deck, player2Deck;
        String p1cardsUsed, p2cardsUsed;
        // READING 
        Player1Str = reader.readLine().trim();  //System.out.println( "WHO " +  Player1Str );
        
        Player2Str = reader.readLine().trim(); // System.out.println( "WHO " +  Player2Str );
        winner = reader.readLine().trim();
        id = reader.readLine();
        when = reader.readLine();
                
       // System.out.println( "----" +  when );
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
         
        LocalDateTime dateTime = LocalDateTime.parse(when, formatter); 
       // System.out.println( dateTime + ","+ when );
      
        player1Deck = reader.readLine();
        player2Deck = reader.readLine();
        p1cardsUsed = reader.readLine();
        p2cardsUsed = reader.readLine();
             
        Player Player1 = this.getPlayerForName(Player1Str); 
        if(Player1 == null  )
        { 
            Player1 = new Player(Player1Str );
            this.addPlayer(Player1);
        }
        Player Player2 = getPlayerForName(Player2Str);
        if( Player2 == null  )
        { 
            Player2  = new Player(Player2Str );
            addPlayer( Player2); 
        }
        assert Player1 != null ; 
        assert Player2 != null ; 
        
        
        
        //String blankLine = reader.readLine();
        //@w18001218: saves on memory
        skipLine(reader); 
        
        Deck p1Deck  = readDeck( player1Deck  )  ;
        List<Card> cdspl1 = processCardsUsed( p1cardsUsed , p1Deck );
        
        Deck p2Deck  = readDeck( player2Deck )  ;
        List<Card> cdspl2 = processCardsUsed( p2cardsUsed , p2Deck );
        //Match( Player  playerA,  Deck dka , List<Card> cardsPlayedA , 
         //   Player  playerB , Deck dkb , List<Card> cardsPlayedB ,  
         //   Player winner  ) 
         Player theWinner=null;
        if(winner.equalsIgnoreCase(Player1Str)  )
        { 
            theWinner = Player1; 
           // p1Deck.addWin();
            //p2Deck.addLoss();
            Player1.addAWin(p1Deck);
            Player2.addLoss(p2Deck);
        } 
        else 
        { 
            assert  winner.equalsIgnoreCase(Player2Str ); 
            theWinner = Player2; 
          //  p2Deck.addWin();
          //  p1Deck.addLoss();
           Player1.addLoss(p1Deck);
           Player2.addAWin(p2Deck);
        }
        
        Match m = new Match( Player1 , p1Deck ,  cdspl1 ,
                                            Player2, p2Deck, cdspl2 ,theWinner); 
       // System.out.println("OK %d %d", cdspl1.size() );
       
        return  m ; 
    }
        //@w18001218: saves memory
    public static void skipLine(BufferedReader br) throws IOException {
        while (true) {
            int c = br.read();
            if (c == -1 || c == '\n') {
                return;
            }
            if (c == '\r') {
                br.mark(1);
                c = br.read();
                if (c != '\n') {
                    br.reset();
                }
                return;
            }
        }
    }
    //--------------------------------------------------------------------------
    /** 
     *   part of main body - 
     */
    public  Set<Match>  readInput(String thefileName )
    { 
        BufferedReader reader=null; 
        
        //@w18001218: brought runtime down by 4 seconds
         if (allMatches.size() > 0)
        {
            clearAllMatches();
        }
        try
        {
            reader = new BufferedReader(new FileReader( thefileName  ));
            Match m = null ; 
            
            do 
            { 
                m = readMatch( reader ); 
                addMatch(m);
            } while( m != null && reader.ready());
            reader.close();
        } catch ( IOException ex )
        { 
            System.out.println("->  Error " + ex);
        }
        finally
        { 
           if( reader !=null ) try 
           {
               reader.close();
           } catch (IOException ex)
           {
               assert false :" GIVE UP ON FILE COULD NOT CLOSE"; 
           } 
        }
        return getAllMatchesAsList() ; // internal var - is convience
    }
    //--------------------------------------------------------------------------
    public void setAllKnownCards(Set<Card> allKnownCards)
    {
        
    }
    //--------------------------------------------------------------------------
    public String getStudentID()
    {
        return studentID;
    }
    //--------------------------------------------------------------------------
    public void setStudentID(String studentID)
    {
        this.studentID = studentID;
    }
    //--------------------------------------------------------------------------
    public TablePresentation getTableGenerator()
    {
        return tableGenerator;
    }
    //--------------------------------------------------------------------------
    public void setTableGenerator(TablePresentation tableGenerator)
    {
        this.tableGenerator = tableGenerator;
    }
  
   
    //--------------------------------------------------------------------------
    public void printAllPlayers( PrintStream out  )
    { 
        String catagorynames[ ]= { "PlayerName",
                                        "Wins",
                                        "Losses",
                                        "Win Ratio", 
                                        "Ranking" , 
                                        "Experiance" } ; 
         
         out.println(tableGenerator.startTable(catagorynames ,"All Players ",this.getStudentID()));
        for( Player who: this.getAllPlayersAsList() )
        { 
            //System.out.println(name); 
            String  name = who.getName();
            String cols[] = { name, 
                                "" + who.getWins(),
                                ""+who.getLossess(), 
                                String.format("%2.2g" ,who.getWinRatio() ),
                                ""+who.getRanking(), 
                                ""+who.getExperiance() } ; 
            out.print(tableGenerator.convertRow(cols));
        }
        out.println( tableGenerator.endTable());
    //String startTable( String catagorynames[]) ; 
    // String convertRow( String items[] ); 
    // String endTable();  
    } 
     //--------------------------------------------------------------------------
    /**
     * print top 50 players.
     * @param out 
     */
    public void printTop50Players( PrintStream out  )
    { 
        out.println( topPlayers(999)); 
    }
    //--------------------------------------------------------------------------
    /**
     *  print all decks.
     * @param out 
     */
    public void printAllDecks( PrintStream out )
    { 
        String catagorynames[ ]= { "Deck" , "Wins", "Lossess" } ; 
        out.println( tableGenerator.startTable( catagorynames , "Cards", studentID) );
        for( Deck name: copyOfAllKnownDecksAsList() )
        {
            String cols[] = { name.getDeckName() , 
                                    ""+name.getWins(), ""+name.getLossess() } ; 
            out.print(tableGenerator.convertRow(cols));
        }
        out.println( tableGenerator.endTable());
    }
    //--------------------------------------------------------------------------
    /**
     * printAllMatches
     * @param out 
     */
    public void printAllMatches( PrintStream out )
    { 
        String catagorynames[ ]= { "Player A ", "Player B" , "Winner", "DeckA" ,
                                        "DeckB",  "when "} ; 
        
        out.println( tableGenerator.startTable( catagorynames  , "Cards", studentID) );
        for( Match name: this.getAllMatchesAsList() )
        {
            String cols[] = {   name.getPlayerA().getName() , 
                                name.getPlayerB().getName() , 
                                name.getWinner().getName() , 
                                name.getPlayerADeck().getDeckName(), 
                                name.getPlayerB().getName() , 
                                ( name.getWhen() ==null?"?": name.getWhen().toString()) 
                            } ; 
            out.print(tableGenerator.convertRow(cols));
        }
        out.println( tableGenerator.endTable() );
    }
    //--------------------------------------------------------------------------
    /**
     * printAllCards creates Cards.html file
     * @param out 
     */
    public void printAllCards( PrintStream out )
    { 
        String catagorynames[ ]= { "Name ", 
                                    "Successes" , 
                                    "cards played" 
                                   } ;
      
        out.println( tableGenerator.startTable( catagorynames  , "Cards", studentID) );
        for( Card name: getAllCards() )
        {
            String cols[] = {  name.getName(),
                                ""+name.getSuccesses() , 
                                ""+name.getCardsPlayed()
                            } ; 
            out.print(tableGenerator.convertRow(cols));
        }
        out.println( tableGenerator.endTable());
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param fileName
     * @throws FileNotFoundException 
     */
    public void processEverything( String fileName ) throws FileNotFoundException 
    { 
     System.out.println("READING- Please wait"); 
     Set<Match> all = readInput(  fileName ); 
     System.out.println("READING COMPLETE EXPORTING"); 
     System.out.println( " Matches " + all.size() );
     System.out.println( " Cards " + howManyCards() );
     System.out.println(" Decks " + this.howManyUniqueDecks() );
     System.out.println(" Players " + howManyPlayers()  );
     
     PrintStream writetoEngineer = new PrintStream(new File ("Players.html"));
     printAllPlayers( writetoEngineer );
     writetoEngineer.close();
     
     writetoEngineer = new PrintStream(new File ("Decks.html"));
     printAllDecks( writetoEngineer );
     writetoEngineer.close();
     
     writetoEngineer = new PrintStream(new File ("Matches.html"));
     printAllMatches( writetoEngineer );
     writetoEngineer.close();
     
     writetoEngineer = new PrintStream(new File ("Cards.html"));
     printAllCards( writetoEngineer );
     writetoEngineer.close();
     
     writetoEngineer = new PrintStream(new File ("top50Players.html"));
     printTop50Players( writetoEngineer );
     writetoEngineer.close();
     
     System.out.println("EXPORT COMPLETE");
     
   ////  PrintStream outStream = new PrintStream( new Outsteam( ) )
      //1.  engine.printAllPlayers(System.out ); 
      //2.  engine.printAllDecks( System.out );
      //3.  engine.printAllMatches(  System.out );
      //4.   engine.printAllCards(  System.out );
        // 2. Print out the player top 500  
      //  printTop50Players( System.out );
        // 3. Print out the top/all decks 
        // 4. print out the top/all matches 
        // 5. print out the top/all carrds. 
        // 6. print out all the/all players. 
        // 7. print detail on each dec. 
    }
    
    
    
    //--------------------------------------------------------------------------
    public Player getPlayerForName( String name )
    { 
        //@w18001218
        if(allPlayers.containsKey(name)){
            return allPlayers.get(name);
        }
        return null; 
    }
    //---------------------------------------------------------------------------
    public boolean playerExists( String name ) 
    { 
        //@w18001218
        if(allPlayers.containsKey(name)){
           return true;
       }
       return false;
    }
    //--------------------------------------------------------------------------
    public ArrayList<Player> getAllPlayersAsList() 
    { 
        //@w18001218
       return  new ArrayList<>( allPlayers.values() );
    }
    //---------------------------------------------------------------------------
    /**
     * Adds a player ( if player is not known.) 
     * not two players with same name not permitted. ( duplicate removed )
     */
    public void addPlayer( Player p )
    { 
        //@w18001218
        this.allPlayers.put(p.getName(), p);
    }
    //---------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    @Override 
    public int howManyPlayers(){ return allPlayers.size(); }
    //---------------------------------------------------------------------------
    /**
     * 
     * @param cardName
     * @return 
     */
    public boolean cardExists( String cardName)
    { 
        //@w18001218
         assert cardName != null ; 
        boolean foundCard = false;
        
        if (allKnownCards.containsKey(cardName)) {
            return true;
        }
        
        return false;
    }
    //---------------------------------------------------------------------------
    /** 
     * given a card name "CRD" return the card ( if known ) or null if not.
     * @see  cardExists
     * @eee  addCard 
     * @param cardname not null 
     * @return the card. 
     */
    public Card getCard( String cardName )
    {  
       return findCard( cardName ) ; 
    }
    //---------------------------------------------------------------------------
    public Card findCard( String cardName )
    { 
        //@w18001218
         assert cardName != null ;
        
        if(allKnownCards.containsKey(cardName)) {
            return allKnownCards.get(cardName);
        }
        
        return null;
    }
    //---------------------------------------------------------------------------
    /**
     * 
     * @param c 
     */
    public void addCard( Card c)
    { 
        //@w18001218
        allKnownCards.put(c.getName(), c); 
    }
    //--------------------------------------------------------------------------
    public Collection<Card> getAllCards()
    { 
        //@w18001218
       return  allKnownCards.values(); 
    }
    //---------------------------------------------------------------------------
    /**
     *  adds a deck to the list of all decks. 
     * @param d 
     */
    public void addDeckNoDuplicates( Deck d ) 
    { 
        //@w18001218
        if( allKnownDecks.containsKey(d.getDeckName())== true)
            return ; 
        allKnownDecks.put(d.getDeckName(), d);
    }
    //---------------------------------------------------------------------------
    public void addMatch( Match m    )
    { 
        allMatches.add(m); 
    }
    public void clearAllMatches()
    { 
        allMatches.clear();
    }
    //--------------------------------------------------------------------------
    public Set<Match> getAllMatchesAsList()
    {
        return allMatches;
    }
    //--------------------------------------------------------------------------
    public void setAllMatches(Set<Match> allMatches)
    {
        this.allMatches = allMatches;
    }
    //--------------------------------------------------------------------------
    public Collection<Card> getAllKnownCards()
    {
        return allKnownCards.values();
    }
    //--------------------------------------------------------------------------
    public  ArrayList<Deck> copyOfAllKnownDecksAsList()
    { 
        return new ArrayList<>(allKnownDecks.values());
    }
    //--------------------------------------------------------------------------
    public int howManyUniqueDecks()
    { 
        return allKnownDecks.size();
    }
    //--------------------------------------------------------------------------
    public int howManyCards() 
    { 
        return allKnownCards.size(); 
    }
    //--------------------------------------------------------------------------
    /** W18001218 
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    { 
        CardCraftEngineStudent engine = new CardCraftEngineStudent("W10010010101"); 
        try 
        {
            engine.processEverything( "SMALL_PUNY.txt" );
           // USE THIS IF YOU WANT TO REALLY TEST YOU ENGINE 
           // engine.processEverything( "Matchfile.txt" );
           
           // SET THIS TO FALSE IF YOU ARE *NOT* RUNNIGN PROFILER
           // So profiling code sent to true ; 
           // if NOT profiling set to false --- see what happens.
           boolean isProfiling = true ; 
           if(isProfiling == false  )
           { 
            File htmlFile = new File("top50Players.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
            
             htmlFile = new File("Players.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
             htmlFile = new File("Matches.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
            
             htmlFile = new File("Cards.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
            
             htmlFile = new File("Decks.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
           } 
            
        } catch (FileNotFoundException ex)
        {
             System.err.println("ERROR problems with files "+ ex); 
        } 
        catch(  IOException ex )
        { 
              System.err.println("Not sure you can open files "+ ex); 
        }
    }
    
    //--------------------------------------------------------------------------  

} // END OF CLASS

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cardcraftengine;

import cardcraftengine.presentation.HTMLTableOutput;
import cardcraftengine.presentation.TablePresentation;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;



/**
 *
 * @author Dr N. Dalton
 */
public class BASECASE_IGNORE_INTERNAL implements  CardCraftInterface
{
    private String studentID ; 
    TablePresentation tableGenerator ; 
    private List<Player> allPlayers ; // people who have played sorted by sc 
    private List<Match>  allMatches ;
    private List<Card>   allKnownCards ;
    private List<Deck>   allKnownDecks; 
 
    //--------------------------------------------------------------------------
    /**
     * 
     * @param StudentID 
     */
    public BASECASE_IGNORE_INTERNAL( String StudentID  )
    { 
        super(); 
     
        this.studentID =  StudentID ; 
        this.tableGenerator = new HTMLTableOutput(); 
         
        this.allPlayers    = new LinkedList<>(); // people who have played sorted by sc 
        this.allMatches    = new LinkedList<>() ;
        this.allKnownCards = new LinkedList<Card>();
        this.allKnownDecks = new LinkedList<Deck>(); 
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
     
        int n = plyrs.size(); 
        for(int i=0; i < n; i++)
        {  
            for(int j=1; j < (n-i); j++)
            {  
                Player b = plyrs.get(j-1) ;
                Player a = plyrs.get(j); 
                if(compareByPlayerWins( b , a)>0)
                {  
                 Player temp = b; 
                 plyrs.set(j-1, plyrs.get(j));
                 plyrs.set(j, temp); 
                }  
                          
            }  
         }  
       
       
      /* Collections.sort(plyrs,  new Comparator<Player>() 
        {
            @Override
            public int compare(Player b, Player a) 
            {
                 return compareByPlayerWins( b , a); 
            }
         }); 
        */
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
       
       String top = new String(); 
       String items[] = {   "PlayerName        ", 
                            "  Wins  ", 
                            "Losses   ", 
                            "Experance",  
                            " Ranking ", 
                            "  Score  " } ;
      
       top += ( tableGenerator.startTable(items,
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
           top += (this.tableGenerator.convertRow(items)); 
           if( c++ > howMany) break ; 
       }
       top += (this.tableGenerator.endTable()); 
       
       return top;
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
        player1Deck = player1Deck.replace("DeckdeckName=,",""); 
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
        List<Card> cds = new ArrayList<Card>(); 
        p1cardsUsed = p1cardsUsed.replace("used,", "");
        String cardsUsed[]  = p1cardsUsed.split(",");
        
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

        String blankLine = reader.readLine();

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
    //--------------------------------------------------------------------------
    /** 
     *   part of main body - 
     */
    public  List<Match>  readInput(String thefileName )
    { 
        BufferedReader reader=null; 
        clearAllMatches();
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
     List<Match> all = readInput(  fileName ); 
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
        Player found = null ; 
        for( Player p : allPlayers )
        { 
            if( p.isNameThisPlayer( name)) found =  p; 
        }
        return found ;  
    }
    //---------------------------------------------------------------------------
    public boolean playerExists( String name ) 
    { 
        boolean found = false ; 
        for( Player p : allPlayers )
        { 
            if( p.isNameThisPlayer( name)) found =  true; 
        }
        return found ;
    }
    //--------------------------------------------------------------------------
    public ArrayList<Player> getAllPlayersAsList() 
    { 
       return  new ArrayList<>( allPlayers );
    }
    //---------------------------------------------------------------------------
    /**
     * Adds a player ( if player is not known.) 
     * not two players with same name not permitted. ( duplicate removed )
     */
    public void addPlayer( Player p )
    { 
        this.allPlayers.add( p ); 
    }
    //---------------------------------------------------------------------------
    public int howManyPlayers(){ return allPlayers.size(); }
    //---------------------------------------------------------------------------
    /**
     * 
     * @param cardName
     * @return 
     */
    public boolean cardExists( String cardName)
    { 
        assert cardName != null ; 
        boolean foundCard = false; 
        for( Card cd :allKnownCards  )
        { 
            if( cd.cardHasName( cardName )  )
            { 
                foundCard = true ; 
            }
        }
        return foundCard; 
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
        assert cardName != null ; 
        Card foundCard = null; 
        for( Card cd :allKnownCards  )
        { 
            if( cd.cardHasName( cardName )  )
            { 
                foundCard = cd ; 
            }
        }
        return foundCard;  
    }
    //---------------------------------------------------------------------------
    /**
     * 
     * @param c 
     */
    public void addCard( Card c)
    { 
        allKnownCards.add( c); 
    }
    //--------------------------------------------------------------------------
    public Collection<Card> getAllCards()
    { 
       return  allKnownCards; 
    }
    //---------------------------------------------------------------------------
    /**
     *  adds a deck to the list of all decks. 
     * @param d 
     */
    public void addDeckNoDuplicates( Deck d ) 
    { 
        if( allKnownDecks.contains( d ) ==true)return ; 
        allKnownDecks.add(d);
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
    public List<Match> getAllMatchesAsList()
    {
        return allMatches;
    }
    //--------------------------------------------------------------------------
    public void setAllMatches(List<Match> allMatches)
    {
        this.allMatches = allMatches;
    }
    //--------------------------------------------------------------------------
    public Collection<Card> getAllKnownCards()
    {
        return allKnownCards;
    }
    //--------------------------------------------------------------------------
    public  ArrayList<Deck> copyOfAllKnownDecksAsList()
    { 
        return new ArrayList<>( allKnownDecks) ; 
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
    /** W10010010101  CHANGE THIS TO BE YOUR ID.
     * @param args the command line arguments
     */
    public static void main(String[] args)
    { 
        BASECASE_IGNORE_INTERNAL engine = new BASECASE_IGNORE_INTERNAL("W10010010101"); 
        try 
        {
            engine.processEverything( "SMALL_PUNY.txt" );
           // engine.processEverything( "Matchfile.txt" );
            
        } catch (FileNotFoundException ex)
        {
             System.err.println("ERROR problems with files "+ ex); 
        } 
    }
    //--------------------------------------------------------------------------
    
    
} // END OF CLASS

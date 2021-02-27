/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;
//--------------------------------------------------------------------------

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author Dr N. Dalton
 */
public class Match
{
    static int gIDS = 1  ; 
    private int ID ; // database ID 
    private Player playerA ; 
    private Deck   playerADeck ;
             HashSet<Card> playACardsUsed ; 
    private Player playerB ; 
    private Deck   playerBDeck ;
            HashSet<Card> playBCardsUsed ; 
    
    private Player winner;
    LocalDateTime when ; 

    //--------------------------------------------------------------------------
    public  Match( Player  playerA,  Deck dka , List<Card> cardsPlayedA , 
            Player  playerB , Deck dkb , List<Card> cardsPlayedB ,  
            Player winner  ) 
    { 
        assert playerA != null ; 
        assert playerB != null;
        assert cardsPlayedB!= null ; 
        assert cardsPlayedA!= null ;
        assert dka !=null ; 
        assert dkb != null ; 
        
        this.playerA  = playerA; 
        this.playerB  = playerB ; 
        this.winner   = winner ; 
        this.ID = gIDS++ ; 
        assert winner == playerA || winner == playerB :" winner must be a player " ; 
        this.playerADeck = dka ; 
        this.playerBDeck = dkb ;
        this.playACardsUsed = new HashSet<Card>( cardsPlayedA ); assert playACardsUsed!=null; 
        this.playBCardsUsed = new HashSet<Card>( cardsPlayedB ); 
        
        if( playerA == winner  )
        {
            playerA.addAWin( dka); 
            playerB.addLoss(dkb);
            playACardsUsed.stream().map((Card crd) -> {
                assert crd != null ;
                return crd;
            }).forEachOrdered((crd) -> {
                crd.addWin();
            });
            
        }else 
        { 
           playerB.addAWin(dkb);
           playerA.addLoss(dkb);
           playBCardsUsed.forEach(Card::addWin);
        } 
    }
       
    //--------------------------------------------------------------------------
    /**
     * returns ID of match.
     * @return  get ID 
     */
    public int getID()
    {
        return ID;
    }
    //--------------------------------------------------------------------------
    public void setID(int ID)
    {
        this.ID = ID;
    }
    //--------------------------------------------------------------------------
    public Player getPlayerA()
    {
        return playerA;
    }
    //--------------------------------------------------------------------------
    public void setPlayerA(Player playerA)
    {
        this.playerA = playerA;
    }
    //--------------------------------------------------------------------------
    public Player getPlayerB()
    {
        return playerB;
    }
    //--------------------------------------------------------------------------
    public void setPlayerB(Player playerB)
    {
        this.playerB = playerB;
    }
    //--------------------------------------------------------------------------
    public Player getWinner()
    {
        return winner;
    }
    //--------------------------------------------------------------------------
    public void setWinner(Player winner)
    {
        this.winner = winner;
    }
    //--------------------------------------------------------------------------
    public Deck getPlayerADeck()
    {
        return playerADeck;
    }
    //--------------------------------------------------------------------------
    public void setPlayerADeck(Deck playerADeck)
    {
        this.playerADeck = playerADeck;
    }
    //--------------------------------------------------------------------------
    public Deck getPlayerBDeck()
    {
        return playerBDeck;
    }
    //--------------------------------------------------------------------------
    public void setPlayerBDeck(Deck playerBDeck)
    {
        this.playerBDeck = playerBDeck;
    }
    //--------------------------------------------------------------------------
    public LocalDateTime getWhen()
    {
        return when;
    }
    //--------------------------------------------------------------------------
    public String getStringForCardsPlayed( Set<Card> theCards )
    { 
        String result = "used" ; 
        for( Card cd : theCards ) 
        { 
            result  = result + "," + cd.getName() ;
        }
        return result ; 
    }
    //--------------------------------------------------------------------------
    public void setWhen(LocalDateTime when)
    {
        this.when = when;
    }
    @Override 
    public String toString()
    { 
        return   playerA.getName()  + "\n"+
        this.playerB.getName() + "\n"  + 
                
        this.winner.getName()  + "\n"  +  
        this.ID  +    "\n"  + 
        this.when.toString()  +"\n" + 
       
        this.playerADeck.getSaveString() + ",\n"  + 
        this.playerBDeck.getSaveString() +  ",\n"  + 
        getStringForCardsPlayed(this.playACardsUsed) + ",\n"  + 
        getStringForCardsPlayed(this.playBCardsUsed) + "\n"  ;
    }
 
    //--------------------------------------------------------------------------
}

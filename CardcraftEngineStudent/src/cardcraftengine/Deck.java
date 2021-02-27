/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;

import java.util.*;
//-----------------------------------------------------------------------------
/**
 *
 * @author Dr N. Dalton
 */
public class Deck
{
    protected int ID;
    
    protected String deckName ; 
    List<Card>  cards ; 
    protected int wins = 0 ; 
    protected int lossess = 0 ;
    
    //--------------------------------------------------------------------------
    /**
     * 
     * @param deckName 
     */
    public Deck(String deckName)
    {
        this.deckName = deckName;
        this.cards = new LinkedList<>();
    }
    //--------------------------------------------------------------------------
    /**
     *  This process winning.  
     */
    public void addWin()
    { 
        assert wins >= 0 ; 
        this.wins += 1; 
        for( Card c : cards )
        {
            c.addWin();
        }
    }
    //--------------------------------------------------------------------------
    /**
     * add a lost from a game 
     */
    public void addLoss()
    { 
        assert lossess >= 0 ; 
        this.lossess += 1 ; 
        for( Card c : cards )
        {
            c.addLoss();
        }
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return Total number of games played. 
     */
    public int getTotalGamesPlayed( ){ return wins + lossess ; }
    //--------------------------------------------------------------------------
    /**
     *  get the name of the deck.
     * @return 
     */     
    public String getDeckName()
    {
        return deckName;
    }
    //--------------------------------------------------------------------------
    /**
     * Set the deck name. ( please don't ) 
     * @param deckName 
     */
    public void setDeckName(String deckName)
    {  
         assert deckName != null : "No null names"; 
         assert deckName.contains("'")== false ; 
        this.deckName = deckName;
    }
    //--------------------------------------------------------------------------
    /**
     *  add a card to a deck.
     * @param it 
     */
    public void addCard( Card it )
    { 
        assert it.getName().contains("'")== false ; 
        
        cards.add(it);
    }
    //--------------------------------------------------------------------------
    /**
     *  does this deck contain this card. 
     * @param it
     * @return 
     */
    public boolean contains( Card it)
    { 
        for( Card c : cards )
        {
            if( c.cardHasName(deckName)) return true  ; 
        }
         
        return false ; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param nm
     * @return 
     */
    public Card findCard(  String nm )
    { 
       Card found = null ; 
       for( Card c : cards )
        {
            if( c.cardHasName(nm)) found  = c ; 
        }
       return found ; 
    }
    //--------------------------------------------------------------------------
    public String getValues( )
    { 
        String result = "" ; 
        for( Card cd : cards )
        {
            String c = cd.getName();
            result += "'"+ c + "'\n";
        }
        return result ; 
    }
    //--------------------------------------------------------------------------
    /**
     *  have a look at the cards in the deck ( useful for for looping ) 
     * @return 
     */
    public Collection<Card> peekCards()
    { 
        return cards; 
    }
    /**
     * Flexiblity for the future.
     * @return 
     */
    public String getSaveString(){ return this.toString() ; } 
    @Override
    public String toString()
    {
        String result = "DeckdeckName=," + deckName ;
        for( Card cd : cards )
        { 
            String c  = cd.getName();
            result = result +"," + c ; 
        } 
        return  result;
    }
    /**
     * The hash value works.
     * @return 
     */
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.deckName);
        return hash;
    }
    /***
     *  A deck is equal to another deck if it has the same name. 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Deck other = (Deck) obj;
        if (!Objects.equals(this.deckName, other.deckName))
        {
            return false;
        }
        return true;
    }
    //--------------------------------------------------------------------------
    /**
     * Get the value of ID
     *
     * @return the value of ID
     */
    public int getID()
    {
        return ID;
    }
    //--------------------------------------------------------------------------
    /**
     * this gets the wins.
     * @return 
     */
    public int getWins()
    {
        return wins;
    }
    //--------------------------------------------------------------------------
    public void setWins(int wins)
    {
        this.wins = wins;
    }
    //--------------------------------------------------------------------------
    public int getLossess()
    {
        return lossess;
    }
    //--------------------------------------------------------------------------
    public void setLossess(int lossess)
    {
        this.lossess = lossess;
    }
    
}

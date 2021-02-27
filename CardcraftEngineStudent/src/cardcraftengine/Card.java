/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;

import java.util.Objects;
//--------------------------------------------------------------------------
/**
 *
 * @author Robert Nobrain. 
 */
public class Card
{
    private static int IDGenerator = 1 ; 
    protected int ID  ; 
    private String  name ; 
    private int     successes = 0  ; 
    private int     lossess = 0; 
    private int     cardsPlayed = 0 ; 

    //--------------------------------------------------------------------------
    /**
     * Makes a card. 
     * @param name 
     */
    public Card(String name)
    {
        this.name = name;
        this.successes = 0  ; 
        this.cardsPlayed = 0 ; 
        this.lossess  =  0 ; 
        this.ID = IDGenerator++ ; // increment.
    }
     //--------------------------------------------------------------------------
    public boolean cardHasName( String charName )
    { 
        return name.equalsIgnoreCase(charName); 
    }
    //--------------------------------------------------------------------------
    public int getID()
    {
        return ID;
    }
    //--------------------------------------------------------------------------
    public void addWin()
    { 
        successes += 1;
        cardsPlayed += 1 ; 
    }
    //--------------------------------------------------------------------------
    public void addLoss()
    { 
        lossess += 1; 
        cardsPlayed += 1 ;
    }
    //--------------------------------------------------------------------------
   /**
    * gets the name of this card. 
    * @return String 
    */
    public String getName()
    {
        return name;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param name 
     */
    public void setName(String name)
    {
        this.name = name;
    }
    //--------------------------------------------------------------------------
    /***
     * 
     * @return 
     */
    public int getSuccesses()
    {
        return successes;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param successes 
     */
    public void setSuccesses(int successes)
    {
        this.successes = successes;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    public int getCardsPlayed()
    {
        return cardsPlayed;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param cardsPlayed 
     */
    public void setCardsPlayed(int cardsPlayed)
    {
        this.cardsPlayed = cardsPlayed;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }
    //--------------------------------------------------------------------------
    /**
     * 
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
        final Card other = (Card) obj;
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        return true;
    }
    //--------------------------------------------------------------------------
    @Override
    public String toString()
    {
        return "Cards{" + "ID=" + ID +  " ,name=" + name + 
                 ", successes=" + successes + 
                ", cardsPlayed=" + cardsPlayed + '}';
    }
    //--------------------------------------------------------------------------
    
}

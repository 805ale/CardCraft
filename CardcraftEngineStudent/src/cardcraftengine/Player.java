/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;

import java.util.*;

/**
 *
 * @author Dr N. Dalton
 */
public class Player implements Comparable<Player>
{
    private String          name ; 
    private int             ranking=0; 
    private int             wins = 0 ;
    private int             lossess = 0 ; 

    
    private boolean         isPrivate =false; 
    private int             experiance =0; 
    Map<String,Deck>        decks;  

    //--------------------------------------------------------------------------
    /**
     * 
     * @param name 
     */
    public Player(String name)
    {
        this.name = name;
        decks = new HashMap<>(); 
    }
    public boolean isNameThisPlayer( String playerName )
    { 
        return playerName.equalsIgnoreCase(name);
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param d 
     */
    public void addAWin( Deck d )
    { 
        assert d!= null :"null deck"; 
        d.addWin(); 
        wins+= 1 ; 
        experiance += 1; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param d 
     */
    public void addLoss( Deck d ) 
    { 
         assert d!= null :"null deck"; 
        d.addLoss();
        lossess += 1; 
        experiance += 1; 
    }
    public int getWins()
    {
        return wins;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return number of losses - 
     */
    public int getLossess()
    {
        return lossess;
    }
    //--------------------------------------------------------------------------
    /**
     * return the win ratio - the ratio of wins to games played
     * @return float  from 1.0 ( top to 0 (bottom ) 
     */
    public float getWinRatio()
    { 
        if( getExperiance() <= 0 )  return -0.0f ;  
        return  (getWins()/(float)getExperiance()) ;         
    }
    //--------------------------------------------------------------------------
    /**
     * add the deck to the player ( but not if they have it already ).
     * @param Deck d - deck to add to player. 
     */
    public void addDeck( Deck d ) 
    { 
        decks.put(d.getDeckName(), d); 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    public String getName()
    {
        return name;
    }
    public int getRanking()
    {
        return ranking;
    }
    /**
     * 
     * @param ranking 
     */
    public void setRanking(int ranking)
    {
        this.ranking = ranking;
    }
    /**
     *  This character doesn't like. 
     * @return 
     */
    public boolean isIsPrivate()
    {
        return isPrivate;
    }
    /**
     * 
     * @param isPrivate 
     */
    public void setIsPrivate(boolean isPrivate)
    {
        this.isPrivate = isPrivate;
    }
    public int getNumberOfDecks()
    { 
        return decks.size() ; 
    }
    public Collection<Deck> peekDecks( ) 
    { 
        
        return decks.values() ;  
    }  
    /**
     * 
     * @return 
     */
    public int getExperiance()
    {
        return experiance;
    }
    /**
     * 
     * @param experiance 
     */
    public void setExperiance(int experiance)
    {
        this.experiance = experiance;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }

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
        final Player other = (Player) obj;
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return "Player{" + "name=" + name + ", ranking=" + ranking + 
                ", isPrivate=" + isPrivate +
                ", experiance=" + experiance + ", decks=" + decks.size() + '}';
    }
    
    @Override
    public int compareTo(Player other)
    {
        if(name.compareTo(other.name)== 0)
        {
            return ranking - other.ranking;
        }
        else
        {
            return name.compareTo(other.name);
        }
    }
    
    void testPlayer()
    {
        ArrayList<Player> plyrs = new ArrayList<Player>();
        Collections.sort(plyrs);
    }
}

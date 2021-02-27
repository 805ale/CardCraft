/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;

import java.io.FileNotFoundException;

/**
 *
 * @author Dr N. Dalton
 */
public interface CardCraftInterface
{

    //--------------------------------------------------------------------------
    /**
     *
     * @param fileName
     * @throws FileNotFoundException
     */
    void processEverything(String fileName) throws FileNotFoundException;
     int howManyPlayers(); 
     int howManyCards(); 
     
}

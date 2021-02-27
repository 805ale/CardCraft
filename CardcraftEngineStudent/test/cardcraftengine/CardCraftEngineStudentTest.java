/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine;

import cardcraftengine.presentation.TablePresentation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dr N. Dalton
 */
public class CardCraftEngineStudentTest
{
    
    public CardCraftEngineStudentTest()
    {
    }
    
    @Before
    public void setUp()
    {
        
    }
    /**
     *  internal supporting method.
     * @param TrialCase
     * @param whos
     * @param fileNAme
     * @param startTime
     * @return 
     */
    
    private double  testThisFile(  CardCraftInterface TrialCase ,  String whos ,  String fileNAme , long startTime)
    {  
        try
        {
            TrialCase.processEverything( fileNAme  );
        } catch (FileNotFoundException ex)
        {
           System.err.println(" FAULT IN FILES ");
           fail(" FILE PROBLEMS FIX THEM FIRST"); 
        }
        
        long endTime =  System.nanoTime(); 
        long diff = endTime - startTime ;
        double inSeconds = diff/(1_000_000_000.);
        
        System.err.println( whos + " TRIAL TIME "+ diff + " nano seconds (ns) = "  + inSeconds + "seconds" );
        return inSeconds; 
    }
    /**
     * Test of main method, of class CardCraftEngineStudent. This is here to avoid
     * students including the timing code in the profiling info ( yes it happened). 
     * 
     */
    @Test
    public void testMain()
    {
        
        System.out.println("TESTING CardCrafEngineStudent");
       
        String fileNAme = "Matchfile.txt" ; // "SMALL_PUNY.txt" ;  ;
       
        long startTime =  System.nanoTime(); 
        CardCraftInterface TrialCase = new  CardCraftEngineStudent ("TESTING");  //  CardCraftEngineBest  
         double inSeconds  = testThisFile(  TrialCase, "Your", fileNAme, startTime ); 
        
        System.err.println("NOTE THE MARKS ARE A MINIMUNM. FINAL MARKS WILL TAKE INTO ACCOUNT INTENT," 
                + "\n ( if the code should go faster but doesn't for some reason)."); 
        System.err.println("\nTESTING ORIGNAL SPEED (PLEASE WAIT ABOUT **1 minute 40 seconds** ) " );
        long baseCaseStrat =  System.nanoTime();
        BASECASE_IGNORE_INTERNAL baseCase = new BASECASE_IGNORE_INTERNAL("TESTING"); 
        double BaseCaseinSeconds  = testThisFile(  baseCase, "ORIGINAL", fileNAme , baseCaseStrat); 
        
        double warpFactor = BaseCaseinSeconds/  inSeconds; 
        
        System.err.format("WARPFACTOR (TIMES FASTER ) = %g %n", warpFactor);
        if(  warpFactor > 10 )
        { 
             double mark = Math.log(  BaseCaseinSeconds/ inSeconds);
             if( mark > 30 )mark = 30.0;
             System.err.format("APPROXIMATE MARK %2g %n --comment: Excellent work you have appeared to master this %n", mark * 7.138132379 );
        }else if ( warpFactor > 1.5  ) 
        { 
             System.err.format("APPROXIMATE MARK 5 --  comment: some basic improvements could do better try looking at using more data structures %n " ); 
        }else 
        { 
            System.err.format("APPROXIMATE MARK 1 - comment: no noticable change %n Try the text books? %n " ); 
        }
        
        assertEquals(baseCase.howManyPlayers(),  TrialCase.howManyPlayers());
        assertEquals(baseCase.howManyCards(),  TrialCase.howManyCards()); 
        
        System.err.format("COMPLETE. Have you put your name and id in the @Author header file %n");
    }
    
}

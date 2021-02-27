/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine.presentation;

/**
 *
 * @author Dr N. Dalton
 */
public class AsciiTableOutput implements TablePresentation 
{
    final static int W = 80; 
    //--------------------------------------------------------------------------
    /**
     * 
     * @param catagorynames
     * @return 
     */
    @Override 
    public   String startTable( String catagorynames[], String Name , String StudenID ) 
      { 
         StringBuffer f = new StringBuffer(); 
         f.append(Name); 
         f.append( StudenID );
         
         for( int c= 0 ; c <W-1 ;c++ ){ f.append("-"); } 
         f.append("\n|"); 
         for( String s: catagorynames)
         { 
             f.append(s); 
             f.append("|");
         }
         f.append("\n");
         
         return f.toString();
   
      }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param items
     * @return 
     */
   @Override 
    public   String convertRow( String items[] )
     { 
         StringBuffer f = new StringBuffer(); 
         f.append("|"); 
         for( String s: items)
         { 
             f.append(s); f.append("|");
         }
         f.append("\n"); 
         return f.toString();
     }
    //--------------------------------------------------------------------------
    /**
     *  return what ever to end the table.
     * @return 
     */
   @Override 
    public    String endTable()
     { 
          StringBuffer f = new StringBuffer(); 
         for( int c= 0 ; c <W-1 ;c++ ){ f.append("-"); } 
         f.append("\n|");
         return f.toString();
     }
    
}

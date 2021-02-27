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
public class HTMLTableOutput implements TablePresentation 
{
    final static int W = 80; 
    //--------------------------------------------------------------------------
    /**
     * 
     * @param catagorynames
     * @return 
     */
    @Override 
    public   String startTable( String catagorynames[] , String Name , String StudenID ) 
      {
          String header = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<style>\n" +
                            "table {\n" +
                            "  font-family: arial, sans-serif;\n" +
                            "  border-collapse: collapse;\n" +
                            "  width: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "td, th {\n" +
                            "  border: 1px solid #dddddd;\n" +
                            "  text-align: left;\n" +
                            "  padding: 8px;\n" +
                            "}\n" +
                            "\n" +
                            "tr:nth-child(even) {\n" +
                            "  background-color: #dddddd;\n" +
                            "}\n" +
                            "</style>" + 
                            "<body>\n" + 
                            "<H2>" + Name + "</H2> <H3> " + StudenID + " </H3>"+ 
                            "<table style=\"width:100%\">\n"; 
          
         StringBuffer f = new StringBuffer(); 
         f.append(header); 
         f.append( "\n<tr>" ); 
         
         //for( int c= 0 ; c <W-1 ;c++ ){ f.append("-"); } 
        
         for( String s: catagorynames)
         { 
             f.append("<th>\n"); 
             f.append(s); 
             f.append("</th>\n");      ;
         }
         f.append(" </tr>\n");
         
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
   
         f.append( "\n<tr>" ); 
         
         for( String s: items)
         { 
             f.append("<td>\n"); 
             f.append(s); 
             f.append("</td>\n");      ;
         }
         f.append(" </tr>\n");
         
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
         String endString  ="</body>\n" +
                            "</html>\n" + 
                            "</table>\n" ;
         
         return endString;
     }
    //--------------------------------------------------------------------------

   
    //--------------------------------------------------------------------------

}

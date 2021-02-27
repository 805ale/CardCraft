/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardcraftengine.presentation;

/**
 *
 * @author Dr Dalton
 */
public interface TablePresentation
{
     String startTable( String catagorynames[] ,  String NAme , String StudenID) ; 
     String convertRow( String items[] ); 
     String endTable(); 
    
}

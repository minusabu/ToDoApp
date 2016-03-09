/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import model.ItemBean;
import model.ListBean;

/**
 *
 * @author MinuRachel
 */
public interface ListDAO {
    
    public int createNewList(ListBean aList, String accountid);
    
    public int createNewItem(ItemBean anItem, String accountid, String listName);
    
    public ArrayList myListSearch(String email);
    
    public ArrayList myRequestSearch(String email);
    
    public ArrayList myListItems(String listName, String email);
    
    public ArrayList myLowListItems(String listName, String email);
    
    public ArrayList myHighListItems(String listName, String email);
    
    public int findListIdFromListName(String listName, int accountid);
    
    public int performComplete(int itemid);
    
    public int performAddBack(int itemid);

    public int NewMemberRequest(String email, String listName, String fromEmail);
    
    public int performAcceptance(int listId, String email);
    
    public int performDenial(int listId, String email);
    
}

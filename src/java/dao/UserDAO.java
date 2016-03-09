/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.UserBean;

/**
 *
 * @author MinuRachel
 */
public interface UserDAO {

    public int createAccount(UserBean aSignUp);

    public int checkUserName(String userName);

    public int findAccount(UserBean aLogin);

    public int findPendingAccount(UserBean aLogin);
    
    public int findAccountID();
    
    public int activateAccount(String aid);
    
    public String findUserName(int accountid);
}

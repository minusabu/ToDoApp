/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ListDAO;
import dao.ListDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.ListBean;
import model.RequestBean;
import model.UserBean;

/**
 *
 * @author MinuRachel
 */
@ManagedBean
@SessionScoped
public class LoginController {

    private String response;
    private String loginValidaton;
    private UserBean theModel;
    private int attemptCount;
    private boolean loginDisabled;
    private boolean loggedIn = false;
    private List<ListBean> myLists;
    private List<ListBean> myRequests;

    public LoginController() {
        loginValidaton = "";
        theModel = new UserBean();
        attemptCount = 1;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        //to be implemented based on what is required to display after logging in
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the loginValidaton
     */
    public String getLoginValidaton() {
        return loginValidaton;
    }

    /**
     * @param loginValidaton the loginValidaton to set
     */
    public void setLoginValidaton(String loginValidaton) {
        this.loginValidaton = loginValidaton;
    }

    /**
     * @return the theModel
     */
    public UserBean getTheModel() {
        return theModel;
    }

    /**
     * @param theModel the theModel to set
     */
    public void setTheModel(UserBean theModel) {
        this.theModel = theModel;
    }

    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String authenticateUserLogin() {
        String validationMessage = null;
        System.out.println("in authenticateUser()");
        String uid = theModel.getEmail();
        String pwd = theModel.getPassword();

        System.out.println("uid=" + uid);
        System.out.println("pwd=" + pwd);

        if (uid.length() == 0) {

            loginValidaton = "Please enter your Email address to login";
            System.out.println("in authenticateUser()1: loginValidaton=" + loginValidaton);
        } else if (pwd.length() == 0) {

            loginValidaton = "Please enter your password.";
            System.out.println("in authenticateUser()2: loginValidaton=" + loginValidaton);
        } else {
            loginValidaton = "";
            System.out.println("in authenticateUser()3: loginValidaton=" + loginValidaton);
            validationMessage = findAccount();
        }
        return validationMessage;
    }

    public String findAccount() {
        UserDAO aLoginDAO = new UserDAOImpl();
        if (attemptCount < 3) {
            int rowCount = aLoginDAO.findAccount(theModel); // Doing anything with the object after this?
            if (rowCount >= 1) //a user with approved account found
            {
//                theModel.setIsLoggedIn("LoggedIn");
                loggedIn = true;
                attemptCount = 0;
                //to find the account type for the user
//                String accountType = aLoginDAO.findUserAccountType(theModel);
//                System.out.println("Account type returned =" + accountType);
//                theModel.setAccountType(accountType);
//                System.out.println("accountType from bean: "+ theModel.getAccountType());
                //to get the firstname, lastname, and email for the user.
//                String[] detailsArray = aLoginDAO.findDetails(theModel);
//                theModel.setFirstName(detailsArray[0]);
//                theModel.setLastName(detailsArray[1]);
//                theModel.setEmail(detailsArray[2]);
//                System.out.println("firstName from controller =" + theModel.getFirstName());
//                System.out.println("lastName from controller =" + theModel.getLastName());
//                System.out.println("Email from controller =" + theModel.getEmail());
                myLists = getMyLists();
                myRequests = getMyRequests();
                return "userLandingPage.xhtml";

            } else //a user may be either in pending status or gave incorrect username/pwd
            {
//                theModel.setIsLoggedIn("NotLoggedIn");               
                loggedIn = false;
                int pendingRowCount = aLoginDAO.findPendingAccount(theModel);
                if (pendingRowCount >= 1) //the user account in pending status
                {
                    loginValidaton = "Your account request is not yet approved. Please wait or contact the admin.";
                } else//incorrect ulid/pwd
                {
                    attemptCount++;
                    loginValidaton = "The username and/or password entered is incorrect. Please try again.";
                }
                return "";
            }
        } else //user exceeded unsuccessful login attempts
        {
            loginValidaton = "You have exceeded your limit of 3 unsuccessful login attempts. "
                    + "Your account has been temporarily locked.";

            return disableUser();
//            return "";
        }

    }

    public String disableUser() {
        loggedIn = false;
        theModel.setEmail("");
        theModel.setPassword("");
        setLoginDisabled(true);
        return "login.xhtml";
    }

    public String logout() {
        loggedIn = false;
        theModel.setEmail("");
        theModel.setPassword("");
        return "login.xhtml";
    }

    /**
     * @return the loginDisabled
     */
    public boolean isLoginDisabled() {
        return loginDisabled;
    }

    /**
     * @param loginDisabled the loginDisabled to set
     */
    public void setLoginDisabled(boolean loginDisabled) {
        this.loginDisabled = loginDisabled;
    }

    /**
     * @return the myLists
     */
    public List<ListBean> getMyLists() {
        ListDAO aList = new ListDAOImpl();
        myLists = aList.myListSearch(theModel.getEmail());

        return myLists;
    }

    /**
     * @param myLists the myLists to set
     */
    public void setMyLists(List<ListBean> myLists) {
        this.myLists = myLists;
    }

    /**
     * @return the myRequests
     */
    public List<ListBean> getMyRequests() {
        ListDAO aList = new ListDAOImpl();
        myRequests = aList.myRequestSearch(theModel.getEmail());

        return myRequests;
    }

    /**
     * @param myRequests the myRequests to set
     */
    public void setMyRequests(List<ListBean> myRequests) {
        this.myRequests = myRequests;
    }
}

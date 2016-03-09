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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.ItemBean;
import model.ListBean;
import model.RequestBean;
import model.UserBean;

/**
 *
 * @author MinuRachel
 */
@ManagedBean
@SessionScoped
public class ListController {

    private ListBean theModel;
    private List<ItemBean> myItems;
    private List<ItemBean> myHighItems;
    private List<ItemBean> myLowItems;
    private UserBean theUserModel;
    private String memberEmail;
    private String validationMessage;
    private RequestBean theRequestModel;

    public ListController() {
        this.theUserModel = new UserBean();
        this.theModel = new ListBean();
        theRequestModel = new RequestBean();

    }

    public ListController(UserBean theUserModel) {
        this.theUserModel = theUserModel;
    }

    /**
     * @return the theModel
     */
    public ListBean getTheModel() {
        return theModel;
    }

    /**
     * @param theModel the theModel to set
     */
    public void setTheModel(ListBean theModel) {
        this.theModel = theModel;
    }

    public String addNewList(String email) {
        System.out.println("accountid=" + email);
        String listName = theModel.getListName();
//        System.out.println("listName: " + listName);
        if (listName.length() == 0) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = sdf.format(date);
            listName = "List " + formattedDate;

            theModel.setListName(listName);
            System.out.println("listName: " + theModel.getListName());
        } else {
            System.out.println("listName: " + theModel.getListName());
        }
        ListDAO aNewList = new ListDAOImpl();
        int rowCount = aNewList.createNewList(theModel, email);
        if (rowCount == 1) {
            return "viewList.xhtml";
        } else {
            return "";
        }
    }

    public String viewSelectedList(String listname, String email) {
        theModel.setListName(listname);
        theUserModel.setEmail(email);
        myItems = getMyItems();
        myHighItems = getMyHighItems();
        myLowItems = getMyLowItems();
        return "viewList.xhtml";
    }

//    public void 
    /**
     * @return the myLists
     */
    public List<ItemBean> getMyItems() {
        ListDAO aList = new ListDAOImpl();
        myItems = aList.myListItems(theModel.getListName(), theUserModel.getEmail());
        return myItems;
    }

    /**
     * @param myItems the myLists to set
     */
    public void setMyItems(List<ItemBean> myItems) {
        this.myItems = myItems;
    }

    /**
     * @return the theUserModel
     */
    public UserBean getTheUserModel() {
        return theUserModel;
    }

    /**
     * @param theUserModel the theUserModel to set
     */
    public void setTheUserModel(UserBean theUserModel) {
        this.theUserModel = theUserModel;
    }

    /**
     * @return the myHighItems
     */
    public List<ItemBean> getMyHighItems() {
        ListDAO aList = new ListDAOImpl();
        myHighItems = aList.myHighListItems(theModel.getListName(), theUserModel.getEmail());
        return myHighItems;
    }

    /**
     * @param myHighItems the myHighItems to set
     */
    public void setMyHighItems(List<ItemBean> myHighItems) {
        this.myHighItems = myHighItems;
    }

    /**
     * @return the myLowItems
     */
    public List<ItemBean> getMyLowItems() {
        ListDAO aList = new ListDAOImpl();
        myLowItems = aList.myLowListItems(theModel.getListName(), theUserModel.getEmail());
        return myLowItems;
    }

    /**
     * @param myLowItems the myLowItems to set
     */
    public void setMyLowItems(List<ItemBean> myLowItems) {
        this.myLowItems = myLowItems;
    }

    public String findUserName(int accountId) {
        String name = null;
        System.out.println("accId: " + accountId);
        UserDAO aUser = new UserDAOImpl();
        name = aUser.findUserName(accountId);
        return name;
    }

    public String performCompletion(int itemId) {
        System.out.println("itemid: " + itemId);
        ListDAO aList = new ListDAOImpl();
        int rowCount = aList.performComplete(itemId);
        if (rowCount == 1) {
            return "viewList.xhtml";

        } else {
            return "";
        }
    }

    public String performAddBack(int itemId) {
        System.out.println("itemid: " + itemId);
        ListDAO aList = new ListDAOImpl();
        int rowCount = aList.performAddBack(itemId);
        if (rowCount == 1) {
            return "viewList.xhtml";

        } else {
            return "";
        }
    }

    public String isCompleted(String status) {
        if (status.equalsIgnoreCase("completed")) {
            return "true";
        } else {
            return "false";
        }
    }

    public String isPending(String status) {
        if (status.equalsIgnoreCase("pending")) {
            return "true";
        } else {
            return "false";
        }

    }

    public String addNewMember(String listName, String fromEmail) {
        ListDAO aList = new ListDAOImpl();
        String result = null;
        String toEmail = getMemberEmail();
        if (toEmail.length() == 0) {
            validationMessage = "Please enter the email address";
        } else {
            int rowCount = aList.NewMemberRequest(toEmail, listName, fromEmail);
            if (rowCount == 1) {
                result = "viewList.xhtml";
            } else {
                result = "";
            }
        }

        return result;
    }

    /**
     * @return the memberEmail
     */
    public String getMemberEmail() {
        return memberEmail;
    }

    /**
     * @param memberEmail the memberEmail to set
     */
    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    /**
     * @return the validationMessage
     */
    public String getValidationMessage() {
        return validationMessage;
    }

    /**
     * @param validationMessage the validationMessage to set
     */
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String performAcceptance(int listId, String email) {
        System.out.println("listId:" + listId);
        System.out.println("email: " + email);
        ListDAO aList = new ListDAOImpl();
        int rowCount = aList.performAcceptance(listId, email);
        return "";
    }

    public String performDenial(int listId, String email) {
        System.out.println("listId:" + listId);
        System.out.println("email: " + email);
        ListDAO aList = new ListDAOImpl();
        int rowCount = aList.performDenial(listId, email);
        
        return "";
    }

    /**
     * @return the theRequestModel
     */
    public RequestBean getTheRequestModel() {
        return theRequestModel;
    }

    /**
     * @param theRequestModel the theRequestModel to set
     */
    public void setTheRequestModel(RequestBean theRequestModel) {
        this.theRequestModel = theRequestModel;
    }
}

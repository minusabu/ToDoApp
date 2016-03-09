/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ListDAO;
import dao.ListDAOImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.ItemBean;
import model.ListBean;
import model.UserBean;

/**
 *
 * @author MinuRachel
 */
@ManagedBean
@SessionScoped
public class ItemController {

    private ListBean theListModel;
    private ItemBean theModel;
    private UserBean theUserModel;
    private String validationMessage;
    

    public ItemController() {
        theListModel = new ListBean();
        theModel = new ItemBean();
        theUserModel = new UserBean();
    }

    /**
     * @return the theListModel
     */
    public ListBean getTheListModel() {
        return theListModel;
    }

    /**
     * @param theListModel the theListModel to set
     */
    public void setTheListModel(ListBean theListModel) {
        this.theListModel = theListModel;
    }

    /**
     * @return the theModel
     */
    public ItemBean getTheModel() {
        return theModel;
    }

    /**
     * @param theModel the theModel to set
     */
    public void setTheModel(ItemBean theModel) {
        this.theModel = theModel;
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

    public String addNewItem(String email, String listName) {
        String result = null;
        System.out.println("accountid=" + email);
        String itemName = theModel.getItemName();
        int quantity = theModel.getQuantity();
        String priority = theModel.getPriority();
//        System.out.println("listName: " + listName);
        if (itemName.length() == 0) {
            validationMessage = "Please enter item name";
        } else if (quantity == 0) {
            validationMessage = "Please enter item name";
        } else if (priority.length() == 0) {
            validationMessage = "Please enter a priority";
        } else {
            ListDAO aNewList = new ListDAOImpl();
            int rowCount = aNewList.createNewItem(theModel, email, listName);
            if (rowCount == 1) {
                result = "viewList.xhtml";
            } else {
                validationMessage = "Cannot add item. Please try again later";
                result = "";
            }
        }
        return result;
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

}

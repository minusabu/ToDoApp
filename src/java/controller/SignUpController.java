/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.faces.context.ExternalContextImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import java.util.Map;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.UserBean;

/**
 *
 * @author MinuRachel
 */
@ManagedBean
@SessionScoped
public class SignUpController {

    private String response;
    private String signUpValidaton;
    private UserBean theModel;
//    @ManagedProperty("#{param.aid}")
    private String aid;

    /**
     * Creates a new instance of SignUpController
     */
    public SignUpController() {
        theModel = new UserBean();
    }

    /**
     * @return the signUpValidaton
     */
    public String getSignUpValidaton() {
        return signUpValidaton;
    }

    /**
     * @param signUpValidaton the signUpValidaton to set
     */
    public void setSignUpValidaton(String signUpValidaton) {
        this.signUpValidaton = signUpValidaton;
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
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        String first = theModel.getFirstName();
        String last = theModel.getLastName();
        if (first.contains("''")) {
            first = first.replace("''", "'");
        } else if (last.contains("''")) {
            last = last.replace("''", "'");
        }
        String resultStr = "";
        resultStr += "<br/>Hello " + first + " " + last + "," + "<br/><br/>";
        resultStr += "Thank you for signing up with ToDoMate. <br/>";
        resultStr += "An email has been sent to to the email address provided. "
                + "Please click on the link provided in the email to confirm your new account. "
                + "Also, check the junk folder in your email if you cannot find it. <br/><br/>";
        resultStr += "Cheers, <br/> ToDoMateTeam.";
        response = resultStr;
        return response;
    }

    public String authenticateSignUp() {
        String validationMessage = null;

        String first = theModel.getFirstName();
        String last = theModel.getLastName();
        String email = theModel.getEmail();
        String pwd = theModel.getPassword();
        String confPwd = theModel.getConfirmPassword();

        UserDAO aSignUpDAO = new UserDAOImpl();
//        int uidCount = aSignUpDAO.checkUserName(email);

        if (first.length() == 0) {
            signUpValidaton = "The first name field cannot be left blank. Please enter your first name";
        } else if (first.length() < 2 || first.length() > 25) {
            signUpValidaton = "Your first name has to be between 2 and 25 letters long";
        } else if (last.length() == 0) {
            signUpValidaton = "The last name field cannot be left blank. Please enter your last name";
        } else if (last.length() < 2 || last.length() > 25) {
            signUpValidaton = "Your last name has to be between 2 and 25 letters long";
        } else if (email.length() == 0) {
            signUpValidaton = "A valid email address is required to create an account. A confirmation email will also be sent to this email. Please enter your email id";
        } else if (pwd.length() == 0) {
            signUpValidaton = "A password is required to create an account. Please enter a password for your account";
        } else if (pwd.length() < 8) {
            signUpValidaton = "The password should have at least 8 characters";
        } else if (confPwd.length() == 0) {
            signUpValidaton = "Please confirm the password";
        } else if (!pwd.equals(confPwd)) {
            signUpValidaton = "Passwords do not match";
        } else {
            signUpValidaton = "";
//            validationMessage = "signUpConfirmation.xhtml";
            validationMessage = createAccount();
//            sendEmailForApproval();
        }
        return validationMessage;
    }

    public String createAccount() {
        UserDAO aSignUpDAO = new UserDAOImpl();    // Creating a new object each time.
        int rowCount = aSignUpDAO.createAccount(theModel); // Doing anything with the object after this?
//        int rowCount = 1;
        if (rowCount == 1) {
//            theModel.setIsLoggedIn("NotLoggedIn");
            int accountid = aSignUpDAO.findAccountID();
            sendEmailForApproval(accountid);
            return "signUpConfirmation.xhtml"; // navigate to "signUpConfirmation.xhtml"
        } else {
//            theModel.setIsLoggedIn("NotLoggedIn");
            return "error.xhtml";
        }
    }

    public void sendEmailForApproval(int accountid) {
        // Recipient's email ID needs to be mentioned.
        String to = "msabu@ilstu.edu"; //have to query db to get the email of admin
        // Sender's email ID needs to be mentioned
        String from = "msabu@ilstu.edu";

        // Assuming you are sending email from this host
        String host = "smtp.ilstu.edu";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.user", "msabu@ilstu.edu"); // if needed
        properties.setProperty("mail.password", "August@2014"); // if needed
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("ToDoMate: Confirm New Account");
            String messageBody = "Thank you for signing up for a new account. Please click on the link below to confirm your email address.";
            messageBody += "<br/><br/><a href = \"http://localhost:8080/ToDoApp/faces/confirmEmail.xhtml?aid=" + accountid + "\">Click here to confirm</a>";
            messageBody += "<br/>Thank you!";
            // Send the actual HTML message, as big as you like
            message.setContent(messageBody,
                    "text/html");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            throw new RuntimeException(mex);
        }
    }

    public String confirmEmail() {
        System.out.println("confirming...");
        Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        aid = (String) requestMap.get("aid");

        System.out.println("aid: " + aid);
        UserDAO aSignUpDAO = new UserDAOImpl();
        int rowCount = aSignUpDAO.activateAccount(aid);
        if(rowCount==1){
           
            return "confirmEmailSuccess.xhtml";
        }
           
        return "";
    }

    /**
     * @return the accountIdToConfirm
     */
    public String getAid() {
        return aid;
    }

    /**
     * @param accountIdToConfirm the accountIdToConfirm to set
     */
    public void setAid(String aid) {
        this.aid = aid;
    }

//    public void init() {
//        if (accountIdToConfirm == null) {
//            String message = "Bad request. Please use a link from within the system.";
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
////            return;
//        }
//        user = userService.find(userId);
//
//        if (user == null) {
//            String message = "Bad request. Unknown user.";
//            FacesContext.getCurrentInstance().addMessage(null, 
//                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
//        }
//    }
}

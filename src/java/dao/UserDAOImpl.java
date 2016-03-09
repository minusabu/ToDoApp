/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.UserBean;

/**
 *
 * @author MinuRachel
 */
public class UserDAOImpl implements UserDAO {

    //    private String myDB = "jdbc:derby://localhost:1527/ToDoApp";
    private String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/msabu_Fa2015_ToDoMateDB";
    private String driver = "org.apache.derby.jdbc.ClientDriver";

    @Override
    public int createAccount(UserBean aSignUp) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        int rowCount = 0;
        try {

            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String accountInsertString;
            Statement stmt = DBConn.createStatement();

            accountInsertString = "INSERT INTO ITKSTU.ACCOUNT (FIRSTNAME, LASTNAME, EMAIL, PASSWORD, STATUS) VALUES ('"
                    + aSignUp.getFirstName()
                    + "','" + aSignUp.getLastName()
                    + "','" + aSignUp.getEmail()
                    + "','" + aSignUp.getPassword()
                    + "', 'pending'"
                    + ")";

            rowCount = stmt.executeUpdate(accountInsertString);
            System.out.println("insert string =" + accountInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Account Table insert completed");

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in createAccount()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    @Override
    public int checkUserName(String email) {
        String query = "SELECT COUNT(*) AS USERCOUNT FROM ITKSTU.ACCOUNT ";
        query += "WHERE EMAIL  = '" + email + "'";

        int existingUserCount = 0;
        try {
            DBHelper.loadDriver(driver);
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                existingUserCount = Integer.parseInt(rs.getString("USERCOUNT"));
            }

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in checkUserName()");
            System.err.println(e.getMessage());
        }

        return existingUserCount;
    }

    @Override
    public int findAccount(UserBean aLogin) {
        String userName = aLogin.getEmail();
        String password = aLogin.getPassword();
        String query = "SELECT COUNT(*) AS USERCOUNT FROM ITKSTU.ACCOUNT ";
        query += "WHERE email  = ? AND password = ? AND status = 'approved'";
        int accountsCount = 0;
        try {
            DBHelper.loadDriver(driver);
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
//            Statement stmt = DBConn.createStatement();
            PreparedStatement stmt = DBConn.prepareStatement(query);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accountsCount = Integer.parseInt(rs.getString("USERCOUNT"));
            }
            System.out.println("approved account count=" + accountsCount);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccount()");
            System.err.println(e.getMessage());
        }
        return accountsCount;
    }

    @Override
    public int findPendingAccount(UserBean aLogin) {
        String userName = aLogin.getEmail();
        String password = aLogin.getPassword();

        String query = "SELECT COUNT(*) AS PENDINGUSERCOUNT FROM ITKSTU.ACCOUNT ";
        query += "WHERE email  = ? AND password = ? AND status = 'pending'";

        int pendingAccountsCount = 0;
        try {
            DBHelper.loadDriver(driver);
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            PreparedStatement stmt = DBConn.prepareStatement(query);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pendingAccountsCount = Integer.parseInt(rs.getString("PENDINGUSERCOUNT"));
            }
            System.out.println("pending user count =" + pendingAccountsCount);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findPendingAccount()");
            System.err.println(e.getMessage());
        }
        return pendingAccountsCount;
    }

    @Override
    public int activateAccount(String aid) {
        String updateString = "UPDATE ITKSTU.ACCOUNT SET status = 'approved' WHERE accountid = " + aid;
        Connection DBConn = null;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int rowCount = 0;
        try {
//            String myDB = "jdbc:derby://localhost:1527/IT353";
            DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            Statement stmt = DBConn.createStatement();

            rowCount = stmt.executeUpdate(updateString);
            System.out.println("updateString =" + updateString);
            System.out.println("rowcount: " + rowCount);
            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findPendingAccount()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    @Override
    public int findAccountID() {
        int maxAcctId = 0;
        String query = "SELECT MAX(ACCOUNTID) AS MAXAID FROM ITKSTU.ACCOUNT";
        try {
            DBHelper.loadDriver(driver);
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            PreparedStatement stmt = DBConn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                maxAcctId = Integer.parseInt(rs.getString("MAXAID"));
            }
            System.out.println("max acct id =" + maxAcctId);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountID()");
            System.err.println(e.getMessage());
        }
        return maxAcctId;
    }

    @Override
    public String findUserName(int accountid) {
        System.out.println("in db acctid: " + accountid);

        String query = "SELECT FIRSTNAME||' '||LASTNAME AS USERNAME FROM ITKSTU.ACCOUNT ";
        query += "WHERE ACCOUNTID  = " + accountid;

        String userName = "";
        try {
            DBHelper.loadDriver(driver);
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                userName = rs.getString("USERNAME");
                System.out.println("added by: " + userName);
            }

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findUserName()");
            System.err.println(e.getMessage());
        }

        return userName;

    }

}

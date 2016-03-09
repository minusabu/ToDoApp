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
import java.util.ArrayList;
import model.ItemBean;
import model.ListBean;
import model.RequestBean;

/**
 *
 * @author MinuRachel
 */
public class ListDAOImpl implements ListDAO {

//    private String myDB = "jdbc:derby://localhost:1527/ToDoApp";
    private String myDB = "jdbc:derby://gfish2.it.ilstu.edu:1527/msabu_Fa2015_ToDoMateDB";
    private String driver = "org.apache.derby.jdbc.ClientDriver";

    @Override
    public int createNewList(ListBean aList, String email) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int accountid = findAccountIdFromEmail(email);
        int rowCount = 0;
        try {

            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String listInsertString, contentInsertString;
            Statement stmt = DBConn.createStatement();

            listInsertString = "INSERT INTO ITKSTU.LIST (LISTNAME, CREATEDATE, OWNER) VALUES ('"
                    + aList.getListName()
                    + "', CAST(CURRENT_TIMESTAMP AS DATE), "
                    + accountid + ")";

            rowCount = stmt.executeUpdate(listInsertString);
            System.out.println("insert string =" + listInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("List Table insert completed");

            int listId = findMaxID("LISTID", "LIST");

            contentInsertString = "INSERT INTO ITKSTU.CONTENT (ACCOUNTID, LISTID) VALUES ("
                    + accountid + ", "
                    + listId + ")";

            rowCount = stmt.executeUpdate(contentInsertString);
            System.out.println("insert string =" + contentInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Content Table insert completed");

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in createAccount()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    private int findAccountIdFromEmail(String email) {

//        email = email.trim();
        int accountID = 0;

        String retrieveIDQuery = "SELECT ACCOUNTID FROM ITKSTU.ACCOUNT WHERE EMAIL = '"
                + email + "'";

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(retrieveIDQuery);
            while (rs.next()) {
                accountID = Integer.parseInt(rs.getString("ACCOUNTID"));
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountIdFromEmail()");
            System.err.println(e.getMessage());
        }
        return accountID;
    }

    @Override
    public int findListIdFromListName(String listName, int accountid) {

        listName = listName.trim();

        int listID = 0;

        String retrieveIDQuery = "SELECT ITKSTU.LIST.LISTID FROM ITKSTU.LIST "
                + "JOIN ITKSTU.CONTENT ON ITKSTU.LIST.LISTID = ITKSTU.CONTENT.LISTID WHERE LISTNAME = '"
                + listName + "' AND ITKSTU.CONTENT.ACCOUNTID = " + accountid;

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formuSlate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(retrieveIDQuery);
            while (rs.next()) {
                listID = Integer.parseInt(rs.getString("LISTID"));
            }
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountIdFromEmail()");
            System.err.println(e.getMessage());
        }
        return listID;
    }

    @Override
    public int createNewItem(ItemBean anItem, String email, String listName) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int accountid = findAccountIdFromEmail(email);
        int listId = findListIdFromListName(listName, accountid);
        int rowCount = 0;
        try {

            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String itemInsertString;
            Statement stmt = DBConn.createStatement();
            System.out.println("just before item insert");
            itemInsertString = "INSERT INTO ITKSTU.ITEM (ITEMNAME, LISTID, ACCOUNTID, QUANTITY, PRIORITY, STATUS) VALUES ('"
                    + anItem.getItemName() + "', "
                    + listId + ", "
                    + accountid + ", "
                    + anItem.getQuantity() + ", '"
                    + anItem.getPriority() + "', default)";

            rowCount = stmt.executeUpdate(itemInsertString);
            System.out.println("insert string =" + itemInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Item Table insert completed");

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in createAccount()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    @Override
    public ArrayList myListSearch(String email) {
        ArrayList listCollection = new ArrayList();
//        String listName;
        int accountId = findAccountIdFromEmail(email);
        ListBean aList;
        String query = "SELECT LIST.LISTNAME, LIST.LISTID, LIST.OWNER, LIST.CREATEDATE "
                + "FROM LIST "
                + "JOIN CONTENT ON LIST.LISTID=CONTENT.LISTID "
                + "JOIN ACCOUNT ON ACCOUNT.ACCOUNTID=CONTENT.ACCOUNTID "
                + "WHERE ACCOUNT.ACCOUNTID=" + accountId;
//        ArrayList searchResults = performSearch(query);

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                aList = new ListBean();
                aList.setListName(rs.getString("LISTNAME"));
                aList.setListID(rs.getString("LISTID"));
                aList.setCreatedDate(rs.getDate("CREATEDATE").toString());
                aList.setOwner(rs.getString("OWNER"));
                listCollection.add(aList);
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in myListSearch()");
            System.err.println(e.getMessage());
        }
        return listCollection;
    }

    @Override
    public ArrayList myRequestSearch(String email) {
        ArrayList requestCollection = new ArrayList();
//        String listName;
        int accountId = findAccountIdFromEmail(email);
        ListBean aList;
        String query = "SELECT LIST.LISTNAME, LIST.LISTID, LIST.OWNER, LIST.CREATEDATE "
                + "FROM LIST "
                + "JOIN REQUEST ON REQUEST.LISTID = LIST.LISTID "
                + "WHERE REQUEST.ACCOUNTID=" + accountId;
//        ArrayList searchResults = performSearch(query);

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                aList = new ListBean();
                aList.setListName(rs.getString("LISTNAME"));
                aList.setListID(rs.getString("LISTID"));
                aList.setCreatedDate(rs.getDate("CREATEDATE").toString());
                aList.setOwner(rs.getString("OWNER"));

                requestCollection.add(aList);
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in myRequestSearch()");
            System.err.println(e.getMessage());
        }
        return requestCollection;
    }

    @Override
    public ArrayList myListItems(String listName, String email) {
        ArrayList itemCollection = new ArrayList();
//        String listName;
        int accountId = findAccountIdFromEmail(email);
        int listId = findListIdFromListName(listName, accountId);
        ItemBean anItem;
        String query = "SELECT ITEM.ITEMNAME, ITEM.PRIORITY, ITEM.QUANTITY, ITEM.ACCOUNTID, ITEM.STATUS, ITEM.ITEMID "
                + "FROM ITEM "
                + "WHERE ITEM.PRIORITY='Medium' and ITEM.LISTID=" + listId;
//        ArrayList searchResults = performSearch(query);

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                anItem = new ItemBean();
                anItem.setItemName(rs.getString("ITEMNAME"));
                anItem.setPriority(rs.getString("PRIORITY"));
                anItem.setQuantity(rs.getInt("QUANTITY"));
                anItem.setAccountId(rs.getInt("ACCOUNTID"));
                anItem.setStatus(rs.getString("STATUS"));
                anItem.setItemId(rs.getInt("ITEMID"));
                itemCollection.add(anItem);
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in myListSearch()");
            System.err.println(e.getMessage());
        }
        return itemCollection;
    }

    @Override
    public ArrayList myLowListItems(String listName, String email) {
        ArrayList itemCollection = new ArrayList();
//        String listName;
        int accountId = findAccountIdFromEmail(email);
        int listId = findListIdFromListName(listName, accountId);
        ItemBean anItem;
        String query = "SELECT ITEM.ITEMNAME, ITEM.PRIORITY, ITEM.QUANTITY, ITEM.ACCOUNTID, ITEM.STATUS, ITEM.ITEMID "
                + "FROM ITEM "
                + "WHERE ITEM.PRIORITY='Low' and ITEM.LISTID=" + listId;
//        ArrayList searchResults = performSearch(query);

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                anItem = new ItemBean();
                anItem.setItemName(rs.getString("ITEMNAME"));
                anItem.setPriority(rs.getString("PRIORITY"));
                anItem.setQuantity(rs.getInt("QUANTITY"));
                anItem.setAccountId(rs.getInt("ACCOUNTID"));
                anItem.setStatus(rs.getString("STATUS"));
                anItem.setItemId(rs.getInt("ITEMID"));
                itemCollection.add(anItem);
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in myListSearch()");
            System.err.println(e.getMessage());
        }
        return itemCollection;
    }

    @Override
    public ArrayList myHighListItems(String listName, String email) {
        ArrayList itemCollection = new ArrayList();
//        String listName;
        int accountId = findAccountIdFromEmail(email);
        int listId = findListIdFromListName(listName, accountId);
        ItemBean anItem;
        String query = "SELECT ITEM.ITEMNAME, ITEM.PRIORITY, ITEM.QUANTITY, ITEM.ACCOUNTID, ITEM.STATUS, ITEM.ITEMID "
                + "FROM ITEM "
                + "WHERE ITEM.PRIORITY='High' and ITEM.LISTID=" + listId;
//        ArrayList searchResults = performSearch(query);

        try {
            DBHelper.loadDriver(driver);
//            DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
//            String myDB = "jdbc:derby://localhost:1527/IT353";
//            String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
            // With the connection made, create a statement to talk to the DB server.
            // Create a SQL statement to query, retrieve the rows one by one (by going to the
            // columns), and formulate the result string to send back to the client.
            Statement stmt = DBConn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                anItem = new ItemBean();
                anItem.setItemName(rs.getString("ITEMNAME"));
                anItem.setPriority(rs.getString("PRIORITY"));
                anItem.setQuantity(rs.getInt("QUANTITY"));
                anItem.setAccountId(rs.getInt("ACCOUNTID"));
                anItem.setStatus(rs.getString("STATUS"));
                anItem.setItemId(rs.getInt("ITEMID"));
                itemCollection.add(anItem);
            }
            rs.close();
            stmt.close();
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in myListSearch()");
            System.err.println(e.getMessage());
        }
        return itemCollection;
    }

    private int findMaxID(String columnName, String tableName) {
        int maxId = 0;
        String query = "SELECT MAX(" + columnName + ") AS MAXID FROM ITKSTU." + tableName;
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
                maxId = Integer.parseInt(rs.getString("MAXID"));
            }
            System.out.println("max id =" + maxId);
            DBConn.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL select in findAccountID()");
            System.err.println(e.getMessage());
        }
        return maxId;
    }

    @Override
    public int performComplete(int itemid) {
        String updateString = "UPDATE ITKSTU.ITEM SET status = 'completed' WHERE itemid = " + itemid;
        System.out.println("query=" + updateString);
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
            System.err.println("ERROR: Problems with SQL select in performComplete()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    @Override
    public int performAddBack(int itemid) {
        String updateString = "UPDATE ITKSTU.ITEM SET status = 'pending' WHERE itemid = " + itemid;
        System.out.println("query=" + updateString);
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
            System.err.println("ERROR: Problems with SQL select in performComplete()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }
@Override
    public int NewMemberRequest(String toEmail, String listName, String fromEmail) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int accountid = findAccountIdFromEmail(toEmail);
        int senderAccountid = findAccountIdFromEmail(fromEmail);
        int listId = findListIdFromListName(listName, senderAccountid);
        int rowCount = 0;
        try {

            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String requestInsertString;
            Statement stmt = DBConn.createStatement();

            requestInsertString = "INSERT INTO ITKSTU.REQUEST (LISTID, ACCOUNTID) VALUES ("
                    + listId + ", "
                    + accountid + ")";

            rowCount = stmt.executeUpdate(requestInsertString);
            System.out.println("insert string =" + requestInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Request Table insert completed");

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in createAccount()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    @Override
    public int performAcceptance(int listId, String email) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int accountid = findAccountIdFromEmail(email);

        int rowCount = 0;
        try {

            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String contentInsertString, requestDeleteString;
            Statement stmt = DBConn.createStatement();

            contentInsertString = "INSERT INTO ITKSTU.CONTENT (LISTID, ACCOUNTID) VALUES ("
                    + listId + ", "
                    + accountid + ")";

            rowCount = stmt.executeUpdate(contentInsertString);
            System.out.println("insert string =" + contentInsertString);
            System.out.println(rowCount + " row(s) inserted");
            System.out.println("Content Table insert completed");

            if (rowCount == 1) {
                requestDeleteString = "DELETE FROM ITKSTU.REQUEST WHERE LISTID = " + listId
                        + " AND ACCOUNTID = " + accountid;
                rowCount = stmt.executeUpdate(requestDeleteString);
                System.out.println("delete string =" + requestDeleteString);
                System.out.println(rowCount + " row(s) deleted");
                System.out.println("Request Table delete completed");
            }

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in performAcceptance()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

    @Override
    public int performDenial(int listId, String email) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        int accountid = findAccountIdFromEmail(email);

        int rowCount = 0;
        try {

            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");

            String requestDeleteString;
            Statement stmt = DBConn.createStatement();

            requestDeleteString = "DELETE FROM ITKSTU.REQUEST WHERE LISTID = " + listId
                    + " AND ACCOUNTID = " + accountid;
            rowCount = stmt.executeUpdate(requestDeleteString);
            System.out.println("delete string =" + requestDeleteString);
            System.out.println(rowCount + " row(s) deleted");
            System.out.println("Request Table delete completed");

            DBConn.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Problems with SQL insert in performDenial()");
            System.err.println(e.getMessage());
        }
        return rowCount;
    }

}

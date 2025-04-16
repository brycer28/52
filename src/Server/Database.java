package Server;
import java.util.*;
import java.sql.*;
import java.io.*;

public class Database
{
    private Connection conn;
    //Add any other data fields you like – at least a Connection object is mandatory
    public Database()
    {
        //Add your code here

        //1. Create a properties object
        Properties prop = new Properties();
        FileInputStream fis = null;

        //2. Open the db.properties with FileInputStrea
        try {
            fis = new FileInputStream("Server/db.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String pass = prop.getProperty("password");

        try {
            conn = DriverManager.getConnection(url,user,pass);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyAccount(String username, String password)
    {
        // Read the database file.
        String key = "'Key'";
        String query = "SELECT username, aes_decrypt(password, " + key + ")"
                + "FROM users "
                + "WHERE username = '" + username + "';";

        // Stop if this account doesn't exist.
        if (query(query) == null)
            return false;

        // if username/password combination exists, return true
        return true;

//        String db_password = query(query).getFirst().split(",")[1];
//
//        // Check the username and password.
//        if (password.equals(db_password))
//            return true;
//        else
//            return false;
    }

    public boolean createNewAccount(String username, String password)
    {
        // Read the database file.
        String key = "'Key'";
        String query = "SELECT username "
                + "FROM users "
                + "WHERE username = '" + username + "';";


        // Stop if this account already exists.
        if (query(query) != null) {
            return false;
        }

        // Add the new account.
        String insert = "INSERT INTO users "
                + "VALUES ('"
                + username + "', aes_encrypt('" + password + "', " + key + "));";

        // Write the username to the database.
        try {
            executeDML(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<String> query(String query)
    {
        //Declare the return value first
        ArrayList<String> list = new ArrayList<String>();
        String record = "";
        //Process the query

        try {
            //1. Create a Statement object
            Statement stmt = conn.createStatement();

            //2. Execute the Query on the stmt
            ResultSet rs = stmt.executeQuery(query);

            //3. Get the Meta Data
            ResultSetMetaData rsmd = rs.getMetaData();

            int noFields = rsmd.getColumnCount();

            //4. Iterate through each record
            while (rs.next()) {
                record = "";
                for (int i = 0; i < noFields; i++) {
                    record += rs.getString(i + 1);
                    record += ",";
                }
                list.add(record);
            }
            //Check for empty result set
            if (list.isEmpty()) {
                return null;
            }

        }
        catch(SQLException e) {
            return null;
        }

        return list;
    }

    public void executeDML(String dml) throws SQLException
    {
        //Create a statement from the connection
        Statement stmt = conn.createStatement();

        //Execute the DML
        stmt.execute(dml);
    }

}

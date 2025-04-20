package server;

import org.junit.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class DatabaseClassTest {

    private String[] users = {"jsmith@uca.edu","msmith@uca.edu","tjones@yahoo.com","jjones@yahoo.com"};
    private String[] passwords = {"hello123","pass123","123456","hello1234"};
    private DatabaseClass db;
    private int rando;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseClass();
        db.setConnection("server/db.properties");
        rando = ((int) (Math.random() * users.length));
    }

    @Test
    public void testSetConnection() {
        db.setConnection("server/db.properties");
        Connection conn = db.getConnection();
        assertNotNull(conn);
    }

    @Test
    public void testQuery() throws SQLException{

        String query = "SELECT aes_decrypt(password, 'Key') "
                + "FROM users "
                + "WHERE username = '" + users[rando] +"';";

        String actual = db.query(query).getFirst().split(",")[0];
        String expected = passwords[rando];
        assertEquals("random #" + rando, expected, actual);
    }

    @Test
    public void testExecuteDML() {

        //1. set connection
        db.setConnection("lab8out/db.properties");

        //2. get connection
        Connection conn = db.getConnection();

        //3. build a DML statement that will insert a new username with any desired password.
        String username = "'thuynh5@uca.edu'";
        String password = "'UCAbears'";

        String dml = "INSERT INTO users "
                + "VALUES (" + username + ", aes_encrypt(" + password + ", 'key'));";

        try {
            db.executeDML(dml);
        }
        // Test will fail if insert statement is invalid
        catch (SQLException e) {
            fail("SQL Exception: " + e.getMessage());
        }

    }


}
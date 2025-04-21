package server;

import org.junit.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class DatabaseClassUnitTest {

    private String[] users = {"thi","bryce","cali","michael", "kendal"};
    private String[] passwords = {"thi123","bryce123","cali123","michael123","kendal123"};
    private DatabaseClass db;
    private int rando;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        db = new DatabaseClass();
        rando = ((int) (Math.random() * users.length));
    }

    @Test
    public void testSetConnection() {
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

        //2. get connection
        Connection conn = db.getConnection();

        //3. build a DML statement that will insert a new username with any desired password.
        String username = "'databaseTest'";
        String password = "'UCAbears'";

        String dml = "INSERT INTO users "
                + "VALUES (" + username + ", aes_encrypt(" + password + ", 'key'), 500);";

        try {
            db.executeDML(dml);
        }
        // Test will fail if insert statement is invalid
        catch (SQLException e) {
            fail("SQL Exception: " + e.getMessage());
        }

    }


}
package parser;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.connection.ConnectionRollback;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class PostgreSqlStoreTest {
    private String propPath = "./src/main/resources/app.properties";
    private Properties config;
    private Connection connection;
    
    private Store store;
    private static final Logger LOG = LoggerFactory.getLogger(PostgreSqlStoreTest.class);
    
    @Before
    public void setUp() {
        initConnectionRollBack();
        this.store = new PostgreSqlStore(this.connection);
    }
    
    @Test
    public void testSaveAndGet() {
        var list = List.of(
                new Post("job1", "111", "www.link1.ru"),
                new Post("job2", "222", "www.link2.ru"),
                new Post("job3", "333", "www.link3.ru")
        );
        list.forEach(store::save);
        
        var r1 = store.get(x -> x.getName().length() > 0);
        assertTrue(r1.size() >= 3);
        
        var r2 = store.get(x -> x.getDesc().equals("111"));
        assertTrue(r2.size() >= 1);
        
        var r3 = store.get(x -> x.getLink().contains("link3"));
        assertTrue(r3.size() > 0);
    }
    
    /**
     * Init connection to DB.
     * <p>
     * If you don't need rollback connection - make comment code below:
     * this.connection = ConnectionRollback.create(connection);
     */
    private void initConnectionRollBack() {
        try {
            this.config = new Properties();
            this.config.load(new FileReader(new File(propPath)));
            this.connection = DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")
            );
            this.connection = ConnectionRollback.create(connection);
        } catch (SQLException | IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
}
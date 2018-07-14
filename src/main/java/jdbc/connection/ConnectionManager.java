package jdbc.connection;

import jdbc.doc.properties.DbPropertyHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class ConnectionManager {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);
    private BlockingQueue<Connection> connections;

    @Autowired
    public ConnectionManager(DbPropertyHolder propertyHolder) {
        Integer poolSize = propertyHolder.poolSize();

        connections = new LinkedBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(propertyHolder.url(), propertyHolder.login(), propertyHolder.password());
            } catch (SQLException e) {
                LOGGER.debug("Could not initialize connection pool!\n" + e.getMessage());
            }
            connections.add(connection);
        }
    }

    public Connection getConnection() {
        try {
            int MAX_WAITING_TIME = 2;
            Connection connection = connections.poll(MAX_WAITING_TIME, TimeUnit.SECONDS);
            if (connection != null) {
                LOGGER.debug("Connection " + connection + " took from connection pool");
            } else {
                LOGGER.error("Couldn't retrieve a connection from pool");
            }
            return connection;
        } catch (InterruptedException e) {
            LOGGER.error("Could not get connection! " + e.getMessage());
            return null;
        }
    }

    public final void release(Connection connection) {
        if (connection != null) {
            try {
                connections.put(connection);
                LOGGER.debug("Connection " + connection + " returned to connection pool");
                LOGGER.debug("There are(is) " + (connections.size() - connections.remainingCapacity()) + " connection(s) in the pool.");
            } catch (InterruptedException e) {
                LOGGER.error("Could not release connection into the pool " + e.getMessage());
            }
        }
    }

    public void printCurrentState() {
        System.out.println("There are(is) " + (connections.size() - connections.remainingCapacity()) + " connection(s) in the pool.");
    }
}

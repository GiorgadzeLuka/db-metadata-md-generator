package jdbc.report.analyzer;

import jdbc.connection.ConnectionManager;
import jdbc.data.Column;
import jdbc.data.Table;
import jdbc.properties.DbPropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class TableAnalyzer {
    private ConnectionManager connectionManager;
    private ColumnAnalyzer columnAnalyzer;
    private PrimaryKeyAnalyzer primaryKeyAnalyzer;
    private DbPropertyHolder dbPropertyHolder;

    @Autowired
    public TableAnalyzer(ConnectionManager connectionManager,
                         ColumnAnalyzer columnAnalyzer,
                         PrimaryKeyAnalyzer primaryKeyAnalyzer,
                         DbPropertyHolder dbPropertyHolder) {
        this.connectionManager = connectionManager;
        this.columnAnalyzer = columnAnalyzer;
        this.primaryKeyAnalyzer = primaryKeyAnalyzer;
        this.dbPropertyHolder = dbPropertyHolder;
    }

    public List<Table> getTableData() throws SQLException {
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = connection.getMetaData().getTables(null, dbPropertyHolder.schemaName(), null, new String[]{"TABLE"});
        List<Table> tables = new ArrayList<>();
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            List<Column> columns = columnAnalyzer.getTableColumns(connection, tableName);
            Map<String, String> primaryKeys = primaryKeyAnalyzer.getPrimaryKeys(connection, tableName);
            tables.add(new Table(tableName, columns, primaryKeys));
        }
        return tables;
    }

    private Table createTableRepresentation(Connection connection, String tableName) throws SQLException {
        List<Column> columns = columnAnalyzer.getTableColumns(connection, tableName);
        Map<String, String> primaryKeys = primaryKeyAnalyzer.getPrimaryKeys(connection, tableName);
        return new Table(tableName, columns, primaryKeys);
    }

    public List<Table> getTableDataParallel() throws SQLException, InterruptedException {
        List<Table> tables = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = connection.getMetaData().getTables(null, dbPropertyHolder.schemaName(), null, new String[]{"TABLE"});
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        List<Callable<Table>> tasks = new ArrayList<>();
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");

            tasks.add(() -> {
                List<Column> columns = columnAnalyzer.getTableColumns(connection, tableName);
                Map<String, String> primaryKeys = primaryKeyAnalyzer.getPrimaryKeys(connection, tableName);
                return new Table(tableName, columns, primaryKeys);
            });
//            executorService.submit(() -> {
//                List<Column> columns = columnAnalyzer.getTableColumns(connection, tableName);
//                Map<String, String> primaryKeys = primaryKeyAnalyzer.getPrimaryKeys(connection, tableName);
//                tables.add(new Table(tableName, columns, primaryKeys));
//            });
        }
        List<Future<Table>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        return tables;
    }
}

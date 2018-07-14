package jdbc.doc.analyzer;

import jdbc.doc.data.Column;
import jdbc.doc.data.Index;
import jdbc.doc.data.Table;
import jdbc.doc.properties.DbPropertyHolder;
import oracle.jdbc.OracleConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TableAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(TableAnalyzer.class);
    private ColumnAnalyzer columnAnalyzer;
    private PrimaryKeyAnalyzer primaryKeyAnalyzer;
    private IndexAnalyzer indexAnalyzer;
    private ReferenceAnalyzer referenceAnalyzer;
    private GroupAnalyzer groupAnalyzer;
    private DbPropertyHolder propertyHolder;

    @Autowired
    public TableAnalyzer(ColumnAnalyzer columnAnalyzer,
                         PrimaryKeyAnalyzer primaryKeyAnalyzer,
                         IndexAnalyzer indexAnalyzer,
                         ReferenceAnalyzer referenceAnalyzer,
                         GroupAnalyzer groupAnalyzer,
                         DbPropertyHolder propertyHolder) {
        this.columnAnalyzer = columnAnalyzer;
        this.primaryKeyAnalyzer = primaryKeyAnalyzer;
        this.indexAnalyzer = indexAnalyzer;
        this.referenceAnalyzer = referenceAnalyzer;
        this.groupAnalyzer = groupAnalyzer;
        this.propertyHolder = propertyHolder;
    }

    public List<Table> getTableData() {
        List<Table> tables = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(propertyHolder.url(), propertyHolder.login(), propertyHolder.password())) {
            turnOnCommentsFetchForOracleDb(connection);
            ResultSet resultSet = connection.getMetaData().getTables(null, propertyHolder.schemaName(), null, new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                tables.add(createTableRepresentation(connection, tableName));
            }
            resultSet.close();
        } catch (SQLException e) {
            LOGGER.error("Unable to process the db data", e);
        }
        return tables;
    }

    private void turnOnCommentsFetchForOracleDb(Connection connection) {
        if (propertyHolder.driverName() != null && propertyHolder.driverName().contains("OracleDriver")) {
            ((OracleConnection) connection).setRemarksReporting(true);
        }
    }

    private Table createTableRepresentation(Connection connection, String tableName) {
        List<Column> columns = columnAnalyzer.getTableColumns(connection, tableName);
        String primaryKeyName = primaryKeyAnalyzer.identifyPrimaryKey(connection, tableName, columns);
        referenceAnalyzer.analyzeTableReferences(connection, tableName, columns);
        List<Index> indices = indexAnalyzer.getTableIndices(connection, tableName);
        Table table = new Table(tableName, primaryKeyName, columns, indices);
        groupAnalyzer.matchTableToGroup(table);
        return table;
    }

    public List<Table> getTableDataConcurrently() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Void>> callables = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(propertyHolder.url(), propertyHolder.login(), propertyHolder.password());
            turnOnCommentsFetchForOracleDb(connection);
            ResultSet resultSet = connection.getMetaData().getTables(null, propertyHolder.schemaName(), null, new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                callables.add(() -> {
                    tables.add(createTableRepresentation(connection, tableName));
                    return null;
                });
            }
            resultSet.close();
            executorService.invokeAll(callables);
            executorService.shutdown();
        } catch (SQLException | InterruptedException e) {
            LOGGER.error("Unable to process the db data", e);
        }
        return tables;
    }

}

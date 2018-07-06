package jdbc.report.analyzer;

import jdbc.connection.ConnectionManager;
import jdbc.data.Column;
import jdbc.data.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TableAnalyzer {
    private ConnectionManager connectionManager;
    private ColumnAnalyzer columnAnalyzer;
    private PrimaryKeyAnalyzer primaryKeyAnalyzer;

    @Autowired
    public TableAnalyzer(ConnectionManager connectionManager, ColumnAnalyzer columnAnalyzer, PrimaryKeyAnalyzer primaryKeyAnalyzer) {
        this.connectionManager = connectionManager;
        this.columnAnalyzer = columnAnalyzer;
        this.primaryKeyAnalyzer = primaryKeyAnalyzer;
    }

    public List<Table> getTableData() throws SQLException {
        Connection connection = connectionManager.getConnection();
        List<String> tableNames = getTableNames(connection);

        List<Table> tables = new ArrayList<>();
        for (String tableName : tableNames) {
            tables.add(createTableRepresentation(connection, tableName));
        }
        return tables;
    }

    private static List<String> getTableNames(Connection connection) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(null, "CDL", null, new String[]{"TABLE"});
        List<String> tableNames = new ArrayList<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString("TABLE_NAME"));
        }
        return tableNames;
    }

    private Table createTableRepresentation(Connection connection, String tableName) throws SQLException {
        List<Column> columns = columnAnalyzer.getTableColumns(connection, tableName);
        Map<String, String> primaryKeys = primaryKeyAnalyzer.getPrimaryKeys(connection, tableName);
        return new Table(tableName, columns, primaryKeys);
    }
}

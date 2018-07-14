package jdbc.doc.analyzer;

import jdbc.doc.data.Column;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ColumnAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(ColumnAnalyzer.class);

    public List<Column> getTableColumns(Connection connection, String tableName) {
        List<Column> columnInfos = new ArrayList<>();
        try (ResultSet tableColumns = connection.getMetaData().getColumns(null, null, tableName, null)) {
            while (tableColumns.next()) {
                String columnName = tableColumns.getString("COLUMN_NAME");
                String columnType = tableColumns.getString("TYPE_NAME");
                String columnSize = tableColumns.getString("COLUMN_SIZE");
                String isNullable = tableColumns.getString("IS_NULLABLE");
                String comments = tableColumns.getString("REMARKS") == null ? "" : tableColumns.getString("REMARKS");
                columnInfos.add(new Column(columnName, columnType, columnSize, isNullable, comments));
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to fetch/process columns for table {}", tableName, e);
        }
        return columnInfos;
    }
}
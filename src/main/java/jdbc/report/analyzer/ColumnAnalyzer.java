package jdbc.report.analyzer;

import jdbc.data.Column;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ColumnAnalyzer {

    public List<Column> getTableColumns(Connection connection, String tableName) throws SQLException {
        ResultSet tableColumns = connection.getMetaData().getColumns(null, null, tableName, null);
        List<Column> columnInfos = new ArrayList<>();
        while (tableColumns.next()) {
            String columnName = tableColumns.getString("COLUMN_NAME");
            String columnType = tableColumns.getString("TYPE_NAME");
            String columnSize = tableColumns.getString("COLUMN_SIZE");
            String isNullable = tableColumns.getString("IS_NULLABLE");
            columnInfos.add(new Column(columnName, columnType, columnSize, isNullable));
        }
        tableColumns.close();
        return columnInfos;
    }
}

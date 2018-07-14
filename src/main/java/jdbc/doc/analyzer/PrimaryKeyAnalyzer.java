package jdbc.doc.analyzer;

import jdbc.doc.data.Column;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.lang.Boolean.compare;
import static jdbc.doc.data.util.ColumnUpdater.findColumnByNameAndUpdate;

@Component
public class PrimaryKeyAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(PrimaryKeyAnalyzer.class);

    public String identifyPrimaryKey(Connection connection, String tableName, List<Column> columns) {
        String primaryKeyName = null;
        try (ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, null, tableName)) {
            while (primaryKeys.next()) {
                if (primaryKeyName == null) {
                    primaryKeyName = primaryKeys.getString("PK_NAME");
                }
                String columnName = primaryKeys.getString("COLUMN_NAME");
                findColumnByNameAndUpdate(columnName, columns, column -> column.setPrimaryKey(true));
            }
            columns.sort((c1, c2) -> compare(c2.isPrimaryKey(), c1.isPrimaryKey()));
            primaryKeys.close();
        } catch (SQLException e) {
            LOGGER.error("Unable to fetch/process primary keys for table {}", tableName, e);
        }
        return primaryKeyName;
    }

}
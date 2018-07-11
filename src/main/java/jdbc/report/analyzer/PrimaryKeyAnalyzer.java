package jdbc.report.analyzer;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PrimaryKeyAnalyzer {

    public Map<String, String> getPrimaryKeys(Connection connection, String tableName) {
        Map<String, String> result = new HashMap<>();
        try {
            ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, null, tableName);
            while (primaryKeys.next()) {
                String columnName = primaryKeys.getString("COLUMN_NAME");
                String primaryKeyName = primaryKeys.getString("PK_NAME");
                result.put(columnName, primaryKeyName);
            }
            primaryKeys.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
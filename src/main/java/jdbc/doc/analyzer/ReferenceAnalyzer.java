package jdbc.doc.analyzer;

import jdbc.doc.data.Column;
import jdbc.doc.data.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static jdbc.doc.data.util.ColumnUpdater.findColumnByNameAndUpdate;

@Component
public class ReferenceAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(ReferenceAnalyzer.class);

    public void analyzeTableReferences(Connection connection, String tableName, List<Column> tableColumns) {
        try (ResultSet importedKeys = connection.getMetaData().getImportedKeys(null, null, tableName)) {
            while (importedKeys.next()) {
                String dependentColumn = importedKeys.getString("FKCOLUMN_NAME");
                String referencedTable = importedKeys.getString("PKTABLE_NAME");
                String referencedColumn = importedKeys.getString("PKCOLUMN_NAME");
                Reference reference = new Reference(referencedTable, referencedColumn);
                findColumnByNameAndUpdate(dependentColumn, tableColumns, column -> column.setReference(reference));
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to fetch/process references for table {}", tableName, e);
        }
    }

}

package jdbc.doc.analyzer;

import javafx.util.Pair;
import jdbc.doc.data.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class IndexAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(IndexAnalyzer.class);
    private static final short TABLE_INDEX_STATISTIC_TYPE = 0;

    public List<Index> getTableIndices(Connection connection, String tableName) {
        Map<String, Index> indexMap = new HashMap<>();
        try (ResultSet indices = connection.getMetaData().getIndexInfo(null, "CDL", tableName, false, false)) {
            while (indices.next()) {
                if (indices.getShort("TYPE") != TABLE_INDEX_STATISTIC_TYPE) {
                    String indexName = indices.getString("INDEX_NAME");
                    String columnName = indices.getString("COLUMN_NAME");
                    String ascOrDesc = indices.getString("ASC_OR_DESC");
                    if (ascOrDesc != null) {
                        if (Objects.equals(ascOrDesc, "A")) {
                            ascOrDesc = "\\u2191";
                        } else {
                            ascOrDesc = "\\u2193";
                        }
                    }
                    addIndexToIndexMap(indexMap, indexName, columnName, ascOrDesc);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to fetch/process indices for table {}", tableName, e);
        }
        return new ArrayList<>(indexMap.values());
    }

    private void addIndexToIndexMap(Map<String, Index> indexMap, String indexName, String columnName, String ascOrDesc) {
        Pair<String, String> columnWithOrdering = new Pair<>(columnName, ascOrDesc);
        if (!indexMap.containsKey(indexName)) {
            List<Pair<String, String>> columnsWithOrdering = new ArrayList<>();
            columnsWithOrdering.add(columnWithOrdering);
            indexMap.put(indexName, new Index(indexName, columnsWithOrdering));
        } else {
            indexMap.get(indexName).getColumnNamesWithOrdering().add(columnWithOrdering);
        }
    }

}

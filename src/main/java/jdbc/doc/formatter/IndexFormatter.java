package jdbc.doc.formatter;

import javafx.util.Pair;
import jdbc.doc.data.Index;

import java.util.List;

import static java.lang.String.format;

public class IndexFormatter {

    public static String formatIndex(Index index) {
        StringBuilder sb = new StringBuilder();
        List<Pair<String, String>> columns = index.getColumnNamesWithOrdering();
        for (int i = 0; i < columns.size(); i++) {
            String indexName = "";
            if (i == 0) {
                indexName = index.getIndexName().toLowerCase();
            }
            String indexOrder = columns.get(i).getValue() != null ? columns.get(i).getValue().toLowerCase() : "";
            sb.append(format("|%s|%s|%s|\n", indexName, columns.get(i).getKey().toLowerCase(), indexOrder));

        }
        return sb.toString();
    }
}

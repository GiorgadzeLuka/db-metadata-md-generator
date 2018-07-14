package jdbc.doc.data;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Index {
    private String indexName;
    private List<Pair<String, String>> columnNamesWithOrdering;
}

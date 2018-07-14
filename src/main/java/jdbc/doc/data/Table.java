package jdbc.doc.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Table {
    private String tableName;
    private String primaryKeyName;
    private List<Column> columns;
    private List<Index> indices;
}

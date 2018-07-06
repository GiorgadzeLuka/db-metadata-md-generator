package jdbc.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Table {
    private String tableName;
    private List<Column> columns;
    private Map<String, String> primaryKeys;
}

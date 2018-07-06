package jdbc.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Column {
    private String columnName;
    private String columnType;
    private String columnSize;
    private String isNullable;
}

package jdbc.doc.data;

import lombok.Data;

@Data
public class Column {
    private String columnName;
    private String columnType;
    private String columnSize;
    private String isNullable;
    private String comments;
    private boolean primaryKey;
    private Reference reference;

    public Column(String columnName, String columnType, String columnSize, String isNullable, String comments) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnSize = columnSize;
        this.isNullable = isNullable;
        this.comments = comments;
    }
}

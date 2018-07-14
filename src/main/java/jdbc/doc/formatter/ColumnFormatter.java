package jdbc.doc.formatter;

import jdbc.doc.data.Column;
import jdbc.doc.data.Reference;

import java.util.Objects;

import static java.lang.String.format;

public class ColumnFormatter {

    public static String formatColumn(Column column) {
        String columnName = formatColumnName(column);
        String columnType = formatColumnType(column.getColumnType(), column.getColumnSize());
        String columnReference = formatColumnReference(column.getReference());

        return format("|%s|%s|%s|%s|%s|\n",
                columnName,
                columnType,
                column.getIsNullable().toLowerCase(),
                column.getComments().toLowerCase(),
                columnReference);
    }

    private static String formatColumnName(Column column) {
        String columnName = column.getColumnName();
        if (column.isPrimaryKey()) {
            columnName = "**_" + columnName + "_**";
        }
        return columnName.toLowerCase();
    }

    private static String formatColumnType(String columnType, String columnSize) {
        String result;
        if (Objects.equals(columnType, "number")) {
            columnSize = Objects.equals(columnSize, "0") ? "*" : columnSize;
            result = columnType + "(" + columnSize + ")";
        } else if (Objects.equals(columnType, "varchar2")) {
            result = columnType + "(" + columnSize + ")";
        } else {
            result = columnType;
        }
        return result.toLowerCase();
    }

    private static String formatColumnReference(Reference reference) {
        String result = "";
        if (reference != null) {
            result = "[" + reference.getReferencedTable().toLowerCase() + " (" + reference.getReferencedColumn().toLowerCase()
                    + ")](#" + reference.getReferencedTable() + ")";
        }
        return result;
    }

}

package jdbc.doc.data.util;

import jdbc.doc.data.Column;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ColumnUpdater {

    public static void findColumnByNameAndUpdate(String columnName, List<Column> columnList, Consumer<Column> update) {
        Optional<Column> primaryColumn = columnList.stream()
                .filter(column -> Objects.equals(columnName, column.getColumnName()))
                .findFirst();
        primaryColumn.ifPresent(update);
    }
}

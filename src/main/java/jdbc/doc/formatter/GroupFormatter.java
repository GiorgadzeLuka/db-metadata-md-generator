package jdbc.doc.formatter;

import jdbc.doc.data.Group;
import jdbc.doc.data.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

public class GroupFormatter {

    public static String formatGroup(Group group) {
        StringBuilder sb = new StringBuilder();
        sb.append(format("#### %s \n", group.getGroupName()));
        sb.append("|Name|Columns #|Indices #|\n");
        sb.append("|---|:---:|:---:|\n");
        List<Table> tables = new ArrayList<>(group.getTables());
        tables.sort(Comparator.comparing(Table::getTableName));
        for (Table table : tables) {
            sb.append(format("|%s|%s|%s\n",
                    "[" + table.getTableName().toLowerCase() + "](#" + table.getTableName() + ")",
                    table.getColumns().size(),
                    table.getIndices().size()));
        }
        return sb.toString();
    }
}

package jdbc.report;

import jdbc.data.Column;
import jdbc.data.Table;
import jdbc.report.analyzer.TableAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

@Component
public class MdGenerator {
    private TableAnalyzer tableAnalyzer;

    @Autowired
    public MdGenerator(TableAnalyzer tableAnalyzer) {
        this.tableAnalyzer = tableAnalyzer;
    }

    public void generateMdFile() throws SQLException, IOException {
        List<Table> tableData = tableAnalyzer.getTableData();

        StringBuilder sb = new StringBuilder();
        sb.append("## Database objects\n");

        for (Table table : tableData) {
            generateTable(table, sb);
        }

        File file = new File("readme.md");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(sb.toString());
        }
    }

    private void generateTable(Table table, StringBuilder sb) {
        sb.append(format("## <a name=\"%s\"></a> %s  \n", table.getTableName(), table.getTableName()));
        printColumns(table, sb);
        sb.append("***\n");
    }

    private void printColumns(Table table, StringBuilder sb) {
        sb.append(format("#### <a name=\"%s_columns\"></a> \"%s\" columns \n", table.getTableName(), table.getTableName()));
        sb.append("|Column name|Type|Size|Mandatory|\n");
        sb.append("|---|---|---|---|\n");
        for (Column column : table.getColumns()) {
            String text = format("|%s|%s|%s|%s|\n",
                    column.getColumnName(),
                    column.getColumnType(),
                    column.getColumnSize(),
                    column.getIsNullable());
            sb.append(text);
        }
    }
}

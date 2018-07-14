package jdbc.doc.generator;

import jdbc.doc.analyzer.GroupAnalyzer;
import jdbc.doc.analyzer.TableAnalyzer;
import jdbc.doc.data.Column;
import jdbc.doc.data.Group;
import jdbc.doc.data.Index;
import jdbc.doc.data.Table;
import jdbc.doc.formatter.GroupFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static jdbc.doc.formatter.ColumnFormatter.formatColumn;
import static jdbc.doc.formatter.IndexFormatter.formatIndex;

@Component
public class MdGenerator {
    private TableAnalyzer tableAnalyzer;
    private GroupAnalyzer groupAnalyzer;

    @Autowired
    public MdGenerator(TableAnalyzer tableAnalyzer, GroupAnalyzer groupAnalyzer) {
        this.tableAnalyzer = tableAnalyzer;
        this.groupAnalyzer = groupAnalyzer;
    }

    public void generateMdFile() throws IOException {
        List<Table> tableData = tableAnalyzer.getTableData();
        if (!tableData.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("## Database information\n");
            generateGroups(sb);
            generateTables(sb, tableData);
            generateReadMe(sb);
        }
    }

    public void generateMdFileConcurrently() throws IOException {
        List<Table> tableData = tableAnalyzer.getTableDataConcurrently();
        if (!tableData.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("## Database information\n");
            generateGroups(sb);
            generateTables(sb, tableData);
            generateReadMe(sb);
        }
    }

    private void generateGroups(StringBuilder sb) {
        for (Group group : groupAnalyzer.getGroups()) {
            sb.append(GroupFormatter.formatGroup(group));
        }
    }

    private void generateTables(StringBuilder sb, List<Table> tableData) {
        sb.append("### All tables\n");
        for (Table table : tableData) {
            generateTable(table, sb);
        }
    }

    private void generateTable(Table table, StringBuilder sb) {
        sb.append(format("#### <a name=\"%s\"></a> %s  \n", table.getTableName(), table.getTableName() + " (primary key: " + table.getPrimaryKeyName() + ")"));
        printColumns(table, sb);
        printIndices(table, sb);
        sb.append("***\n");
    }

    private void printColumns(Table table, StringBuilder sb) {
        sb.append(format("##### <a name=\"%s_columns\"></a> Columns:\n", table.getTableName()));
        sb.append("|Name|Type|Mandatory|Comments|Reference|\n");
        sb.append("|---|:---:|:---:|---|---|\n");
        for (Column column : table.getColumns()) {
            sb.append(formatColumn(column));
        }
    }

    private void printIndices(Table table, StringBuilder sb) {
        sb.append(format("##### <a name=\"%s_indexes\"></a> Indexes: \n", table.getTableName()));
        sb.append("|Index name|Affected columns|Order(Asc or Desc)|\n");
        sb.append("|---|:---:|:---:|\n");
        for (Index index : table.getIndices()) {
            String text = formatIndex(index);
            sb.append(text);
        }
    }

    private void generateReadMe(StringBuilder sb) throws IOException {
        File file = new File("readme.md");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(sb.toString());
        }
    }
}

package jdbc.report;

import jdbc.data.Table;
import jdbc.report.analyzer.TableAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class DbTablesAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(DbTablesAnalyzer.class);
    private TableAnalyzer tableAnalyzer;

    @Autowired
    public DbTablesAnalyzer(TableAnalyzer tableAnalyzer) {
        this.tableAnalyzer = tableAnalyzer;
    }

    public List<Table> generateDbTablesStructure() throws SQLException {
        long startTime = System.nanoTime();

        List<Table> tables = tableAnalyzer.getTableData();

        long endTime = System.nanoTime();
        long durationInMilliSeconds = (endTime - startTime) / 1000000;
        System.out.println("done in: " + durationInMilliSeconds);

        return tables;
    }

}

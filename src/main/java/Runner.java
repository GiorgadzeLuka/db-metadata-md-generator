import jdbc.AppConfig;
import jdbc.report.MdGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;
import java.sql.SQLException;

public class Runner {

    public static void main(String[] args) throws SQLException, IOException {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MdGenerator mdGenerator = context.getBean(MdGenerator.class);
        mdGenerator.generateMdFile();
    }

}

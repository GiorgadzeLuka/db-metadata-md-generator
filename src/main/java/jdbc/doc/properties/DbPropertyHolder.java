package jdbc.doc.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbPropertyHolder {
    @Value("${url:jdbc:oracle:thin:@//epkzkarw0570.moscow.epam.com:1521/orcl.moscow.epam.com}")
    private String url;
    @Value("${login:CDL}")
    private String login;
    @Value("${password:pwd}")
    private String password;
    @Value("${driverName:oracle.jdbc.driver.OracleDriver}")
    private String driverName;
    @Value("${schemaName:CDL}")
    private String schemaName;
    @Value("${poolSize:10}")
    private String poolSize;

    public String url() {
        return url;
    }

    public String login() {
        return login;
    }

    public String password() {
        return password;
    }

    public String driverName() {
        return driverName;
    }

    public String schemaName() {
        return schemaName;
    }

    public int poolSize() {
        return Integer.valueOf(poolSize);
    }
}

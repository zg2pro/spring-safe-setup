package com.github.zg2pro.spring.safe.setup.db;

import com.github.zg2pro.spring.safe.setup.utc.UtcVerifier;
import com.github.zg2pro.spring.safe.setup.utf8.Utf8Verifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Spring boot server (tomcat embedded) runner
 *
 * @author zg2pro
 */
@RunWith(MockitoJUnitRunner.class)
@EnableAutoConfiguration
@Configuration
class ApplicationBoot {

}

/**
 *
 * @author zg2pro
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ApplicationBoot.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostgresTest {

    private String postgresDbUser = "postgres";
    private String postgresDbPwd = "";
    private String postgresHost = "localhost";
    private int postgresPort = 5432;
    private String postgresDbName = "travis_ci_test";

    @Test
    public void testCheck() throws ClassNotFoundException, SQLException {
        System.out.println("######################");
        System.out.println(System.getProperty("TRAVIS"));
        System.out.println("######################");
        if ("true".equals(System.getProperty("TRAVIS"))) {
            System.out.println("WE ARE TESTING THE DB RELATED FEATURES");
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + postgresHost + ":" + postgresPort
                    + "/" + postgresDbName, postgresDbUser, postgresDbPwd);
            Utf8Verifier.checkDbEncoding(connection, Utf8Verifier.AvailableRdbms.POSTGRESQL);
            UtcVerifier.checkDbTimeZone(connection, UtcVerifier.AvailableRdbms.POSTGRESQL);
            connection.close();
        } 
        // hard to test locally... testing in continuous integration will be considered enough
    }

}

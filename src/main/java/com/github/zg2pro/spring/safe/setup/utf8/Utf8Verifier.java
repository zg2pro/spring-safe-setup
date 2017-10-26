/*
 * The MIT License
 *
 * Copyright 2017 zg2pro.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.zg2pro.spring.safe.setup.utf8;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @since 0.2
 *
 * To make sure your host is configured in UTF8, as well as your jvm
 * @author zg2pro
 *
 * Most important, don't forget to add the necessary to your web.xml
 *
 * AND also set your encoding at container level most often by adding a utf8 arg
 * to your deploy command
 */
public class Utf8Verifier {

    /*
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter </filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>

    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <servlet-name>webapp</servlet-name>
    </filter-mapping>

    <jsp-config>
        <jsp-property-group>
            <url-pattern>2 stars one slash one star</url-pattern>
            <page-encoding>UTF-8</page-encoding>
        </jsp-property-group>
    </jsp-config>
     */
    private static final Logger logger = LoggerFactory.getLogger(Utf8Verifier.class);

    static {
        checkHostEncoding();
    }

    /**
     * checks timezone on the operating system
     */
    public static void checkHostEncoding() {
        System.setProperty("client.override.encoding", StandardCharsets.UTF_8.name());
    }

    public static enum AvailableRdbms {
        ORACLE("SELECT * FROM NLS_DATABASE_PARAMETERS", "NLS_CHARACTERSET", "UTF8"),
        POSTGRESQL("SHOW SERVER_ENCODING", "SERVER_ENCODING", "UTF8");

        private String query;
        private String colname;
        private String expectedResult;

        private AvailableRdbms(String query, String colname, String expectedResult) {
            this.query = query;
            this.colname = colname;
            this.expectedResult = expectedResult;
        }

    }

    /**
     * checks encoding in database don't forget to close the connection when no
     * further needed
     *
     * @param connection
     * @param rdbms
     * @throws SQLException
     */
    public static void checkDbEncoding(Connection connection, AvailableRdbms rdbms) throws SQLException {
        Statement st = null;
        ResultSet res = null;
        String result = null;
        try {
            try {
                st = connection.createStatement();
                res = st.executeQuery(rdbms.query);
                res.next();
                result = res.getString(rdbms.colname).trim();
            } finally {
                if (res != null) {
                    res.close();
                }
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
        logger.info("encoding set for {} : {}", connection.getSchema(), result);
        if (!rdbms.expectedResult.equals(result)) {
            throw new IllegalStateException(connection.getSchema() + " database is not set to UTF8");
        }
    }

}

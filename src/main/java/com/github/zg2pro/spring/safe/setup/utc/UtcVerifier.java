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
package com.github.zg2pro.spring.safe.setup.utc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @since 0.2
 *
 * To make sure your host is configured in UTC timezone, create a spring bean in
 * spring config as follows:
 *
 * @Bean Boolean checkHostTimezone(){ UtcVerifier.checkHostTimezone(); return
 * Boolean.TRUE; }
 *
 * @author zg2pro
 */
public class UtcVerifier {

    private static final Logger logger = LoggerFactory.getLogger(UtcVerifier.class);

    static {
        checkHostTimezone();
    }

    /**
     * checks timezone on the operating system
     */
    public static void checkHostTimezone() {
        if (!TimeZone.getTimeZone("UTC").getID().equals(TimeZone.getDefault().getID())) {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        }
    }

    public static enum AvailableRdbms {
        ORACLE("select DBTIMEZONE from DUAL", "DBTIMEZONE", "+00:00"),
        MYSQL("SELECT @@session.time_zone, @@global.time_zone", "@@session.time_zone", "+00:00"),
        POSTGRESQL("show timezone", "timezone", "UTC");

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
     * checks timezone in database
     * don't forget to close the connection when no further needed
     *
     * @param connection
     * @param rdbms
     * @throws SQLException
     */
    public static void checkDbTimeZone(Connection connection, AvailableRdbms rdbms) throws SQLException {
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
        logger.info("timezone set for {} : {}", connection.getSchema(), result);
        if (!rdbms.expectedResult.equals(result)) {
            throw new IllegalStateException(connection.getSchema() + " database is not set to UTC timezone");
        }
    }
    
}

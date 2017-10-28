package com.github.zg2pro.spring.safe.setup.fs;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
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
public class FsReadyTest {

    @Test
    public void testCheck() {
        try {
            FsReady.checkFileSystemIsReady(0, new File(System.getProperty("java.io.tmpdir")));
        } catch (FileSystemException ex) {
            fail("this much space should be available", ex);
        }
        try {
            FsReady.checkFileSystemIsReady(Long.MAX_VALUE, new File(System.getProperty("java.io.tmpdir")));
            fail("this much space should NOT be available");
        } catch (FileSystemException ex) {
            assertThat(ex.getMessage()).contains("short disk space");
        }
        if ("true".equals(System.getProperty("TRAVIS"))) {
            try {
                FsReady.checkFileSystemIsReady(0, new File("/root"));
            } catch (FileSystemException ex) {
                assertThat(ex.getMessage()).contains("this path is locked for");
            }
        } else {
            try {
                FsReady.checkFileSystemIsReady(0, new File("/c/Users/Administrateur"));
                fail("this path should not be readable if no sudo");
            } catch (FileSystemException ex) {
                assertThat(ex.getMessage()).contains("this path is locked for");
            }
        }
    }

}

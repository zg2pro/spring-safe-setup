package com.github.zg2pro.spring.safe.setup.security;

import com.github.zg2pro.spring.safe.setup.security.test.PermissionCheckAnnotation;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
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
class ApplicationOneBoot {

    @Bean
    Boolean checkSecurity() {
        PreAuthorizeAllRemoteStrategy paars = new PreAuthorizeAllRemoteStrategy(PermissionCheckAnnotation.class,
                PermissionCheckAnnotation.class.getPackage().getName() + ".one");
        try {
            paars.processVerification();
            return Boolean.TRUE;
        } catch (SecurityException se) {
            return Boolean.FALSE;
        }
    }

}

/**
 *
 * unit tests about spring security config with annotations and PreAuthorize
 *
 * @author zg2pro
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ApplicationOneBoot.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OneTest {

    @Autowired
    private Boolean checkSecurity;

    @Test
    public void testCheck() {
        assertThat(checkSecurity).isEqualTo(Boolean.FALSE);
    }

}

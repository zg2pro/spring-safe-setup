package com.github.zg2pro.spring.safe.setup.security.test.three;

import com.github.zg2pro.spring.safe.setup.security.test.PermissionCheckAnnotation;
import com.github.zg2pro.spring.safe.setup.security.test.PermissionEnum;

/**
 *
 * @author zg2pro
 */
public interface ThreeServiceRemote {

    @PermissionCheckAnnotation(PermissionEnum.CAN_DO_THREE)
    boolean exampleMethod(String dummyArg);

}

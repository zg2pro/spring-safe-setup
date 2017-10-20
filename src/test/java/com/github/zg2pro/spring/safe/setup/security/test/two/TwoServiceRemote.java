package com.github.zg2pro.spring.safe.setup.security.test.two;

import com.github.zg2pro.spring.safe.setup.security.test.PermissionCheckAnnotation;
import com.github.zg2pro.spring.safe.setup.security.test.PermissionEnum;

/**
 *
 * @author zg2pro
 */
public interface TwoServiceRemote {

    @PermissionCheckAnnotation(PermissionEnum.CAN_DO_TWO)
    boolean exampleMethod(String dummyArg);

}

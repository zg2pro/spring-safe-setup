package com.github.zg2pro.spring.safe.setup.security.test.two;

import org.springframework.stereotype.Service;

/**
 *
 * @author zg2pro
 */
@Service
public class TwoServiceImpl implements TwoServiceRemote, TwoServiceLocal {

    @Override
    public boolean exampleMethod(String dummyArg) {
        return true;
    }

    @Override
    public boolean exampleLocalMethod(int dummyArg) {
        return true;
    }
    
}

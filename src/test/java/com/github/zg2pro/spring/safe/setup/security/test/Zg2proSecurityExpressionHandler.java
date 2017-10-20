package com.github.zg2pro.spring.safe.setup.security.test;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * This class is given as an example, yet is not used in the current library
 * 
 * binds the expression root and permission evaluator to the defaultmethodSecurityHandler bean
 */
public class Zg2proSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    /**
     * default constructor
     */
    public Zg2proSecurityExpressionHandler() {
        super();
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        Zg2proSecurityExpressionRoot root = new Zg2proSecurityExpressionRoot(authentication, invocation);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        return root;
    }
}

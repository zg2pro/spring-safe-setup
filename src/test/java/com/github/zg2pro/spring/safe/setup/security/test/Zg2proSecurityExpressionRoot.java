package com.github.zg2pro.spring.safe.setup.security.test;

import com.github.zg2pro.spring.safe.setup.security.PreAuthorizeAllRemoteStrategy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

/**
 * This class is given as an example, yet is not used in the current library
 * 
 * lists all the methods available in the PreAuthorize annotation
 */
public class Zg2proSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private static final Logger logger = LoggerFactory.getLogger(Zg2proSecurityExpressionRoot.class);

    private Object filterObject;
    private Object returnObject;
    private Object target;

    private MethodInvocation request;

    /**
     * parent constructor
     *
     * @param a
     */
    public Zg2proSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    /**
     * constructor enabling the holding of the method invocation object
     *
     * @param a
     * @param fi
     */
    public Zg2proSecurityExpressionRoot(Authentication a, MethodInvocation fi) {
        super(a);
        this.request = fi;
    }

    private boolean checkPermissionAnnotation(PermissionEnum... perms) {
        for (PermissionEnum keyElement : perms) {
            for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
                if (keyElement.name().equals(ga.getAuthority())) {
                    return true;
                }
            }
        }
        logger.warn("a method invokation was rejected because the user did not have the right:" + StringUtils.arrayToDelimitedString(perms, " OR "));
        return false;
    }

    /**
     * basic autorization verifier of Nest
     *
     * @return
     */
    public boolean checkPermissionAnnotation() {
        Method meth = request.getMethod();
        Method mRemote = null;
        Class clazz = meth.getDeclaringClass();
        if (clazz.getSimpleName().endsWith("Local")) {
            return true;
        } else if (!clazz.getSimpleName().endsWith("Remote")) {
            mRemote = PreAuthorizeAllRemoteStrategy.searchRemote(clazz, meth);
        }
        if (mRemote == null) {
            mRemote = meth;
        }
        for (Annotation anno : mRemote.getAnnotations()) {
            if (PermissionCheckAnnotation.class.equals(anno.annotationType())) {
                return checkPermissionAnnotation(((PermissionCheckAnnotation) anno).value());
            }
        }
        //if no annotation on a Remote interface, the autorization shall not be granted
        return false;
    }

   

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }
}

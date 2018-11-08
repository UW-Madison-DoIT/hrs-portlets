/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.wisc.hrs.dao;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

/**
 * Utilities for parsing the PeopleSoft data model
 * 
 * @author Eric Dalquist
 */
public class HrsUtils {
    /**
     * Utility for calling HRS schema generated wrapper objects that simply have a getValue() method.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object obj) {
        return (T)callMethodSafe(obj, "getValue");
    }
    
    /**
     * Utility for creating HRS schema generated wrapper objects that simply have a setValue method 
     */
    public static <T> T createValue(Class<T> clazz, Object value) {
        final T instance;
        try {
            instance = clazz.newInstance();
        }
        catch (InstantiationException e) {
            ReflectionUtils.handleReflectionException(e);
            return null; //This will never run, just used to make compiler's final check happy
        }
        catch (IllegalAccessException e) {
            ReflectionUtils.handleReflectionException(e);
            return null; //This will never run, just used to make compiler's final check happy
        }
        
        callMethodSafe(instance, "setValue", value);
        return instance;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> T callMethodSafe(Object obj, String method, Object... args) {
        if (obj == null) {
            return null;
        }
        
        final Class<? extends Object> cls = obj.getClass();
        
        if (args == null) {
            final Method valueMethod = ReflectionUtils.findMethod(cls, method);
            return (T)ReflectionUtils.invokeMethod(valueMethod, obj);
        }
        
        final Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];
            if (arg == null) {
                types[i] = Object.class;
            }
            else {
                types[i] = arg.getClass();
            }
        }
        
        final Method valueMethod = ReflectionUtils.findMethod(cls, method, types);
        return (T)ReflectionUtils.invokeMethod(valueMethod, obj, args);
    }
}

package com.k2.Adapter.annotation;

/**
 * The @Adapts annotation is used to identify classes that are adapters to be marshaled by the AdapterFactory
 *
 * Classes annotated with the Adapts annotation must have a public zero arg constructor and implement the 
 * Adapter interface
 */
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Adapts {

}

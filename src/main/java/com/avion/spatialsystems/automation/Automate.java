package com.avion.spatialsystems.automation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Automate { Class tile() default NoTile.class; String name() default "ERROR"; Class type() default Infer.class; }

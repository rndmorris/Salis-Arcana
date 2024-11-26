package dev.rndmorris.tfixins.common.commands.arguments.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NamedArg {

    String name();

    Class<? extends IArgumentHandler> handler();

    String[] excludes() default "";

    String descLangKey() default "";
}

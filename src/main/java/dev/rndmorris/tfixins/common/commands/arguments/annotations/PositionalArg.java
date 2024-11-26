package dev.rndmorris.tfixins.common.commands.arguments.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PositionalArg {

    int index();

    Class<? extends IArgumentHandler> handler();

    String descLangKey() default "";
}

package dev.rndmorris.salisarcana.common.commands.arguments.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.IFlagArgumentHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlagArg {

    String name();

    Class<? extends IFlagArgumentHandler> handler() default FlagHandler.class;

    String[] excludes() default "";

    String descLangKey() default "";
}

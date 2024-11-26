package dev.rndmorris.tfixins.common.commands.arguments.annotations;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.flag.IFlagArgumentHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlagArg {

    String name();

    Class<? extends IFlagArgumentHandler> handler() default FlagHandler.class;

    String[] excludes() default "";

    String descLangKey() default "";
}

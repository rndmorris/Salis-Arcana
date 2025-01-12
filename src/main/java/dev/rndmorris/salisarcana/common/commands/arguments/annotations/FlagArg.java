package dev.rndmorris.salisarcana.common.commands.arguments.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.IFlagArgumentHandler;

/**
 * A flag (e.g. {@code --flag}) argument. Only valid on {@code boolean} fields.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlagArg {

    /**
     * The name of the flag (e.g. {@code --flag}).
     */
    String name();

    /**
     * The handler of the flag. Should pretty much only ever be {@link FlagHandler}.
     */
    Class<? extends IFlagArgumentHandler> handler() default FlagHandler.class;

    /**
     * The name of any other named or flag arguments that cannot be used alongside this one.
     */
    String[] excludes() default "";

    /**
     * The subname used to get this argument's description from the lang file. Called by
     * {@link dev.rndmorris.salisarcana.common.commands.HelpCommand}.
     * The langkey in question should have the structure
     * {@code salisarrcana:command.COMMAND_NAME.args.DESC_LANG_KEY=§7[--ARG_NAME]§r Description text here.}
     */
    String descLangKey() default "";
}

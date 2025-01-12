package dev.rndmorris.salisarcana.common.commands.arguments.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;

/**
 * A positional argument that is only available at a given handler index.
 * (Pretty much only used with index 0, for mandatory coordinates).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PositionalArg {

    /**
     * The handler index at which this argument can be read. The handler index increases by {@code 1}
     * after each handler is processed, even if a handler pulls multiple values from the args iterator.
     */
    int index();

    /**
     * The handler used to populate the field annotated with this {@link PositionalArg}.
     */
    Class<? extends IPositionalArgumentHandler> handler();

    /**
     * The subname used to get this argument's description from the lang file. Called by
     * {@link dev.rndmorris.salisarcana.common.commands.HelpCommand}.
     * The langkey in question should have the structure
     * {@code salisarrcana:command.COMMAND_NAME.args.DESC_LANG_KEY=§7<VAL> or [<VAL> <VAL> <VAL>]§r Description text here.}
     */
    String descLangKey() default "";
}

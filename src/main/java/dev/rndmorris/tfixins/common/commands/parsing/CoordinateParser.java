package dev.rndmorris.tfixins.common.commands.parsing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class CoordinateParser implements IArgType {

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        final var player = CommandBase.getCommandSenderAsPlayer(sender);
        return (int) CommandBase.func_110666_a(sender, getPosition(player), current);
    }

    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        return args.hasNext()
            ? null
            : Collections.singletonList("~");
    }

    protected abstract double getPosition(EntityPlayerMP player);

    public static class X extends CoordinateParser {

        public static final IArgType INSTANCE = new X();

        @Override
        protected double getPosition(EntityPlayerMP player) {
            return player.posX;
        }
    }

    public static class Y extends CoordinateParser {

        public static final IArgType INSTANCE = new Y();

        @Override
        protected double getPosition(EntityPlayerMP player) {
            return player.posY;
        }
    }

    public static class Z extends CoordinateParser {

        public static final IArgType INSTANCE = new Z();

        @Override
        protected double getPosition(EntityPlayerMP player) {
            return player.posZ;
        }
    }
}

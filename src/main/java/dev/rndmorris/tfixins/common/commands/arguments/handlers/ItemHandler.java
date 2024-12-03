package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;

public class ItemHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final ItemHandler INSTANCE = new ItemHandler();

    private final IntHandler metadataHandler = new IntHandler(0, Integer.MAX_VALUE, 0);

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        final var item = CommandBase.getItemByText(sender, current);
        Integer damage = null;
        if (args.hasNext()) {
            damage = (Integer) metadataHandler.parse(sender, args.next(), args);
        }
        if (damage == null) {
            damage = metadataHandler.getSuggestedValue();
        }

        return new ItemStack(item, damage);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (!args.hasNext()) {
            //noinspection unchecked
            return new ArrayList<String>(Item.itemRegistry.getKeys());
        }
        args.next();
        if (!args.hasNext()) {
            return metadataHandler.getAutocompleteOptions(sender, current, args);
        }
        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return ItemStack.class;
    }
}

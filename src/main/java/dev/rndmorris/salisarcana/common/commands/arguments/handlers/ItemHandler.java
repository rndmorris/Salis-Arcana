package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;

public class ItemHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final ItemHandler INSTANCE = new ItemHandler();

    private final List<String> itemKeys;
    private final IntHandler metadataHandler = new IntHandler(0, Integer.MAX_VALUE, 0);

    public ItemHandler() {
        this(null);
    }

    public ItemHandler(Collection<String> itemKeys) {
        if (itemKeys != null) {
            this.itemKeys = new ArrayList<>(itemKeys);
        } else {
            this.itemKeys = null;
        }
    }

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var item = CommandBase.getItemByText(sender, args.next());
        Integer damage = null;
        if (args.hasNext()) {
            damage = (Integer) metadataHandler.parse(sender, args);
        }
        if (damage == null) {
            damage = metadataHandler.getSuggestedValue();
        }

        return new ItemStack(item, 0, damage);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        args.next();
        if (!args.hasNext()) {
            if (itemKeys != null) {
                return itemKeys;
            }
            // noinspection unchecked
            return new ArrayList<String>(Item.itemRegistry.getKeys());
        }
        return metadataHandler.getAutocompleteOptions(sender, args);
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return ItemStack.class;
    }
}

package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import static dev.rndmorris.salisarcana.lib.WandFocusHelper.getAppliedUpgrades;
import static dev.rndmorris.salisarcana.lib.WandFocusHelper.getFocusFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import dev.rndmorris.salisarcana.lib.IntegerHelper;
import dev.rndmorris.salisarcana.lib.KnowItAll;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;

public class FocusUpgradesHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final FocusUpgradesHandler INSTANCE = new FocusUpgradesHandler();

    private final Map<Short, String> upgradeKeyCache = new TreeMap<>();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var resultList = new ArrayList<FocusUpgradeType>(5);
        String peeked = null;
        while (args.hasNext() && (peeked = args.peek()) != null && !peeked.startsWith("-")) {
            final var key = args.next();
            final var upgrade = getUpgradeFromKey(key);
            if (upgrade == null) {
                throw new CommandException("salisarcana:error.invalid_focus_upgrade", key);
            }
            resultList.add(upgrade);
        }

        return resultList;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        final var player = CommandBase.getCommandSenderAsPlayer(sender);
        ItemStack heldItem;
        ItemFocusBasic heldFocus;
        if ((heldItem = player.getCurrentEquippedItem()) == null || (heldFocus = getFocusFrom(heldItem)) == null) {
            return Arrays.stream(FocusUpgradeType.types)
                .map(this::formatUpgradeKey)
                .collect(Collectors.toList());
        }

        // copied, so we can modify it as we work
        final var workingItem = heldItem.copy();
        final var appliedUpgrades = getAppliedUpgrades(heldFocus, workingItem);

        if (appliedUpgrades.size() >= 5) {
            return null;
        }

        String peeked = null;
        while (appliedUpgrades.size() < 5 && args.hasNext()
            && (peeked = args.peek()) != null
            && !peeked.startsWith("-")) {
            final var key = args.next();
            final var upgrade = getUpgradeFromKey(key);
            if (upgrade != null) {
                appliedUpgrades.add(upgrade);
                heldFocus.applyUpgrade(workingItem, upgrade, appliedUpgrades.size());
            }
        }

        if (peeked != null && peeked.startsWith("-")) {
            return null;
        }

        final var possibleUpgrades = heldFocus.getPossibleUpgradesByRank(heldItem, appliedUpgrades.size() + 1);
        if (possibleUpgrades == null) {
            return null;
        }

        final var result = new ArrayList<String>(possibleUpgrades.length);
        for (var upgrade : possibleUpgrades) {
            if (heldFocus.canApplyUpgrade(workingItem, KnowItAll.getInstance(), upgrade, appliedUpgrades.size() + 1)) {
                result.add(formatUpgradeKey(upgrade));
            }
        }

        return result;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return List.class;
    }

    private @Nonnull String formatUpgradeKey(@Nonnull FocusUpgradeType focusUpgrade) {
        return upgradeKeyCache.computeIfAbsent(focusUpgrade.id, (id) -> {
            final var cleanedName = focusUpgrade.getLocalizedName()
                .toLowerCase()
                .replaceAll(" +", "-")
                .replaceAll("[^\\w-]", "");
            return String.format("%s-%d", cleanedName, focusUpgrade.id);
        });
    }

    private @Nullable FocusUpgradeType getUpgradeFromKey(@Nonnull String key) {
        final var keyParts = key.split("-");
        if (keyParts.length < 1) {
            return null;
        }
        final var upgradeId = IntegerHelper.tryParse(keyParts[keyParts.length - 1]);
        if (upgradeId == null) {
            return null;
        }
        return ArrayHelper.tryGet(FocusUpgradeType.types, upgradeId)
            .data();
    }

}

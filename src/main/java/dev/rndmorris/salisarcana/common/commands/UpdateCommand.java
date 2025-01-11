package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import dev.rndmorris.salisarcana.updater.UpdateScreen;
import dev.rndmorris.salisarcana.updater.Updater;

public class UpdateCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "salis-arcana-update";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "salis-arcana-update <version>";
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new ArrayList<>(Updater.versions.keySet());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 2) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        String version = args[0];
        if (args.length == 2 && args[1].equals("--ask")) {
            UpdateScreen screen = new UpdateScreen((result, id) -> {
                if (result) {
                    update(sender, version);
                }
            }, "Update Salis Arcana", "Are you sure you want to update Salis Arcana to version " + args[0] + "?", 0);
            Minecraft.getMinecraft()
                .displayGuiScreen(screen);
        } else {
            update(sender, version);
        }

    }

    private void update(ICommandSender sender, String version) {
        if (!Updater.versions.containsKey(version)) {
            throw new IllegalArgumentException("Invalid version");
        }
        boolean success = Updater.downloadNewVersion(version);
        if (!success) {
            sender.addChatMessage(
                new ChatComponentText("Automatic update failed. You can download the new version manually from here:"));
            IChatComponent message = new ChatComponentText(Updater.getUrl(version));
            message.getChatStyle()
                .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Updater.getUrl(version)));
            return;
        }
        sender.addChatMessage(new ChatComponentText("Update successful. Restart your game to apply changes."));
    }
}

package dev.rndmorris.salisarcana.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.ChatComponentTranslation;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.Setting;
import io.netty.buffer.ByteBuf;

public class MessageLogin implements IMessage, IMessageHandler<MessageLogin, IMessage> {

    public static boolean connectionFailed = false;
    // map of all settings in a category in a group that don't match the server
    public static HashMap<ConfigGroup, HashMap<String, List<Setting>>> settingGroups = new HashMap<>();

    @Override
    public void fromBytes(ByteBuf buf) {
        settingGroups.clear();
        String settingName;
        while (buf.readableBytes() > 0) {
            settingName = ByteBufUtils.readUTF8String(buf);
            boolean isEnabled = buf.readBoolean();
            String[] parts = settingName.split(":", 3);
            if (parts.length != 3) {
                continue; // Invalid format, skip
            }
            Setting setting = SalisConfig.getSettingByName(parts[0], parts[1], parts[2]);
            if (!(setting == null || (setting.isEnabled() == isEnabled))) {
                if (!settingGroups.containsKey(setting.getConfigGroup())) {
                    settingGroups.put(setting.getConfigGroup(), new HashMap<>());
                }
                HashMap<String, List<Setting>> categorySettings = settingGroups.get(setting.getConfigGroup());
                if (!categorySettings.containsKey(setting.getCategory())) {
                    categorySettings.put(setting.getCategory(), new ArrayList<>());
                }
                categorySettings.get(setting.getCategory())
                    .add(setting);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (ConfigGroup group : SalisConfig.getConfigGroups()) {
            for (Setting setting : group.getRequiredMatchedSettings()) {
                ByteBufUtils.writeUTF8String(buf, setting.toString());
                buf.writeBoolean(setting.isEnabled());
            }
        }
    }

    @Override
    public IMessage onMessage(MessageLogin message, MessageContext ctx) {
        if (!settingGroups.isEmpty()) {
            NetHandlerPlayClient net = ctx.getClientHandler();
            if (net != null && net.getNetworkManager() != null) {
                net.getNetworkManager()
                    .closeChannel(new ChatComponentTranslation("salisarcana:configmismatch.1"));
                MessageLogin.connectionFailed = true;
            }
        }
        return null;
    }
}

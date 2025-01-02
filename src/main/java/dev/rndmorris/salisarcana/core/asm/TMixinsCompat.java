package dev.rndmorris.salisarcana.core.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.IEnabler;

public class TMixinsCompat implements IAsmEditor {

    private static final HashMap<String, IEnabler> compatMap = new HashMap<>();
    static {
        compatMap.put("MixinBlockCosmeticSolid", ConfigModuleRoot.bugfixes.beaconBlockFixSetting);
        compatMap.put("MixinBlockCandleRenderer", ConfigModuleRoot.bugfixes.candleRendererCrashes);
        compatMap.put("MixinBlockCandle", ConfigModuleRoot.bugfixes.candleRendererCrashes);
        compatMap.put("MixinItemShard", ConfigModuleRoot.bugfixes.itemShardColor);
        compatMap.put("MixinWandManager", ConfigModuleRoot.enhancements.useAllBaublesSlots);
        compatMap.put("MixinEventHandlerRunic", ConfigModuleRoot.enhancements.useAllBaublesSlots);
        compatMap.put("MixinWarpEvents", ConfigModuleRoot.enhancements.useAllBaublesSlots);
    }

    @Override
    public void edit(MethodNode method) {
        InsnNode node = null;
        for (int i = 0; i < method.instructions.size(); i++) {
            if (method.instructions.get(i)
                .getOpcode() == Opcodes.ARETURN) {
                node = (InsnNode) method.instructions.get(i);
                break;

            }
        }
        MethodInsnNode methodNode = new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "dev/rndmorris/salisarcana/core/asm/TMixinsCompat",
            "tmixins_compat",
            "(Ljava/util/List;)Ljava/util/List;",
            false);
        method.instructions.insertBefore(node, methodNode);
    }

    @Override
    public String getClassName() {
        return "xyz.uniblood.thaumicmixins.mixinplugin.ThaumicMixinsLateMixins";
    }

    @Override
    public String getMethodName() {
        return "getMixins";
    }

    @Override
    public String getMethodDesc() {
        return "(Ljava/util/Set;)Ljava/util/List;";
    }

    @SuppressWarnings("unused")
    public static List<String> tmixins_compat(List<String> mixins) {

        for (String mixin : new ArrayList<>(mixins)) {
            if (compatMap.containsKey(mixin)) {
                if (compatMap.get(mixin)
                    .isEnabled()) {
                    mixins.remove(mixin);
                }
            }
        }

        return mixins;
    }
}

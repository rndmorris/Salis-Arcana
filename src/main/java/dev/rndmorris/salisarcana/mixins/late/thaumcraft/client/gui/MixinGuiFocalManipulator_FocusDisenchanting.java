package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.common.DisenchantFocusUpgrade;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.gui.GuiFocalManipulator;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileFocalManipulator;

@Mixin(GuiFocalManipulator.class)
public class MixinGuiFocalManipulator_FocusDisenchanting {

    @Shadow(remap = false)
    ArrayList<FocusUpgradeType> upgrades;

    @Shadow(remap = false)
    private TileFocalManipulator table;

    @Shadow(remap = false)
    ArrayList<FocusUpgradeType> possibleUpgrades;

    @Shadow(remap = false)
    int rank;

    @Shadow(remap = false)
    int selected;

    @WrapMethod(method = "gatherInfo", remap = false)
    public void addDisenchantOption(Operation<Void> original) {
        original.call();

        if (!this.upgrades.isEmpty()
            && (this.selected == DisenchantFocusUpgrade.upgradeID || this.salisArcana$disenchantResearchComplete())) {
            ItemStack focusStack = this.table.getStackInSlot(0);
            if (focusStack != null && focusStack.getItem() instanceof ItemFocusBasic focus) {
                this.possibleUpgrades.add(DisenchantFocusUpgrade.createSpecific(focus.getAppliedUpgrades(focusStack)));
                if (this.rank == -1) {
                    this.rank = 6;
                }
            }
        }
    }

    @WrapOperation(
        method = "mouseClicked",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/research/ResearchManager;reduceToPrimals(Lthaumcraft/api/aspects/AspectList;)Lthaumcraft/api/aspects/AspectList;",
            remap = false))
    public AspectList replaceDisenchantCalculation(AspectList al, Operation<AspectList> original,
        @Local FocusUpgradeType type) {
        if (this.selected == DisenchantFocusUpgrade.upgradeID && type instanceof DisenchantFocusUpgrade upgrade) {
            return upgrade.getVisPoints();
        } else {
            return original.call(al);
        }
    }

    @ModifyArg(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/gui/GuiFocalManipulator$Sparkle;<init>(Lthaumcraft/client/gui/GuiFocalManipulator;FFFFFFF)V",
            ordinal = 1,
            remap = false),
        index = 1)
    public float moveSparkles(float x) {
        return (this.rank == 6 || this.selected == DisenchantFocusUpgrade.upgradeID) ? x - 16f : x;
    }

    @ModifyExpressionValue(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/client/gui/GuiFocalManipulator;nextSparkle:J",
            opcode = Opcodes.GETFIELD,
            remap = false))
    public long cancelFifthUpgradeSparkles(long nextSparkle) {
        return (this.rank == 6 && this.selected == -1) ? Long.MAX_VALUE : nextSparkle;
    }

    @ModifyVariable(method = "drawGuiContainerBackgroundLayer", at = @At("STORE"), name = "xx")
    public float moveSparkleEndpoint(float xx) {
        return this.selected == DisenchantFocusUpgrade.upgradeID ? xx - (16f / 9f) : xx;
    }

    @ModifyVariable(method = "drawGuiContainerBackgroundLayer", at = @At("STORE"), name = "xp")
    public int overrideXpCost(int xp) {
        return this.selected == DisenchantFocusUpgrade.upgradeID ? 0 : xp;
    }

    @ModifyExpressionValue(
        method = { "drawScreen", "drawGuiContainerBackgroundLayer", "mouseClicked" },
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;experienceLevel:I"))
    public int forceXpChecks(int original) {
        return this.selected == DisenchantFocusUpgrade.upgradeID
            ? (salisArcana$disenchantResearchComplete() ? Integer.MAX_VALUE : Integer.MIN_VALUE)
            : original;
    }

    @Unique
    private boolean salisArcana$disenchantResearchComplete() {
        final var playerName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
        return ResearchManager.isResearchComplete(playerName, DisenchantFocusUpgrade.RESEARCH_KEY);
    }
}

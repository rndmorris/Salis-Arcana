package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.MultiContainer;
import dev.rndmorris.salisarcana.lib.ifaces.IReconnectableContainer;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.container.SlotCraftingArcaneWorkbench;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@Mixin(ContainerArcaneWorkbench.class)
public abstract class MixinContainerArcaneWorkbench_MultiContainer extends Container
    implements IReconnectableContainer, IInventory {

    @Shadow(remap = false)
    private TileArcaneWorkbench tileEntity;

    @Shadow
    public abstract void onCraftMatrixChanged(IInventory par1IInventory);

    @Unique
    private final InventoryFake salisArcana$outputSlot = new InventoryFake(new ItemStack[1]);

    @Unique
    private boolean salisArcana$connected;

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileArcaneWorkbench;eventHandler:Lnet/minecraft/inventory/Container;",
            remap = false),
        remap = false)
    public void setEventHandler(TileArcaneWorkbench instance, Container value, Operation<Void> original) {
        original.call(instance, MultiContainer.mergeContainers(instance.eventHandler, value));
        this.salisArcana$connected = true;
    }

    @WrapOperation(
        method = "onContainerClosed",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileArcaneWorkbench;eventHandler:Lnet/minecraft/inventory/Container;",
            remap = false))
    public void removeEventHandler(TileArcaneWorkbench instance, Container value, Operation<Void> original) {
        original.call(instance, MultiContainer.removeContainer(instance.eventHandler, this));
        this.salisArcana$connected = false;
    }

    @ModifyExpressionValue(
        method = "onContainerClosed",
        at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
    public boolean removeEventHanderOnClientToo(boolean original) {
        return false;
    }

    @WrapOperation(
        method = "<init>",
        at = @At(value = "NEW", target = "thaumcraft/common/container/SlotCraftingArcaneWorkbench", remap = false),
        remap = false)
    public SlotCraftingArcaneWorkbench useFakeOutputSlot(EntityPlayer player, IInventory matrix, IInventory output,
        int slotNum, int xPos, int yPos, Operation<SlotCraftingArcaneWorkbench> original) {
        return original.call(player, matrix, salisArcana$outputSlot, 0, xPos, yPos);
    }

    @WrapOperation(
        method = "onCraftMatrixChanged",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/tiles/TileArcaneWorkbench;setInventorySlotContentsSoftly(ILnet/minecraft/item/ItemStack;)V",
            remap = false))
    public void setOutputSlot(TileArcaneWorkbench instance, int i, ItemStack itemStack, Operation<Void> original) {
        salisArcana$outputSlot.setInventorySlotContents(0, itemStack);
    }

    @WrapOperation(
        method = "onCraftMatrixChanged",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/tiles/TileArcaneWorkbench;getStackInSlot(I)Lnet/minecraft/item/ItemStack;",
            ordinal = 1))
    public ItemStack getOutputSlot(TileArcaneWorkbench instance, int i, Operation<ItemStack> original) {
        return salisArcana$outputSlot.getStackInSlot(0);
    }

    @Override
    public void salisArcana$reconnect() {
        if (!this.salisArcana$connected) {
            this.tileEntity.eventHandler = MultiContainer.mergeContainers(this.tileEntity.eventHandler, this);
            this.onCraftMatrixChanged(this);
            this.salisArcana$connected = true;
        }
    }
}

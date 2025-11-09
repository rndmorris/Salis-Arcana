package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container;

import net.minecraft.inventory.Container;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.MultiContainer;
import dev.rndmorris.salisarcana.lib.ifaces.IReconnectableContainer;
import thaumcraft.common.container.ContainerThaumatorium;
import thaumcraft.common.tiles.TileThaumatorium;

@Mixin(ContainerThaumatorium.class)
public abstract class MixinContainerThaumatorium_MultiContainer extends Container implements IReconnectableContainer {

    @Shadow(remap = false)
    private TileThaumatorium thaumatorium;

    @Unique
    private boolean salisArcana$connected;

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileThaumatorium;eventHandler:Lnet/minecraft/inventory/Container;",
            opcode = Opcodes.PUTFIELD,
            remap = false),
        remap = false)
    public void setEventHandler(TileThaumatorium instance, Container value, Operation<Void> original) {
        original.call(instance, MultiContainer.mergeContainers(instance.eventHandler, value));
        this.salisArcana$connected = true;
    }

    @WrapOperation(
        method = "onContainerClosed",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileThaumatorium;eventHandler:Lnet/minecraft/inventory/Container;",
            opcode = Opcodes.PUTFIELD,
            remap = false))
    public void removeEventHandler(TileThaumatorium instance, Container value, Operation<Void> original) {
        original.call(instance, MultiContainer.removeContainer(instance.eventHandler, this));
        this.salisArcana$connected = false;
    }

    @ModifyExpressionValue(
        method = "onContainerClosed",
        at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z", opcode = Opcodes.GETFIELD))
    public boolean removeClientEventHandlerOnClose(boolean initial) {
        return false;
    }

    @Override
    public void salisArcana$reconnect() {
        if (!this.salisArcana$connected) {
            this.thaumatorium.eventHandler = MultiContainer.mergeContainers(this.thaumatorium.eventHandler, this);
            this.salisArcana$connected = true;
        }
    }
}

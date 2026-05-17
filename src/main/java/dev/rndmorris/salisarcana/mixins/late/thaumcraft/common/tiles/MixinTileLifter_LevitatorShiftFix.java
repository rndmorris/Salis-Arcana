package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.tiles.TileLifter;

@Mixin(value = TileLifter.class)
public abstract class MixinTileLifter_LevitatorShiftFix extends TileEntity {

    @Shadow(remap = false)
    public int rangeAbove;

    @Inject(
        method = "updateEntity",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"),
        cancellable = true)
    private void mixinUpdateEntity(CallbackInfo ci) {
        List<Entity> targets = this.worldObj.getEntitiesWithinAABB(
            Entity.class,
            AxisAlignedBB.getBoundingBox(
                this.xCoord,
                this.yCoord + 1,
                this.zCoord,
                this.xCoord + 1,
                this.yCoord + 1 + this.rangeAbove,
                this.zCoord + 1));
        if (!targets.isEmpty()) {
            for (Entity e : targets) {
                if (e.isSneaking()) {
                    if (e.motionY < 0.0D) {
                        e.motionY *= 0.8999999761581421D;
                    }
                } else if (e.motionY < 0.3499999940395355D) {
                    e.motionY += 0.10000000149011612D;
                }
                e.fallDistance = 0.0F;
            }
        }
        ci.cancel();
    }
}

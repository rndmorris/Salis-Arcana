package dev.rndmorris.salisarcana.mixins.late.world.dim;

import java.util.ArrayList;
import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import dev.rndmorris.salisarcana.lib.Maze;
import thaumcraft.common.lib.world.dim.MazeGenerator;
import thaumcraft.common.lib.world.dim.MazeThread;

@Mixin(value = MazeThread.class, remap = false)
public class MixinMazeThread {

    @Shadow
    int w;
    @Shadow
    int h;
    @Shadow
    long seed;

    private ArrayList<Integer> data = new ArrayList<>();

    /**
     * @author Midnight145
     * @reason The current data stored in labyrinth.dat is inefficient, so we modify the stored data structure which is
     *         not compatible with the current MazeThread implementation.
     */
    @Overwrite
    public void run() {
        MazeGenerator gen;
        do {
            gen = new MazeGenerator(this.w, this.h, this.seed++);
        } while (!gen.generate());

        int top = this.w, left = this.h, bottom = -1, right = -1;
        for (int y = 0; y < this.w; ++y) {
            for (int x = 0; x < this.h; ++x) {
                if (gen.grid[x][y] > 0) {
                    data.add(gen.grid[x][y]);
                    if (y < top) top = y;
                    if (x < left) left = x;
                    if (y > bottom) bottom = y;
                    if (x > right) right = x;
                }
            }
        }
        data.addAll(0, Arrays.asList(top, left, bottom, right, w));
        new Maze(
            data.stream()
                .mapToInt(i -> i)
                .toArray());
    }
}

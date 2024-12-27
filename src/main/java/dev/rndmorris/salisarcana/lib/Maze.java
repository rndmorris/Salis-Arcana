package dev.rndmorris.salisarcana.lib;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Maze {

    private static final ConcurrentLinkedQueue<Maze> mazes = new ConcurrentLinkedQueue<>();

    public int x, z, x1, z1, w, h;
    int[][] rooms;
    int[] data;

    public Maze(int[] data) {
        this.data = data;
        this.x = data[0];
        this.z = data[1];
        this.x1 = data[2];
        this.z1 = data[3];
        this.w = data[4];
        this.h = (data.length - 6) / w;
        this.rooms = new int[w][h];
        for (int i = 1; i < data.length; i++) {
            this.rooms[i % w][i / w] = data[i];
        }
        mazes.add(this);
    }

    public short getRoom(int x, int y) {
        return (short) rooms[x][y];
    }

    private boolean __intersects(int x, int z) {
        return x >= this.x && x <= this.x1 && z >= this.z && z <= this.z1;
    }

    public static boolean intersects(int x, int z) {
        for (Maze maze : mazes) {
            if (maze.__intersects(x, z)) {
                return true;
            }
        }
        return false;
    }

    public static Maze getMazeAtPoint(int x, int z) {
        for (Maze maze : mazes) {
            if (maze.__intersects(x, z)) {
                return maze;
            }
        }
        return null;
    }

    public static NBTTagCompound writeNBT(int version) {
        NBTTagCompound tag = new NBTTagCompound();
        if (version == 2) {
            tag.setInteger("version", version);
            NBTTagList list = new NBTTagList();
            for (Maze maze : mazes) {
                NBTTagCompound mazeTag = new NBTTagCompound();
                mazeTag.setIntArray("data", maze.data);
                list.appendTag(mazeTag);
            }
        }
        return tag;
    }

    public static NBTTagCompound readNBT(NBTTagCompound tag) {
        if (tag.hasKey("version")) {
            int version = tag.getInteger("version");
            if (version == 2) {
                NBTTagList list = tag.getTagList("mazes", 10);
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound mazeTag = list.getCompoundTagAt(i);
                    new Maze(mazeTag.getIntArray("data"));
                }
            }
        }
        return tag;
    }

}

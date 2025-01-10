package dev.rndmorris.salisarcana.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

import org.apache.commons.lang3.NotImplementedException;

public class Maze {

    private static final ConcurrentLinkedQueue<Maze> mazes = new ConcurrentLinkedQueue<>();

    public int x, z, x1, z1, w, h;
    int[][] rooms;
    int[] data; // used for saving back to NBT

    public Maze(int[] data) {
        /*
         * Data Structure:
         * First four integers are the bounding box, x, z, x1, z1
         * Fifth integer is the width of the maze
         * Height is autocalculated based on the remaining data
         */
        this.data = data;
        this.x = data[0];
        this.z = data[1];
        this.x1 = data[2];
        this.z1 = data[3];
        this.w = data[4];
        this.h = (data.length - 6) / w;
        this.rooms = new int[w][h];

        for (int i = 5; i < data.length; i++) {
            this.rooms[i % w][i / w] = data[i]; // convert 1d array to 2d array
        }

        mazes.add(this);
    }

    public short getRoom(int x, int y) {
        return (short) rooms[x][y];
    }

    private boolean __intersects(int x, int z) {
        // Private helper method to check a specific maze against any given point
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
        tag.setInteger("version", version);
        if (version == 2) {
            NBTTagList list = new NBTTagList();
            for (Maze maze : mazes) {
                // foreach maze, pull stored data into the tag list
                NBTTagIntArray mazeTag = new NBTTagIntArray(maze.data);
                list.appendTag(mazeTag);
            }
            tag.setTag("data", list); // we have to do this for compat
        }
        return tag;
    }

    public static void readNBT(NBTTagCompound tag, int version) throws IOException {
        mazes.clear();
        if (version == 1) {
            throw new NotImplementedException("Porting from version 1 is not yet supported");
        } else if (version == 2) {
            File file = new File(String.format("labyrinth_v%d.dat", version));
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create labyrinth.dat file");
                }
            }
            tag = CompressedStreamTools.readCompressed(new FileInputStream(file));
            // type 11 denotes NBTTagIntArray
            NBTTagList list = tag.getTagList("mazes", 11);
            for (int i = 0; i < list.tagCount(); i++) {
                int[] mazeData = list.func_150306_c(i); // func_150306_c is getIntArray
                new Maze(mazeData);
            }
        }
    }

}

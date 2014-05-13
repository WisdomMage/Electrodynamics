package com.edxmod.electrodynamics.common.world.gen.feature;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Royalixor.
 */
public class GenLimestone implements IWorldGenerator {

    public int frequency = 5;
    public int maxDepth = 2;
    public int maxSize = 4096;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.isSurfaceWorld() && world.getWorldInfo().getTerrainType() != WorldType.FLAT) {
            generate(world, chunkX, chunkZ, random);
        }
    }

    public boolean exposed(World world, int x, int y, int z) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int xMod = x + dir.offsetX;
            int yMod = y + dir.offsetY;
            int zMod = z + dir.offsetZ;

            if (world.isAirBlock(xMod, yMod, zMod)) {
                return true;
            }
        }
        return false;
    }

    public void generate(World world, int chunkX, int chunkZ, Random random) {
        if (random.nextInt(this.frequency) != 1) {
            return;
        }

        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;
        int y;
        for (y = 96; y >= 40; y--) {
            Block block = world.getBlock(x, y, z);
            if ((block != null) && (block.isReplaceableOreGen(world, x, y, z, Blocks.stone)) && (exposed(world, x, y, z)))
                break;
        }
        if (y == 39) {
            return;
        }
        int initialMapSize = (int) Math.floor(this.maxSize * 1.4D);

        HashMap depthMap = new HashMap(initialMapSize);
        LinkedList work = new LinkedList();
        HashMap placed = new HashMap(initialMapSize);

        List t = new ArrayList(Arrays.asList(new Integer[]{new Integer(x), new Integer(y), new Integer(z)}));
        work.add(t);
        depthMap.put(t, new Integer(0));
        while ((work.size() > 0) && (placed.size() < this.maxSize)) {
            t = (List) work.remove();
            Integer d = (Integer) depthMap.get(t);

            if (d.intValue() <= this.maxDepth) {
                int x1 = ((Integer) t.get(0)).intValue();
                int y1 = ((Integer) t.get(1)).intValue();
                int z1 = ((Integer) t.get(2)).intValue();
                if ((d.intValue() > 0) && (exposed(world, x1, y1, z1))) {
                    d = Integer.valueOf(0);
                    depthMap.put(t, new Integer(0));
                }

                Block block = world.getBlock(x1, y1, z1);

                if (block != null) {
                    if (block.isReplaceableOreGen(world, x1, y1, z1, Blocks.stone)) {
                        placed.put(t, Boolean.valueOf(true));

                        Integer localInteger1 = d;
                        Integer localInteger2 = d = Integer.valueOf(d.intValue() + 1);

                        Integer x2 = (Integer) t.get(0);
                        Integer y2 = (Integer) t.get(1);
                        Integer z2 = (Integer) t.get(2);
                        Object u = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(x2.intValue() + 1), y2, z2}));
                        if (!placed.containsKey(u))
                            work.add(u);
                        if ((depthMap.get(u) == null) || (((Integer) depthMap.get(u)).intValue() > d.intValue()))
                            depthMap.put(u, d);
                        u = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(x2.intValue() - 1), y2, z2}));
                        if (!placed.containsKey(u))
                            work.add(u);
                        if ((depthMap.get(u) == null) || (((Integer) depthMap.get(u)).intValue() > d.intValue()))
                            depthMap.put(u, d);
                        u = new ArrayList(Arrays.asList(new Integer[]{x2, Integer.valueOf(y2.intValue() + 1), z2}));
                        if (!placed.containsKey(u))
                            work.add(u);
                        if ((depthMap.get(u) == null) || (((Integer) depthMap.get(u)).intValue() > d.intValue()))
                            depthMap.put(u, d);
                        u = new ArrayList(Arrays.asList(new Integer[]{x2, Integer.valueOf(y2.intValue() - 1), z2}));
                        if (!placed.containsKey(u))
                            work.add(u);
                        if ((depthMap.get(u) == null) || (((Integer) depthMap.get(u)).intValue() > d.intValue()))
                            depthMap.put(u, d);
                        u = new ArrayList(Arrays.asList(new Integer[]{x2, y2, Integer.valueOf(z2.intValue() + 1)}));
                        if (!placed.containsKey(u))
                            work.add(u);
                        if ((depthMap.get(u) == null) || (((Integer) depthMap.get(u)).intValue() > d.intValue()))
                            depthMap.put(u, d);
                        u = new ArrayList(Arrays.asList(new Integer[]{x2, y2, Integer.valueOf(z2.intValue() - 1)}));
                        if (!placed.containsKey(u))
                            work.add(u);
                        if ((depthMap.get(u) == null) || (((Integer) depthMap.get(u)).intValue() > d.intValue()))
                            depthMap.put(u, d);
                    }
                }
            }
        }
    }
}
package com.avion.spatialsystems.util;

import com.google.common.base.Optional;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class MBStruct{
    @SuppressWarnings("unchecked")
    private static final List<EnumFacing> h = Arrays.asList(EnumFacing.HORIZONTALS);
    public static final char WLD = '*';

    protected final Map<Integer, Plane> structure = new HashMap<Integer, Plane>();
    protected final Map<Character, Block> mappings = new HashMap<Character, Block>();

    public MBStruct(){ }

    public MBStruct addLayer(int relativeOffset, Plane layer){ structure.put(relativeOffset, layer); return this; }
    public MBStruct registerMapping(char layerMapping, Block mappedBlock){
        if(layerMapping==WLD) throw new RuntimeException("Can't assign wildcard mapping '"+WLD+"' to "+mappedBlock);
        mappings.put(layerMapping, mappedBlock);
        return this;
    }

    public Optional<EnumFacing[]> findStructure(World w, BlockPos centre){ return findStructure(w, centre, EnumFacing.HORIZONTALS); }

    public Optional<EnumFacing[]> findStructure(World w, BlockPos centre, EnumFacing... search){
        List<EnumFacing> l = new ArrayList<EnumFacing>();
        for(EnumFacing f : search)
            if(findStructure(w, centre, f))
                l.add(f);
        return l.size()==0?Optional.<EnumFacing[]>absent():Optional.of(l.toArray(new EnumFacing[l.size()]));
    }
    protected boolean findStructure(World w, BlockPos centre, EnumFacing dir){
        if(!h.contains(dir)) return false;
        Plane p;
        for(Integer i : structure.keySet())
            for(Pair<BlockPos, Character>[] pos : (p=structure.get(i)).getPosMap(centre, i, dir))
                for(Pair<BlockPos, Character> pos1 : pos)
                    if(pos1.getKey().equals(centre) /* Ignore controller block */ || !matches(w, pos1.getKey(), pos1.getValue()))
                        return false;
        return true;
    }

    /**
     * The assigned wildcard character '*' will always be considered a wildcard.
     * @param w World to check.
     * @param at Position to check.
     * @param mapping Character mapping to check.
     * @return Whether or not the given position matches the multi-block structure.
     */
    protected boolean matches(World w, BlockPos at, char mapping){
        if(!mappings.containsKey(mapping)) throw new RuntimeException("Unregistered character-block mapping '"+mapping+"'");
        return mapping==WLD || !mappings.containsKey(mapping) || w.getBlockState(at).getBlock().equals(mappings.get(mapping));
    }

    public static class Plane{
        List<char[]> rows = new ArrayList<char[]>();
        final int vOff, hOff;
        public Plane(int vOff, int hOff){ this.vOff = vOff; this.hOff = hOff; }
        public Plane addRow(char... row){ rows.add(row); return this; }
        public Plane addPlane(char[]... plane){ for(char[] c : plane) addRow(c); return this; }
        public Plane copy(Plane p){
            for(char[] c : p.rows)
                rows.add(Arrays.copyOf(c, c.length));
            return this;
        }
        public Pair<BlockPos, Character>[][] getPosMap(BlockPos centre, int xOff, EnumFacing direction){
            int largest = 0;
            for(char[] c : rows) if(c.length>largest) largest = c.length;
            Pair<BlockPos, Character>[][] b = new Pair[rows.size()][largest];
            for(int i = 0; i<rows.size(); ++i) {
                char[] c = rows.get(i);
                for (int j = 0; j<largest; ++j) {
                    BlockPos b1 = new BlockPos(centre.getX() + xOff, centre.getY() + i - hOff, centre.getZ() + j - vOff);
                    if(direction!=EnumFacing.EAST)
                        b1 = WorldHelper.rotatePos(
                                direction==EnumFacing.WEST && (direction=EnumFacing.SOUTH)==EnumFacing.SOUTH?
                                        WorldHelper.rotatePos(b1, centre, WorldHelper.Rotate.RIGHT):
                                        b1, centre, direction==EnumFacing.SOUTH?WorldHelper.Rotate.RIGHT:WorldHelper.Rotate.LEFT);
                    b[i][j] = new Pair<BlockPos, Character>(b1, j < c.length ? c[j] : MBStruct.WLD);
                }
            }
            return b;
        }
    }
}

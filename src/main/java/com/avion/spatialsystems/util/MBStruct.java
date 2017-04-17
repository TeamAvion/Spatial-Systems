package com.avion.spatialsystems.util;

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
    /**
     * Wildcard search operator
     */
    public static final char WLD = '*';

    protected final Map<Integer, Plane> structure = new HashMap<Integer, Plane>();
    protected final Map<Character, Block> mappings = new HashMap<Character, Block>();

    /**
     * Add a new search layer. Higher numbers are ordered behind lower numbers (relative to centre).
     * @param relativeOffset Offset behind centre position. Higher number => further back.
     * @param layer The specified search plane to use for this layer
     * @return Reference to this object for chaining.
     */
    public MBStruct addLayer(int relativeOffset, Plane layer){ structure.put(relativeOffset, layer); return this; }

    /**
     * Add a copy of a given search layer at the supplied relative offset. Higher numbers are ordered behind lower numbers (relative to centre).
     * <em>NOTE:</em> This uses the {@link Plane#copy()} method, meaning that manipulating one of the referenced layers will not affect the other.
     * @param relativeOffset Offset behind centre position. Higher number => further back.
     * @param source Layer to copy from.
     * @return Reference to this object for chaining.
     */
    public MBStruct addLayer(int relativeOffset, int source){ if(structure.containsKey(source)) structure.put(relativeOffset, structure.get(source)); return this; }

    /**
     * Registers a character-block mapping. This is used to correlate a supplied character in the search planes with a block in game.<br>
     * <em>NOTE:</em> Character defined in {@link MBStruct#WLD} cannot be mapped to a block.
     * @param blockMapping Character to correlate with a block
     * @param mappedBlock Block to search when the specified character is found in a plane.
     * @return Reference to this object for chaining.
     */
    public MBStruct registerMapping(char blockMapping, Block mappedBlock){
        if(blockMapping==WLD) throw new RuntimeException("Can't assign wildcard mapping '"+WLD+"' to "+mappedBlock);
        mappings.put(blockMapping, mappedBlock);
        return this;
    }

    /**
     * Search for the given block structure/collection in any/all of the horizontal directions.
     * @param w World to search in.
     * @param centre Where the search is originating from. (In the case of multiblocks, this is usually the controller's position).
     * @return Array of all directions the structure was found.
     */
    public EnumFacing[] findStructure(World w, BlockPos centre){ return findStructure(w, centre, EnumFacing.HORIZONTALS); }

    /**
     * Search for the given block structure/collection in the given orientations.
     * @param w World to search in.
     * @param centre Where the search is originating from. (In the case of multiblocks, this is usually the controller's position).
     * @param search Which directions the structure is allowed to face. (Direction is considered as the direction of an observer standing in layer -1 looking at layer 0)
     * @return Array of all directions where the structure was found.
     */
    public EnumFacing[] findStructure(World w, BlockPos centre, EnumFacing... search){
        List<EnumFacing> l = new ArrayList<EnumFacing>();
        for(EnumFacing f : search)
            if(findStructure(w, centre, f))
                l.add(f);
        return l.toArray(new EnumFacing[l.size()]);
    }

    /**
     * Create an exact (yet completely separate) copy of this object. This means that no amount of manipulations to the planes, layers or block mappings will affect the original object.
     * @return The copy.
     */
    public MBStruct copy(){
        MBStruct m = new MBStruct();
        for(Integer i : structure.keySet()) m.structure.put(i.intValue(), structure.get(i).copy());
        for(Character c : mappings.keySet()) m.mappings.put(c.charValue(), mappings.get(c));
        return m;
    }

    protected boolean findStructure(World w, BlockPos centre, EnumFacing dir){
        if(!h.contains(dir)) return false;
        Plane p;
        boolean state = false;
        for(Integer i : structure.keySet())
            for(Pair<BlockPos, Character>[] pos : (p=structure.get(i)).getPosMap(centre, i, dir))
                for(Pair<BlockPos, Character> pos1 : pos)
                    if(!pos1.getKey().equals(centre) /* Ignore controller block */ && !matches(w, pos1.getKey(), pos1.getValue()))
                        return false;
        return true;
    }

    /**
     * The assigned wildcard character {@link MBStruct#WLD} will always be considered a wildcard.
     * @param w World to check.
     * @param at Position to check.
     * @param mapping Character mapping to check.
     * @return Whether or not the given position matches the multi-block structure.
     */
    protected boolean matches(World w, BlockPos at, char mapping){
        if(mapping!=WLD && !mappings.containsKey(mapping)) throw new RuntimeException("Unregistered character-block mapping '"+mapping+"'");
        return mapping==WLD || w.getBlockState(at).getBlock().equals(mappings.get(mapping));
    }

    /**
     * A two-dimensional plane of characters representing blocks.
     */
    public static class Plane{
        List<char[]> rows = new ArrayList<char[]>();
        final int vOff, hOff;

        /**
         * Create a new plane with a vertical and horizontal offset.
         * @param vOff Offset from leftmost column in the plane that should be considered the centre.
         * @param hOff Offset from bottommost row in the plane that should be considered the centre.
         */
        public Plane(int vOff, int hOff){ this.vOff = vOff; this.hOff = hOff; }

        /**
         * Add a row to the bottom of the plane.
         * @param row Defined block mappings
         * @return  Reference to this object for chaining.
         */
        public Plane addRow(char... row){ rows.add(row); return this; }

        /**
         * Add a complete 2d plane of character-block mappings. If previous rows were defined, this plane will be concatenated under it/them.
         * @param plane
         * @return Reference to this object for chaining.
         */
        public Plane addPlane(char[]... plane){ for(char[] c : plane) addRow(c); return this; }

        /**
         * Create an array mapping this plane to an in-world block position based on the supplied centre, the vertical offset and the horizontal offset.
         * After the map is created, it is rotated to align with the given direction.
         * @param centre Specified centre of the plane (as dictated by verital and horizontal offset).
         * @param xOff Offset in the second vertical axis. For simplicity, this is referred to as Axis-X when referring the the rotationally ambiguous plane (pre-rotation).
         * @param direction Axis to align plane with.
         * @return Block-position-correlated character map relative to the centre.
         */
        public Pair<BlockPos, Character>[][] getPosMap(BlockPos centre, int xOff, EnumFacing direction){
            int largest = 0;
            for(char[] c : rows) if(c.length>largest) largest = c.length;
            Pair<BlockPos, Character>[][] b = new Pair[rows.size()][largest];
            for(int i = 0; i<rows.size(); ++i) {
                char[] c = rows.get(i);
                for (int j = 0; j<largest; ++j) {
                    BlockPos b1 = new BlockPos(centre.getX() + xOff, centre.getY() + i - vOff, centre.getZ() + j - hOff);
                    if(direction!=EnumFacing.EAST)
                        b1 = WorldHelper.rotatePos(
                                direction==EnumFacing.WEST?WorldHelper.rotatePos(b1, centre, WorldHelper.Rotate.RIGHT):
                                        b1, centre, direction==EnumFacing.SOUTH||direction==EnumFacing.WEST?WorldHelper.Rotate.RIGHT:WorldHelper.Rotate.LEFT);
                    b[i][j] = new Pair<BlockPos, Character>(b1, j < c.length ? c[j] : MBStruct.WLD);
                }
            }
            return b;
        }

        /**
         * Create an exact (and completely separate) copy of this plane.
         * @return Reference to this object for chaining.
         */
        public Plane copy(){
            Plane p = new Plane(vOff, hOff);
            for(char[] c : rows) p.rows.add(Arrays.copyOf(c, c.length));
            return p;
        }
    }
}

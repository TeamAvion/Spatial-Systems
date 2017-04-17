package com.avion.spatialsystems.util;

import com.google.common.base.Optional;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class MBStruct {
    @SuppressWarnings("unchecked")
    private static final List<EnumFacing> h = Arrays.asList(EnumFacing.HORIZONTALS);

    /**
     * Wildcard search operator
     */
    public static final char WLD = '*';

    protected final Map<Integer, Plane> structure = new HashMap<Integer, Plane>();
    protected final Map<Character, Pair<ObjectReference<Block>, Optional<Integer>>> mappings = new HashMap<Character, Pair<ObjectReference<Block>, Optional<Integer>>>();
    protected boolean strict = false;

    /**
     * Add a new search layer. Higher numbers are ordered behind lower numbers (relative to centre).
     * @param relativeOffset Offset behind centre position. Higher number => further back.
     * @param layer The specified search plane to use for this layer
     * @return Reference to this object for chaining.
     */
    public MBStruct addLayer(int relativeOffset, Plane layer){ structure.put(relativeOffset, layer); return this; }

    /**
     * Remove search layer at the given offset.
     * @param relativeOffset  Offset behind centre position. Higher number => further back.
     * @return Reference to this object for chaining.
     */
    public MBStruct removeLayer(int relativeOffset){ structure.remove(relativeOffset); return this; }

    /**
     * Get the search layer at the given offset.
     * @param relativeOffset  Offset behind centre position. Higher number => further back.
     * @return Plane defining search for this layer.
     */
    public Plane getLayer(int relativeOffset){ return structure.get(relativeOffset); }

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
        mappings.put(blockMapping, new Pair<ObjectReference<Block>, Optional<Integer>>(new ImmutableReference<Block>(mappedBlock), Optional.<Integer>absent()));
        return this;
    }

    /**
     * Delete all character-block mappings.
     * @return Reference to this object for chaining.
     */
    public MBStruct clearMappings(){ mappings.clear(); return this; }

    /**
     * Registers a character-block mapping with a specific metadata. This is used to correlate a supplied character in the search planes with a block in game.<br>
     * <em>NOTE:</em> Character defined in {@link MBStruct#WLD} cannot be mapped to a block.
     * @param blockMapping Character to correlate with a block
     * @param mappedBlock Block to search when the specified character is found in a plane.
     * @param meta Specific metadata to allow when searching.
     * @return Reference to this object for chaining.
     */
    public MBStruct registerMapping(char blockMapping, Block mappedBlock, int meta){
        if(blockMapping==WLD) throw new RuntimeException("Can't assign wildcard mapping '"+WLD+"' to "+mappedBlock);
        mappings.put(blockMapping, new Pair<ObjectReference<Block>, Optional<Integer>>(new ImmutableReference<Block>(mappedBlock), Optional.of(meta)));
        return this;
    }

    /**
     * Registers a character-block mapping. This mapping uses {@link ObjectReference}. This means that the supplied block reference doesn't necessarily have to exist at this time.
     * The only requirement is that a proper value exists when {@link MBStruct#findStructure(World, BlockPos)} (or any overloads) can be invoked.
     * This is used to correlate a supplied character in the search planes with a block in game.<br>
     * <em>NOTE:</em> Character defined in {@link MBStruct#WLD} cannot be mapped to a block.
     * @param blockMapping Character to correlate with a block
     * @param reference Reference to block to search when the specified character is found in a plane.
     * @return Reference to this object for chaining.
     */
    public MBStruct registerLazyMapping(char blockMapping, ObjectReference<Block> reference){
        if(blockMapping==WLD) throw new RuntimeException("Can't assign wildcard mapping '"+WLD+"' to "+reference);
        mappings.put(blockMapping, new Pair<ObjectReference<Block>, Optional<Integer>>(reference, Optional.<Integer>absent()));
        return this;
    }

    /**
     * Registers a character-block mapping with a specific metadata. This mapping uses {@link ObjectReference}. This means that the supplied block reference doesn't necessarily have to exist at this time.
     * The only requirement is that a proper value exists when {@link MBStruct#findStructure(World, BlockPos)} (or any overloads) can be invoked.
     * This is used to correlate a supplied character in the search planes with a block in game.<br>
     * <em>NOTE:</em> Character defined in {@link MBStruct#WLD} cannot be mapped to a block.
     * @param blockMapping Character to correlate with a block
     * @param reference Reference to block to search when the specified character is found in a plane.
     * @param meta Specific metadata to allow when searching.
     * @return Reference to this object for chaining.
     */
    public MBStruct registerLazyMapping(char blockMapping, ObjectReference<Block> reference, int meta){
        if(blockMapping==WLD) throw new RuntimeException("Can't assign wildcard mapping '"+WLD+"' to "+reference);
        mappings.put(blockMapping, new Pair<ObjectReference<Block>, Optional<Integer>>(reference, Optional.of(meta)));
        return this;
    }

    /**
     * Gets a character-block mapping from a given character if it exists.
     * @param of Character corresponding to mapping.
     * @return Mapped block and metadata if metadata was specified. If no mapping is linked to the given character, return is null.
     */
    public Pair<ObjectReference<Block>, Optional<Integer>> getMapping(char of){ return mappings.containsKey(of)?mappings.get(of):null; }

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
        Pair<ObjectReference<Block>, Optional<Integer>> p;
        for(Integer i : structure.keySet()) m.structure.put(i, structure.get(i).copy());
        for(Character c : mappings.keySet())
            m.mappings.put(c, new Pair<ObjectReference<Block>, Optional<Integer>>(
                    new ImmutableReference<Block>((p=mappings.get(c)).getKey().get()),
                    Optional.fromNullable(p.getValue().isPresent()?p.getValue().get():null))
            );
        m.strict = strict;
        return m;
    }

    protected boolean findStructure(World w, BlockPos centre, EnumFacing dir){
        if(!h.contains(dir)) return false;
        Plane p;
        Pair<ObjectReference<Block>, Optional<Integer>> p1;
        IBlockState b;
        HashMap<Block, Integer> strictMap = null;
        if(strict) strictMap = new HashMap<Block, Integer>();
        boolean b1 = false;
        boolean state = false;
        for(Integer i : structure.keySet())
            for(Pair<BlockPos, Character>[] pos : (p=structure.get(i)).getPosMap(centre, i, dir))
                for(Pair<BlockPos, Character> pos1 : pos)
                    if((b1=!pos1.getKey().equals(centre)) /* Ignore controller block */ && !matches(w, pos1.getKey(), pos1.getValue(), strictMap))
                        return false;
                    else if(b1 && strict && !strictMap.containsKey((b=w.getBlockState(pos1.getKey())))) strictMap.put(b.getBlock(), b.getBlock().getMetaFromState(b)); // Temporary strict-mode metadata mapping
        return true;
    }

    /**
     * Defines whether or not blocks in the structure (defined under the same character mapping) that doesn't have a
     * specified metadata should enforce matching metadata for that character-block mapping based on metadata inferred from first block of that type found.
     * <em>Example:</em> Searching for a structure with one character mapping - say 'b', the strict mode will ensure that
     * (despite no specific metadata was defined) all blocks attempting to match 'b' must have the same metadata.
     * This is good for generalizing a block structure where there are mutiple versions of the same block that should be supported
     * without allowing for mixing without needing to create multiple MBStruct objects (one for each).
     * @param strict Whether or not to enforce strict mode.
     * @return Reference to this object for chaining.
     */
    public MBStruct setStrict(boolean strict){ this.strict = strict; return this; }

    /**
     * The assigned wildcard character {@link MBStruct#WLD} will always be considered a wildcard.
     * @param w World to check.
     * @param at Position to check.
     * @param mapping Character mapping to check.
     * @return Whether or not the given position matches the multi-block structure.
     */
    protected boolean matches(World w, BlockPos at, char mapping, HashMap<Block, Integer> strictMap){
        if(mapping!=WLD && !mappings.containsKey(mapping)) throw new RuntimeException("Unregistered character-block mapping '"+mapping+"'");
        IBlockState b;
        Pair<ObjectReference<Block>, Optional<Integer>> p;
        return
                mapping==WLD || (
                        (b=w.getBlockState(at)).getBlock().equals((p=mappings.get(mapping)).getKey().get()) &&
                        (!p.getValue().isPresent() || b.getBlock().getMetaFromState(b)==p.getValue().or(0)) &&
                        (!strictMap.containsKey(b.getBlock()) || b.getBlock().getMetaFromState(b)==strictMap.get(b.getBlock()))
                );
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

    public static interface ObjectReference<T> { public T get(); }
    public static class ImmutableReference<T> implements ObjectReference<T>{
        private final T t;
        public ImmutableReference(T t){ this.t = t; }
        @Override public T get() { return t; }
    }
    public static class StaticFieldReference<T> implements ObjectReference<T> {
        protected final String fieldName;
        protected final String clazzName;
        protected Class resolved;
        protected Field resolvedField;

        public StaticFieldReference(String fieldName, String clazzName){ this.fieldName = fieldName; this.clazzName = clazzName; }
        public StaticFieldReference(Field field, String clazzName){ this.resolvedField = field; this.fieldName = null; this.clazzName = clazzName; }
        public StaticFieldReference(Field field, Class clazz){ this.resolvedField = field; this.resolved = clazz; this.fieldName = null; this.clazzName = null; }
        public StaticFieldReference(String fieldName, Class clazz){ this.fieldName = fieldName; this.clazzName = null; this.resolved = clazz; }

        @Override
        public T get() {
            if(resolved == null) try{ resolved = Class.forName(clazzName); }catch(Throwable e){ e.printStackTrace(); }
            if(resolvedField == null) try{ resolvedField = resolved.getDeclaredField(fieldName); resolvedField.setAccessible(true); }catch(Throwable e){ e.printStackTrace(); }
            try{ return (T) resolvedField.get(null); }catch(Throwable e){ e.printStackTrace(); }
            return null;
        }
    }
}

package com.avion.spatialsystems.util;

import com.google.common.base.Optional;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.avion.spatialsystems.util.DynamicMBStruct.SearchMode.*;
import static net.minecraft.util.EnumFacing.*;

/**
 * Finds a multi-block structure of a dynamic size based on allowed/disallowed block types.
 */
@SuppressWarnings("unused")
public class DynamicMBStruct {
    protected final HashMap<Integer, Pair<ObjectReference<? extends Block>, Optional<Integer>>> search = new HashMap<Integer, Pair<ObjectReference<? extends Block>, Optional<Integer>>>();
    protected SearchMode defaultSearchMode = CARDINAL;
    protected boolean strict = false;
    public final HashMap<ObjectReference<? extends Block>, WorldPredicate> customBlockHandler = new HashMap<ObjectReference<? extends Block>, WorldPredicate>();

    public boolean isAllowed(Block b, int meta){
        for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(b.equals(p.getKey().get()) && (!p.getValue().isPresent() || p.getValue().get()==meta)) return true;
        return false;
    }

    public boolean isAllowed(Block b){
        for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(b.equals(p.getKey().get())) return true;
        return false;
    }

    public boolean isAllowed(ObjectReference<Block> b, int meta){
        for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(p.getKey().equals(b) && (!p.getValue().isPresent() || p.getValue().get()==meta)) return true;
        return false;
    }

    public boolean isAllowed(ObjectReference<Block> b){
        for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(p.getKey().equals(b)) return true;
        return false;
    }

    public boolean isAllowed(IBlockState b){ return isAllowed(b.getBlock(), b.getBlock().getMetaFromState(b)); }

    public boolean hasSpecificMeta(Block b){
        for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(p.getKey().equals(b) && p.getValue().isPresent()) return true;
        return false;
    }

    public <T extends Block> DynamicMBStruct registerCustomBlockHandler(T b, WorldPredicate p){ return registerCustomBlockHandler(new ImmutableReference<T>(b), p); }

    public <T extends Block> DynamicMBStruct registerCustomBlockHandler(ObjectReference<T> b, WorldPredicate p){
        customBlockHandler.put(b, p);
        return this;
    }

    protected boolean isAllowedStrict(IBlockState b, ArrayList<Pair<Block, Integer>> a){
        Block b1 = b.getBlock();
        int i = b1.getMetaFromState(b);
        boolean foundBlock = false;
        for(Pair<Block, Integer> p : a)
            if(p.getKey().equals(b1)){
                if(p.getValue()==i) return true;
                foundBlock = true;
            }
        return !foundBlock;
    }

    protected boolean isRegistered(Block b, ArrayList<Pair<Block, Integer>> a){
        for(Pair<Block, Integer> p : a) if(p.getKey().equals(b)) return true;
        return false;
    }

    public DynamicMBStruct setStrict(boolean strict){ this.strict = strict; return this; }

    public DynamicMBStruct add(int id, Block b, int meta){
        if(!isAllowed(b, meta)) search.put(id, new Pair<ObjectReference<? extends Block>, Optional<Integer>>(new ImmutableReference<Block>(b), Optional.of(meta)));
        return this;
    }

    public DynamicMBStruct add(int id, Block b){
        if(!isAllowed(b)) search.put(id, new Pair<ObjectReference<? extends Block>, Optional<Integer>>(new ImmutableReference<Block>(b), Optional.<Integer>absent()));
        clearWithMeta(b);
        return this;
    }

    public DynamicMBStruct add(int id, ObjectReference<Block> typeRef){
        if(!isAllowed(typeRef)) search.put(id, new Pair<ObjectReference<? extends Block>, Optional<Integer>>(typeRef, Optional.<Integer>absent()));
        return this;
    }

    public DynamicMBStruct add(int id, ObjectReference<Block> typeRef, int meta){
        if(!isAllowed(typeRef)) search.put(id, new Pair<ObjectReference<? extends Block>, Optional<Integer>>(typeRef, Optional.of(meta)));
        return this;
    }

    public DynamicMBStruct remove(int id){
        search.remove(id);
        return this;
    }

    public Optional<Integer> getIdFor(Block b){
        Pair<ObjectReference<? extends Block>, Optional<Integer>> p;
        for(Integer i : search.keySet()) if(b.equals((p=search.get(i)).getKey().get()) && !p.getValue().isPresent()) return Optional.of(i);
        return Optional.absent();
    }

    public Optional<Integer> getIdFor(Block b, int meta){
        Pair<ObjectReference<? extends Block>, Optional<Integer>> p;
        for(Integer i : search.keySet()) if(b.equals((p=search.get(i)).getKey().get()) && p.getValue().isPresent() && p.getValue().get()==meta) return Optional.of(i);
        return Optional.absent();
    }

    protected void clearWithMeta(Block b){
        Pair<ObjectReference<? extends Block>, Optional<Integer>> p;
        for(Integer i : search.keySet()) if(b.equals((p=search.get(i)).getKey().get()) && p.getValue().isPresent()) search.remove(i);
    }

    protected void purge(){ for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(!p.getValue().isPresent()) clearWithMeta(p.getKey().get()); }

    public DynamicMBStruct setDefaultSearchMode(SearchMode mode){ defaultSearchMode = mode; return this; }

    public List<BlockPos> find(World w, BlockPos start){ return find(w, start, defaultSearchMode); }
    public List<BlockPos> find(World w, BlockPos start, SearchMode mode){
        purge();
        ArrayList<BlockPos> a = new ArrayList<BlockPos>();
        a.add(start);
        findAt(w, start, a, new ArrayList<Pair<Block, Integer>>(), mode);
        return a;
    }

    protected void findAt(World w, BlockPos at, List<BlockPos> exclude, ArrayList<Pair<Block, Integer>> mStrict, SearchMode mode){
        BlockPos[] temp = new BlockPos[mode==CARDINAL?6:mode==CARDINAL_DIAGONAL?14:mode==CROSS?8:26];
        ArrayList<BlockPos> nPos = new ArrayList<BlockPos>();
        int ctr = -1;
        if(mode!=CROSS){
            for(int i = 0; i<EnumFacing.values().length; ++i) temp[++ctr] = getAt(at, EnumFacing.values()[i]);
            if(mode==CARDINAL_DIAGONAL || mode==CUBIC){
                for(int i = 0; (i/2)<EnumFacing.HORIZONTALS.length; i+=2){
                    temp[++ctr] = translate(at, HORIZONTALS[i], UP);
                    temp[++ctr] = translate(at, HORIZONTALS[i], DOWN);
                }
            }
        }
        if(mode==CROSS || mode==CUBIC){
            temp[++ctr] = translate(at, UP, EAST, NORTH);
            temp[++ctr] = translate(at, UP, EAST, SOUTH);
            temp[++ctr] = translate(at, UP, WEST, NORTH);
            temp[++ctr] = translate(at, UP, WEST, SOUTH);
            temp[++ctr] = translate(at, DOWN, EAST, NORTH);
            temp[++ctr] = translate(at, DOWN, EAST, SOUTH);
            temp[++ctr] = translate(at, DOWN, WEST, NORTH);
            temp[++ctr] = translate(at, DOWN, WEST, SOUTH);
        }
        IBlockState b1;
        boolean b2;
        WorldPredicate p;
        for(BlockPos b : temp)
            if(((p=getCustomBlockHandler((b1=w.getBlockState(b)).getBlock()))==null || (p instanceof MBPredicate?((MBPredicate)p).apply(w, b, exclude, mStrict, this):p.apply(w, b))) &&
                    defaultApply(exclude, mStrict, b, w, b1)) { // Handling system
                exclude.add(b);
                nPos.add(b);
                if(strict && !isRegistered(b1.getBlock(), mStrict) && hasSpecificMeta(b1.getBlock())) mStrict.add(new Pair<Block, Integer>(b1.getBlock(), b1.getBlock().getMetaFromState(b1)));
            }
        for(BlockPos b : nPos) findAt(w, b, exclude, mStrict, mode); // Recursively find more until all blocks have been found
    }

    boolean defaultApply(List<BlockPos> exclude, ArrayList<Pair<Block, Integer>> mStrict, BlockPos b, World w, IBlockState b1){
        return (!exclude.contains(b) && isAllowed(w.getBlockState(b)) && (!strict || (!hasSpecificMeta(b1.getBlock()) && isAllowedStrict(b1, mStrict))));
    }

    public WorldPredicate getCustomBlockHandler(Block b){
        for(ObjectReference<? extends Block> o : customBlockHandler.keySet()) if(b.equals(o.get())) return customBlockHandler.get(o);
        return null;
    }

    protected ArrayList<Pair<ObjectReference<? extends Block>, Optional<Integer>>> findByBlock(Block b){
        ArrayList<Pair<ObjectReference<? extends Block>, Optional<Integer>>> a = new ArrayList<Pair<ObjectReference<? extends Block>, Optional<Integer>>>();
        for(Pair<ObjectReference<? extends Block>, Optional<Integer>> p : search.values()) if(p.getKey().get().equals(b)) a.add(p);
        return a;
    }

    protected BlockPos getAt(BlockPos relativeTo, EnumFacing direction){
        return new BlockPos(relativeTo.getX()+(direction==SOUTH?-1:direction==NORTH?1:0), relativeTo.getY()+(direction==DOWN?-1:direction==UP?1:0), relativeTo.getZ()+(direction==WEST?-1:direction==EAST?1:0));
    }

    protected BlockPos translate(BlockPos from, EnumFacing... by){
        for(EnumFacing e : by) from = getAt(from, e);
        return from;
    }

    public void isBlockViable(){}

    public enum SearchMode{ CARDINAL, CARDINAL_DIAGONAL, CROSS, CUBIC }

    public static abstract class MBPredicate extends WorldPredicate {

        private World w;
        private BlockPos p;
        private List<BlockPos> exclude;
        private ArrayList<Pair<Block, Integer>> mStrict;
        private DynamicMBStruct inst;

        public boolean isRegularlyViable(){ return inst.defaultApply(exclude, mStrict, p, w, w.getBlockState(p)); }

        final boolean apply(World w, BlockPos p, List<BlockPos> exclude, ArrayList<Pair<Block, Integer>> mStrict, DynamicMBStruct inst){
            this.w = w;
            this.p = p;
            this.exclude = exclude;
            this.mStrict = mStrict;
            this.inst = inst;
            return apply(w, p);
        }
    }
}

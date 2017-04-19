package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import com.avion.spatialsystems.tile.TileFurnaceBinder;
import com.avion.spatialsystems.util.MBStruct;
import com.google.common.base.Optional;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import static com.avion.spatialsystems.SpatialSystems.GUI_FURNACE;
import static com.avion.spatialsystems.SpatialSystems.instance;
import static com.avion.spatialsystems.blocks.Properties.FACING;

@SuppressWarnings("ALL")
public class AdvancedFurnaceController extends BlockContainer {

    public AdvancedFurnaceController(){
        super(Material.ROCK);
        setup();
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1F);
    }

    public AdvancedFurnaceController(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setup();
    }

    private void setup(){
        setUnlocalizedName("advancedFurnaceController").setCreativeTab(SpatialSystems.TAB).setRegistryName("advancedfurnacecontroller");
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        InventoryHelper.dropInventoryItems(worldIn, pos, (TileAdvancedFurnace) worldIn.getTileEntity(pos));
        TileAdvancedFurnace t;
        if(((t=(TileAdvancedFurnace)worldIn.getTileEntity(pos))).isBound()){
            unregister(worldIn, t.getBound());
            t.unbind();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileAdvancedFurnace(); }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        //List<BlockPos> l = ModBlocks.dynamicFurnace.find(worldIn, pos); // Load a dynamic size & shape
        EnumFacing[] e;
        MBStruct mb;
        if(((e=(mb=ModBlocks.furnaceMultiBlockGrand).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.furnaceMultiBlockBig).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.furnaceMultiBlock).findStructure(worldIn, pos)).length!=0) // Static shapes
                /*(l.size()==26 || l.size()==98 || l.size()==218 || l.size()==1352)*/ && !placer.isSneaking()) { // Dynamic shapes
            BlockPos[] b;
            registerUnregistered(worldIn, b=clean(mb.getMap(worldIn, pos, e[0])));
            ((TileAdvancedFurnace) worldIn.getTileEntity(pos)).bind(b);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //List<BlockPos> l = ModBlocks.dynamicFurnace.find(worldIn, pos); // Load a dynamic size & shape
        EnumFacing[] e;
        MBStruct mb;
        if(((e=(mb=ModBlocks.furnaceMultiBlockGrand).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.furnaceMultiBlockBig).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.furnaceMultiBlock).findStructure(worldIn, pos)).length!=0) // Static shapes
                /*(l.size()==26 || l.size()==98 || l.size()==218 || l.size()==1352)*/ && !playerIn.isSneaking()) { // Dynamic shapes
            BlockPos[] b;
            registerUnregistered(worldIn, b=clean(mb.getMap(worldIn, pos, e[0])));
            ((TileAdvancedFurnace) worldIn.getTileEntity(pos)).bind(b);
            playerIn.openGui(instance, GUI_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true; //TODO: Check if multi-block is formed
    }

    protected void registerUnregistered(World w, BlockPos[] all){
        for(BlockPos p : all)
            if(w.getTileEntity(p)==null){
                TileEntity t = new TileFurnaceBinder(p);
                t.setPos(p);
                t.setWorld(w);
                w.addTileEntity(t);
            }
    }

    protected void unregister(World w, BlockPos[] all){
        for(BlockPos p : all)
            if(w.getTileEntity(p)!=null)
                w.removeTileEntity(p);
    }

    protected BlockPos[] clean(Pair<BlockPos, Optional<IBlockState>>[] mapData){
        BlockPos[] b = new BlockPos[mapData.length];
        int ctr = -1;
        for(Pair<BlockPos, Optional<IBlockState>> p : mapData) b[++ctr] = p.getKey();
        return b;
    }

    @Nullable @Override public TileEntity createNewTileEntity(World worldIn, int meta) { return new TileAdvancedFurnace(); }
}

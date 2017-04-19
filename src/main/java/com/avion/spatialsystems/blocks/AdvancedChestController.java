package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.tile.TileChestBinder;
import com.avion.spatialsystems.util.MBStruct;
import com.google.common.base.Optional;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.avion.spatialsystems.SpatialSystems.GUI_CHEST;
import static com.avion.spatialsystems.SpatialSystems.instance;
import static com.avion.spatialsystems.blocks.Properties.FACING;

//Created by Bread10 at 10:18 on 15/04/2017
@SuppressWarnings("ALL")
public class AdvancedChestController extends Block implements ITileEntityProvider {

    public AdvancedChestController(){
        super(Material.WOOD);
        setUnlocalizedName("advancedChestController").setCreativeTab(SpatialSystems.TAB).setRegistryName("advancedchestcontroller");
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
        this.setHarvestLevel("axe", 1);
        this.setHardness(1F);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAdvancedChest();
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing[] e;
        MBStruct mb;
        if(!worldIn.isRemote &&
                ((e=(mb=ModBlocks.chestMultiBlockGrand).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.chestMultiBlockBig).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.chestMultiBlock).findStructure(worldIn, pos)).length!=0) // Static shapes
                /*(l.size()==26 || l.size()==98 || l.size()==218 || l.size()==1352)*/ && !placer.isSneaking()) { // Dynamic shapes
            BlockPos[] b;
            registerUnregistered(worldIn, b=clean(mb.getMap(worldIn, pos, e[0])));
            ((TileAdvancedChest) worldIn.getTileEntity(pos)).bind(b);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumFacing[] e;
        MBStruct mb;
        if(!worldIn.isRemote &&
                ((e=(mb=ModBlocks.chestMultiBlockGrand).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.chestMultiBlockBig).findStructure(worldIn, pos)).length!=0 ||
                (e=(mb=ModBlocks.chestMultiBlock).findStructure(worldIn, pos)).length!=0) // Static shapes
                /*(l.size()==26 || l.size()==98 || l.size()==218 || l.size()==1352)*/ && !playerIn.isSneaking()) { // Dynamic shapes
            BlockPos[] b;
            registerUnregistered(worldIn, b=clean(mb.getMap(worldIn, pos, e[0])));
            ((TileAdvancedChest) worldIn.getTileEntity(pos)).bind(b);
            playerIn.openGui(instance, GUI_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    protected void registerUnregistered(World w, BlockPos[] all){
        for(BlockPos p : all)
            if(w.getTileEntity(p)==null){
                TileEntity t = new TileChestBinder(p);
                t.setPos(p);
                t.setWorld(w);
                w.addTileEntity(t);
                ModBlocks.advancedChestBlock.updateBlockState(w.getBlockState(p), w, p);
            }
    }

    protected void unregister(World w, BlockPos[] all){
        for(BlockPos p : all)
            if(w.getTileEntity(p)!=null) {
                w.removeTileEntity(p);
                ModBlocks.advancedChestBlock.resetBlockState(w.getBlockState(p), w, p);
            }
    }

    protected BlockPos[] clean(Pair<BlockPos, Optional<IBlockState>>[] mapData){
        BlockPos[] b = new BlockPos[mapData.length];
        int ctr = -1;
        for(Pair<BlockPos, Optional<IBlockState>> p : mapData) b[++ctr] = p.getKey();
        return b;
    }

}

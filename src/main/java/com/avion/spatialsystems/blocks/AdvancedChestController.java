package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.TileAdvancedChest;
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
        return this.getDefaultState().withProperty(FACING, facing);
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
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            TileAdvancedChest te = (TileAdvancedChest) worldIn.getTileEntity(pos);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.isSneaking()) {
            ((TileAdvancedChest) worldIn.getTileEntity(pos)).sync();
            playerIn.openGui(instance, GUI_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }
}

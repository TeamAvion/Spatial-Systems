package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.avion.spatialsystems.SpatialSystems.GUI_FURNACE;
import static com.avion.spatialsystems.SpatialSystems.instance;

public class AdvancedFurnaceController extends Block implements ITileEntityProvider {

    public AdvancedFurnaceController(){
        super(Material.ROCK);
        setup();
    }

    public AdvancedFurnaceController(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setup();
    }

    private void setup(){
        setUnlocalizedName("advancedFurnaceController").setCreativeTab(SpatialSystems.TAB).setRegistryName("advancedfurnacecontroller");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAdvancedFurnace();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true; //TODO: Check if multi-block is formed
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        playerIn.openGui(instance, GUI_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true; //TODO: Check if multi-block is formed
    }

}

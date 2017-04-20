package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.BoundTileImpl;
import com.avion.spatialsystems.tile.TileChestBinder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings({"deprecation", "unused"})
public class AdvancedChestBlock extends Block {

    public AdvancedChestBlock() {
        super(Material.WOOD);
        this.setUnlocalizedName("advancedChestBlock");
        this.setRegistryName("advancedchestblock");
        this.setCreativeTab(SpatialSystems.TAB);
        this.setHarvestLevel("axe", 1);
        this.setHardness(1F);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileChestBinder(); }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te;
        if((te=worldIn.getTileEntity(pos))!=null && te instanceof BoundTileImpl) ((BoundTileImpl) te).triggerBreak();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BoundTileImpl t;
        return (t=(BoundTileImpl)worldIn.getTileEntity(pos))!=null &&
                t.isBound() &&
                worldIn.getBlockState(t.getBoundSource()).getBlock().onBlockActivated(worldIn, t.getBoundSource(), state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

}

package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.util.LogHelper;
import com.avion.spatialsystems.util.MBStruct;
import com.google.common.base.Optional;
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
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

import static com.avion.spatialsystems.blocks.Properties.FACING;
import static com.avion.spatialsystems.util.MBStruct.WLD;

//Created by Bread10 at 10:18 on 15/04/2017
public class AdvancedChestController extends Block implements ITileEntityProvider {

    public AdvancedChestController(){
        super(Material.WOOD);
        setUnlocalizedName("advancedChestController").setCreativeTab(SpatialSystems.TAB).setRegistryName("advancedchestcontroller");
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
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
            if(te==null) throw new AssertionError("This is wrong!");
            te.findMultiBlockStructure();
            MBStruct mb = new MBStruct().addLayer(0, new MBStruct.Plane(1, 1)
                    .addRow('b', 'b', 'b')
                    .addRow('b', WLD, 'b')
                    .addRow('b', 'b', 'b')
            ).registerMapping('b', ModBlocks.advancedChestBlock);
            Optional<EnumFacing[]> o = mb.findStructure(worldIn, pos);
            if(o.isPresent()) LogHelper.array(Level.DEBUG, o.orNull());
            else System.out.println("Break me");
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.isSneaking()) {

        }
        return false;
    }
}

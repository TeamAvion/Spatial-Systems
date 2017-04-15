package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.misc.EnumLevel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import static com.avion.spatialsystems.blocks.Properties.LEVEL;

//Created by Bread10 at 08:27 on 15/04/2017
public class AdvancedFurnaceBlock extends Block {

    public AdvancedFurnaceBlock() {
        super(Material.ROCK);
        this.setUnlocalizedName("advancedFurnaceBlock");
        this.setRegistryName("advancedfurnaceblock");
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, EnumLevel.BASIC));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LEVEL, EnumLevel.getLevelFromValue(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumLevel) state.getValue(LEVEL)).getValue();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (EnumLevel level : EnumLevel.values()) {
            list.add(new ItemStack(itemIn, 1, level.getValue()));
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

}

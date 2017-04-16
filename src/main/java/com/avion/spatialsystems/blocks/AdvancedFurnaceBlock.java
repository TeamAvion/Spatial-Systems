package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.util.EnumLevel;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.Random;

import static com.avion.spatialsystems.SpatialSystems.GUI_FURNACE;
import static com.avion.spatialsystems.SpatialSystems.instance;
import static com.avion.spatialsystems.blocks.Properties.LEVEL;

//Created by Bread10 at 08:27 on 15/04/2017
public class AdvancedFurnaceBlock extends Block {

    public AdvancedFurnaceBlock() {
        super(Material.ROCK);
        this.setUnlocalizedName("advancedFurnaceBlock");
        this.setRegistryName("advancedfurnaceblock");
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, EnumLevel.BASIC));
        this.setCreativeTab(SpatialSystems.TAB);
        this.setHarvestLevel("pixkaxe", 1);
        this.setHardness(1F);
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

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAdvancedFurnace(); // Smelt speed of 160/20 = 8 ticks... or something... :/
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        playerIn.openGui(instance, GUI_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
        //return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        InventoryHelper.dropInventoryItems(worldIn, pos, (TileAdvancedFurnace) worldIn.getTileEntity(pos));
        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.advancedFurnaceBlock);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModBlocks.advancedFurnaceBlock);
    }
}

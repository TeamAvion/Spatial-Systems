package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import com.avion.spatialsystems.util.EnumLevel;
import com.avion.spatialsystems.util.MBStruct;
import com.avion.spatialsystems.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
public class AdvancedFurnaceController extends Block {

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
        InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) worldIn.getTileEntity(pos));
        TileAdvancedFurnace t;
        if(((t=(TileAdvancedFurnace)worldIn.getTileEntity(pos))).isBound()){
            WorldHelper.unbindAll(worldIn, t.getBound());
            t.unbind();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAdvancedFurnace();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        //List<BlockPos> l = ModBlocks.dynamicFurnace.find(worldIn, pos); // Load a dynamic size & shape
        EnumFacing[] e;
        MBStruct mb;
        if(!worldIn.isRemote && (e=(mb=ModBlocks.furnaceMultiBlock).findStructure(worldIn, pos)).length!=0 // Static shapes
                /*(l.size()==26 || l.size()==98 || l.size()==218 || l.size()==1352)*/ && !placer.isSneaking()) { // Dynamic shapes
            BlockPos[] b;
            IBlockState s;
            WorldHelper.bindAll(worldIn, b=MBStruct.clean(mb.getMap(worldIn, pos, e[0])), pos);
            TileAdvancedFurnace tile = (TileAdvancedFurnace) worldIn.getTileEntity(pos);
            tile.bind(b);
            tile.setLevel(EnumLevel.values()[(s = worldIn.getBlockState(WorldHelper.translate(pos, EnumFacing.DOWN))).getBlock().getMetaFromState(s)]);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //List<BlockPos> l = ModBlocks.dynamicFurnace.find(worldIn, pos); // Load a dynamic size & shape
        EnumFacing[] e;
        MBStruct mb;
        if(!worldIn.isRemote && (e=(mb=ModBlocks.furnaceMultiBlock).findStructure(worldIn, pos)).length!=0 // Static shapes
                /*(l.size()==26 || l.size()==98 || l.size()==218 || l.size()==1352)*/ && !playerIn.isSneaking()) { // Dynamic shapes
            BlockPos[] b;
            IBlockState s;
            WorldHelper.bindAll(worldIn, b=MBStruct.clean(mb.getMap(worldIn, pos, e[0])), pos);
            TileAdvancedFurnace tile = (TileAdvancedFurnace) worldIn.getTileEntity(pos);
            tile.bind(b);
            tile.setLevel(EnumLevel.values()[(s = worldIn.getBlockState(WorldHelper.translate(pos, EnumFacing.DOWN))).getBlock().getMetaFromState(s)]);
            playerIn.openGui(instance, GUI_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }
}

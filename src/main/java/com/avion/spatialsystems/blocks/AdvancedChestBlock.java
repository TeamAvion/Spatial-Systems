package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.tile.BoundTileImpl;
import com.avion.spatialsystems.tile.TileChestBinder;
import com.avion.spatialsystems.tile.TileFurnaceBinder;
import com.avion.spatialsystems.util.EnumType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import static com.avion.spatialsystems.blocks.Properties.TYPE;
import static net.minecraft.init.Blocks.AIR;

@SuppressWarnings({"deprecation", "unused"})
public class AdvancedChestBlock extends Block {

    public AdvancedChestBlock() {
        super(Material.WOOD);
        this.setUnlocalizedName("advancedChestBlock");
        this.setRegistryName("advancedchestblock");
        this.setCreativeTab(SpatialSystems.TAB);
        setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.NORMAL));
        this.setHarvestLevel("axe", 1);
        this.setHardness(1F);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumType.getTypeFromValue(meta));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) { return new TileChestBinder(); }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType) state.getValue(TYPE)).getValue();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te;
        if((te=worldIn.getTileEntity(pos))!=null && te instanceof BoundTileImpl) ((BoundTileImpl) te).triggerBreak();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BoundTileImpl t;
        return (t=(BoundTileImpl)worldIn.getTileEntity(pos))!=null &&
                t.isBound() &&
                worldIn.getBlockState(t.getBoundSource()).getBlock().onBlockActivated(worldIn, t.getBoundSource(), state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public void updateBlockState(IBlockState state, World worldIn, BlockPos pos) {
        Block up = worldIn.getBlockState(pos.down()).getBlock();
        Block north = worldIn.getBlockState(pos.north()).getBlock();
        Block east = worldIn.getBlockState(pos.east()).getBlock();
        Block south = worldIn.getBlockState(pos.south()).getBlock();
        Block west = worldIn.getBlockState(pos.west()).getBlock();
        Block down = worldIn.getBlockState(pos.down()).getBlock();
        Block[] sides = new Block[] {up, north, east, south, west};

        int airSides = 0;
        for (Block side : sides) {
            if (side == Blocks.AIR)
                airSides++;
        }

        EnumType type = EnumType.NORMAL;
        switch (airSides) {
            case 0:
                type = EnumType.NORMAL;
                break;
            case 1:
                type = EnumType.FACE;
                break;
            case 2:
                if (up != AIR && down != AIR)
                    type = EnumType.EDGE_Y;
                else if (north != AIR && south != AIR)
                    type = EnumType.EDGE_Z;
                else if (east != AIR && south != AIR)
                    type = EnumType.EDGE_Z;
                break;
            case 3:
                if (up == AIR)
                    if (east == AIR)
                        if (north == AIR)
                            type = EnumType.CORNER_SWD;
                        else
                            type = EnumType.CORNER_NWD;
                    else
                        if (north == AIR)
                            type = EnumType.CORNER_SED;
                        else
                            type = EnumType.CORNER_NED;
                else
                    if (east == AIR)
                        if (north == AIR)
                            type = EnumType.CORNER_SWU;
                        else
                            type = EnumType.CORNER_NWU;
                    else
                    if (north == AIR)
                        type = EnumType.CORNER_SEU;
                    else
                        type = EnumType.CORNER_NEU;
                break;
        }
        state.withProperty(TYPE, type);

        worldIn.notifyBlockUpdate(pos, state, state, 3);
    }

    public void resetBlockState(IBlockState state, World worldIn, BlockPos pos) {
        state.withProperty(TYPE, EnumType.NORMAL);

        worldIn.notifyBlockUpdate(pos, state, state, 3);
    }

}

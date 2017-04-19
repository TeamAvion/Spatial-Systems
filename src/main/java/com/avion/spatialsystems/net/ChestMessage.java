package com.avion.spatialsystems.net;

import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.util.RefHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChestMessage implements IMessage{

    protected final Pattern pagination = Pattern.compile("(\\d+?),(\\d+?),(\\d+?),(\\d+?),(\\d+)");

    protected BlockPos pos;
    protected World world;
    protected TileEntity tile;
    protected int page;

    public ChestMessage(BlockPos pos, World world, TileEntity tile, int page){ this.pos = pos; this.world = world; this.tile = tile; this.page = page; }
    public ChestMessage(){}

    @Override
    public void fromBytes(ByteBuf buf) {
        try{
            Matcher m = pagination.matcher(new String(buf.array()).substring(1));
            if(!m.find()) return;
            tile = (world=DimensionManager.getWorld(Integer.parseInt(m.group(2)))).getTileEntity(pos=new BlockPos(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))));
            page = Integer.parseInt(m.group(1));
        }catch(Exception e){ e.printStackTrace(); }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] b = (page+","+world.provider.getDimension()+","+pos.getX()+","+pos.getY()+","+pos.getZ()).getBytes();
        buf.writeBytes(b, 0, b.length);
        buf.capacity(b.length+1);
    }

    public BlockPos getPos(){ return pos.toImmutable(); }
    public World getWorld(){ return world; }
    public TileEntity getTile(){ return tile; }
    public int getPage(){ return page; }
}

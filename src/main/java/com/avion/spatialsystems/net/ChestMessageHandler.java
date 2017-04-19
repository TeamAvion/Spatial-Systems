package com.avion.spatialsystems.net;

import com.avion.spatialsystems.tile.TileAdvancedChest;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChestMessageHandler implements IMessageHandler<ChestMessage, IMessage>{
    @Override
    public IMessage onMessage(final ChestMessage message, MessageContext ctx) {
        if(ctx.side==Side.CLIENT) return null; // Should never be handled on client side
        ((IThreadListener)ctx.getServerHandler().playerEntity.world).addScheduledTask(new Runnable() {
            @Override
            public void run() {
                if(message.tile instanceof TileAdvancedChest) ((TileAdvancedChest) message.tile).setCurrentPage(message.page);
            }
        });
        return null;
    }
}

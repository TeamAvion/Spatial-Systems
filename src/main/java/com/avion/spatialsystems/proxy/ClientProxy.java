package com.avion.spatialsystems.proxy;

import com.avion.spatialsystems.blocks.ModBlocks;

//Created by Bread10 at 08:23 on 15/04/2017
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        ModBlocks.registerRenders();
    }

}

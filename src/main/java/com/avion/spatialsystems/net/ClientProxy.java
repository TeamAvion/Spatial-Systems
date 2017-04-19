package com.avion.spatialsystems.net;

import com.avion.spatialsystems.blocks.ModBlocks;

//Created by Bread10 at 08:23 on 15/04/2017
public class ClientProxy extends CommonProxy {

    @Override
    public void init(){
        super.init();
        ModBlocks.registerRenders();
        // Raw localized data
        //Map<String, String> localized = (Map<String, String>) RefHelper.getValue("properties", RefHelper.getValue("i18nLocale", null, I18n.class), Locale.class);
    }

}

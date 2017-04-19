package com.avion.spatialsystems.proxy;

import com.avion.spatialsystems.blocks.ModBlocks;
import com.avion.spatialsystems.util.RefHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;

import java.util.Map;

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

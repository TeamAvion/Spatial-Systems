package com.avion.spatialsystems;

import com.avion.spatialsystems.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SpatialSystems.MODID, version = SpatialSystems.VERSION)
public class SpatialSystems {
    public static final String MODID = "spatialsystems";
    public static final String VERSION = "1.0";

    @Instance
    public static SpatialSystems instance;

    @SidedProxy(clientSide = "com.avion.spatialsystems.proxy.ClientProxy", serverSide = "com.avion.spatialsystems.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

}

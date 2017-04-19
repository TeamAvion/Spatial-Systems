package com.avion.spatialsystems;

import com.avion.spatialsystems.blocks.ModBlocks;
import com.avion.spatialsystems.gui.GUIHandler;
import com.avion.spatialsystems.net.ChestMessage;
import com.avion.spatialsystems.net.ChestMessageHandler;
import com.avion.spatialsystems.net.CommonProxy;
import com.avion.spatialsystems.util.LogHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = SpatialSystems.MODID, version = SpatialSystems.VERSION)
public class SpatialSystems {

    public static final String MODID = "spatialsystems";
    public static final String VERSION = "1.0";
    public static final int GUI_FURNACE = 'd' + 'a' + 'n' + 'k'; // Unique values, they said...
    public static final int GUI_CHEST = 'p' + 'e' + 'p' + 'e';
    public static final byte PACKET_ID = ('R' + 'E' + 'E' + 'E')%256;
    public SimpleNetworkWrapper net;

    public static final CreativeTabs TAB = new CreativeTabs("spatialsystems") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModBlocks.advancedFurnaceBlock, 1, 2);
        }
    };


    @Instance
    public static SpatialSystems instance;

    @SidedProxy(clientSide = "com.avion.spatialsystems.net.ClientProxy", serverSide = "com.avion.spatialsystems.net.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.setLogger(event.getModLog());
        net = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        net.registerMessage(ChestMessageHandler.class, ChestMessage.class, PACKET_ID, Side.SERVER);

        proxy.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler()); // Memes are created here
        proxy.init(); // Renders are created here

    }





/* ASCII Pepe greets you!
___________████████________██████
_________█░░░░░░░░██_██░░░░░░█
________█░░░░░░░░░░░█░░░░░░░░░█
_______█░░░░░░░███░░░█░░░░░░░░░█
_______█░░░░███░░░███░█░░░████░█
______█░░░██░░░░░░░░███░██░░░░██
_____█░░░░░░░░░░░░░░░░░█░░░░░░░░███
____█░░░░░░░░░░░░░██████░░░░░████░░█
____█░░░░░░░░░█████░░░████░░██░░██░░█
___██░░░░░░░███░░░░░░░░░░█░░░░░░░░███
__█░░░░░░░░░░░░░░█████████░░█████████
█░░░░░░░░░░█████_████████_█████_█
█░░░░░░░░░░█___█_████___███_█_█
█░░░░░░░░░░░░█_████_████__██_██████
░░░░░░░░░░░░░█████████░░░████████░░░█
░░░░░░░░░░░░░░░░█░░░░░█░░░░░░░░░░░░█
░░░░░░░░░░░░░░░░░░░░██░░░░█░░░░░░██
░░░░░░░░░░░░░░░░░░██░░░░░░░███████
░░░░░░░░░░░░░░░░██░░░░░░░░░░█░░░░░█
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
░░░░░░░░░░░█████████░░░░░░░░░░░░░░██
░░░░░░░░░░█▒▒▒▒▒▒▒▒███████████████▒▒█
░░░░░░░░░█▒▒███████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█
░░░░░░░░░█▒▒▒▒▒▒▒▒▒█████████████████
░░░░░░░░░░████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█
░░░░░░░░░░░░░░░░░░██████████████████
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
██░░░░░░░░░░░░░░░░░░░░░░░░░░░██
▓██░░░░░░░░░░░░░░░░░░░░░░░░██
▓▓▓███░░░░░░░░░░░░░░░░░░░░█
▓▓▓▓▓▓███░░░░░░░░░░░░░░░██
▓▓▓▓▓▓▓▓▓███████████████▓▓█
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓██
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█
▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█
*/
}
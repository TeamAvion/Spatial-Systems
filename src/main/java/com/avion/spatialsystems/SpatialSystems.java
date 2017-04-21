package com.avion.spatialsystems;

import com.avion.spatialsystems.blocks.ModBlocks;
import com.avion.spatialsystems.gui.GUIHandler;
import com.avion.spatialsystems.net.ChestMessage;
import com.avion.spatialsystems.net.ChestMessageHandler;
import com.avion.spatialsystems.net.CommonProxy;
import com.avion.spatialsystems.util.LogHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
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

        // Crafting
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(3, 3, new ItemStack[]{
                ItemStack.EMPTY, new ItemStack(Blocks.WOODEN_BUTTON), ItemStack.EMPTY,
                new ItemStack(Blocks.WOODEN_BUTTON), new ItemStack(Blocks.WOODEN_BUTTON), new ItemStack(Blocks.WOODEN_BUTTON),
                ItemStack.EMPTY, new ItemStack(Blocks.WOODEN_BUTTON), ItemStack.EMPTY
        }, new ItemStack(ModBlocks.advancedChestBlock)));
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(3, 3, new ItemStack[]{
                new ItemStack(ModBlocks.advancedChestBlock), new ItemStack(ModBlocks.advancedChestBlock), new ItemStack(ModBlocks.advancedChestBlock),
                new ItemStack(ModBlocks.advancedChestBlock), new ItemStack(Blocks.CHEST), new ItemStack(ModBlocks.advancedChestBlock),
                new ItemStack(ModBlocks.advancedChestBlock), new ItemStack(ModBlocks.advancedChestBlock), new ItemStack(ModBlocks.advancedChestBlock)
        }, new ItemStack(ModBlocks.advancedChestController)));
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(3, 3, new ItemStack[]{
                ItemStack.EMPTY, new ItemStack(Blocks.STONE_BUTTON), ItemStack.EMPTY,
                new ItemStack(Blocks.STONE_BUTTON), new ItemStack(Blocks.STONE_BUTTON), new ItemStack(Blocks.STONE_BUTTON),
                ItemStack.EMPTY, new ItemStack(Blocks.STONE_BUTTON), ItemStack.EMPTY
        }, new ItemStack(ModBlocks.advancedFurnaceBlock)));
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(3, 3, new ItemStack[]{
                new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock),
                new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(Items.GOLD_INGOT), new ItemStack(ModBlocks.advancedFurnaceBlock),
                new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock)
        }, new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1)));
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(3, 3, new ItemStack[]{
                new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1), new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1), new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1),
                new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1), new ItemStack(Items.DIAMOND), new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1),
                new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1), new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1), new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 1)
        }, new ItemStack(ModBlocks.advancedFurnaceBlock, 8, 2)));
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(3, 3, new ItemStack[]{
                new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock),
                new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(Blocks.FURNACE), new ItemStack(ModBlocks.advancedFurnaceBlock),
                new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock), new ItemStack(ModBlocks.advancedFurnaceBlock)
        }, new ItemStack(ModBlocks.advancedFurnaceController)));
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
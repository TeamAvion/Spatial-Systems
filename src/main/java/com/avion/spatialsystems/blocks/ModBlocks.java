package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.automation.Automate;
import com.avion.spatialsystems.automation.Infer;
import com.avion.spatialsystems.automation.NoMeta;
import com.avion.spatialsystems.automation.NoTile;
import com.avion.spatialsystems.items.ItemBlockMeta;
import com.avion.spatialsystems.misc.EnumLevel;
import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.tile.TileAdvancedChestBlock;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

//Created by Bread10 at 08:23 on 15/04/2017
public class ModBlocks {

    public static @Automate(itemBlock = ItemBlockMeta.class, meta = EnumLevel.class)            AdvancedFurnaceBlock advancedFurnaceBlock;
    public static @Automate(tile = TileAdvancedFurnace.class, name = "Advanced Furnace")        AdvancedFurnaceController advancedFurnaceController;
    public static @Automate(tile = TileAdvancedChest.class, name = "Advanced Chest")            AdvancedChestController advancedChestController;
    public static @Automate(tile = TileAdvancedChestBlock.class, name = "Advanced Chest Block") AdvancedChestBlock advancedChestBlock;

    public static void register() {
        // Automation
        Automate a;
        for(Field f : ModBlocks.class.getDeclaredFields())
            if((a=f.getAnnotation(Automate.class))!=null && Modifier.isStatic(f.getModifiers()))
                try { // I honestly can't be bothered to check for faulty stuff. It'll solve itself. If it doesn't, it's the programmer's fault :)
                    Object o = (a.type()==Infer.class?f.getType():a.type()).newInstance(); // Get class to instantiate and instantiate
                    f.set(null, o); // Set field
                    GameRegistry.register((IForgeRegistryEntry) o); // Register
                    if(o instanceof Block) //noinspection unchecked
                        GameRegistry.register(a.itemBlock().getDeclaredConstructor(Block.class).newInstance(o), ((Block) o).getRegistryName() ); // Register ItemBlock if it's a block
                    if(a.tile()!=NoTile.class) //noinspection unchecked
                        GameRegistry.registerTileEntity(a.tile(), a.name()); // Register associated tile
                }catch(Exception e){ e.printStackTrace(); }
    }

    public static void registerRenders() {
        Automate a;
        for(Field f : ModBlocks.class.getDeclaredFields())
            if((a=f.getAnnotation(Automate.class))!=null && Block.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers()))
                try{
                    if(a.meta()!=NoMeta.class)
                        for(Enum<?> e : (Enum<?>[]) a.meta().getDeclaredMethod("values").invoke(null))
                            registerRender((Block)f.get(null), e.ordinal());
                }catch(Exception e){ e.printStackTrace(); }
            else try{ registerRender((Block)f.get(null), 0); }catch(Exception e){ e.printStackTrace(); }

        /*
        for (EnumLevel level : EnumLevel.values()) {
            registerRender(advancedFurnaceBlock, level.getValue());
        }

        registerRender(advancedChestController);
        registerRender(advancedChestBlock);

        registerRender(advancedFurnaceBlock);
        registerRender(advancedChestBlock);
        */
    }

    private static void registerRender(Block block) { registerRender(block, 0); }

    private static void registerRender(Block block, int meta) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}

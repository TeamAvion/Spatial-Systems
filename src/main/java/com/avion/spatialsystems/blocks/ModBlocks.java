package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.items.ItemBlockMeta;
import com.avion.spatialsystems.misc.EnumLevel;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

//Created by Bread10 at 08:23 on 15/04/2017
public class ModBlocks {

    public static Block advancedFurnaceBlock;

    public static void register() {
        GameRegistry.register(advancedFurnaceBlock = new AdvancedFurnaceBlock());
        GameRegistry.register(new ItemBlockMeta(advancedFurnaceBlock), advancedFurnaceBlock.getRegistryName());
    }

    public static void registerRenders() {
        for (EnumLevel level : EnumLevel.values()) {
            registerRender(advancedFurnaceBlock, level.getValue());
        }
    }

    private static void register(Block block) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block), block.getRegistryName());
    }

    private static void registerRender(Block block) {
        registerRender(block, 0);
    }

    private static void registerRender(Block block, int meta) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}

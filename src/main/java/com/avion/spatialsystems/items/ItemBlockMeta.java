package com.avion.spatialsystems.items;

import com.avion.spatialsystems.misc.EnumLevel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

//Created by Bread10 at 08:41 on 15/04/2017
public class ItemBlockMeta extends ItemBlock {

    public ItemBlockMeta(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + EnumLevel.getLevelFromValue(stack.getMetadata()).getName();
    }

}

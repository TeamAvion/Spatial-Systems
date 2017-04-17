package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.SpatialSystems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

//Created by Bread10 at 09:48 on 15/04/2017
public class AdvancedChestBlock extends Block {

    public AdvancedChestBlock() {
        super(Material.WOOD);
        this.setUnlocalizedName("advancedChestBlock");
        this.setRegistryName("advancedchestblock");
        this.setCreativeTab(SpatialSystems.TAB);
        this.setHarvestLevel("axe", 1);
        this.setHardness(1F);
    }

}

package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.automation.Automate;
import com.avion.spatialsystems.automation.Infer;
import com.avion.spatialsystems.automation.NoMeta;
import com.avion.spatialsystems.automation.NoTile;
import com.avion.spatialsystems.items.ItemBlockMeta;
import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import com.avion.spatialsystems.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static com.avion.spatialsystems.util.MBStruct.WLD;

public class ModBlocks {

    public static @Automate(itemBlock = ItemBlockMeta.class, meta = EnumLevel.class)            AdvancedFurnaceBlock advancedFurnaceBlock;
    public static @Automate(tile = TileAdvancedFurnace.class, name = "Advanced Furnace")        AdvancedFurnaceController advancedFurnaceController;
    public static @Automate(tile = TileAdvancedChest.class, name = "Advanced Chest")            AdvancedChestController advancedChestController;
    public static @Automate                                                                     AdvancedChestBlock advancedChestBlock;
    public static final char PMC = 'b';
    public static final MBStruct chestMultiBlock = new MBStruct()
            .addLayer(0, new MBStruct.Plane(1, 1).addPlane(PMC, 3, 3).replace(WLD, 1, 1)) // Layer 0
            .addLayer(1, new MBStruct.Plane(1, 1).addPlane(PMC, 3, 3)) // Layer 1
            .addLayer(2, 1) // Layer 2
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedChestBlock"))
            .setStrict(true);
    // Exactly the same as above except different mapping for primary mapping character
    public static final MBStruct furnaceMultiBlock = chestMultiBlock.copy().registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock"));
    public static final MBStruct chestMultiBlockBig = new MBStruct()
            .addLayer(0, new MBStruct.Plane(3, 3).addPlane(PMC, 5, 5).replace(WLD, 2, 2))
            .addLayers(1, 4, new MBStruct.Plane(3, 3).addPlane(PMC, 5, 5))
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedChestBlock"))
            .setStrict(true);
    public static final MBStruct furnaceMultiBlockBig = chestMultiBlockBig.copy().registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock"));
    public static final MBStruct chestMultiBlockGrand = new MBStruct()
            .addLayer(0, new MBStruct.Plane(4, 4).addPlane(PMC, 7, 7).replace(WLD, 3, 3))
            .addLayers(1, 6, new MBStruct.Plane(3, 3).addPlane(PMC, 7, 7))
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedChestBlock"))
            .setStrict(true);
    public static final MBStruct furnaceMultiBlockGrand = chestMultiBlockGrand.copy().registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock"));
    public static final DynamicMBStruct dynamicFurnace = new DynamicMBStruct().add(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock")).setStrict(true);


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
                if(a.meta()!=NoMeta.class)
                    try{ for(Enum<?> e : (Enum<?>[]) a.meta().getDeclaredMethod("values").invoke(null)) registerRender((Block)f.get(null), e.ordinal()); }catch(Exception e){ e.printStackTrace(); }
                else try{ registerRender((Block)f.get(null), 0); }catch(Exception e){ e.printStackTrace(); }
    }

    private static void registerRender(Block block, int meta) {
        if(block.getRegistryName()==null) throw new AssertionError("Block \""+block.getUnlocalizedName()+"\" has no registry name!");
        try{
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }catch(Exception e){ e.printStackTrace(); }
    }

}

package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.automation.Automate;
import com.avion.spatialsystems.automation.Infer;
import com.avion.spatialsystems.automation.NoMeta;
import com.avion.spatialsystems.automation.NoTile;
import com.avion.spatialsystems.items.ItemBlockMeta;
import com.avion.spatialsystems.tile.*;
import com.avion.spatialsystems.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static com.avion.spatialsystems.util.MBStruct.WLD;

public class ModBlocks {

    // Auto-injected objects
    public static @Automate(itemBlock = ItemBlockMeta.class, meta = EnumLevel.class, tile = TileFurnaceBinder.class, tileName = "Furnace Binder")   AdvancedFurnaceBlock        advancedFurnaceBlock;
    public static @Automate(tile = TileAdvancedFurnace.class, tileName = "Advanced Furnace")                                                        AdvancedFurnaceController   advancedFurnaceController;
    public static @Automate(tile = TileAdvancedChest.class, tileName = "Advanced Chest")                                                            AdvancedChestController     advancedChestController;
    public static @Automate(tile = TileChestBinder.class, tileName = "Chest Binder")                                                                AdvancedChestBlock          advancedChestBlock;

    public static final char PMC = 'b'; // Primary mapping character

    private static final MBStruct.MBPredicate tilePred = new MBStruct.MBPredicate() {
        @Override
        public boolean apply(IBlockAccess w, BlockPos p, BlockPos source) {
            TileEntity e;
            return this.matchesDefault() && ((e=w.getTileEntity(p))==null || (e instanceof BoundTile && ((BoundTile) e).isBound(source)));
        }
    };
    public static final MBStruct chestMultiBlock = new MBStruct()
            .addLayer(0, new MBStruct.Plane(1, 1).addPlane(PMC, 3, 3).replace(WLD, 1, 1)) // Layer 0
            .addLayer(1, new MBStruct.Plane(1, 1).addPlane(PMC, 3, 3).replace(WLD, 1, 1)) // Layer 1
            .addLayer(2, new MBStruct.Plane(1, 1).addPlane(PMC, 3, 3)) // Layer 2
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedChestBlock"))
            .setStrict(true);
    // Exactly the same as above except different mapping for primary mapping character
    public static final MBStruct furnaceMultiBlock = chestMultiBlock
            .copy()
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock"))
            .registerCustomBlockHandler(FieldReference.<Block>staticFromHere("advancedFurnaceBlock"), tilePred);
    public static final MBStruct chestMultiBlockBig = new MBStruct()
            .addLayer(0, new MBStruct.Plane(2, 2)
                    .addPlane(PMC, 5, 5)
                    .replace(WLD, 2, 2)
            )
            .addLayers(1, 3, new MBStruct.Plane(2, 2)
                    .addPlane(PMC, 5, 5)
                    .graftPlane(WLD, 3, 3, 1, 1)
            )
            .addLayer(4, new MBStruct.Plane(2, 2)
                    .addPlane(PMC, 5, 5)
            )
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedChestBlock"))
            .setStrict(true)
            .registerCustomBlockHandler(FieldReference.<Block>staticFromHere("advancedChestBlock"), tilePred);
    public static final MBStruct furnaceMultiBlockBig = chestMultiBlockBig.copy().registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock"))
            .registerCustomBlockHandler(FieldReference.<Block>staticFromHere("advancedFurnaceBlock"), tilePred);
    public static final MBStruct chestMultiBlockGrand = new MBStruct()
            .addLayer(0, new MBStruct.Plane(3, 3)
                    .addPlane(PMC, 7, 7)
                    .replace(WLD, 3, 3)
            )
            .addLayers(1, 5, new MBStruct.Plane(3, 3)
                    .addPlane(PMC, 7, 7)
                    .graftPlane(WLD, 5, 5, 1, 1)
            )
            .addLayer(6, new MBStruct.Plane(3, 3)
                    .addPlane(PMC, 7, 7)
            )
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedChestBlock"))
            .setStrict(true)
            .registerCustomBlockHandler(FieldReference.<Block>staticFromHere("advancedChestBlock"), tilePred);
    public static final MBStruct furnaceMultiBlockGrand = chestMultiBlockGrand.copy()
            .registerLazyMapping(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock"))
            .registerCustomBlockHandler(FieldReference.<Block>staticFromHere("advancedFurnaceBlock"), tilePred);
    // 1,352 is the outer block count of a 16*16*16 cube (16*16*16 - 14*14*14)
    public static final DynamicMBStruct dynamicFurnace = new DynamicMBStruct().add(PMC, FieldReference.<Block>staticFromHere("advancedFurnaceBlock")).setStrict(true).setMaxSize(1352);


    public static void register() {
        // Automatic object injection:
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
                        GameRegistry.registerTileEntity(a.tile(), a.tileName()); // Register associated tile
                }catch(Exception e){ e.printStackTrace(); }

        // Manual object instantiation:

    }

    public static void registerRenders() {
        // Automatic renderer registering:
        Automate a;
        for(Field f : ModBlocks.class.getDeclaredFields())
            if((a=f.getAnnotation(Automate.class))!=null && Block.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers()))
                if(a.meta()!=NoMeta.class)
                    try{ for(Enum<?> e : (Enum<?>[]) a.meta().getDeclaredMethod("values").invoke(null)) registerRender((Block)f.get(null), e.ordinal()); }catch(Exception e){ e.printStackTrace(); }
                else try{ registerRender((Block)f.get(null), 0); }catch(Exception e){ e.printStackTrace(); }

        // Manual renderer registering:

    }

    private static void registerRender(Block block, int meta) {
        if(block.getRegistryName()==null) throw new AssertionError("Block \""+block.getUnlocalizedName()+"\" has no registry tileName!");
        try{
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }catch(Exception e){ e.printStackTrace(); }
    }

}

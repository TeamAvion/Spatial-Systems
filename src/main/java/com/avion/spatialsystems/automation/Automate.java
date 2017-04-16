package com.avion.spatialsystems.automation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Automate {
    Class tile() default NoTile.class;
    String name() default "ERROR";
    Class type() default Infer.class;
    Class<? extends IForgeRegistryEntry> itemBlock() default ItemBlock.class;
    Class<? extends Enum> meta() default NoMeta.class;
}
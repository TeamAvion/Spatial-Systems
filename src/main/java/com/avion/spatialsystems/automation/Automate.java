package com.avion.spatialsystems.automation;
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Automate {
    Class tile() default NoTile.class;
    String tileName() default "ERROR";
    Class type() default Infer.class;
    Class<? extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry> itemBlock() default net.minecraft.item.ItemBlock.class;
    Class<? extends Enum> meta() default NoMeta.class;
}
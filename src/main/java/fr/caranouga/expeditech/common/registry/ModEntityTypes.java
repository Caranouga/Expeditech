package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.entities.TechLevelExperienceOrb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Expeditech.MODID);

    public static final RegistryObject<EntityType<TechLevelExperienceOrb>> TECH_LEVEL_XP_ORB = ENTITY_TYPES.register(
            "tech_level_xp_orb",
            () -> EntityType.Builder.of(TechLevelExperienceOrb::new, EntityClassification.MISC)
                    .sized(.5F, .5F).build(modLocation("tech_level_xp_orb").toString()));
    /*public static final RegistryObject<EntityType<TechLevelExperienceOrb>> TECH_LEVEL_XP_ORB = register(
            "tech_level_xp_orb",
            EntityType.Builder.of(TechLevelExperienceOrb::new, EntityClassification.MISC).sized(.5F, .5F)
    );*/

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.Builder<T> entityType){
        return ENTITY_TYPES.register(id, () -> entityType.build(modLocation(id).toString()));
    }

    public static void register(IEventBus eBus){
        ENTITY_TYPES.register(eBus);
    }
}

package fr.caranouga.expeditech.items;

import fr.caranouga.expeditech.recipes.SandingRecipe;
import fr.caranouga.expeditech.registry.ModRecipes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class SandingPaperItem extends Item {
    public SandingPaperItem(Properties properties) {
        super(properties.durability(16)/*.setISTER(() -> SandingPaperRenderer::new)*/);
    }

    @Override
    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);

        if(stack.getOrCreateTag().contains("Sanding")){
            pPlayer.startUsingItem(pHand);
            return new ActionResult<>(ActionResultType.PASS, stack);
        }

        Hand otherHand = pHand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack otherStack = pPlayer.getItemInHand(otherHand);
        if(canSand(otherStack, pLevel)) {
            ItemStack item = otherStack.copy();
            ItemStack toSand = item.split(1);
            pPlayer.startUsingItem(pHand);
            stack.getOrCreateTag().put("Sanding", toSand.serializeNBT());
            pPlayer.setItemInHand(otherHand, item);

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, World pLevel, LivingEntity pEntityLiving) {
        if(!(pEntityLiving instanceof PlayerEntity)) {
            return pStack;
        }

        PlayerEntity player = (PlayerEntity) pEntityLiving;
        CompoundNBT tag = pStack.getOrCreateTag();
        if(tag.contains("Sanding")) {
            ItemStack sanding = ItemStack.of(tag.getCompound("Sanding"));
            ItemStack sanded = applySanding(sanding, pLevel);

            if(pLevel.isClientSide()){
                spawnParticles(pEntityLiving.getEyePosition(1).add(pEntityLiving.getLookAngle().scale(.5f)), sanding, pLevel);

                return pStack;
            }

            if(!sanded.isEmpty()) {
                player.inventory.placeItemBackInInventory(pLevel, sanded);
            }

            tag.remove("Sanding");
            pStack.hurtAndBreak(1, pEntityLiving, p -> p.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));
        }

        return pStack;
    }

    @Override
    public void releaseUsing(ItemStack pStack, World pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if(!(pEntityLiving instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) pEntityLiving;
        CompoundNBT tag = pStack.getOrCreateTag();
        if(tag.contains("Sanding")) {
            ItemStack sanding = ItemStack.of(tag.getCompound("Sanding"));
            player.inventory.placeItemBackInInventory(pLevel, sanding);
            tag.remove("Sanding");
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // TODO: CHeck l'utilitié de cette méthode
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        // TODO: CHeck l'utilitié de cette méthode
        return 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 5;
    }

    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        return ActionResultType.PASS;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.EAT;
    }






    private boolean canSand(ItemStack stack, World world) {
        return getSandingRecipe(stack, world).isPresent();
    }

    private Optional<SandingRecipe> getSandingRecipe(ItemStack stack, World world) {
        Inventory inv = new Inventory(1);
        inv.setItem(0, stack);

        return world.getRecipeManager().getRecipeFor(ModRecipes.SANDING_RECIPE, inv, world);
    }

    private ItemStack applySanding(ItemStack stack, World world) {
        Optional<SandingRecipe> recipe = getSandingRecipe(stack, world);
        if(recipe.isPresent()) {
            ItemStack result = recipe.get().getResultItem();
            return result.copy();
        } else {
            return ItemStack.EMPTY;
        }
    }

    private void spawnParticles(Vector3d location, ItemStack stack, World world) {
        for (int i = 0; i < 20; i++) {
            Vector3d motion = offsetRandomly(world.random);
            world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), location.x, location.y,
                    location.z, motion.x, motion.y, motion.z);
        }
    }

    private Vector3d offsetRandomly(Random r) {
        float radius = 1 / 8f;
        return new Vector3d((r.nextFloat() - .5f) * 2 * radius, (r.nextFloat() - .5f) * 2 * radius,
                (r.nextFloat() - .5f) * 2 * radius);
    }
}

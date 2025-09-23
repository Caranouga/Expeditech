package fr.caranouga.expeditech.common.entities;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.capability.techlevel.TechLevelUtils;
import fr.caranouga.expeditech.common.packets.SpawnTechLevelExperienceOrbPacket;
import fr.caranouga.expeditech.common.registry.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TechLevelExperienceOrb extends Entity {
    public int tickCount;
    public int age;
    public int throwTime;
    private int health = 5;
    public int value;
    private PlayerEntity followingPlayer;
    private int followingTime;

    private TechLevelExperienceOrb(World pLevel, double pX, double pY, double pZ, int pValue) {
        this(ModEntityTypes.TECH_LEVEL_XP_ORB.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.yRot = (float)(this.random.nextDouble() * 360.0D);
        this.setDeltaMovement((this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D);
        this.value = pValue;
    }

    public TechLevelExperienceOrb(EntityType<TechLevelExperienceOrb> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public static TechLevelExperienceOrb createEntity(World pLevel, double pX, double pY, double pZ, int pValue){
        return new TechLevelExperienceOrb(pLevel, pX, pY, pZ, pValue);
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.throwTime > 0) {
            --this.throwTime;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }

        if (this.level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), (double)0.2F, (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
            this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        if (!this.level.noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
        }

        double d0 = 8.0D;
        if (this.followingTime < this.tickCount - 20 + this.getId() % 100) {
            if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 64.0D) {
                this.followingPlayer = this.level.getNearestPlayer(this, 8.0D);
            }

            this.followingTime = this.tickCount;
        }

        if (this.followingPlayer != null && this.followingPlayer.isSpectator()) {
            this.followingPlayer = null;
        }

        if (this.followingPlayer != null) {
            Vector3d vector3d = new Vector3d(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double)this.followingPlayer.getEyeHeight() / 2.0D - this.getY(), this.followingPlayer.getZ() - this.getZ());
            double d1 = vector3d.lengthSqr();
            if (d1 < 64.0D) {
                double d2 = 1.0D - Math.sqrt(d1) / 8.0D;
                this.setDeltaMovement(this.getDeltaMovement().add(vector3d.normalize().scale(d2 * d2 * 0.1D)));
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround) {
            BlockPos pos =new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
            f = this.level.getBlockState(pos).getSlipperiness(this.level, pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double)f, 0.98D, (double)f));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
        }

        ++this.tickCount;
        ++this.age;
        if (this.age >= 6000) {
            this.remove();
        }

    }

    private void setUnderwaterMovement() {
        Vector3d vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x * (double)0.99F, Math.min(vector3d.y + (double)5.0E-4F, (double)0.06F), vector3d.z * (double)0.99F);
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.level.isClientSide || this.removed) return false;
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.markHurt();
            this.health = (int)((float)this.health - pAmount);
            if (this.health <= 0) {
                this.remove();
            }

            return false;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putShort("Health", (short)this.health);
        pCompound.putShort("Age", (short)this.age);
        pCompound.putShort("Value", (short)this.value);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT pCompound) {
        this.health = pCompound.getShort("Health");
        this.age = pCompound.getShort("Age");
        this.value = pCompound.getShort("Value");
    }

    @Override
    public void playerTouch(PlayerEntity pEntity) {
        if (!this.level.isClientSide) {
            if (this.throwTime == 0 && pEntity.takeXpDelay == 0) {
                //if (MinecraftForge.EVENT_BUS.post(new PlayerXpEvent.PickupXp(pEntity, this))) return;
                pEntity.takeXpDelay = 2;
                pEntity.take(this, 1);

                Expeditech.LOGGER.debug("{}", this.value);

                if (this.value > 0) {
                    TechLevelUtils.addTechXp(pEntity, this.value);
                }

                this.remove();
            }

        }
    }

    public int getValue() {
        return this.value;
    }

    /**
     * This functions return the texture icon's idx based on the value of this orb
     * @return the texture icon's idx based on the value of this orb
     */
    @OnlyIn(Dist.CLIENT)
    public int getIcon() {
        if (this.value >= 2477) {
            return 10;
        } else if (this.value >= 1237) {
            return 9;
        } else if (this.value >= 617) {
            return 8;
        } else if (this.value >= 307) {
            return 7;
        } else if (this.value >= 149) {
            return 6;
        } else if (this.value >= 73) {
            return 5;
        } else if (this.value >= 37) {
            return 4;
        } else if (this.value >= 17) {
            return 3;
        } else if (this.value >= 7) {
            return 2;
        } else {
            return this.value >= 3 ? 1 : 0;
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    // CHANGE
    public IPacket<?> getAddEntityPacket() {
        return new SpawnTechLevelExperienceOrbPacket(this);
    }
}

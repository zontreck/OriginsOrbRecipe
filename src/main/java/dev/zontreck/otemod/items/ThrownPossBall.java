package dev.zontreck.otemod.items;

import dev.zontreck.libzontreck.lore.ExtraLore;
import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;
import java.util.UUID;

public class ThrownPossBall extends ThrowableItemProjectile
{
    boolean captured = false;
    LivingEntity shooter;
    public ThrownPossBall(EntityType<? extends ThrownPossBall> entity, Level level)
    {
        super(entity, level);
    }
    public ThrownPossBall(Level level, LivingEntity shooter)
    {
        super(EntityType.SNOWBALL, shooter, level);

        this.shooter = shooter;
    }

    public ThrownPossBall(Level pLevel, double pX, double pY, double pZ)
    {
        super(EntityType.SNOWBALL, pX, pY, pZ, pLevel);
    }

    @Override
    protected Item getDefaultItem()
    {
        return ModItems.POSS_BALL.get();
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if(pId == 3)
        {
            double size = 0.08;

            for (int i = 0; i < 8; ++i)
            {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if(getItem().getTag().contains("entity"))
        {
            // Don't capture the entity

            pResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.1F);
        } else {
            if(pResult.getEntity() instanceof LivingEntity le && !(le instanceof Player))
            {
                // We don't want to capture players
                // Store the entity in the entity tag, then kill the entity
                CompoundTag tag = new CompoundTag();
                String entityName = le.getName().getString();
                le.save(tag);

                getItem().getTag().put("entity", tag);
                captured=true;

                LoreContainer cont = new LoreContainer(getItem());
                cont.miscData.LoreData.clear();
                LoreEntry entry = new LoreEntry();
                entry.bold = true;
                entry.text = dev.zontreck.libzontreck.chat.ChatColor.doColors("!Dark_Green!Captured Mob: !Dark_Purple!" + entityName);

                cont.miscData.LoreData.add(entry);

                cont.commitLore();

                le.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if(!this.level().isClientSide)
        {
            // We do two things here

            // 1. If we contain an entity, spawn it
            // 2. If no entity, and none was captured, decrease the durability a little
            // 3. Drop the PossBall with entity, or without

            ItemStack item = getItem();
            CompoundTag tag = item.getTag();
            if(tag.contains("entity"))
            {
                if(captured)
                {
                    // Spawn poss ball item with the entity NBT
                    ItemEntity entity;
                    if(shooter != null)
                        entity = new ItemEntity(level(), shooter.position().x, shooter.position().y, shooter.position().z, item, 0, 0, 0);
                    else
                        entity = new ItemEntity(level(), shooter.position().x, shooter.position().y, shooter.position().z, item, 0, 0, 0);
                    level().addFreshEntity(entity);
                } else {
                    // Spawn the real entity
                    Optional<Entity> entity = EntityType.create(tag.getCompound("entity"), level());
                    if(entity.isPresent())
                    {
                        Entity xEntity = entity.get();
                        xEntity.setUUID(UUID.randomUUID());
                        xEntity.setPos(position());
                        level().addFreshEntity(xEntity);
                    }

                    LoreContainer cont = new LoreContainer(item);
                    cont.miscData.LoreData.clear();
                    cont.commitLore();

                    if(item.getDamageValue() == 0)
                    {
                        item.setTag(new CompoundTag());
                    }else {
                        tag.remove("entity");
                    }

                    item = new ItemStack(ModItems.POSS_BALL.get(), 1);
                    ItemEntity x;

                    if(shooter!=null)
                        x = new ItemEntity(level(), shooter.position().x, shooter.position().y, shooter.position().z, item, 0, 0, 0);
                    else
                        x = new ItemEntity(level(), position().x, position().y, position().z, item, 0, 0, 0);
                    level().addFreshEntity(x);
                }
            } else {
                // No capture
                // Decrease the durability
                /*
                int damage = item.getDamageValue();
                damage++;
                item.setDamageValue(damage);*/

                item = new ItemStack(ModItems.POSS_BALL.get(), 1);


                // Ensure no entity tag!
                //tag.remove("entity");


                /*
                if(item.getDamageValue() >= item.getMaxDamage())
                    return;
                */

                ItemEntity entity;

                if(shooter!= null)
                    entity = new ItemEntity(level(),shooter.position().x, shooter.position().y, shooter.position().z, item, 0, 0, 0);
                else

                    entity = new ItemEntity(level(), position().x, position().y, position().z, item, 0, 0, 0);
                level().addFreshEntity(entity);
            }

            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();

        }
    }
}

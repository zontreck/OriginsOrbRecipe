package dev.zontreck.otemod.items;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.otemod.blocks.entity.ModEntities;
import dev.zontreck.otemod.entities.ModEntityTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;
import java.util.UUID;

public class ThrownMobCaptureBall extends ThrowableItemProjectile
{
    boolean captured = false;
    LivingEntity shooter;
    ItemStack self;

    public ThrownMobCaptureBall(EntityType entity, Level level)
    {
        super(entity, level);
    }
    public ThrownMobCaptureBall(Level level, LivingEntity shooter, ItemStack item)
    {
        super(EntityType.EGG, shooter, level);

        this.shooter = shooter;
        if(item.getTag() == null)
        {
            item.setTag(new CompoundTag());
        }
        self=item;
    }

    @Override
    protected Item getDefaultItem()
    {
        return ModItems.MIAB.get();
    }

    void returnBall()
    {

        ItemEntity x;

        if(shooter!=null)
            x = new ItemEntity(level(), shooter.position().x, shooter.position().y, shooter.position().z, self, 0, 0, 0);
        else
            x = new ItemEntity(level(), position().x, position().y, position().z, self, 0, 0, 0);
        level().addFreshEntity(x);
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
        if(ServerUtilities.isServer())
        {
            CompoundTag tag = self.getTag();
            if(tag == null)tag = new CompoundTag();
            if(tag.contains(ItemStack.TAG_DAMAGE))
            {
                tag.remove(ItemStack.TAG_DAMAGE);
            }


            if(self.getTag() == null || !self.getTag().contains("entity"))
            {
            /*

            Ensure the tag is not null

             */

                if(pResult.getEntity() instanceof LivingEntity le && !(le instanceof Player player))
                {
                    // We don't want to capture players
                    // Store the entity in the entity tag, then kill the entity


                    CompoundTag store = new CompoundTag();
                    String entityName = le.getName().getString();
                    le.save(store);

                    self.getTag().put("entity", store);
                    self.setCount(1);
                    captured=true;

                    LoreContainer cont = new LoreContainer(self);
                    cont.miscData.loreData.clear();
                    LoreEntry entry = new LoreEntry.Builder().bold(true).text(ChatColor.doColors("!Dark_Green!Captured Mob: !Dark_Purple!" + entityName)).build();
                    cont.miscData.loreData.add(entry);

                    cont.commitLore();

                    le.remove(RemovalReason.DISCARDED);
                }
            } else {

                // Don't capture the entity

                //pResult.getEntity().hurt(this.shooter.getLastDamageSource().thrown(this, this.getOwner()), 0.1F);
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        if(ServerUtilities.isServer())
        {
            // We do two things here

            // 1. If we contain an entity, spawn it
            // 2. If no entity, and none was captured, decrease the durability a little
            // 3. Drop the PossBall with entity, or without

            ItemStack item = self;
            CompoundTag tag = item.getTag();

            if(tag==null)tag=new CompoundTag();

            if(tag.contains(ItemStack.TAG_DAMAGE))
            {
                tag.remove(ItemStack.TAG_DAMAGE); // Migrate existing poss balls to remove the obsolete damage tag
            }

            if(tag.contains("entity"))
            {
                if(captured)
                {
                    // Spawn poss ball item with the entity NBT
                    returnBall();
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
                    cont.miscData.loreData.clear();
                    cont.commitLore();

                    if(item.getDamageValue() == 0)
                    {
                        item.setTag(new CompoundTag());
                    }else {
                        tag.remove("entity");
                    }


                    returnBall();
                }
            } else {
                // No capture
                // Decrease the durability
                //int damage = item.getDamageValue();
                //damage++;
                //item.setDamageValue(damage);
                // Ensure no entity tag!
                tag.remove("entity");

                if(tag.size()==0)
                {
                    tag=null;
                    item.setTag(new CompoundTag());
                }


                //if(item.getDamageValue() >= item.getMaxDamage())
                //    return;


                returnBall();
            }

            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();

        }
    }
}

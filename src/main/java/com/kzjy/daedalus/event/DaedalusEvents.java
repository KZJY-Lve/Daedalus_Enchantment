package com.kzjy.daedalus.event;

import com.kzjy.daedalus.Daedalus;
import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.duck.IDaedalusDamageSource;
import com.kzjy.daedalus.duck.IDaedalusLivingEvent;
import com.kzjy.daedalus.registry.DaedalusRegistries;
import com.kzjy.daedalus.mixin.ZombieVillagerInvoker;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Kzjy<br>
 * 代达罗斯模组的核心事件处理类<br>
 * 涵盖了所有附魔的逻辑实现，包括攻击流程、伤害计算、死亡处理及 Tick 更新
 */
@Mod.EventBusSubscriber(modid = Daedalus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DaedalusEvents {
    private static final Random RANDOM = new Random();

    // 辅助：获取理论面板伤害
    private static float calculateBaseDamage(LivingEntity attacker, LivingEntity target) {
        double baseAttributeDamage = attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float enchantmentBonus = EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), target.getMobType());
        if (attacker instanceof Player player) {
            float scale = player.getAttackStrengthScale(0.5f);
            baseAttributeDamage *= (0.2f + scale * scale * 0.8f);
            enchantmentBonus *= scale;
        }
        return (float) baseAttributeDamage + enchantmentBonus;
    }

    private static float getCataclysmDamageCap(LivingEntity entity) {
        try {
            Method method = entity.getClass().getMethod("DamageCap");
            if (method.getReturnType() == float.class) return (float) method.invoke(entity);
        } catch (Exception ignored) {}
        return Float.MAX_VALUE;
    }

    // =================================================================================================
    // 1. 攻击初始化 (Attack Phase)
    // =================================================================================================

    /**
     * 玩家攻击事件覆写<br>
     * 处理虚空破壁附魔的物理攻击破无敌逻辑
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerAttackOverride(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        // 虚空破壁: 物理攻击破无敌
        if (DaedalusConfig.COMMON.voidBreachEnabled.get() &&
                EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_BREACH.get(), stack) > 0) {
            if (event.getTarget() instanceof LivingEntity target) {
                target.invulnerableTime = 0;
                if (target instanceof WitherBoss wither && wither.getInvulnerableTicks() > 0) {
                    wither.setInvulnerableTicks(0);
                }
                if (target instanceof Player tp && tp.getAbilities().invulnerable) {
                    tp.getAbilities().invulnerable = false;
                }
            }
        }
    }

    /**
     * 生物攻击初始化<br>
     * 标记伤害源属性，处理虚空破壁与耀星之噬的预处理逻辑
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttackInit(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            // 虚空破壁: 标记伤害源
            if (DaedalusConfig.COMMON.voidBreachEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_BREACH.get(), weapon) > 0) {
                if (event.getSource() instanceof IDaedalusDamageSource ds) ds.daedalus$setVoidBreach(true);
            }

            // 耀星之噬: 绝对真伤初始化
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), weapon) > 0) {
                if (event.getSource() instanceof IDaedalusDamageSource ds) ds.daedalus$setBypassAll(true);
                // 开启不可取消 + 只能增伤
                if (event instanceof IDaedalusLivingEvent dle) {
                    dle.daedalus$setUncancelable(true);
                    dle.daedalus$setOnlyAmountUp(true);
                }
            }
        }
    }

    /**
     * 强制恢复被取消的 Attack 事件<br>
     * 确保耀星之噬的攻击无法被其他模组取消
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onLivingAttackEnforce(LivingAttackEvent event) {
        if (event.isCanceled() && event.getSource() instanceof IDaedalusDamageSource ds && ds.daedalus$isBypassAll()) {
            event.setCanceled(false);
        }
    }

    // =================================================================================================
    // 2. 伤害计算前置 (Hurt Phase)
    // =================================================================================================

    /**
     * 伤害计算前置处理<br>
     * 再次确认无敌帧移除与事件锁定，防止在 Hurt 阶段被拦截
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurtPre(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            // 虚空破壁
            if (DaedalusConfig.COMMON.voidBreachEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_BREACH.get(), weapon) > 0) {
                event.getEntity().invulnerableTime = 0;
            }

            // 耀星之噬: 再次锁死
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), weapon) > 0) {
                if (event.getSource() instanceof IDaedalusDamageSource ds) ds.daedalus$setBypassAll(true);
                if (event instanceof IDaedalusLivingEvent dle) {
                    dle.daedalus$setUncancelable(true);
                    dle.daedalus$setOnlyAmountUp(true); // 关键：防止 Hurt 阶段减伤
                }
            }
        }
    }

    /**
     * 伤害逻辑计算<br>
     * 处理诅咒锁链、罪孽印记、神圣裁决、灵魂饥渴及生命虹吸的具体效果
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurtLogic(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            // 诅咒锁链
            if (DaedalusConfig.COMMON.cursedChainEnabled.get()) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.CURSED_CHAIN.get(), weapon);
                if (level > 0) {
                    float reduction = 0.5f - (level * 0.05f);
                    float multiplier = 1.0f - reduction;
                    event.setAmount(event.getAmount() * multiplier);

                    float bonusPct = level * 0.1f;
                    float bonusDmg = event.getAmount() * bonusPct;
                    if (bonusDmg > 0) {
                        LivingEntity target = event.getEntity();
                        int oldInv = target.invulnerableTime;
                        target.invulnerableTime = 0;
                        target.hurt(attacker.damageSources().magic(), bonusDmg);
                        target.invulnerableTime = oldInv;
                    }
                }
            }

            // 罪孽印记
            if (DaedalusConfig.COMMON.markOfSinEnabled.get()) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.MARK_OF_SIN_ENCHANT.get(), weapon);
                if (level > 0) {
                    int effLvl = level * DaedalusConfig.COMMON.markOfSinLevelPerLevel.get();
                    event.getEntity().addEffect(new MobEffectInstance(DaedalusRegistries.MARK_OF_SIN.get(), 100, effLvl - 1));
                }
            }

            // 神圣裁决
            if (DaedalusConfig.COMMON.divineJudgmentEnabled.get()) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.DIVINE_JUDGMENT.get(), weapon);
                if (level > 0 && event.getEntity().getMobType() == MobType.UNDEAD) {
                    float bonus = 1.0f + (level * DaedalusConfig.COMMON.divineJudgmentUndeadBonusPerLevel.get().floatValue());
                    event.setAmount(event.getAmount() * bonus);
                    if (event.getEntity() instanceof ZombieVillager zv) {
                        if (RANDOM.nextFloat() < (level * DaedalusConfig.COMMON.divineJudgmentCureChance.get())) {
                            if (!zv.level().isClientSide) ((ZombieVillagerInvoker) zv).invokeStartConverting(attacker.getUUID(), 1);
                        }
                    }
                }
            }

            // 灵魂饥渴 (伤害部分)
            if (DaedalusConfig.COMMON.soulThirstEnabled.get()) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.SOUL_THIRST.get(), weapon);
                if (level > 0) {
                    CompoundTag tag = weapon.getOrCreateTag();
                    int souls = tag.getInt("DaedalusSouls");
                    int maxSouls = DaedalusConfig.COMMON.soulThirstMaxThreshold.get();
                    if (souls >= maxSouls) {
                        float baseMulti = DaedalusConfig.COMMON.soulThirstBaseBonus.get().floatValue();
                        float levelBonus = (level > 1) ? (level - 1) * DaedalusConfig.COMMON.soulThirstBonusPerLevel.get().floatValue() : 0;
                        float totalBonus = baseMulti + levelBonus;
                        float extraDmg = event.getAmount() * totalBonus;

                        LivingEntity target = event.getEntity();
                        int oldInv = target.invulnerableTime;
                        target.invulnerableTime = 0;
                        target.hurt(attacker.damageSources().magic(), extraDmg);
                        target.invulnerableTime = oldInv;
                        tag.putInt("DaedalusSouls", 0);
                    }
                }
            }

            // 生命虹吸
            if (DaedalusConfig.COMMON.lifeSiphonEnabled.get()) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.LIFE_SIPHON.get(), weapon);
                if (level > 0) {
                    float basePct = DaedalusConfig.COMMON.lifeSiphonBaseHeal.get().floatValue();
                    float lvlPct = (level > 1) ? (level - 1) * DaedalusConfig.COMMON.lifeSiphonHealPerLevel.get().floatValue() : 0;
                    attacker.heal(event.getAmount() * (basePct + lvlPct));
                }
            }
        }
    }

    // =================================================================================================
    // 3. 最终伤害结算 (Damage Phase)
    // =================================================================================================

    /**
     * 最终伤害结算初始化<br>
     * 再次锁定耀星之噬与虚空撕裂的伤害数值，防止在 Damage 阶段被修改
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamageInit(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            // 耀星之噬: 再次锁死 (针对 LivingDamageEvent 新对象)
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), weapon) > 0) {
                if (event.isCanceled()) event.setCanceled(false);
                if (event instanceof IDaedalusLivingEvent dle) {
                    dle.daedalus$setUncancelable(true);
                    dle.daedalus$setOnlyAmountUp(true);
                }
            }

            // 虚空撕裂: 开启“只能增伤”锁，保护真伤底线
            else if (DaedalusConfig.COMMON.voidRendEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_REND.get(), weapon) > 0) {
                if (event instanceof IDaedalusLivingEvent dle) {
                    dle.daedalus$setOnlyAmountUp(true);
                }
            }
        }
    }

    /**
     * 最终伤害强制执行<br>
     * 处理耀星之噬的底线伤害、虚空撕裂的斩杀、虚空破壁的伤害上限突破及不朽的动态减伤
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onLivingDamageEnforce(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            LivingEntity target = event.getEntity();

            // 耀星之噬: 强制伤害底线
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), weapon) > 0) {
                if (event.isCanceled()) event.setCanceled(false);
                if (event instanceof IDaedalusLivingEvent dle) {
                    dle.daedalus$setUncancelable(true);
                    dle.daedalus$setOnlyAmountUp(true);
                }
                float rawDamage = calculateBaseDamage(attacker, target);
                if (event.getAmount() < rawDamage) event.setAmount(rawDamage);
                target.invulnerableTime = 0;
            }

            // 虚空撕裂
            if (DaedalusConfig.COMMON.voidRendEnabled.get()) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_REND.get(), weapon);
                if (level > 0) {
                    if (event.getSource() instanceof IDaedalusDamageSource ds) ds.daedalus$setBypassAll(true);

                    float trueDmgPct = level * DaedalusConfig.COMMON.voidRendTrueDamagePercentPerLevel.get().floatValue();
                    float attrDmg = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    float minDmg = attrDmg * trueDmgPct;
                    if (event.getAmount() < minDmg) event.setAmount(minDmg);

                    float hpThreshold = DaedalusConfig.COMMON.voidRendRagnarokThreshold.get().floatValue();
                    if (target.getHealth() / target.getMaxHealth() < hpThreshold) {
                        float chance = level * DaedalusConfig.COMMON.voidRendRagnarokChancePerLevel.get().floatValue();
                        if (RANDOM.nextFloat() < chance) {
                            target.setHealth(0.0f);
                            target.die(attacker.damageSources().genericKill());
                        }
                    }
                }
            }

            // 虚空破壁: 伤害上限突破
            if (EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_BREACH.get(), weapon) > 0) {
                float cap = getCataclysmDamageCap(target);
                if (cap != Float.MAX_VALUE && event.getAmount() > cap) {
                    event.setAmount(cap);
                }
            }
        }

        // 不朽 (Immortal): 动态减伤
        LivingEntity victim = event.getEntity();
        if (DaedalusConfig.COMMON.immortalEnabled.get()) {
            int level = getArmorEnchantmentLevel(victim, DaedalusRegistries.IMMORTAL.get());
            if (level > 0) {
                CompoundTag data = victim.getPersistentData();
                int baseDur = DaedalusConfig.COMMON.immortalBaseDuration.get();
                int durPerLvl = DaedalusConfig.COMMON.immortalDurationPerLevel.get();
                int maxDuration = baseDur + (level * durPerLvl);

                double baseRed = DaedalusConfig.COMMON.immortalBaseReduction.get();
                double redPerLvl = DaedalusConfig.COMMON.immortalReductionPerLevel.get();
                double reductionPct = baseRed + (level * redPerLvl);

                int currentTimer = data.getInt("DaedalusImmortalTimer");
                float currentMultiplier = data.contains("DaedalusImmortalMultiplier") ? data.getFloat("DaedalusImmortalMultiplier") : 1.0f;

                event.setAmount(event.getAmount() * currentMultiplier);

                float nextMultiplier;
                if (currentTimer > 0) {
                    nextMultiplier = currentMultiplier * (float)(1.0 - reductionPct);
                } else {
                    nextMultiplier = (float)(1.0 - reductionPct);
                }
                if (nextMultiplier < 0.01f) nextMultiplier = 0.01f;

                data.putFloat("DaedalusImmortalMultiplier", nextMultiplier);
                data.putInt("DaedalusImmortalTimer", maxDuration);
            }
        }
    }

    // =================================================================================================
    // 4. 死亡事件 (Death Phase)
    // =================================================================================================

    /**
     * 死亡事件初始化<br>
     * 耀星之噬锁定复活，灵魂饥渴收集灵魂
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeathInit(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            // 耀星之噬: 锁死复活
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), weapon) > 0) {
                if (event instanceof IDaedalusLivingEvent dle) dle.daedalus$setUncancelable(true);
            }
        }

        // 灵魂饥渴: 收集灵魂
        if (event.getSource().getEntity() instanceof Player player && DaedalusConfig.COMMON.soulThirstEnabled.get()) {
            ItemStack weapon = player.getMainHandItem();
            int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.SOUL_THIRST.get(), weapon);
            if (level > 0) {
                CompoundTag tag = weapon.getOrCreateTag();
                int current = tag.getInt("DaedalusSouls");
                int gain = (int)(level * DaedalusConfig.COMMON.soulThirstGainPerLevel.get());
                int max = DaedalusConfig.COMMON.soulThirstMaxThreshold.get();
                tag.putInt("DaedalusSouls", Math.min(max, current + gain));
            }
        }
    }

    /**
     * 死亡事件强制执行<br>
     * 确保耀星之噬击杀的目标无法通过事件取消来复活
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onLivingDeathEnforce(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            // 耀星之噬: 强制执行死亡
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), weapon) > 0) {
                if (event.isCanceled()) event.setCanceled(false);
                event.getEntity().setHealth(0.0f);
            }
        }
    }

    // =================================================================================================
    // 5. 治疗事件 (Heal Phase)
    // =================================================================================================

    /**
     * 治疗事件初始化<br>
     * 处理天使的加护带来的治疗增幅及无视禁疗效果
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHealInit(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(DaedalusRegistries.ANGELIC_PROTECTION.get())) {
            int amp = entity.getEffect(DaedalusRegistries.ANGELIC_PROTECTION.get()).getAmplifier();
            int level = amp + 1;

            float originalAmount = event.getAmount();
            float boostedAmount = originalAmount * (1.0f + level);

            // 天使的加护: 无视禁疗
            if (level >= 4) {
                if (event.isCanceled()) event.setCanceled(false);
                if (event instanceof IDaedalusLivingEvent dle) {
                    dle.daedalus$setUncancelable(true);
                    dle.daedalus$setOnlyAmountUp(true);
                }
                if (boostedAmount <= 0.01f) boostedAmount = entity.getMaxHealth() * 0.05f;
            }
            event.setAmount(boostedAmount);
        }
    }

    // =================================================================================================
    // 6. 其他逻辑 (Tick, Tooltip, Effects)
    // =================================================================================================

    /**
     * 生物 Tick 更新<br>
     * 处理不朽的计时器、负面效果清除以及深渊/天使加护的 Buff 施加
     */
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();

        // 不朽: 计时器逻辑
        CompoundTag data = entity.getPersistentData();
        int immortalTimer = data.getInt("DaedalusImmortalTimer");
        if (immortalTimer > 0) {
            data.putInt("DaedalusImmortalTimer", immortalTimer - 1);
            if (immortalTimer - 1 <= 0) {
                data.putFloat("DaedalusImmortalMultiplier", 1.0f);
            }
        }

        if (entity.tickCount % 10 != 0) return;

        // 不朽: 清除负面效果
        if (DaedalusConfig.COMMON.immortalEnabled.get()) {
            int level = getArmorEnchantmentLevel(entity, DaedalusRegistries.IMMORTAL.get());
            if (level > 0) {
                List<MobEffectInstance> effects = new ArrayList<>(entity.getActiveEffects());
                for (MobEffectInstance effect : effects) {
                    if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        entity.removeEffect(effect.getEffect());
                    }
                }
            }
        }

        // 深渊的加护: 给予 Buff
        if (DaedalusConfig.COMMON.abyssalProtectionEnabled.get()) {
            ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
            int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.ABYSSAL_PROTECTION_ENCHANT.get(), chest);
            if (level > 0) {
                int buffLevel = level * DaedalusConfig.COMMON.abyssalProtectionLevelPerLevel.get();
                entity.addEffect(new MobEffectInstance(DaedalusRegistries.ABYSSAL_PROTECTION.get(), 100, buffLevel - 1, false, false));
            }
        }

        // 天使的加护: 给予 Buff
        if (entity instanceof Player player) {
            boolean hasAngel = false;
            if (DaedalusConfig.COMMON.angelicProtectionEnabled.get()) {
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
                int chestLvl = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.ANGELIC_PROTECTION_ENCHANT.get(), chest);
                int legsLvl = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.ANGELIC_PROTECTION_ENCHANT.get(), legs);

                int totalLvl = 0;
                if (chestLvl > 0 || legsLvl > 0) {
                    int baseLvl = Math.max(chestLvl, legsLvl);
                    if (chestLvl > 0 && legsLvl > 0 && chestLvl == legsLvl) baseLvl += 1;
                    totalLvl = baseLvl * DaedalusConfig.COMMON.angelicProtectionLevelPerLevel.get();
                    if (totalLvl >= 4) hasAngel = true;
                    entity.addEffect(new MobEffectInstance(DaedalusRegistries.ANGELIC_PROTECTION.get(), 100, totalLvl - 1, false, false));
                }
            }
            if (!hasAngel && !player.isCreative() && !player.isSpectator()) {
                if (player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }
        }
    }

    /**
     * 药水效果应用判定<br>
     * 不朽附魔防止施加负面效果
     */
    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (DaedalusConfig.COMMON.immortalEnabled.get() && event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            LivingEntity entity = event.getEntity();
            if (getArmorEnchantmentLevel(entity, DaedalusRegistries.IMMORTAL.get()) > 0) {
                event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
            }
        }
    }

    /**
     * 玩家 Tick 更新<br>
     * 处理深渊凝视的视线判定逻辑
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide || event.player.tickCount % 5 != 0) return;
        if (!DaedalusConfig.COMMON.abyssalGazeEnabled.get()) return;

        Player player = event.player;
        ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
        int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.ABYSSAL_GAZE.get(), helm);

        if (level > 0) {
            Vec3 lookVec = player.getLookAngle();
            Vec3 eyePos = player.getEyePosition();
            AABB box = player.getBoundingBox().expandTowards(lookVec.scale(20)).inflate(2);
            List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != player);

            for (LivingEntity target : entities) {
                Vec3 targetPos = target.getBoundingBox().getCenter();
                Vec3 dirToTarget = targetPos.subtract(eyePos).normalize();
                double dot = lookVec.dot(dirToTarget);
                if (dot > 0.95 && eyePos.distanceTo(targetPos) <= 20) {
                    if (player.hasLineOfSight(target)) {
                        int curseLevel = level * DaedalusConfig.COMMON.abyssalGazeCurseLevelPerLevel.get();
                        target.addEffect(new MobEffectInstance(DaedalusRegistries.ABYSSAL_CURSE.get(), 100, curseLevel - 1));
                    }
                }
            }
        }
    }

    /**
     * 物品使用 Tick 更新<br>
     * 处理代达罗斯之弓的蓄力速度加成
     */
    @SubscribeEvent
    public static void onItemUseTick(LivingEntityUseItemEvent.Tick event) {
        if (!DaedalusConfig.COMMON.daedalusBowEnabled.get()) return;
        ItemStack stack = event.getItem();
        if (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem || stack.getItem() instanceof TridentItem) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.DAEDALUS_BOW.get(), stack);
            if (level > 0) {
                double speedBonus = level * DaedalusConfig.COMMON.daedalusBowSpeedPerLevel.get();
                CompoundTag data = event.getEntity().getPersistentData();
                String key = "DaedalusBowAccumulator";
                float accumulator = data.getFloat(key);
                accumulator += speedBonus;
                int extraReduction = (int) accumulator;
                if (extraReduction > 0) {
                    int newDuration = event.getDuration() - extraReduction;
                    if (newDuration < 0) newDuration = 0;
                    event.setDuration(newDuration);
                    accumulator -= extraReduction;
                }
                data.putFloat(key, accumulator);
            }
        }
    }

    /**
     * 物品提示框事件<br>
     * 显示灵魂饥渴的灵魂数量
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.SOUL_THIRST.get(), stack) > 0) {
            int souls = stack.getOrCreateTag().getInt("DaedalusSouls");
            int max = DaedalusConfig.COMMON.soulThirstMaxThreshold.get();
            event.getToolTip().add(Component.literal("Souls: " + souls + "/" + max).withStyle(ChatFormatting.DARK_RED));
        }
    }

    /**
     * 输出伤害事件<br>
     * 处理深渊诅咒对攻击者造成的伤害削减
     */
    @SubscribeEvent
    public static void onOutputDamage(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(DaedalusRegistries.ABYSSAL_CURSE.get())) {
                int amp = attacker.getEffect(DaedalusRegistries.ABYSSAL_CURSE.get()).getAmplifier();
                int level = amp + 1;
                float reduction = Math.min(0.8f, level * 0.1f);
                event.setAmount(event.getAmount() * (1.0f - reduction));
            }
        }
    }

    /**
     * 防御事件<br>
     * 处理深渊庇护的闪避与减伤，以及天使庇佑对亡灵的防御加成
     */
    @SubscribeEvent
    public static void onDefend(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();

        // 深渊庇护
        if (victim.hasEffect(DaedalusRegistries.ABYSSAL_PROTECTION.get())) {
            boolean voidBreach = false;
            if (event.getSource() instanceof IDaedalusDamageSource ds && (ds.daedalus$isVoidBreach() || ds.daedalus$isBypassAll())) {
                voidBreach = true;
            }

            int amp = victim.getEffect(DaedalusRegistries.ABYSSAL_PROTECTION.get()).getAmplifier();
            int level = amp + 1;

            // 概率无视伤害 (闪避)
            if (!voidBreach) {
                if (RANDOM.nextFloat() < (level * 0.05)) {
                    event.setCanceled(true);
                    return;
                }
            }
            // 伤害减免
            float reduction = level * 0.1f;
            event.setAmount(event.getAmount() * (1.0f - reduction));
        }

        // 天使庇佑
        if (victim.hasEffect(DaedalusRegistries.ANGELIC_PROTECTION.get()) && event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.getMobType() == MobType.UNDEAD) {
                int amp = victim.getEffect(DaedalusRegistries.ANGELIC_PROTECTION.get()).getAmplifier();
                int level = amp + 1;
                float reduction = Math.min(0.99f, level * 0.25f);
                event.setAmount(event.getAmount() * (1.0f - reduction));
            }
        }
    }

    private static int getArmorEnchantmentLevel(LivingEntity entity, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        int max = 0;
        for (ItemStack armor : entity.getArmorSlots()) {
            int lvl = EnchantmentHelper.getItemEnchantmentLevel(enchantment, armor);
            if (lvl > max) max = lvl;
        }
        return max;
    }
}

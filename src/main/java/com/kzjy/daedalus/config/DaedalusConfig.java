package com.kzjy.daedalus.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DaedalusConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        // #1 代达罗斯之弓 / Daedalus Bow
        public final ForgeConfigSpec.BooleanValue daedalusBowEnabled;
        public final ForgeConfigSpec.IntValue daedalusBowMaxLevel;
        public final ForgeConfigSpec.BooleanValue daedalusBowTradeable;
        public final ForgeConfigSpec.BooleanValue daedalusBowTreasure;
        public final ForgeConfigSpec.DoubleValue daedalusBowSpeedPerLevel;

        // #2 深渊凝视 / Abyssal Gaze
        public final ForgeConfigSpec.BooleanValue abyssalGazeEnabled;
        public final ForgeConfigSpec.IntValue abyssalGazeMaxLevel;
        public final ForgeConfigSpec.BooleanValue abyssalGazeTradeable;
        public final ForgeConfigSpec.BooleanValue abyssalGazeTreasure;
        public final ForgeConfigSpec.IntValue abyssalGazeCurseLevelPerLevel;

        // #3 诅咒锁链 / Cursed Chain
        public final ForgeConfigSpec.BooleanValue cursedChainEnabled;
        public final ForgeConfigSpec.IntValue cursedChainMaxLevel;
        public final ForgeConfigSpec.BooleanValue cursedChainTradeable;
        public final ForgeConfigSpec.BooleanValue cursedChainTreasure;
        public final ForgeConfigSpec.DoubleValue cursedChainBaseReduction;
        public final ForgeConfigSpec.DoubleValue cursedChainReductionRecoveryPerLevel;
        public final ForgeConfigSpec.DoubleValue cursedChainBonusDamagePerLevel;

        // #4 罪孽印记 / Mark of Sin
        public final ForgeConfigSpec.BooleanValue markOfSinEnabled;
        public final ForgeConfigSpec.IntValue markOfSinMaxLevel;
        public final ForgeConfigSpec.BooleanValue markOfSinTradeable;
        public final ForgeConfigSpec.BooleanValue markOfSinTreasure;
        public final ForgeConfigSpec.IntValue markOfSinLevelPerLevel;

        // #5 虚空撕裂 / Void Rend
        public final ForgeConfigSpec.BooleanValue voidRendEnabled;
        public final ForgeConfigSpec.IntValue voidRendMaxLevel;
        public final ForgeConfigSpec.BooleanValue voidRendTradeable;
        public final ForgeConfigSpec.BooleanValue voidRendTreasure;
        public final ForgeConfigSpec.DoubleValue voidRendTrueDamagePercentPerLevel;
        public final ForgeConfigSpec.DoubleValue voidRendRagnarokThreshold;
        public final ForgeConfigSpec.DoubleValue voidRendRagnarokChancePerLevel;

        // #6 灵魂饥渴 / Soul Thirst
        public final ForgeConfigSpec.BooleanValue soulThirstEnabled;
        public final ForgeConfigSpec.IntValue soulThirstMaxLevel;
        public final ForgeConfigSpec.BooleanValue soulThirstTradeable;
        public final ForgeConfigSpec.BooleanValue soulThirstTreasure;
        public final ForgeConfigSpec.DoubleValue soulThirstGainPerLevel;
        public final ForgeConfigSpec.IntValue soulThirstMaxThreshold;
        public final ForgeConfigSpec.DoubleValue soulThirstBaseBonus;
        public final ForgeConfigSpec.DoubleValue soulThirstBonusPerLevel;

        // #7 深渊的加护 / Abyssal Protection
        public final ForgeConfigSpec.BooleanValue abyssalProtectionEnabled;
        public final ForgeConfigSpec.IntValue abyssalProtectionMaxLevel;
        public final ForgeConfigSpec.BooleanValue abyssalProtectionTradeable;
        public final ForgeConfigSpec.BooleanValue abyssalProtectionTreasure;
        public final ForgeConfigSpec.IntValue abyssalProtectionLevelPerLevel;

        // #8 虚空破壁 / Void Breach
        public final ForgeConfigSpec.BooleanValue voidBreachEnabled;
        public final ForgeConfigSpec.BooleanValue voidBreachTradeable;
        public final ForgeConfigSpec.BooleanValue voidBreachTreasure;

        // #9 神圣裁决 / Divine Judgment
        public final ForgeConfigSpec.BooleanValue divineJudgmentEnabled;
        public final ForgeConfigSpec.IntValue divineJudgmentMaxLevel;
        public final ForgeConfigSpec.BooleanValue divineJudgmentTradeable;
        public final ForgeConfigSpec.BooleanValue divineJudgmentTreasure;
        public final ForgeConfigSpec.DoubleValue divineJudgmentUndeadBonusPerLevel;
        public final ForgeConfigSpec.DoubleValue divineJudgmentCureChance;

        // #10 天使的加护 / Angelic Protection
        public final ForgeConfigSpec.BooleanValue angelicProtectionEnabled;
        public final ForgeConfigSpec.IntValue angelicProtectionMaxLevel;
        public final ForgeConfigSpec.BooleanValue angelicProtectionTradeable;
        public final ForgeConfigSpec.BooleanValue angelicProtectionTreasure;
        public final ForgeConfigSpec.IntValue angelicProtectionLevelPerLevel;

        // #11 生命虹吸 / Life Siphon
        public final ForgeConfigSpec.BooleanValue lifeSiphonEnabled;
        public final ForgeConfigSpec.IntValue lifeSiphonMaxLevel;
        public final ForgeConfigSpec.BooleanValue lifeSiphonTradeable;
        public final ForgeConfigSpec.BooleanValue lifeSiphonTreasure;
        public final ForgeConfigSpec.DoubleValue lifeSiphonBaseHeal;
        public final ForgeConfigSpec.DoubleValue lifeSiphonHealPerLevel;

        // #12 耀星之噬 / Stellar Eater
        public final ForgeConfigSpec.BooleanValue stellarEaterEnabled;
        public final ForgeConfigSpec.BooleanValue stellarEaterTradeable;
        public final ForgeConfigSpec.BooleanValue stellarEaterTreasure;

        // #13 不朽 / Immortal
        public final ForgeConfigSpec.BooleanValue immortalEnabled;
        public final ForgeConfigSpec.IntValue immortalMaxLevel;
        public final ForgeConfigSpec.BooleanValue immortalTradeable;
        public final ForgeConfigSpec.BooleanValue immortalTreasure;
        public final ForgeConfigSpec.IntValue immortalBaseDuration;
        public final ForgeConfigSpec.IntValue immortalDurationPerLevel;
        public final ForgeConfigSpec.DoubleValue immortalBaseReduction;
        public final ForgeConfigSpec.DoubleValue immortalReductionPerLevel;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Daedalus Enchantment Configuration", "代达罗斯附魔配置").push("enchantments");

            // #1
            builder.push("daedalus_bow");
            daedalusBowEnabled = builder.comment("Is Daedalus Bow enabled? // 是否启用代达罗斯之弓").define("enabled", true);
            daedalusBowMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 5, 1, 10);
            daedalusBowTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            daedalusBowTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            daedalusBowSpeedPerLevel = builder.comment("Charge speed increase per level (0.05 = 5%) // 每级增加的拉弓速度 (0.05 = 5%)").defineInRange("speed_per_level", 0.05, 0.0, 5.0);
            builder.pop();

            // #2
            builder.push("abyssal_gaze");
            abyssalGazeEnabled = builder.comment("Is Abyssal Gaze enabled? // 是否启用深渊凝视").define("enabled", true);
            abyssalGazeMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 3, 1, 10);
            abyssalGazeTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            abyssalGazeTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            abyssalGazeCurseLevelPerLevel = builder.comment("Curse level applied per enchantment level // 每级施加的深渊诅咒等级").defineInRange("curse_level_per_level", 1, 1, 10);
            builder.pop();

            // #3
            builder.push("cursed_chain");
            cursedChainEnabled = builder.comment("Is Cursed Chain enabled? // 是否启用诅咒锁链").define("enabled", true);
            cursedChainMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 5, 1, 10);
            cursedChainTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            cursedChainTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            cursedChainBaseReduction = builder.comment("Base damage reduction (0.5 = 50%) // 基础降低伤害 (0.5 = 50%)").defineInRange("base_reduction", 0.5, 0.0, 1.0);
            cursedChainReductionRecoveryPerLevel = builder.comment("Reduction recovery per level (0.05 = 5%) // 每级减少的诅咒伤害削减 (0.05 = 5%)").defineInRange("reduction_recovery", 0.05, 0.0, 1.0);
            cursedChainBonusDamagePerLevel = builder.comment("Cursed damage bonus per level (0.1 = 10%) // 每级额外附带的诅咒伤害百分比 (0.1 = 10%)").defineInRange("bonus_damage", 0.1, 0.0, 10.0);
            builder.pop();

            // #4
            builder.push("mark_of_sin");
            markOfSinEnabled = builder.comment("Is Mark of Sin enabled? // 是否启用罪孽印记").define("enabled", true);
            markOfSinMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 4, 1, 10);
            markOfSinTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", false);
            markOfSinTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", true);
            markOfSinLevelPerLevel = builder.comment("Effect level applied per enchantment level // 每级施加的罪孽印记等级").defineInRange("effect_level", 1, 1, 10);
            builder.pop();

            // #5
            builder.push("void_rend");
            voidRendEnabled = builder.comment("Is Void Rend enabled? // 是否启用虚空撕裂").define("enabled", true);
            voidRendMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 3, 1, 10);
            voidRendTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            voidRendTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            voidRendTrueDamagePercentPerLevel = builder.comment("True damage percent per level (0.05 = 5%) // 每级提供的不可减免伤害百分比 (0.05 = 5%)").defineInRange("true_damage_percent", 0.05, 0.0, 1.0);
            voidRendRagnarokThreshold = builder.comment("Ragnarok HP threshold (0.05 = 5%) // 诸神黄昏触发的血量阈值 (0.05 = 5%)").defineInRange("ragnarok_threshold", 0.05, 0.0, 1.0);
            voidRendRagnarokChancePerLevel = builder.comment("Ragnarok trigger chance per level (0.1 = 10%) // 每级触发诸神黄昏的概率 (0.1 = 10%)").defineInRange("ragnarok_chance", 0.1, 0.0, 1.0);
            builder.pop();

            // #6
            builder.push("soul_thirst");
            soulThirstEnabled = builder.comment("Is Soul Thirst enabled? // 是否启用灵魂饥渴").define("enabled", true);
            soulThirstMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 5, 1, 10);
            soulThirstTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            soulThirstTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            soulThirstGainPerLevel = builder.comment("Souls gained multiplier per level // 每级附魔获得的灵魂倍率").defineInRange("gain_multiplier", 1.0, 0.0, 10.0);
            soulThirstMaxThreshold = builder.comment("Max soul threshold // 最大灵魂阈值").defineInRange("max_threshold", 10, 1, 100);
            soulThirstBaseBonus = builder.comment("Base bonus damage at max souls (4.0 = 400%) // 追加的魔法伤害基础数值 (4.0 = 400%)").defineInRange("base_bonus", 4.0, 0.0, 100.0);
            soulThirstBonusPerLevel = builder.comment("Bonus damage increase per level after level 1 (0.5 = 50%) // 每级额外提升的百分比 (0.5 = 50%)").defineInRange("bonus_per_level", 0.5, 0.0, 10.0);
            builder.pop();

            // #7
            builder.push("abyssal_protection");
            abyssalProtectionEnabled = builder.comment("Is Abyssal Protection enabled? // 是否启用深渊的加护").define("enabled", true);
            abyssalProtectionMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 5, 1, 10);
            abyssalProtectionTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            abyssalProtectionTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            abyssalProtectionLevelPerLevel = builder.comment("Buff level per enchantment level // 每级提供的深渊庇护等级").defineInRange("buff_level", 1, 1, 10);
            builder.pop();

            // #8
            builder.push("void_breach");
            voidBreachEnabled = builder.comment("Is Void Breach enabled? // 是否启用虚空破壁").define("enabled", true);
            voidBreachTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", false);
            voidBreachTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", true);
            builder.pop();

            // #9
            builder.push("divine_judgment");
            divineJudgmentEnabled = builder.comment("Is Divine Judgment enabled? // 是否启用神圣裁决").define("enabled", true);
            divineJudgmentMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 5, 1, 10);
            divineJudgmentTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            divineJudgmentTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            divineJudgmentUndeadBonusPerLevel = builder.comment("Damage bonus vs Undead per level (0.1 = 10%) // 每级对亡灵生物的加伤百分比 (0.1 = 10%)").defineInRange("undead_bonus", 0.1, 0.0, 10.0);
            divineJudgmentCureChance = builder.comment("Chance to cure Zombie Villager per level (0.2 = 20%) // 转化僵尸村民的百分比 (0.2 = 20%)").defineInRange("cure_chance", 0.2, 0.0, 1.0);
            builder.pop();

            // #10
            builder.push("angelic_protection");
            angelicProtectionEnabled = builder.comment("Is Angelic Protection enabled? // 是否启用天使的加护").define("enabled", true);
            angelicProtectionMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 4, 1, 10);
            angelicProtectionTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", false);
            angelicProtectionTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", true);
            angelicProtectionLevelPerLevel = builder.comment("Buff level per enchantment level // 每级提供的天使庇佑等级").defineInRange("buff_level", 1, 1, 10);
            builder.pop();

            // #11
            builder.push("life_siphon");
            lifeSiphonEnabled = builder.comment("Is Life Siphon enabled? // 是否启用生命虹吸").define("enabled", true);
            lifeSiphonMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 5, 1, 10);
            lifeSiphonTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", true);
            lifeSiphonTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", false);
            lifeSiphonBaseHeal = builder.comment("Base heal percentage (0.1 = 10%) // 默认治疗百分比 (0.1 = 10%)").defineInRange("base_heal", 0.1, 0.0, 10.0);
            lifeSiphonHealPerLevel = builder.comment("Heal percentage increase per level after level 1 (0.05 = 5%) // 每级增加的百分比 (0.05 = 5%)").defineInRange("heal_increase", 0.05, 0.0, 10.0);
            builder.pop();

            // #12
            builder.push("stellar_eater");
            stellarEaterEnabled = builder.comment("Is Stellar Eater enabled? // 是否启用耀星之噬").define("enabled", true);
            stellarEaterTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", false);
            stellarEaterTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", true);
            builder.pop();

            // #13
            builder.push("immortal");
            immortalEnabled = builder.comment("Is Immortal enabled? // 是否启用不朽").define("enabled", true);
            immortalMaxLevel = builder.comment("Max Level // 最大等级").defineInRange("max_level", 3, 1, 10);
            immortalTradeable = builder.comment("Is tradeable? // 是否可交易").define("tradeable", false);
            immortalTreasure = builder.comment("Is treasure? // 是否为宝藏附魔").define("treasure", true);
            immortalBaseDuration = builder.comment("Base duration in ticks (20 ticks = 1s) // 基础动态减伤持续时间").defineInRange("base_duration", 20, 1, 200);
            immortalDurationPerLevel = builder.comment("Duration increase per level in ticks // 每级附魔提高的动态减伤持续时间").defineInRange("duration_per_level", 5, 0, 100);
            immortalBaseReduction = builder.comment("Base reduction percentage (0.5 = 50%) // 基础减免伤害百分比").defineInRange("base_reduction", 0.5, 0.0, 1.0);
            immortalReductionPerLevel = builder.comment("Reduction increase per level (0.05 = 5%) // 每级附魔提高的减免伤害百分比").defineInRange("reduction_per_level", 0.05, 0.0, 1.0);
            builder.pop();

            builder.pop();
        }
    }
}

package com.kzjy.daedalus.registry;

import com.kzjy.daedalus.Daedalus;
import com.kzjy.daedalus.effect.*;
import com.kzjy.daedalus.enchantment.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author Kzjy<br>
 * 模组注册中心<br>
 * 负责注册所有的物品、附魔、药水效果及创造模式选项卡
 */
public class DaedalusRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Daedalus.MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Daedalus.MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Daedalus.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Daedalus.MODID);

    public static final RegistryObject<MobEffect> ABYSSAL_CURSE = MOB_EFFECTS.register("abyssal_curse", AbyssalCurseEffect::new);
    public static final RegistryObject<MobEffect> MARK_OF_SIN = MOB_EFFECTS.register("mark_of_sin", MarkOfSinEffect::new);
    public static final RegistryObject<MobEffect> ABYSSAL_PROTECTION = MOB_EFFECTS.register("abyssal_protection", AbyssalProtectionEffect::new);
    public static final RegistryObject<MobEffect> ANGELIC_PROTECTION = MOB_EFFECTS.register("angelic_protection", AngelicProtectionEffect::new);

    public static final RegistryObject<Enchantment> DAEDALUS_BOW = ENCHANTMENTS.register("daedalus_bow", DaedalusBowEnchantment::new);
    public static final RegistryObject<Enchantment> ABYSSAL_GAZE = ENCHANTMENTS.register("abyssal_gaze", AbyssalGazeEnchantment::new);
    public static final RegistryObject<Enchantment> CURSED_CHAIN = ENCHANTMENTS.register("cursed_chain", CursedChainEnchantment::new);
    public static final RegistryObject<Enchantment> MARK_OF_SIN_ENCHANT = ENCHANTMENTS.register("mark_of_sin", MarkOfSinEnchantment::new);
    public static final RegistryObject<Enchantment> VOID_REND = ENCHANTMENTS.register("void_rend", VoidRendEnchantment::new);
    public static final RegistryObject<Enchantment> SOUL_THIRST = ENCHANTMENTS.register("soul_thirst", SoulThirstEnchantment::new);
    public static final RegistryObject<Enchantment> ABYSSAL_PROTECTION_ENCHANT = ENCHANTMENTS.register("abyssal_protection", AbyssalProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> VOID_BREACH = ENCHANTMENTS.register("void_breach", VoidBreachEnchantment::new);
    public static final RegistryObject<Enchantment> DIVINE_JUDGMENT = ENCHANTMENTS.register("divine_judgment", DivineJudgmentEnchantment::new);
    public static final RegistryObject<Enchantment> ANGELIC_PROTECTION_ENCHANT = ENCHANTMENTS.register("angelic_protection", AngelicProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> LIFE_SIPHON = ENCHANTMENTS.register("life_siphon", LifeSiphonEnchantment::new);
    public static final RegistryObject<Enchantment> STELLAR_EATER = ENCHANTMENTS.register("stellar_eater", StellarEaterEnchantment::new);
    public static final RegistryObject<Enchantment> IMMORTAL = ENCHANTMENTS.register("immortal", ImmortalEnchantment::new);

    public static final RegistryObject<CreativeModeTab> DAEDALUS_TAB = CREATIVE_MODE_TABS.register("daedalus_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(net.minecraft.world.item.Items.ENCHANTED_BOOK))
            .title(Component.translatable("itemGroup.daedalus"))
            .displayItems((parameters, output) -> {
                for (RegistryObject<Enchantment> entry : ENCHANTMENTS.getEntries()) {
                    Enchantment enchantment = entry.get();
                    int min = enchantment.getMinLevel();
                    int max = enchantment.getMaxLevel();
                    if (max < min) {
                        output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, min)));
                    } else {
                        for (int i = min; i <= max; ++i) {
                            output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
                        }
                    }
                }
            })
            .build());
}

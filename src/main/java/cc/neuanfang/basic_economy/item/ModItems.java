package cc.neuanfang.basic_economy.item;

import cc.neuanfang.basic_economy.BasicEconomy;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static Item ADVANCED_OMINOUS_TRIAL_KEY = registerItem("advanced_ominous_trial_key",
            new PolyItem(new Item.Settings().maxCount(1), "advanced_ominous_trial_key"));
    public static final Item SHOP_KEY = registerItem("shop_key",
            new ShopKeyItem(new Item.Settings().maxCount(1), "shop_key"));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BasicEconomy.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BasicEconomy.LOGGER.info("Registering Mod Items for " + BasicEconomy.MOD_ID);
        ItemGroup.Builder builder = PolymerItemGroupUtils.builder();
        builder.icon(() -> new ItemStack(ModItems.SHOP_KEY, 1));
        builder.displayName(Text.translatable("item-group.basic_economy.items"));

        builder.entries((displayContext, entries) -> {
            entries.add(SHOP_KEY);
            entries.add(ADVANCED_OMINOUS_TRIAL_KEY);
        });
        ItemGroup polymerGroup = builder.build();
        PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(BasicEconomy.MOD_ID, "items"), polymerGroup);
    }
}

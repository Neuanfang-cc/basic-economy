package cc.neuanfang.basic_economy.item;

import cc.neuanfang.basic_economy.BasicEconomy;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item ADVANCED_OMINOUS_TRIAL_KEY = registerItem("advanced_ominous_trial_key",
            new Item(new Item.Settings()));
    public static final Item SHOP_KEY = registerItem("shop_key",
            new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BasicEconomy.MOD_ID, name), item);
    }

    private static void customIngredients(FabricItemGroupEntries entries) {
        entries.add(ADVANCED_OMINOUS_TRIAL_KEY);
        entries.add(SHOP_KEY);
    }

    public static void registerModItems() {
        BasicEconomy.LOGGER.info("Registering Mod Items for " + BasicEconomy.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::customIngredients);
    }
}

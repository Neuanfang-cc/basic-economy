package cc.neuanfang.basic_economy;

import cc.neuanfang.basic_economy.command.*;
import cc.neuanfang.basic_economy.item.ModItems;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicEconomy implements ModInitializer {
	public static final String MOD_ID = "basic_economy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		DatabaseManager.connect();

		ModItems.registerModItems();

		CommandRegistrationCallback.EVENT.register(BalanceCommand::register);
		CommandRegistrationCallback.EVENT.register(PayCommand::register);

		CommandRegistrationCallback.EVENT.register(BalanceAdminCommand::register);
		CommandRegistrationCallback.EVENT.register(BalanceAdminAddCommand::register);
		CommandRegistrationCallback.EVENT.register(BalanceAdminSetCommand::register);
		CommandRegistrationCallback.EVENT.register(BalanceAdminSubtractCommand::register);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				DatabaseManager.disconnect();
				LOGGER.info("Exit mod " + MOD_ID);
			}
		}, "Shutdown-thread"));

		if (PolymerResourcePackUtils.addModAssets(MOD_ID)) {
			LOGGER.info("Successfully added mod assets for " + MOD_ID);
		} else {
			LOGGER.error("Failed to add mod assets for " + MOD_ID);
		}
		//initModels();
		PolymerResourcePackUtils.markAsRequired();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
package cc.neuanfang.basic_economy.command;

import cc.neuanfang.basic_economy.BasicEconomy;
import cc.neuanfang.basic_economy.EconomyAPI;
import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class BalanceAdminCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("balanceadmin")
                .requires(source -> Permissions.check(source.getPlayer(), (BasicEconomy.MOD_ID + "balanceadmin.get"), 2))
                .then(CommandManager.literal("get")
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(context -> (
                                        run(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target")
                                        )
                                ))
                        )
                )
        );
    }

    private static int run(ServerCommandSource source, ServerPlayerEntity target) {
        try {
            UUID target_uuid = Objects.requireNonNull(target.getUuid());
            String balance = EconomyAPI.df.format(EconomyAPI.getBalance(target_uuid));
            String player_name = target.getName().getLiteralString();

            source.sendFeedback(() -> Text.translatable("balanceadmin.get.success", player_name, balance), false);
            return 1;
        } catch (Exception e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
            return -1;
        }
    }
}

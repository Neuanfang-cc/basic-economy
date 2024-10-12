package cc.neuanfang.basic_economy.command;

import cc.neuanfang.basic_economy.BasicEconomy;
import cc.neuanfang.basic_economy.EconomyAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
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

public class BalanceAdminSubtractCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("balanceadmin")
                .requires(source -> Permissions.check(source.getPlayer(), (BasicEconomy.MOD_ID + "balanceadmin.subtract"), 2))
                .then(CommandManager.literal("sub")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                        .executes(context -> (
                            run(
                                context.getSource(),
                                EntityArgumentType.getPlayer(context, "target"),
                                FloatArgumentType.getFloat(context, "amount")
                            )
                ))))));
    }

    private static int run(ServerCommandSource source, ServerPlayerEntity target, float amount) {
        try {
            UUID target_uuid = Objects.requireNonNull(target.getUuid());
            String balance = EconomyAPI.df.format(amount);
            String target_name = target.getName().getLiteralString();

            double balance_before = EconomyAPI.getBalance(target_uuid);
            EconomyAPI.subtractBalance(target_uuid, amount);
            double balance_after = EconomyAPI.getBalance(target_uuid);

            if (balance_before != balance_after) {
                source.sendFeedback(() -> Text.translatable("balanceadmin.subtract.success", target_name, balance), true);
                return 1;
            } else {
                source.sendFeedback(() -> Text.translatable("balanceadmin.failed", target_name), false);
            }
        } catch (Exception e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }
}

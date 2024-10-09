package cc.neuanfang.basic_economy.command;

import cc.neuanfang.basic_economy.EconomyAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

public class PayCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("pay")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                    .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                        .executes(context -> (
                            run(
                                context.getSource(),
                                EntityArgumentType.getPlayer(context, "target"),
                                FloatArgumentType.getFloat(context, "amount")
                            )
                )))));
    }

    private static int run(ServerCommandSource source, ServerPlayerEntity target, float amount) {
        UUID source_uuid = Objects.requireNonNull(source.getPlayer()).getUuid();
        UUID target_uuid = Objects.requireNonNull(target.getUuid());
        String balance = EconomyAPI.df.format(amount);
        String target_name = target.getName().getLiteralString();

        if (EconomyAPI.transfer(source_uuid, target_uuid, amount)) {
            source.sendFeedback(() -> Text.translatable("balance.pay.success", balance,  target_name), true);
            return 1;
        } else {
            source.sendFeedback(() -> Text.translatable("balance.pay.failed", target_name), true);
            return -1;
        }
    }
}

package cc.neuanfang.basic_economy.command;

import cc.neuanfang.basic_economy.EconomyAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

public class BalanceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("balance")
                .executes(BalanceCommand::run));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        UUID uuid = Objects.requireNonNull(context.getSource().getPlayer()).getUuid();
        String balance = EconomyAPI.df.format(EconomyAPI.getBalance(uuid));

        context.getSource().sendFeedback(() -> Text.translatable("balance.get", balance), true);
        return 1;
    }
}

package io.github.hhservers.btokens.commands;

import io.github.hhservers.btokens.Util;
import lombok.SneakyThrows;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class AddCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.<String>getOne(Text.of("tokenID")).isPresent() && args.<String>getOne(Text.of("command")).isPresent()) {
            Util util = new Util();
            String tokenID = args.<String>getOne(Text.of("tokenID")).get().toLowerCase();
            String command = args.<String>getOne(Text.of("command")).get();
            Player p = (Player) src;
            util.addTokenCommand(tokenID, command);
            Sponge.getGame().getCommandManager().process(p, "btoken command " + tokenID);
            p.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b Command added to token: &a" + tokenID));
        }
        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("btokens.admin.addcmd")
                .arguments(GenericArguments.string(Text.of("tokenID")), GenericArguments.string(Text.of("command")))
                .description(Text.of("Base command"))
                .executor(new AddCommand())
                .build();
    }
}

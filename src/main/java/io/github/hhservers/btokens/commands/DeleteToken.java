package io.github.hhservers.btokens.commands;

import io.github.hhservers.btokens.Util;
import lombok.SneakyThrows;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class DeleteToken implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.<String>getOne(Text.of("tokenID")).isPresent()) {
            Util util = new Util();
            String tokenID = args.<String>getOne(Text.of("tokenID")).get().toLowerCase();
            if (util.deleteToken(tokenID)) {
                src.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b Token removed."));
            } else {
                src.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r &cError &bremoving &aB&dToken&b. Is the ID correct?"));
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .arguments(GenericArguments.string(Text.of("tokenID")))
                .permission("btokens.admin.deltoken")
                .description(Text.of("Base command"))
                .executor(new DeleteToken())
                .build();
    }
}

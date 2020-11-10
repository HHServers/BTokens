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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

public class RemoveCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.<String>getOne(Text.of("tokenID")).isPresent() && args.<Integer>getOne(Text.of("commandIndex")).isPresent()) {
            Util util = new Util();
            String tokenID = args.<String>getOne(Text.of("tokenID")).get().toLowerCase();
            Integer commandIndex = args.<Integer>getOne(Text.of("commandIndex")).get();
            Player p = (Player) src;
            int trueIndex = commandIndex - 1;
            if (util.removeTokenCommand(tokenID, trueIndex)) {
                /*Sponge.getGame().getCommandManager().process(p, "btoken command " + tokenID);*/

                PaginationList.builder()
                        .title(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&b | &r&a" + tokenID))
                        .padding(util.textDeserializer("&a=&d="))
                        .contents(util.tokenCommandList(tokenID))
                        .sendTo(p);

                p.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b Command removed from token: &a" + tokenID));
            }
        }
        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("btokens.admin.removecmd")
                .arguments(GenericArguments.string(Text.of("tokenID")), GenericArguments.integer(Text.of("commandIndex")))
                .description(Text.of("Base command"))
                .executor(new RemoveCommand())
                .build();
    }
}

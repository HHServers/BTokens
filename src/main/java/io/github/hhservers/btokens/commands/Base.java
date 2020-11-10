package io.github.hhservers.btokens.commands;

import io.github.hhservers.btokens.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

public class Base implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Util util = new Util();
        Player p = (Player) src;
        PaginationList.builder()
                .title(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r"))
                .padding(util.textDeserializer("&a=&d="))
                .contents(util.activeTokenList())
                .sendTo(p);
        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .child(Child.build(), "command")
                .child(GenToken.build(), "gen")
                .child(GiveToken.build(), "give")
                //.arguments(GenericArguments.string(Text.of("StringArg")), GenericArguments.integer(Text.of("IntArg")))
                .permission("btokens.user.base")
                .description(Text.of("Base command"))
                .executor(new Base())
                .build();
    }
}

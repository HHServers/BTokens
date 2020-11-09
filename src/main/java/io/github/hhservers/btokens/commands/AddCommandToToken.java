package io.github.hhservers.btokens.commands;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class AddCommandToToken implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //TODO: add command to token with ID
        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .arguments(GenericArguments.uuid(Text.of("tokenID")))
               //TODO: iterate through config for all token IDs and put inside map so they appear when pressing TAB
               //TODO: possibly in Util class so other commands can use map w/o messy code
                //.arguments(GenericArguments.choices(Text.of("tokenID"), ImmutableMap.<String, String>builder().put()))
                .permission("bstarter.user.base")
                .description(Text.of("Base command"))
                .executor(new AddCommandToToken())
                .build();
    }
}

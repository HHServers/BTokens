package io.github.hhservers.btokens.commands;

import io.github.hhservers.btokens.Util;
import io.github.hhservers.btokens.config.Token;
import lombok.SneakyThrows;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

public class GenToken implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("Command format: /gentoken displayName description command"));
        String displayName=null;
        String description=null;
        String command=null;

        if(args.<String>getOne(Text.of("displayName")).isPresent()){
            displayName=args.<String>getOne(Text.of("displayName")).get();
        }

        if(args.<String>getOne(Text.of("description")).isPresent()){
            description=args.<String>getOne(Text.of("description")).get();
        }

        if(args.<String>getOne(Text.of("command")).isPresent()){
            command=args.<String>getOne(Text.of("command")).get();
        }

        if(!displayName.equals(null) || !command.equals(null)){
        Util util = new Util();
        Token newToken = util.genToken(displayName,description,command);
        Player p = (Player)src;
        p.getInventory().offer(
                ItemStack.builder()
                        .itemType(ItemTypes.PAPER)
                        .add(Keys.DISPLAY_NAME, util.textDeserializer(displayName))
                        .add(Keys.ITEM_LORE, Arrays.asList(util.textDeserializer(description),util.textDeserializer("&o&8"+newToken.getUuid().toString())))
                        .add(Keys.ITEM_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.UNBREAKING, 5)))
                        .build());
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .arguments(GenericArguments.string(Text.of("displayName")), GenericArguments.optionalWeak(GenericArguments.string(Text.of("description"))), GenericArguments.string(Text.of("command")))
                .permission("bstarter.user.base")
                .description(Text.of("Base command"))
                .executor(new GenToken())
                .build();
    }
}

package io.github.hhservers.btokens.commands;

import io.github.hhservers.btokens.BTokens;
import io.github.hhservers.btokens.Util;
import io.github.hhservers.btokens.config.MainPluginConfig;
import io.github.hhservers.btokens.config.Token;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class GiveToken implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Util util = new Util();

        if(args.hasAny(Text.of("player"))){
            Optional<Player> optionalPlayer = args.<Player>getOne(Text.of("player"));
            if(optionalPlayer.isPresent()){
                Player p = optionalPlayer.get();
            }
        }

        if(args.getOne(Text.of("tokenID")).isPresent()){
            UUID tokenID = args.<UUID>getOne("tokenID").get();
            MainPluginConfig conf = BTokens.getMainPluginConfig();
            for(Token token : conf.getTokenList()){
                if(token.getUuid().equals(tokenID)){
                    Player commandRunner = (Player)src;
                    commandRunner.getInventory().offer(
                            ItemStack.builder()
                                    .itemType(ItemTypes.PAPER)
                                    .add(Keys.DISPLAY_NAME, util.textDeserializer(token.getDisplayName()))
                                    .add(Keys.ITEM_LORE, Arrays.asList(util.textDeserializer(token.getDescription()),util.textDeserializer("&o&8"+token.getUuid().toString())))
                                    .add(Keys.ITEM_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.UNBREAKING, 5)))
                                    .build()
                    );
                }
            }
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){
       return CommandSpec.builder()
                .arguments(GenericArguments.uuid(Text.of("tokenID")), GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .permission("bstarter.user.base")
                .description(Text.of("Base command"))
                .executor(new GiveToken())
                .build();
    }
}

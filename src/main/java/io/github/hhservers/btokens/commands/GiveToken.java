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

public class GiveToken implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Util util = new Util();

        if (args.hasAny(Text.of("player"))) {
            Optional<Player> optionalPlayer = args.getOne(Text.of("player"));
            if (optionalPlayer.isPresent()) {
                Player p = optionalPlayer.get();
                if (args.getOne(Text.of("tokenID")).isPresent()) {
                    String tokenID = args.<String>getOne("tokenID").get().toLowerCase();
                    MainPluginConfig conf = BTokens.getMainPluginConfig();
                    for (Token token : conf.getTokenList()) {
                        if (token.getTokenID().equals(tokenID)) {
                            Player commandRunner = (Player) src;
                            p.getInventory().offer(
                                    ItemStack.builder()
                                            .itemType(ItemTypes.PAPER)
                                            .add(Keys.DISPLAY_NAME, util.textDeserializer(token.getDisplayName()))
                                            .add(Keys.ITEM_LORE, Arrays.asList(util.textDeserializer(token.getDescription()), util.textDeserializer("&o&8&k" + token.getUuid().toString())))
                                            .add(Keys.ITEM_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.UNBREAKING, 5)))
                                            .add(Keys.HIDE_ENCHANTMENTS, true)
                                            .build()
                            );
                            commandRunner.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b Token &a" + tokenID + "&b has been given to &d" + p.getName()));
                        }
                    }
                }

            }
        } else {
            if (args.getOne(Text.of("tokenID")).isPresent()) {
                String tokenID = args.<String>getOne("tokenID").get().toLowerCase();
                MainPluginConfig conf = BTokens.getMainPluginConfig();
                for (Token token : conf.getTokenList()) {
                    if (token.getTokenID().equals(tokenID)) {
                        Player commandRunner = (Player) src;
                        commandRunner.getInventory().offer(
                                ItemStack.builder()
                                        .itemType(ItemTypes.PAPER)
                                        .add(Keys.DISPLAY_NAME, util.textDeserializer(token.getDisplayName()))
                                        .add(Keys.ITEM_LORE, Arrays.asList(util.textDeserializer(token.getDescription()), util.textDeserializer("&o&8&k" + token.getUuid().toString())))
                                        .add(Keys.ITEM_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.UNBREAKING, 5)))
                                        .add(Keys.HIDE_ENCHANTMENTS, true)
                                        .build()
                        );
                    }
                }
            }
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .arguments(GenericArguments.string(Text.of("tokenID")), GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .permission("btokens.admin.givetoken")
                .description(Text.of("Command to give tokens to the user or another player."))
                .executor(new GiveToken())
                .build();
    }
}

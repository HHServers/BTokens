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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Optional;

public class GenToken implements CommandExecutor {
    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String displayName = null;
        String description = null;
        String command = null;
        String tokenID = null;

        if (args.<String>getOne(Text.of("displayName")).isPresent()) {
            displayName = args.<String>getOne(Text.of("displayName")).get();
        }

        if (args.<String>getOne(Text.of("description")).isPresent()) {
            description = args.<String>getOne(Text.of("description")).get();
        } //else { description = "&bRight click to redeem!"; }

        if (args.<String>getOne(Text.of("command")).isPresent()) {
            command = args.<String>getOne(Text.of("command")).get();
        }

        if (args.<String>getOne(Text.of("tokenID")).isPresent()) {
            tokenID = args.<String>getOne(Text.of("tokenID")).get();
        }

        if (!displayName.equals(null) || !command.equals(null) || !tokenID.equals(null)) {
            Util util = new Util();
            Optional<Token> optionalToken = util.genToken(tokenID, displayName, description, command);
            if (optionalToken.isPresent()) {
                Token newToken = optionalToken.get();
                Player p = (Player) src;
                p.getInventory().offer(
                        ItemStack.builder()
                                .itemType(ItemTypes.PAPER)
                                .add(Keys.DISPLAY_NAME, util.textDeserializer(newToken.getDisplayName()))
                                .add(Keys.ITEM_LORE, Arrays.asList(util.textDeserializer(newToken.getDescription()), util.textDeserializer("&o&8&k" + newToken.getUuid().toString())))
                                .add(Keys.ITEM_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.UNBREAKING, 5)))
                                .add(Keys.HIDE_ENCHANTMENTS, true)
                                .build());
                p.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&a B&dToken&b generated."));
            } else {
                src.sendMessage(util.textDeserializer("&l&8[&r&aB&dTokens&l&8]&r &cError creating token. Does the tokenID already exist?"));
            }
        } else {
            src.sendMessage(Text.of(TextColors.GREEN, "Command format: /gentoken tokenID displayName [<description>] \"command text here\""));
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .arguments(GenericArguments.string(Text.of("tokenID")), GenericArguments.string(Text.of("displayName")), GenericArguments.string(Text.of("command")), GenericArguments.optionalWeak(GenericArguments.string(Text.of("description"))))
                .permission("btokens.admin.gentoken")
                .description(Text.of("Base command"))
                .executor(new GenToken())
                .build();
    }
}

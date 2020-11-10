package io.github.hhservers.btokens;

import io.github.hhservers.btokens.config.MainPluginConfig;
import io.github.hhservers.btokens.config.Token;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public Text textDeserializer(String s) {
        return TextSerializers.FORMATTING_CODE.deserialize(s);
    }

    public String textSerializer(Text t) {
        return TextSerializers.PLAIN.serialize(t);
    }

    private boolean checkIfPresent(String tokenID) {

        BTokens.getMainPluginConfig().getTokenList().stream()
                .forEach(token -> BTokens.getInstance().getLogger().info(token.getTokenID()));

        Boolean bool = BTokens.getMainPluginConfig().getTokenList().stream()
                .anyMatch(token -> token.getTokenID().equals(tokenID));
        BTokens.getInstance().getLogger().info(bool.toString());
        return bool;
    }

    public Optional<Token> genToken(String tokenID, String displayName, String description, String command) throws IOException, ObjectMappingException {
        Token newToken = new Token();
        newToken.setCommands(Arrays.asList(command));

        if (description != null) {
            newToken.setDescription(description);
        }

        newToken.setDisplayName(displayName);
        newToken.setUuid(UUID.randomUUID());
        newToken.setTokenID(tokenID);

        MainPluginConfig conf = BTokens.getMainPluginConfig();

        if (BTokens.getMainPluginConfig().getTokenList().isEmpty()) {
            conf.getTokenList().add(newToken);
        } else {
            if (checkIfPresent(newToken.getTokenID())) {
                return Optional.empty();
            } else {
                conf.getTokenList().add(newToken);
            }
        }
        BTokens.getConfigHandler().saveConfig(conf);
        return Optional.of(newToken);

    }

    public Boolean deleteToken(String tokenID) throws IOException, ObjectMappingException {
        MainPluginConfig conf = BTokens.getMainPluginConfig();
        List<Token> tokens = conf.getTokenList();
        ListIterator<Token> iterator = tokens.listIterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            if (token.getTokenID().equals(tokenID)) {
                iterator.remove();
                return true;
            }
        }
        BTokens.getConfigHandler().saveConfig(conf);
        return false;
    }

    public List<Text> activeTokenList() {
        List<Text> contentList = new ArrayList<>();

        for (Token token : BTokens.getMainPluginConfig().getTokenList()) {
            contentList.add(
                    Text.builder()
                            .append(textDeserializer("&d" + token.getTokenID() + "&b | &6" + token.getCommands().size() + "&b commands."))
                            .onClick(TextActions.runCommand("/btoken command " + token.getTokenID()))
                            .onHover(TextActions.showText(Text.of(textDeserializer("&bClick here to view this &aB&dToken&b's commands."))))

                            .append(textDeserializer("&b | "))

                            .append(Text.builder()
                                    .append(textDeserializer("&a[SPAWN]&r"))
                                    .onHover(TextActions.showText(textDeserializer("&bClick to spawn this &aB&dToken&b to your inventory.")))
                                    .onClick(TextActions.executeCallback(source -> {
                                        Sponge.getGame().getCommandManager().process(source, "btoken give " + token.getTokenID());
                                        source.sendMessage(textDeserializer("&l&8[&r&aB&dTokens&l&8]&r &aB&dToken&b spawned!"));
                                    }))
                                    .build())

                            .append(textDeserializer("&b | "))

                            .append(Text.builder()
                                    .append(textDeserializer("&c[REMOVE]&r"))
                                    .onHover(TextActions.showText(textDeserializer("&bClick to delete this &aB&dToken&b.")))
                                    .onClick(TextActions.executeCallback(source -> {
                                        try {
                                            if (deleteToken(token.getTokenID())) {
                                                source.sendMessage(textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b Token removed."));
                                            } else {
                                                source.sendMessage(textDeserializer("&l&8[&r&aB&dTokens&l&8]&r &cError &bremoving &aB&dToken&b. Is the ID correct?"));
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (ObjectMappingException e) {
                                            e.printStackTrace();
                                        }
                                    }))
                                    .build())

                            .build()
            );
        }
        return contentList;
    }

    public Boolean removeTokenCommand(String tokenID, int index) throws IOException, ObjectMappingException {
        MainPluginConfig conf = BTokens.getMainPluginConfig();
        ListIterator<Token> iterator = conf.getTokenList().listIterator();
        BTokens.getInstance().getLogger().info("index:" + index);
        while (iterator.hasNext()) {
            Token token = iterator.next();
            if (token.getTokenID().equals(tokenID)) {
                token.getCommands().remove(index);
                return true;
            }
        }
        BTokens.getConfigHandler().saveConfig(conf);
        return false;
    }

    public void addTokenCommand(String tokenID, String cmd) throws IOException, ObjectMappingException {
        MainPluginConfig conf = BTokens.getMainPluginConfig();
        ListIterator<Token> iterator = conf.getTokenList().listIterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            if (token.getTokenID().equals(tokenID)) {
                token.getCommands().add(cmd);
            }
        }
        BTokens.getConfigHandler().saveConfig(conf);
    }

    public List<Text> tokenCommandList(String tokenID) {
        List<Text> tokenCommands = new ArrayList<>();
        List<Token> tokenList = BTokens.getMainPluginConfig().getTokenList();
        if (tokenList.stream().anyMatch(token -> token.getTokenID().equals(tokenID))) {
            Token foundToken = tokenList.stream()
                    .filter(token -> token.getTokenID().equals(tokenID))
                    .collect(Collectors.toList()).get(0);

            ListIterator<String> listIterator = foundToken.getCommands().listIterator();
            while (listIterator.hasNext()) {
                int trueIndex = listIterator.nextIndex();
                String command = listIterator.next();
                int index = trueIndex + 1;
                tokenCommands.add(
                        textDeserializer("&b" + index + ".&r - &a" + command)
                                .toBuilder()
                                .append(textDeserializer("&b | "))
                                .append(Text.builder()
                                        .append(textDeserializer("&c[REMOVE]&r"))

                                        .onClick(TextActions.executeCallback(source -> {
                                            try {
                                                if (removeTokenCommand(tokenID, trueIndex)) {

                                                    PaginationList.builder()
                                                            .title(textDeserializer("&l&8[&r&aB&dTokens&l&8]&b | &r&a" + tokenID))
                                                            .padding(textDeserializer("&a=&d="))
                                                            .contents(tokenCommandList(tokenID))
                                                            .sendTo(source);

                                                    source.sendMessage(textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b Command removed."));
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (ObjectMappingException e) {
                                                e.printStackTrace();
                                            }
                                        }))
                                        .onHover(TextActions.showText(Text.of("Click to remove this command")))
                                        .build())
                                .build()
                );
            }
        }
        return tokenCommands;
    }

    @Listener
    public void onPlayerRedeemToken(InteractItemEvent.Secondary e, @First Player p) throws IOException, ObjectMappingException {
        if (!e.getItemStack().isEmpty()) {
            if (e.getItemStack().getType().equals(ItemTypes.PAPER)) {
                if (e.getItemStack().get(Keys.ITEM_LORE).isPresent()) {
                    List<Text> lore = e.getItemStack().get(Keys.ITEM_LORE).get();
                    for (Token token : BTokens.getMainPluginConfig().getTokenList()) {
                        if (textSerializer(lore.get(1)).equals(token.getUuid().toString())) {
                            if (p.hasPermission("btokens.token." + token.getTokenID())) {
                                p.getInventory().query(QueryOperationTypes.ITEM_STACK_EXACT.of(e.getItemStack().createStack())).poll(1);
                                for (String command : token.getCommands()) {
                                    String realCommand = command.replace("%p", p.getName());
                                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), realCommand);
                                }
                            } else {
                                p.sendMessage(textDeserializer("&l&8[&r&aB&dTokens&l&8]&r&b You do not have permission to redeem this &aB&dToken&b!"));
                            }
                        }
                    }
                }
            }
        }
    }
}

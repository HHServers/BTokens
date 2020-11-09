package io.github.hhservers.btokens;

import io.github.hhservers.btokens.config.MainPluginConfig;
import io.github.hhservers.btokens.config.Token;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class Util {

    public Text textDeserializer(String s){
        return TextSerializers.FORMATTING_CODE.deserialize(s);
    }
    public String textSerializer(Text t){
        return TextSerializers.PLAIN.serialize(t);
    }

    public Token genToken(String displayName, String description, String command) throws IOException, ObjectMappingException {

        Token newToken = new Token();
        newToken.setCommands(Arrays.asList(command));
        if(!description.equals(null)){newToken.setDescription(description);}
        newToken.setDisplayName(displayName);
        newToken.setUuid(UUID.randomUUID());

        MainPluginConfig conf = BTokens.getMainPluginConfig();
        conf.getTokenList().add(newToken);
       /* List<Token> tokenList = conf.getTokenList();
        if(!tokenList.isEmpty()) {
            ListIterator<Token> iterator = tokenList.listIterator();
            while(iterator.hasNext()) {
                if (!iterator.next().getUuid().equals(newToken.getUuid())) {
                    iterator.add(newToken);
                }
            }
        } else {conf.getTokenList().add(newToken);}*/
        BTokens.getConfigHandler().saveConfig(conf);
        return newToken;

    }

    @Listener
    public void onPlayerRedeemToken(InteractItemEvent.Secondary e, @First Player p) throws IOException, ObjectMappingException {
        if(!e.getItemStack().isEmpty()){
            if(e.getItemStack().getType().equals(ItemTypes.PAPER)){
                if(e.getItemStack().get(Keys.ITEM_LORE).isPresent()){
                    List<Text> lore = e.getItemStack().get(Keys.ITEM_LORE).get();
                    for(Token token : BTokens.getMainPluginConfig().getTokenList()){
                        if(textSerializer(lore.get(1)).equals(token.getUuid().toString())){
                            p.getInventory().query(QueryOperationTypes.ITEM_STACK_EXACT.of(e.getItemStack().createStack())).poll(1);
                            //TODO: remove from config after redeem
                            /*MainPluginConfig conf = BTokens.getMainPluginConfig();
                            ListIterator<Token> listIterator = conf.getTokenList().listIterator();
                            while(listIterator.hasNext()){
                                Token premadeToken = listIterator.next();
                                if(premadeToken.getUuid().equals(UUID.fromString(textSerializer(lore.get(1))))){
                                    listIterator.remove();
                                }
                            }
                            BTokens.getConfigHandler().saveConfig(conf);*/
                            for(String command : token.getCommands()){
                                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
                            }
                        }
                    }
                }
            }
        }
    }
}

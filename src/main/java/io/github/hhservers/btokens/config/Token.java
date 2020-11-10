package io.github.hhservers.btokens.config;

import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ConfigSerializable
@Data
public class Token {

    @Setting("tokenID")
    private String tokenID;

    @Setting("displayName")
    private String displayName = "&atokenDisplayname";

    @Setting("description")
    private String description = "&bRight click to redeem!";

    @Setting("commands")
    private List<String> commands = Arrays.asList("msg %p BToken redeemed!");

    @Setting("uuid")
    private UUID uuid = UUID.randomUUID();


}

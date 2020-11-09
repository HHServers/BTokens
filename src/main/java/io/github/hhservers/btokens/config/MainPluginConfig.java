package io.github.hhservers.btokens.config;

import lombok.Data;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConfigSerializable @Data
public class MainPluginConfig {

    @Setting(value = "tokenList")
    private List<Token> tokenList = new ArrayList<>();

}

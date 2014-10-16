package com.dominion.utils;

import com.dominion.common.playerAction.PlayerAction;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public final class PlayerActionSerializer extends JsonSerializer<PlayerAction> {

    @Override
    public void serialize(PlayerAction action, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("receiver");
        generator.writeString(action.receiver().name());
        generator.writeFieldName("phase");
        generator.writeNumber(action.phase().ordinal());
        generator.writeFieldName("action_name");
        generator.writeString(action.actionName());
        generator.writeObjectField("action_cards", action.eligibleCards());
        generator.writeFieldName("phase");
        generator.writeNumber(action.numCards());
        generator.writeEndObject();
        generator.writeEndObject();
    }

}

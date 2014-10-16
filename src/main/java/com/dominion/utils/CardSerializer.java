package com.dominion.utils;

import com.dominion.common.Card;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public final class CardSerializer extends JsonSerializer<Card> {

    @Override
    public void serialize(Card card, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString(card.name());
        generator.writeFieldName("cost");
        generator.writeNumber(card.cost());
        generator.writeFieldName("img");
        generator.writeString(card.image());
        generator.writeFieldName("desc");
        generator.writeString(card.desc());
        generator.writeEndObject();
    }

}

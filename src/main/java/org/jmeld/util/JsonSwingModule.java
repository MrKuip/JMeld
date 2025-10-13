package org.jmeld.util;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonSwingModule
  extends SimpleModule
{
  public JsonSwingModule()
  {
    addSerializer(new AwtColorSerializer(Color.class));
    addDeserializer(Color.class, new SimpleAwtColorDeserializer(Color.class));

    addSerializer(new AwtFontSerializer(Font.class));
    addDeserializer(Font.class, new SimpleAwtFontDeserializer(Font.class));
  }

  private static class AwtColorSerializer
    extends StdSerializer<Color>
  {
    private AwtColorSerializer(Class<Color> t)
    {
      super(t);
    }

    @Override
    public void serialize(Color value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
      jgen.writeStartObject();
      jgen.writeNumberField("red", value.getRed());
      jgen.writeNumberField("green", value.getGreen());
      jgen.writeNumberField("blue", value.getBlue());
      jgen.writeEndObject();
    }
  }

  private static class AwtFontSerializer
    extends StdSerializer<Font>
  {
    private AwtFontSerializer(Class<Font> t)
    {
      super(t);
    }

    @Override
    public void serialize(Font value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
      jgen.writeStartObject();
      jgen.writeStringField("family", value.getFamily());
      jgen.writeNumberField("style", value.getStyle());
      jgen.writeNumberField("size", value.getSize());
      jgen.writeEndObject();
    }
  }

  private static class SimpleAwtColorDeserializer
    extends StdDeserializer<Color>
  {
    private SimpleAwtColorDeserializer(Class<Color> t)
    {
      super(t);
    }

    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
      JsonNode node;

      node = p.getCodec().readTree(p);

      return new Color(node.get("red").asInt(), node.get("green").asInt(), node.get("blue").asInt());
    }
  }

  private static class SimpleAwtFontDeserializer
    extends StdDeserializer<Font>
  {
    private SimpleAwtFontDeserializer(Class<Font> t)
    {
      super(t);
    }

    @Override
    public Font deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
      JsonNode node;
      String family;
      int style;
      int size;

      node = p.getCodec().readTree(p);

      family = node.get("family").asText();
      style = node.get("style").asInt();
      size = node.get("size").asInt();

      return new Font(family, style, size);
    }
  }
}

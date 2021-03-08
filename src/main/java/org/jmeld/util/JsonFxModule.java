package org.jmeld.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class JsonFxModule
    extends SimpleModule
{
  public JsonFxModule()
  {
    addSerializer(new FxColorSerializer(Color.class));
    addDeserializer(Color.class, new SimpleFxColorDeserializer(Color.class));

    addSerializer(new FxFontSerializer(Font.class));
    addDeserializer(Font.class, new SimpleFxFontDeserializer(Font.class));

    addSerializer(new SimplePropertySerializer(Property.class));
    addDeserializer(SimpleBooleanProperty.class,
        new PropertyDeserializer<SimpleBooleanProperty>(SimpleBooleanProperty.class,
                                                        JsonNode::asBoolean));
    addDeserializer(SimpleIntegerProperty.class,
        new PropertyDeserializer<SimpleIntegerProperty>(SimpleIntegerProperty.class,
                                                        JsonNode::asInt));
    addDeserializer(SimpleStringProperty.class,
        new PropertyDeserializer<SimpleStringProperty>(SimpleStringProperty.class,
                                                       JsonNode::asText));
    addDeserializer(SimpleObjectProperty.class,
        new PropertyDeserializer<SimpleObjectProperty>(SimpleObjectProperty.class,
                                                       JsonNode::asText));
  }

  private static class FxColorSerializer
      extends StdSerializer<Color>
  {
    private FxColorSerializer(Class<Color> t)
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

  private static class FxFontSerializer
      extends StdSerializer<Font>
  {
    private FxFontSerializer(Class<Font> t)
    {
      super(t);
    }

    @Override
    public void serialize(Font value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
      jgen.writeStartObject();
      jgen.writeStringField("family", value.getFamily());
      jgen.writeStringField("style", value.getStyle());
      jgen.writeNumberField("size", value.getSize());
      jgen.writeEndObject();
    }
  }

  private static class SimpleFxColorDeserializer
      extends StdDeserializer<Color>
  {
    private SimpleFxColorDeserializer(Class<Color> t)
    {
      super(t);
    }

    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException
    {
      JsonNode node;

      node = p.getCodec().readTree(p);

      return Color.color(node.get("red").asDouble(), node.get("green").asDouble(), node.get("blue").asDouble());
    }
  }

  private static class SimpleFxFontDeserializer
      extends StdDeserializer<Font>
  {
    private SimpleFxFontDeserializer(Class<Font> t)
    {
      super(t);
    }

    @Override
    public Font deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException
    {
      JsonNode node;
      String family;
      String style;
      FontWeight fontWeight;
      FontPosture fontPosture;
      double size;

      node = p.getCodec().readTree(p);

      family = node.get("family").asText();
      style = node.get("style").asText();
      size = node.get("size").asDouble();

      fontWeight = getFontWeight(style);
      fontPosture = getFontPosture(style);

      return Font.font(family, fontWeight, fontPosture, size);
    }

    private FontPosture getFontPosture(String style)
    {
      for (String text : style.split(" "))
      {
        FontPosture fontPosture;

        fontPosture = FontPosture.findByName(text);
        if (fontPosture != null)
        {
          return fontPosture;
        }
      }

      return null;
    }

    private FontWeight getFontWeight(String style)
    {
      for (String text : style.split(" "))
      {
        FontWeight fontWeight;

        fontWeight = FontWeight.findByName(text);
        if (fontWeight != null)
        {
          return fontWeight;
        }
      }

      return null;
    }
  }

  private static class SimplePropertySerializer
      extends StdSerializer<Property>
  {
    private SimplePropertySerializer(Class<Property> t)
    {
      super(t);
    }

    @Override
    public void serialize(Property value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
      jgen.writeStartObject();
      jgen.writeObjectField("value", value.getValue());
      jgen.writeEndObject();
    }
  }

  private static class PropertyDeserializer<T extends Property>
      extends StdDeserializer<T>
  {
    private Function<JsonNode, Object> valueFunction;

    private PropertyDeserializer(Class<T> t,
        Function<JsonNode, Object> valueFunction)
    {
      super(t);

      this.valueFunction = valueFunction;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt, T property)
        throws IOException, JsonProcessingException
    {
      JsonNode node;

      node = p.getCodec().readTree(p);
      property.setValue(valueFunction.apply(node.get("value")));

      return property;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException
    {
      return null;
    }

    @Override
    public Boolean supportsUpdate(DeserializationConfig config)
    {
      return true;
    }
  }
}

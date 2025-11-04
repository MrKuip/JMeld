package org.jmeld.util;

import java.io.File;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper.Builder;

public class JsonPersister
{
  // class variables:
  private static JsonPersister instance1 = new JsonPersister();

  private ObjectMapper m_objectMapper;

  private JsonPersister()
  {
    init();
  }

  public static JsonPersister getInstance()
  {
    return instance1;
  }

  /**
   * Read a object of type 'clazz' from a file.
   */
  public <T> T read(File file, Class<T> clazz) throws Exception
  {
    return m_objectMapper.readValue(file, clazz);
  }

  /**
   * Write a object to a file.
   */
  public void write(File file, Object object) throws Exception
  {
    m_objectMapper.writeValue(file, object);
  }

  private void init()
  {
    Builder builder;

    builder = JsonMapper.builder();
    builder.enable(SerializationFeature.INDENT_OUTPUT);
    builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    builder.enable(MapperFeature.AUTO_DETECT_GETTERS);
    builder.enable(MapperFeature.AUTO_DETECT_SETTERS);
    builder.enable(MapperFeature.AUTO_DETECT_IS_GETTERS);
    builder.disable(MapperFeature.AUTO_DETECT_FIELDS);
    builder.disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);
    builder.visibility(PropertyAccessor.FIELD, Visibility.NONE);
    builder.visibility(PropertyAccessor.GETTER, Visibility.ANY);
    builder.visibility(PropertyAccessor.SETTER, Visibility.ANY);
    builder.visibility(PropertyAccessor.IS_GETTER, Visibility.ANY);
    builder.defaultMergeable(true);
    builder.addModule(new JsonFxModule());
    builder.addModule(new JsonSwingModule());

    m_objectMapper = builder.build();
  }
}

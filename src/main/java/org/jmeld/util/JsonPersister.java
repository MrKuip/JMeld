package org.jmeld.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;

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
  public <T> T read(File file, Class<T> clazz)
      throws Exception
  {
    return m_objectMapper.readValue(file, clazz);
  }

  /**
   * Write a object to a file.
   */
  public void write(File file, Object object)
      throws Exception
  {
    m_objectMapper.writeValue(file, object);
  }

  private void init()
  {
    m_objectMapper = new ObjectMapper();
    m_objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    m_objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    m_objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    m_objectMapper.enable(MapperFeature.AUTO_DETECT_GETTERS);
    m_objectMapper.enable(MapperFeature.AUTO_DETECT_SETTERS);
    m_objectMapper.enable(MapperFeature.AUTO_DETECT_IS_GETTERS);
    m_objectMapper.disable(MapperFeature.AUTO_DETECT_FIELDS);
    m_objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
    m_objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.ANY);
    m_objectMapper.setVisibility(PropertyAccessor.SETTER, Visibility.ANY);
    m_objectMapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.ANY);
    m_objectMapper.setDefaultMergeable(true);

    m_objectMapper.registerModule(new JsonFxModule());
  }
}

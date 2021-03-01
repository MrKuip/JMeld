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
  private static JsonPersister instance = new JsonPersister();

  private ObjectMapper m_objectMapper;

  private JsonPersister()
  {
    init();
  }

  public static JsonPersister getInstance()
  {
    return instance;
  }

  /**
   * Read a object of type 'clazz' from a file.
   */
  public <T> T read(Class<T> clazz,
      File file)
      throws Exception
  {
    return m_objectMapper.readValue(file,
                                    clazz);
  }

  /**
   * Write a object to a file.
   */
  public void write(Object object,
      File file)
      throws Exception
  {
    m_objectMapper.writeValue(file,
                              object);
  }

  private void init()
  {
    m_objectMapper = new ObjectMapper();
    m_objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    m_objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    m_objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    m_objectMapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
    m_objectMapper.disable(MapperFeature.AUTO_DETECT_SETTERS);
    m_objectMapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
    m_objectMapper.enable(MapperFeature.AUTO_DETECT_FIELDS);
    m_objectMapper.setVisibility(PropertyAccessor.FIELD,
                                 Visibility.ANY);

  }
}

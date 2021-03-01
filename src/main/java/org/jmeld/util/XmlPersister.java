package org.jmeld.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;

public class XmlPersister
{
  // class variables:
  private static XmlPersister instance = new XmlPersister();

  private XmlMapper m_objectMapper;

  private XmlPersister()
  {
    init();
  }

  public static XmlPersister getInstance()
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

  public <T> T read(Class<T> clazz,
      byte[] data)
      throws Exception
  {
    return m_objectMapper.readValue(data,
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
    m_objectMapper = new XmlMapper();
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

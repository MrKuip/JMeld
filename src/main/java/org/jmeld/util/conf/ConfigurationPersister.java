package org.jmeld.util.conf;

import java.io.File;
import java.io.FileNotFoundException;
import org.jmeld.util.JsonPersister;

public class ConfigurationPersister
{
  // class variables:
  private static ConfigurationPersister instance = new ConfigurationPersister();

  private ConfigurationPersister()
  {
  }

  public static ConfigurationPersister getInstance()
  {
    return instance;
  }

  /**
   * Read a configuration of type 'clazz' from a file.
   */
  public <T extends AbstractConfiguration> T read(Class<T> clazz, File file)
      throws FileNotFoundException
  {
    T configuration;

    try
    {
      configuration = JsonPersister.getInstance().read(file, clazz);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }

    configuration.init();

    return configuration;
  }

  /**
   * Write a configuration to a file.
   */
  public void write(AbstractConfiguration configuration, File file)
      throws Exception
  {
    JsonPersister.getInstance().write(file, configuration);
  }
}

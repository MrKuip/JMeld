package org.jmeld.util.conf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jmeld.util.WeakHashSet;

public class ConfigurationManager
{
  // class variables:
  private static ConfigurationManager instance = new ConfigurationManager();

  // instance variables:
  private Map<String, AbstractConfiguration> configurations = new HashMap<>();
  private Map<String, WeakHashSet<ConfigurationListenerIF>> listenerMap = new HashMap<>();

  private ConfigurationManager()
  {
  }

  public static ConfigurationManager getInstance()
  {
    return instance;
  }

  public boolean reload(File file,
      Class<? extends AbstractConfiguration> clazz)
  {
    AbstractConfiguration configuration;

    configuration = load(clazz,
                         file);
    if (configuration == null)
    {
      return false;
    }

    // Set the new filename AFTER the load was succesfull!
    configuration.setConfigurationFile(file);

    configurations.put(clazz.getName(),
                       configuration);

    // Let everybody know that there is a new configuration!
    fireChanged(clazz);

    return true;
  }

  public AbstractConfiguration get(Class<? extends AbstractConfiguration> clazz)
  {
    AbstractConfiguration configuration;
    String key;

    key = clazz.getName();

    configuration = configurations.get(key);
    if (configuration == null)
    {
      configuration = load(clazz);
      if (configuration == null)
      {
        try
        {
          configuration = clazz.newInstance();
          configuration.disableFireChanged(true);
          configuration.init();
          configuration.disableFireChanged(false);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }

      configurations.put(key,
                         configuration);
    }

    return configuration;
  }

  private AbstractConfiguration load(Class<? extends AbstractConfiguration> clazz)
  {
    ConfigurationPreference preference;
    File file;

    preference = new ConfigurationPreference(clazz);
    file = preference.getFile();

    return load(clazz,
                file);
  }

  private AbstractConfiguration load(Class<? extends AbstractConfiguration> clazz,
      File file)
  {
    if (file.exists())
    {
      try
      {
        return ConfigurationPersister.getInstance().read(clazz,
                                                         file);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }

    return null;
  }

  void addConfigurationListener(Class<? extends AbstractConfiguration> clazz,
      ConfigurationListenerIF listener)
  {
    WeakHashSet<ConfigurationListenerIF> listeners;
    String key;

    key = clazz.getName();

    listeners = listenerMap.get(key);
    if (listeners == null)
    {
      listeners = new WeakHashSet<ConfigurationListenerIF>();
      listenerMap.put(key,
                      listeners);
    }

    listeners.add(listener);
  }

  void removeConfigurationListener(Class<? extends AbstractConfiguration> clazz,
      ConfigurationListenerIF listener)
  {
    Set<ConfigurationListenerIF> listeners;

    listeners = listenerMap.get(clazz.getName());
    if (listeners != null)
    {
      listeners.remove(listener);
    }
  }

  void fireChanged(Class<? extends AbstractConfiguration> clazz)
  {
    Set<ConfigurationListenerIF> listeners;

    listeners = listenerMap.get(clazz.getName());
    if (listeners != null)
    {
      for (ConfigurationListenerIF listener : listeners)
      {
        listener.configurationChanged();
      }
    }
  }
}

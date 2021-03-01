package org.jmeld.util.conf;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.File;
import java.io.IOException;

@JsonTypeInfo(use = Id.CLASS)
public abstract class AbstractConfiguration
{
  private transient boolean changed;
  private transient ConfigurationPreference preference;
  private transient boolean disableFireChanged;

  public AbstractConfiguration()
  {
    preference = new ConfigurationPreference(getClass());
  }

  public abstract void init();

  public String getConfigurationFileName()
  {
    try
    {
      return preference.getFile().getCanonicalPath();
    }
    catch (IOException ex)
    {
      return "??";
    }
  }

  public void setConfigurationFile(File file)
  {
    preference.setFile(file);
  }

  public boolean isChanged()
  {
    return changed;
  }

  public void save()
  {
    try
    {
      ConfigurationPersister.getInstance().write(this,
                                                preference.getFile());
      changed = false;
      fireChanged(changed);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void addConfigurationListener(ConfigurationListenerIF listener)
  {
    getManager().addConfigurationListener(getClass(),
                                          listener);
  }

  public void removeConfigurationListener(ConfigurationListenerIF listener)
  {
    getManager().removeConfigurationListener(getClass(),
                                             listener);
  }

  void disableFireChanged(boolean disableFireChanged)
  {
    this.disableFireChanged = disableFireChanged;
  }

  public void fireChanged()
  {
    if (disableFireChanged)
    {
      return;
    }

    fireChanged(true);
  }

  public void fireChanged(boolean changed)
  {
    this.changed = changed;
    getManager().fireChanged(getClass());
  }

  private ConfigurationManager getManager()
  {
    return ConfigurationManager.getInstance();
  }
}

package org.jmeld.util.conf;

import java.util.*;

abstract public class AbstractConfigurationElement
{
  protected AbstractConfiguration configuration;

  public AbstractConfigurationElement()
  {
  }

  public void init(AbstractConfiguration configuration)
  {
    this.configuration = configuration;
  }

  public void fireChanged()
  {
    if (configuration != null)
    {
      configuration.fireChanged();
    }
  }
}
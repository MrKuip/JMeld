package org.jmeld.util.conf;

abstract public class AbstractConfigurationElement
{
  protected transient AbstractConfiguration configuration;

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

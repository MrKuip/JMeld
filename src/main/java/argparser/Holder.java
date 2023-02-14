package argparser;

import java.io.Serializable;

/**
 * Wrapper class which ``holds'' a character value, enabling methods to return character values through arguments.
 */
public class Holder<T>
    implements Serializable
{
  /** Serial Unique Version Id (SUID) */
  private static final long serialVersionUID = -1L;

  /**
   * Has this holder been initialized.
   */
  private boolean m_initialized;
  private boolean m_mandatory;
  private T m_value;

  public Holder()
  {
  }

  public final void setValue(T value)
  {
    m_value = value;
    setInitialized(true);
  }

  public final T getValue()
  {
    return m_value;
  }

  protected final void setInitialized(boolean initialized)
  {
    m_initialized = initialized;
  }

  public final boolean isInitialized()
  {
    return m_initialized;
  }

  final void setMandatory(boolean mandatory)
  {
    m_mandatory = mandatory;
  }

  public final boolean isMandatory()
  {
    return m_mandatory;
  }
}

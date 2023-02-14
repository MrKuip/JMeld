package argparser;

/**
 * Wrapper class which ``holds'' an Object reference, enabling methods to return Object references through arguments.
 */
public class ObjectHolder
    extends Holder<Object>
{
  /**
   * Constructs a new <code>ObjectHolder</code> with an initial value of <code>null</code>.
   */
  public ObjectHolder()
  {
    this(null);
  }

  /**
   * Constructs a new <code>ObjectHolder</code> with a specific initial value.
   * 
   * @param o
   *          Initial Object reference.
   */
  public ObjectHolder(Object value)
  {
    setValue(value);
  }

  private static final long serialVersionUID = 1L;
}

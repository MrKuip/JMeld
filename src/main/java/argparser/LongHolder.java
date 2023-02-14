package argparser;

/**
 * Wrapper class which ``holds'' a long value, enabling methods to return long values through arguments.
 */
public class LongHolder
    extends Holder<Long>
{
  /**
   * Constructs a new <code>LongHolder</code> with an initial value of 0.
   */
  public LongHolder()
  {
    this(0);
  }

  /**
   * Constructs a new <code>LongHolder</code> with a specific initial value.
   * 
   * @param value
   *          Initial long value.
   */
  public LongHolder(long value)
  {
    setValue(value);
  }

  private static final long serialVersionUID = 1L;
}

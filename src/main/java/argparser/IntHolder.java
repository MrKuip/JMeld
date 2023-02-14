package argparser;

/**
 * Wrapper class which ``holds'' an integer value, enabling methods to return integer values through arguments.
 */
public class IntHolder
    extends Holder<Integer>
{
  /**
   * Constructs a new <code>IntHolder</code> with an initial value of 0.
   */
  public IntHolder()
  {
    this(0);
  }

  /**
   * Constructs a new <code>IntHolder</code> with a specific initial value.
   * 
   * @param i
   *          Initial integer value.
   */
  public IntHolder(int value)
  {
    setValue(value);
  }

  private static final long serialVersionUID = 1L;
}

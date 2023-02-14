package argparser;

/**
 * Wrapper class which ``holds'' a double value, enabling methods to return double values through arguments.
 */
public class DoubleHolder
    extends Holder<Double>
{
  /**
   * Constructs a new <code>DoubleHolder</code> with an initial value of 0.
   */
  public DoubleHolder()
  {
    this(0);
  }

  /**
   * Constructs a new <code>DoubleHolder</code> with a specific initial value.
   * 
   * @param d
   *          Initial double value.
   */
  public DoubleHolder(double value)
  {
    setValue(value);
  }

  private static final long serialVersionUID = 1L;
}

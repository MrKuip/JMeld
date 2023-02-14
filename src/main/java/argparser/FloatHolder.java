package argparser;

/**
 * Wrapper class which ``holds'' a float value, enabling methods to return float values through arguments.
 */
public class FloatHolder
    extends Holder<Float>
{
  /**
   * Constructs a new <code>FloatHolder</code> with an initial value of 0.
   */
  public FloatHolder()
  {
    this(0);
  }

  /**
   * Constructs a new <code>FloatHolder</code> with a specific initial value.
   * 
   * @param f
   *          Initial float value.
   */
  public FloatHolder(float value)
  {
    setValue(value);
  }

  private static final long serialVersionUID = 1L;
}

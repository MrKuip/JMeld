package argparser;

/**
 * Wrapper class which ``holds'' a boolean value, enabling methods to return boolean values through arguments.
 */
public class BooleanHolder
    extends Holder<Boolean>
{
  /**
   * Constructs a new <code>BooleanHolder</code> with an initial value of <code>false</code>.
   */
  public BooleanHolder()
  {
    setValue(Boolean.FALSE);
  }

  /**
   * Constructs a new <code>BooleanHolder</code> with a specific initial value.
   * 
   * @param b
   *          Initial boolean value.
   */
  public BooleanHolder(boolean b)
  {
    setValue(b);
  }

  private static final long serialVersionUID = 1L;
}

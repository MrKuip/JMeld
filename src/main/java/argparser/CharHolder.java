package argparser;

/**
 * Wrapper class which ``holds'' a character value, enabling methods to return character values through arguments.
 */
public class CharHolder
    extends Holder<Character>
{
  /**
   * Constructs a new <code>CharHolder</code> with an initial value of 0.
   */
  public CharHolder()
  {
    setValue(Character.valueOf('0'));
  }

  /**
   * Constructs a new <code>CharHolder</code> with a specific initial value.
   * 
   * @param c
   *          Initial character value.
   */
  public CharHolder(char value)
  {
    setValue(value);
  }

  private static final long serialVersionUID = 1L;
}

package argparser;

/**
 * Wrapper class which ``holds'' a String reference, enabling methods to return String references through arguments.
 */
public class StringHolder
    extends Holder<String>
{
  /**
   * Constructs a new <code>StringHolder</code> with an initial value of <code>null</code>.
   */
  public StringHolder()
  {
    this(null);
  }

  /**
   * Constructs a new <code>StringHolder</code> with a specific initial value.
   * 
   * @param s
   *          Initial String reference.
   */
  public StringHolder(String s)
  {
    setValue(s);
  }

  private static final long serialVersionUID = 1L;
}

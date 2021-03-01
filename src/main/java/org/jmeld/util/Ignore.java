package org.jmeld.util;

import org.jmeld.util.conf.AbstractConfiguration;
import org.jmeld.util.conf.AbstractConfigurationElement;

public class Ignore
    extends AbstractConfigurationElement
{
  // Class variables:
  static public final Ignore NULL_IGNORE = new Ignore();

  // Instance variables:
  public boolean ignoreWhitespaceAtBegin;
  public boolean ignoreWhitespaceInBetween;
  public boolean ignoreWhitespaceAtEnd;
  public boolean ignoreEOL;
  public boolean ignoreBlankLines;
  public boolean ignoreCase;
  // Transient:
  public transient boolean ignore;
  public transient boolean ignoreWhitespace;

  public Ignore(Ignore ignore)
  {
    this(ignore.ignoreWhitespaceAtBegin,
         ignore.ignoreWhitespaceInBetween,
         ignore.ignoreWhitespaceAtEnd,
         ignore.ignoreEOL,
         ignore.ignoreBlankLines,
         ignore.ignoreCase);
  }

  public Ignore()
  {
    this(false,
         false,
         false);
  }

  public Ignore(boolean ignoreWhitespace,
      boolean ignoreEOL,
      boolean ignoreBlankLines)
  {
    this(ignoreWhitespace,
         ignoreWhitespace,
         ignoreWhitespace,
         ignoreEOL,
         ignoreBlankLines,
         false);
  }

  public Ignore(boolean ignoreWhitespaceAtBegin,
      boolean ignoreWhitespaceInBetween,
      boolean ignoreWhitespaceAtEnd,
      boolean ignoreEOL,
      boolean ignoreBlankLines,
      boolean ignoreCase)
  {
    this.ignoreWhitespaceAtBegin = ignoreWhitespaceAtBegin;
    this.ignoreWhitespaceInBetween = ignoreWhitespaceInBetween;
    this.ignoreWhitespaceAtEnd = ignoreWhitespaceAtEnd;
    this.ignoreEOL = ignoreEOL;
    this.ignoreBlankLines = ignoreBlankLines;
    this.ignoreCase = ignoreCase;

    init();
  }

  @Override
  public void init(AbstractConfiguration configuration)
  {
    super.init(configuration);
    init();
  }

  private void init()
  {
    this.ignore = (ignoreWhitespaceAtBegin || ignoreWhitespaceInBetween || ignoreWhitespaceAtEnd || ignoreEOL
        || ignoreBlankLines || ignoreCase);
    this.ignoreWhitespace = (ignoreWhitespaceAtBegin || ignoreWhitespaceInBetween || ignoreWhitespaceAtEnd);
  }

  @Override
  public String toString()
  {
    return "ignore: " + (!ignore ? "nothing" : "") + (ignoreWhitespaceAtBegin ? "whitespace[begin] " : "")
        + (ignoreWhitespaceInBetween ? "whitespace[in between] " : "")
        + (ignoreWhitespaceAtEnd ? "whitespace[end] " : "") + (ignoreEOL ? "eol " : "")
        + (ignoreBlankLines ? "blanklines " : "") + (ignoreCase ? "case " : "");
  }
}

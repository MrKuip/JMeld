/**
 * Copyright John E. Lloyd, 2004. All rights reserved. Permission to use, copy, modify and redistribute is granted,
 * provided that this copyright notice is retained and the author is given credit whenever appropriate.
 *
 * This software is distributed "as is", without any warranty, including any implied warranty of merchantability or
 * fitness for a particular use. The author assumes no responsibility for, and shall not be liable for, any special,
 * indirect, or consequential damages, or any damages whatsoever, arising out of or in connection with the use of this
 * software.
 */
package argparser;

class StringScanner
{
  private char[] m_buf;
  private int m_idx;
  private int m_len;
  private String m_stringDelimiters = "";

  public StringScanner(String s)
  {
    m_buf = new char[s.length() + 1];
    s.getChars(0,
               s.length(),
               m_buf,
               0);
    m_len = s.length();
    m_buf[m_len] = 0;
    m_idx = 0;
  }

  public int getIndex()
  {
    return m_idx;
  }

  public void setIndex(int i)
  {
    if (i < 0)
    {
      m_idx = 0;
    }
    else if (i > m_len)
    {
      m_idx = m_len;
    }
    else
    {
      m_idx = i;
    }
  }

  public void setStringDelimiters(String s)
  {
    m_stringDelimiters = s;
  }

  public String getStringDelimiters()
  {
    return m_stringDelimiters;
  }

  public char scanChar()
      throws StringScanException
  {
    int idxSave = m_idx;
    skipWhiteSpace();
    try
    {
      if (m_buf[m_idx] == '\'')
      {
        return scanQuotedChar();
      }
      else
      {
        return scanUnquotedChar();
      }
    }
    catch (StringScanException e)
    {
      m_idx = idxSave;
      throw e;
    }
  }

  public char scanQuotedChar()
      throws StringScanException
  {
    StringScanException exception = null;
    char retval = 0;
    int idxSave = m_idx;

    skipWhiteSpace();
    if (m_idx == m_len)
    {
      exception = new StringScanException(m_idx,
                                          "end of input");
    }
    else if (m_buf[m_idx++] == '\'')
    {
      try
      {
        retval = scanUnquotedChar();
      }
      catch (StringScanException e)
      {
        exception = e;
      }
      if (exception == null)
      {
        if (m_idx == m_len)
        {
          exception = new StringScanException(m_idx,
                                              "end of input");
        }
        else if (m_buf[m_idx++] != '\'')
        {
          exception = new StringScanException(m_idx - 1,
                                              "unclosed quoted character");
        }
      }
    }
    else
    {
      exception = new StringScanException(m_idx - 1,
                                          "uninitialized quoted character");
    }
    if (exception != null)
    {
      m_idx = idxSave;
      throw exception;
    }
    return retval;
  }

  public char scanUnquotedChar()
      throws StringScanException
  {
    StringScanException exception = null;
    char c;
    char retval = 0;
    int idxSave = m_idx;

    if (m_idx == m_len)
    {
      exception = new StringScanException(m_idx,
                                          "end of input");
    }
    else if ((c = m_buf[m_idx++]) == '\\')
    {
      if (m_idx == m_len)
      {
        exception = new StringScanException(m_idx,
                                            "end of input");
      }
      else
      {
        c = m_buf[m_idx++];
        if (c == '"')
        {
          retval = '"';
        }
        else if (c == '\'')
        {
          retval = '\'';
        }
        else if (c == '\\')
        {
          retval = '\\';
        }
        else if (c == 'n')
        {
          retval = '\n';
        }
        else if (c == 't')
        {
          retval = '\t';
        }
        else if (c == 'b')
        {
          retval = '\b';
        }
        else if (c == 'r')
        {
          retval = '\r';
        }
        else if (c == 'f')
        {
          retval = '\f';
        }
        else if ('0' <= c && c < '8')
        {
          int v = c - '0';
          for (int j = 0; j < 2; j++)
          {
            if (m_idx == m_len)
            {
              break;
            }
            c = m_buf[m_idx];
            if ('0' <= c && c < '8' && (v * 8 + (c - '0')) <= 255)
            {
              v = v * 8 + (c - '0');
              m_idx++;
            }
            else
            {
              break;
            }
          }
          retval = (char) v;
        }
        else
        {
          exception = new StringScanException(m_idx - 1,
                                              "illegal escape character '" + c + "'");
        }
      }
    }
    else
    {
      retval = c;
    }
    if (exception != null)
    {
      m_idx = idxSave;
      throw exception;
    }
    return retval;
  }

  public String scanQuotedString()
      throws StringScanException
  {
    StringScanException exception = null;
    StringBuilder sbuf = new StringBuilder(m_len);
    char c;
    int idxSave = m_idx;

    skipWhiteSpace();
    if (m_idx == m_len)
    {
      exception = new StringScanException(m_idx,
                                          "end of input");
    }
    else if ((c = m_buf[m_idx++]) == '"')
    {
      while (m_idx < m_len && (c = m_buf[m_idx]) != '"' && c != '\n')
      {
        if (c == '\\')
        {
          try
          {
            c = scanUnquotedChar();
          }
          catch (StringScanException e)
          {
            exception = e;
            break;
          }
        }
        else
        {
          m_idx++;
        }
        sbuf.append(c);
      }
      if (exception == null && m_idx >= m_len)
      {
        exception = new StringScanException(m_len,
                                            "end of input");
      }
      else if (exception == null && c == '\n')
      {
        exception = new StringScanException(m_idx,
                                            "unclosed quoted string");
      }
      else
      {
        m_idx++;
      }
    }
    else
    {
      exception = new StringScanException(m_idx - 1,
                                          "quoted string must start with \"");
    }
    if (exception != null)
    {
      m_idx = idxSave;
      throw exception;
    }
    return sbuf.toString();
  }

  public String scanNonWhiteSpaceString()
      throws StringScanException
  {
    StringBuilder sbuf = new StringBuilder(m_len);
    int idxSave = m_idx;
    char c;

    skipWhiteSpace();
    if (m_idx == m_len)
    {
      StringScanException e = new StringScanException(m_idx,
                                                      "end of input");
      m_idx = idxSave;
      throw e;
    }
    else
    {
      c = m_buf[m_idx++];
      while (m_idx < m_len && !Character.isWhitespace(c) && m_stringDelimiters.indexOf(c) == -1)
      {
        sbuf.append(c);
        c = m_buf[m_idx++];
      }
      if (Character.isWhitespace(c) || m_stringDelimiters.indexOf(c) != -1)
      {
        m_idx--;
      }
      else
      {
        sbuf.append(c);
      }
    }
    return sbuf.toString();
  }

  public String scanString()
      throws StringScanException
  {
    int idxSave = m_idx;
    skipWhiteSpace();
    try
    {
      if (m_buf[m_idx] == '"')
      {
        return scanQuotedString();
      }
      else
      {
        return scanNonWhiteSpaceString();
      }
    }
    catch (StringScanException e)
    {
      m_idx = idxSave;
      throw e;
    }
  }

  public String getString()
  {
    StringBuilder sbuf = new StringBuilder(m_len);
    while (m_idx < m_len)
    {
      sbuf.append(m_buf[m_idx++]);
    }
    return sbuf.toString();
  }

  public long scanInt()
      throws StringScanException
  {
    int idxSave = m_idx;
    char c;
    int sign = 1;

    skipWhiteSpace();
    if ((c = m_buf[m_idx]) == '-' || c == '+')
    {
      sign = (c == '-' ? -1 : 1);
      m_idx++;
    }
    try
    {
      if (m_idx == m_len)
      {
        throw new StringScanException(m_len,
                                      "end of input");
      }
      else if (m_buf[m_idx] == '0')
      {
        if ((c = m_buf[m_idx + 1]) == 'x' || c == 'X')
        {
          m_idx += 2;
          return sign * scanInt(16,
                                false);
        }
        else
        {
          return sign * scanInt(8,
                                false);
        }
      }
      else
      {
        return sign * scanInt(10,
                              false);
      }
    }
    catch (StringScanException e)
    {
      m_idx = idxSave;
      throw e;
    }
  }

  public long scanInt(int radix)
      throws StringScanException
  {
    return scanInt(radix, /* skipWhite= */
                   true);
  }

  private String baseDesc(int radix)
  {
    switch (radix)
    {

      case 10:
        return "decimal";

      case 8:
        return "octal";

      case 16:
        return "hex";

      default:
        return "base " + radix;
    }
  }

  public long scanInt(int radix,
      boolean skipWhite)
      throws StringScanException
  {
    StringScanException exception = null;
    int charval;
    int idxSave = m_idx;
    char c;
    long val = 0;
    boolean negate = false;

    if (skipWhite)
    {
      skipWhiteSpace();
    }
    if ((c = m_buf[m_idx]) == '-' || c == '+')
    {
      negate = (c == '-');
      m_idx++;
    }
    if (m_idx >= m_len)
    {
      exception = new StringScanException(m_len,
                                          "end of input");
    }
    else if ((charval = Character.digit(m_buf[m_idx++],
                                        radix)) == -1)
    {
      exception = new StringScanException(m_idx - 1,
                                          "malformed " + baseDesc(radix) + " integer");
    }
    else
    {
      val = charval;
      while ((charval = Character.digit(m_buf[m_idx],
                                        radix)) != -1)
      {
        val = val * radix + charval;
        m_idx++;
      }
      if (Character.isLetter(c = m_buf[m_idx]) || Character.isDigit(c) || c == '_')
      {
        exception = new StringScanException(m_idx,
                                            "malformed " + baseDesc(radix) + " integer");
      }
    }
    if (exception != null)
    {
      m_idx = idxSave;
      throw exception;
    }
    return negate ? -val : val;
  }

  public double scanDouble()
      throws StringScanException
  {
    StringScanException exception = null;
    int idxSave = m_idx;
    char c;

    // parse [-][0-9]*[.][0-9]*[eE][-][0-9]*
    boolean hasDigits = false;
    double value = 0;

    skipWhiteSpace();
    if (m_idx == m_len)
    {
      exception = new StringScanException("end of input");
    }
    else
    {
      if ((c = m_buf[m_idx]) == '-' || c == '+')
      {
        m_idx++;
      }
      if (matchDigits())
      {
        hasDigits = true;
      }
      if (m_buf[m_idx] == '.')
      {
        m_idx++;
      }
      if (!hasDigits && (m_buf[m_idx] < '0' || m_buf[m_idx] > '9'))
      {
        if (m_idx == m_len)
        {
          exception = new StringScanException(m_idx,
                                              "end of input");
        }
        else
        {
          exception = new StringScanException(m_idx,
                                              "malformed floating number: no digits");
        }
      }
      else
      {
        matchDigits();

        if ((c = m_buf[m_idx]) == 'e' || c == 'E')
        {
          m_idx++;
          if ((c = m_buf[m_idx]) == '-' || c == '+')
          {
            m_idx++;
          }
          if (m_buf[m_idx] < '0' || m_buf[m_idx] > '9')
          {
            if (m_idx == m_len)
            {
              exception = new StringScanException(m_idx,
                                                  "end of input");
            }
            else
            {
              exception = new StringScanException(m_idx,
                                                  "malformed floating number: no digits in exponent");
            }
          }
          else
          {
            matchDigits();
          }
        }
      }
    }
    if (exception == null)
    {
      // if (Character.isLetterOrDigit(c=buf[idx]) || c == '_')
      // { exception = new StringScanException (idx,
      // "malformed floating number");
      // }
      // else
      {
        try
        {
          value = Double.parseDouble(new String(m_buf,
                                                idxSave,
                                                m_idx - idxSave));
        }
        catch (NumberFormatException e)
        {
          exception = new StringScanException(m_idx,
                                              "malformed floating number");
        }
      }
    }
    if (exception != null)
    {
      m_idx = idxSave;
      throw exception;
    }
    return value;
  }

  public boolean scanBoolean()
      throws StringScanException
  {
    StringScanException exception = null;
    int idxSave = m_idx;
    String testStr = "false";
    boolean testval = false;
    char c;

    skipWhiteSpace();
    if (m_buf[m_idx] == 't')
    {
      testStr = "true";
      testval = true;
    }
    else
    {
      testval = false;
    }
    int i = 0;
    for (i = 0; i < testStr.length(); i++)
    {
      if (testStr.charAt(i) != m_buf[m_idx])
      {
        if (m_idx == m_len)
        {
          exception = new StringScanException(m_idx,
                                              "end of input");
        }
        break;
      }
      m_idx++;
    }
    if (exception == null)
    {
      if (i < testStr.length() || Character.isLetterOrDigit(c = m_buf[m_idx]) || c == '_')
      {
        exception = new StringScanException(m_idx,
                                            "illegal boolean");
      }
    }
    if (exception != null)
    {
      m_idx = idxSave;
      throw exception;
    }
    return testval;
  }

  public boolean matchString(String s)
  {
    int k = m_idx;
    for (int i = 0; i < s.length(); i++)
    {
      if (k >= m_len || s.charAt(i) != m_buf[k++])
      {
        return false;
      }
    }
    m_idx = k;
    return true;
  }

  public boolean matchDigits()
  {
    int k = m_idx;
    char c;

    while ((c = m_buf[k]) >= '0' && c <= '9')
    {
      k++;
    }
    if (k > m_idx)
    {
      m_idx = k;
      return true;
    }
    else
    {
      return false;
    }
  }

  public void skipWhiteSpace()
  {
    while (Character.isWhitespace(m_buf[m_idx]))
    {
      m_idx++;
    }
  }

  public boolean atEnd()
  {
    return m_idx == m_len;
  }

  public boolean atBeginning()
  {
    return m_idx == 0;
  }

  public void ungetc()
  {
    if (m_idx > 0)
    {
      m_idx--;
    }
  }

  public char getc()
  {
    char c = m_buf[m_idx];
    if (m_idx < m_len)
    {
      m_idx++;
    }
    return c;
  }

  public char peekc()
  {
    return m_buf[m_idx];
  }

  public String substring(int i0,
      int i1)
  {
    if (i0 < 0)
    {
      i0 = 0;
    }
    else if (i0 >= m_len)
    {
      i0 = m_len - 1;
    }
    if (i1 < 0)
    {
      i1 = 0;
    }
    else if (i1 > m_len)
    {
      i1 = m_len;
    }
    if (i1 <= i0)
    {
      return "";
    }
    return new String(m_buf,
                      i0,
                      i1 - i0);
  }

  public String substring(int i0)
  {
    if (i0 < 0)
    {
      i0 = 0;
    }
    if (i0 >= m_len)
    {
      return "";
    }
    else
    {
      return new String(m_buf,
                        i0,
                        m_len - i0);
    }
  }
}

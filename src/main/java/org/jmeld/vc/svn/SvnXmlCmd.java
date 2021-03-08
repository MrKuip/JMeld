package org.jmeld.vc.svn;

import org.jmeld.util.Result;
import org.jmeld.util.XmlPersister;
import org.jmeld.vc.util.VcCmd;

public class SvnXmlCmd<T>
    extends VcCmd<T>
{
  private Class<T> clazz;

  public SvnXmlCmd(Class<T> clazz)
  {
    this.clazz = clazz;
  }

  @Override
  public void build(byte[] data)
  {
    try
    {
      setResultData(XmlPersister.getInstance().read(clazz,
                                                    data));
      setResult(Result.TRUE());
    }
    catch (Exception ex)
    {
      setResult(Result.FALSE(ex.getMessage(),
                             ex));
    }
  }
}

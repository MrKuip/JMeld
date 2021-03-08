package org.jmeld.vc.git;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
    Result result;
    InputStream is;
    ByteArrayOutputStream baos;

    try
    {
      setResultData(XmlPersister.getInstance().read(clazz,
                                                    data));

      /*
      is = new ByteArrayInputStream(data);
      setResultData(JaxbPersister.getInstance().load(clazz,
                                                     is));
      is.close();
      */
      setResult(Result.TRUE());
    }
    catch (Exception ex)
    {
      setResult(Result.FALSE(ex.getMessage(),
                             ex));
    }
  }
}

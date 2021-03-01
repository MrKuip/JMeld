package org.jmeld.vc.git;

import java.io.File;
import org.jmeld.util.Result;
import org.jmeld.vc.BaseFile;
import org.jmeld.vc.util.VcCmd;

public class CatCmd
    extends VcCmd<BaseFile>
{
  // Instance variables:
  private File file;

  public CatCmd(File file)
  {
    this.file = file;
  }

  public Result execute()
  {
    super.execute("git",
                  "show",
                  "HEAD:" + file.getPath());

    return getResult();
  }

  protected void build(byte[] data)
  {
    setResultData(new BaseFile(data));
  }

  public static void main(String[] args)
  {
    BaseFile result;
    byte[] byteArray;

    try
    {
      result = new GitVersionControl().getBaseFile(new File(args[0]));
      byteArray = result.getByteArray();
      System.out.write(byteArray,
                       0,
                       byteArray.length);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

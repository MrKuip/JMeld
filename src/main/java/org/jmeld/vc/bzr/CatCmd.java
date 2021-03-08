package org.jmeld.vc.bzr;

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

    initWorkingDirectory(file);
  }

  public Result execute()
  {
    super.execute("bzr",
                  "cat",
                  "--noninteractive",
                  file.getAbsolutePath());

    return getResult();
  }

  protected void build(byte[] data)
  {
    setResultData(new BaseFile(data));
  }
}

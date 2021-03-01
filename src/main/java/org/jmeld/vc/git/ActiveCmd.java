package org.jmeld.vc.git;

import java.io.File;
import org.jmeld.util.Result;
import org.jmeld.vc.util.VcCmd;

public class ActiveCmd
    extends VcCmd<Boolean>
{
  private File file;

  public ActiveCmd(File file)
  {
    this.file = file;
  }

  public Result execute()
  {
    super.execute("git",
                  "status",
                  file.getAbsolutePath());

    return getResult();
  }

  protected void build(byte[] data)
  {
    setResultData(Boolean.TRUE);
  }
}

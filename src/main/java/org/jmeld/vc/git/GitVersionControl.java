package org.jmeld.vc.git;

import java.io.File;
import org.jmeld.vc.BaseFile;
import org.jmeld.vc.BlameIF;
import org.jmeld.vc.DiffIF;
import org.jmeld.vc.StatusResult;
import org.jmeld.vc.VersionControlIF;
import org.jmeld.vc.svn.BlameCmd;

public class GitVersionControl
    implements VersionControlIF
{
  private Boolean installed;

  @Override
  public String getName()
  {
    return "git";
  }

  @Override
  public boolean isInstalled()
  {
    InstalledCmd cmd;

    if (installed == null)
    {
      cmd = new InstalledCmd();
      cmd.execute();
      installed = cmd.getResult().isTrue();
    }

    return installed.booleanValue();
  }

  @Override
  public boolean isEnabled(File file)
  {
    ActiveCmd cmd;

    cmd = new ActiveCmd(file);
    cmd.execute();

    return cmd.getResult().isTrue();
  }

  public BlameIF executeBlame(File file)
  {
    BlameCmd cmd;

    cmd = new BlameCmd(file);
    cmd.execute();
    return cmd.getResultData();
  }

  public DiffIF executeDiff(File file,
      boolean recursive)
  {
    DiffCmd cmd;

    cmd = new DiffCmd(file,
                      recursive);
    cmd.execute();
    return cmd.getResultData();
  }

  @Override
  public StatusResult executeStatus(File file)
  {
    StatusCmd cmd;

    cmd = new StatusCmd(file);
    cmd.execute();
    return cmd.getResultData();
  }

  @Override
  public BaseFile getBaseFile(File file)
  {
    CatCmd cmd;

    cmd = new CatCmd(file);
    cmd.execute();
    return cmd.getResultData();
  }

  @Override
  public String toString()
  {
    return getName();
  }
}

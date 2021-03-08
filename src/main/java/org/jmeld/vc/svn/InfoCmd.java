package org.jmeld.vc.svn;

import java.io.File;
import org.jmeld.util.Result;

public class InfoCmd
    extends SvnXmlCmd<InfoData>
{
  private File file;

  public InfoCmd(File file)
  {
    super(InfoData.class);

    this.file = file;
  }

  public Result execute()
  {
    super.execute("svn",
                  "info",
                  "--non-interactive",
                  "-R",
                  "--xml",
                  file.getPath());

    return getResult();
  }

  public InfoData getInfoData()
  {
    return getResultData();
  }

  public static void main(String[] args)
  {
    InfoCmd cmd;

    cmd = new InfoCmd(new File(args[0]));
    if (cmd.execute().isTrue())
    {
      for (InfoData.Entry entry : cmd.getInfoData().getEntryList())
      {
        System.out.println(entry.getRevision() + " : " + entry.getPath());
        System.out.println("1:" + entry.getUrl());
        System.out.println("2:" + entry.getRepository().getRoot());
        System.out.println("3:" + entry.getRepository().getUUID());
        System.out.println("4:" + entry.getWcInfo().getChecksum());
        System.out.println("5:" + entry.getWcInfo().getSchedule());
        System.out.println("6:" + entry.getWcInfo().getTextUpdated());
        System.out.println("7:" + entry.getCommit().getAuthor());
        System.out.println("8:" + entry.getCommit().getDate());
        System.out.println("9:" + entry.getCommit().getRevision());
      }
    }
    else
    {
      cmd.printError();
    }
  }
}

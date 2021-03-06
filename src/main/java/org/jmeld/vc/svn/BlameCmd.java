package org.jmeld.vc.svn;

import java.io.File;
import org.jmeld.util.Result;
import org.jmeld.vc.BlameIF;

public class BlameCmd
    extends SvnXmlCmd<BlameData>
{
  private File file;

  public BlameCmd(File file)
  {
    super(BlameData.class);

    this.file = file;
  }

  public Result execute()
  {
    super.execute("svn",
                  "blame",
                  "--non-interactive",
                  "--xml",
                  file.getPath());

    return getResult();
  }

  public BlameIF getBlame()
  {
    return getResultData();
  }

  public static void main(String[] args)
  {
    BlameCmd cmd;

    cmd = new BlameCmd(new File(args[0]));
    if (cmd.execute().isTrue())
    {
      for (BlameIF.TargetIF target : cmd.getBlame().getTargetList())
      {
        for (BlameIF.EntryIF entry : target.getEntryList())
        {
          System.out.println(entry.getLineNumber() + " : " + entry.getCommit().getRevision() + " -> "
              + entry.getCommit().getAuthor() + " " + entry.getCommit().getDate());
        }
      }
    }
    else
    {
      cmd.printError();
    }
  }
}

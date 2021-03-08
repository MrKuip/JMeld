package org.jmeld.vc.git;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.jmeld.util.Result;
import org.jmeld.vc.StatusResult;
import org.jmeld.vc.util.VcCmd;

public class StatusCmd
    extends VcCmd<StatusResult>
{
  private File file;

  public StatusCmd(File file)
  {
    this.file = file;

    initWorkingDirectory(file);
  }

  public Result execute()
  {
    super.execute("git",
                  "status",
                  "-s",
                  file.getAbsolutePath());

    return getResult();
  }

  @Override
  protected void build(byte[] data)
  {
    StatusResult statusResult;
    StatusResult.Status status;
    BufferedReader reader;
    String text;

    statusResult = new StatusResult(file);

    reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
    try
    {
      while ((text = reader.readLine()) != null)
      {
        text = text.trim();
        if (text.length() < 3)
        {
          continue;
        }

        status = null;
        switch (text.charAt(0))
        {
          case 'M':
          case 'R':
            status = StatusResult.Status.modified;
            break;
          case 'A':
            status = StatusResult.Status.added;
            break;
          case 'D':
            status = StatusResult.Status.removed;
            break;
          case '!':
            status = StatusResult.Status.ignored;
            break;
          case '?':
            status = StatusResult.Status.unversioned;
            break;
          case ' ':
            status = StatusResult.Status.clean;
            break;
        }

        statusResult.addEntry(text.substring(2),
                              status);
      }
    }
    catch (IOException ex)
    {
      // This cannot happen! We are reading from a byte array.
    }

    setResultData(statusResult);
  }

  public static void main(String[] args)
  {
    StatusResult result;

    result = new GitVersionControl().executeStatus(new File(args[0]));
    if (result != null)
    {
      for (StatusResult.Entry entry : result.getEntryList())
      {
        System.out.println(entry.getStatus().getShortText() + " " + entry.getName());
      }
    }
  }
}

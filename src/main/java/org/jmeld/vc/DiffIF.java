package org.jmeld.vc;

import java.util.List;
import org.jmeld.diff.JMRevision;

public interface DiffIF
{
  public List<? extends TargetIF> getTargetList();

  public interface TargetIF
  {
    public String getPath();

    public JMRevision getRevision();
  }
}

package org.jmeld.util.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jmeld.ui.StatusBar;

public class DirectoryScanner
{
  private final Path m_baseDir;
  private final List<Pattern> m_includeList = new ArrayList<>();
  private final List<Pattern> m_excludeList = new ArrayList<>();
  private final List<Path> m_files = new ArrayList<>();

  public DirectoryScanner(Path baseDir)
  {
    m_baseDir = baseDir;
  }

  public void setIncludes(List<String> includeList)
  {
    m_includeList.clear();
    includeList.forEach(this::addInclude);
  }

  public void addInclude(String include)
  {
    m_includeList.add(Pattern.compile(include));
  }

  public void setExcludes(List<String> excludeList)
  {
    m_excludeList.clear();
    excludeList.forEach(this::addExclude);
  }

  public void addExclude(String exclude)
  {
    System.out.println("addExclude: " + exclude);
    m_excludeList.add(Pattern.compile(exclude));
  }

  public List<Path> scan()
  {
    try
    {
      Files.walkFileTree(m_baseDir, new SimpleFileVisitor<Path>()
      {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
        {
          if (isIncluded(toString(file)))
          {
            m_files.add(file);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        {
          if (isExcluded(toString(dir)))
          {
            return FileVisitResult.SKIP_SUBTREE;
          }
          setState("Scan directory: %s", dir);
          return FileVisitResult.CONTINUE;
        }

        private String toString(Path path)
        {
          return path.toString().replace("\\", "/");
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc)
        {
          return FileVisitResult.CONTINUE;
        }

        private boolean isIncluded(String path)
        {
          if (m_includeList.isEmpty())
          {
            return !isExcluded(path);
          }

          for (Pattern p : m_includeList)
          {
            if (p.matcher(path).matches())
            {
              if (!isExcluded(path))
              {
                return true;
              }
            }
          }

          return false;
        }

        private boolean isExcluded(String path)
        {
          for (Pattern p : m_excludeList)
          {
            if (p.matcher(path).matches())
            {
              return true;
            }
          }

          return false;
        }
      });
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return m_files;
  }

  private void setState(String format, Object... args)
  {
    StatusBar.getInstance().setState(format, args);
  }

  public static void main(String[] args)
  {
    DirectoryScanner scanner;

    scanner = new DirectoryScanner(Path.of("/projecten/own/jmeld/org/JMeld"));
    //scanner.addInclude(".*/*.java");
    scanner.addInclude(".*/*.class");
    //scanner.addInclude(".*/*.png");
    scanner.addExclude(".*/build/.*");

    List<Path> fileList = scanner.scan();
    for (var file : fileList)
    {
      System.out.println(file);
    }
  }

}

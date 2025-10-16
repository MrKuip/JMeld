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
  private final List<Path> m_fileList = new ArrayList<>();
  private final List<Path> m_directoryList = new ArrayList<>();

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
    m_includeList.add(Pattern.compile(globToRegex(include)));
  }

  public void setExcludes(List<String> excludeList)
  {
    m_excludeList.clear();
    excludeList.forEach(this::addExclude);
  }

  public void addExclude(String exclude)
  {
    m_excludeList.add(Pattern.compile(globToRegex(exclude)));
  }

  public List<Path> scan()
  {
    try
    {
      Files.walkFileTree(m_baseDir, new SimpleFileVisitor<Path>()
      {
        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
        {
          if (isIncluded(toString(path)))
          {
            m_fileList.add(path);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attrs)
        {
          if (isExcluded(toString(directory)))
          {
            return FileVisitResult.SKIP_SUBTREE;
          }
          m_directoryList.add(directory);
          setState("Scan directory: %s", directory);
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

    return getFileList();
  }

  public List<Path> getFileList()
  {
    return m_fileList;
  }

  public List<Path> getDirectoryList()
  {
    return m_directoryList;
  }

  private void setState(String format, Object... args)
  {
    StatusBar.getInstance().setState(format, args);
  }

  private String globToRegex(String glob)
  {
    StringBuilder regex;
    char[] chars;
    int i;
    
    regex = new StringBuilder("^");
    chars = glob.replace("\\", "/").toCharArray();
    i = 0;
    while (i < chars.length)
    {
      char c;
      
      c = chars[i];
      switch (c)
      {
        case '*':
          if (i + 1 < chars.length && chars[i + 1] == '*')
          {
            regex.append(".*");
            i++;
          }
          else
          {
            regex.append("[^/]*");
          }
          break;
        case '?':
          regex.append("[^/]");
          break;
        case '.':
          regex.append("\\.");
          break;
        case '/':
          regex.append("/");
          break;
        default:
          regex.append(Pattern.quote(String.valueOf(c)));
      }
      i++;
    }
    regex.append("$");
    return regex.toString();
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

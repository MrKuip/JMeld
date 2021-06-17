/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.jmeld.tools.ant.util;

import java.io.File;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.jmeld.tools.ant.BuildException;
import org.apache.jmeld.tools.ant.taskdefs.condition.Os;

/**
 * This class also encapsulates methods which allow Files to be referred to
 * using abstract path names which are translated to native system file paths at
 * runtime as well as copying files or setting their last modification time.
 *
 */
public class FileUtils
{
  private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
  private static final int EXPAND_SPACE = 50;
  private static final FileUtils PRIMARY_INSTANCE = new FileUtils();

  // get some non-crypto-grade randomness from various places.
  private static Random rand = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());

  private static final boolean ON_NETWARE = Os.isFamily("netware");
  private static final boolean ON_DOS = Os.isFamily("dos");
  private static final boolean ON_WIN9X = Os.isFamily("win9x");
  private static final boolean ON_WINDOWS = Os.isFamily("windows");

  static final int BUF_SIZE = 8192;

  /**
   * The granularity of timestamps under FAT.
   */
  public static final long FAT_FILE_TIMESTAMP_GRANULARITY = 2000;

  /**
   * The granularity of timestamps under Unix.
   */
  public static final long UNIX_FILE_TIMESTAMP_GRANULARITY = 1000;

  /**
   * The granularity of timestamps under the NT File System. NTFS has a
   * granularity of 100 nanoseconds, which is less than 1 millisecond, so we round
   * this up to 1 millisecond.
   */
  public static final long NTFS_FILE_TIMESTAMP_GRANULARITY = 1;

  private static final FileAttribute[] TMPFILE_ATTRIBUTES = new FileAttribute[]
  {
      PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE))
  };
  private static final FileAttribute[] TMPDIR_ATTRIBUTES = new FileAttribute[]
  {
      PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
          PosixFilePermission.OWNER_EXECUTE))
  };
  private static final FileAttribute[] NO_TMPFILE_ATTRIBUTES = new FileAttribute[0];

  /**
   * A one item cache for fromUri. fromUri is called for each element when parsing
   * ant build files. It is a costly operation. This just caches the result of the
   * last call.
   */
  private Object cacheFromUriLock = new Object();
  private String cacheFromUriRequest = null;
  private String cacheFromUriResponse = null;

  /**
   * Factory method.
   *
   * @return a new instance of FileUtils.
   * @deprecated since 1.7. Use getFileUtils instead, FileUtils do not have state.
   */
  @Deprecated
  public static FileUtils newFileUtils()
  {
    return new FileUtils();
  }

  /**
   * Method to retrieve The FileUtils, which is shared by all users of this
   * method.
   * 
   * @return an instance of FileUtils.
   * @since Ant 1.6.3
   */
  public static FileUtils getFileUtils()
  {
    return PRIMARY_INSTANCE;
  }

  /**
   * Empty constructor.
   */
  protected FileUtils()
  {
  }

  /**
   * Removes a leading path from a second path.
   *
   * <p>
   * This method uses {@link #normalize} under the covers and does not resolve
   * symbolic links.
   * </p>
   *
   * @param leading The leading path, must not be null, must be absolute.
   * @param path    The path to remove from, must not be null, must be absolute.
   *
   * @return path's normalized absolute if it doesn't start with leading; path's
   *         path with leading's path removed otherwise.
   *
   * @since Ant 1.5
   */
  public String removeLeadingPath(File leading, File path)
  {
    String l = normalize(leading.getAbsolutePath()).getAbsolutePath();
    String p = normalize(path.getAbsolutePath()).getAbsolutePath();
    if (l.equals(p))
    {
      return "";
    }
    // ensure that l ends with a /
    // so we never think /foo was a parent directory of /foobar
    if (!l.endsWith(File.separator))
    {
      l += File.separator;
    }
    return (p.startsWith(l)) ? p.substring(l.length()) : p;
  }

  /**
   * &quot;Normalize&quot; the given absolute path.
   *
   * <p>
   * This includes:
   * <ul>
   * <li>Uppercase the drive letter if there is one.</li>
   * <li>Remove redundant slashes after the drive spec.</li>
   * <li>Resolve all ./, .\, ../ and ..\ sequences.</li>
   * <li>DOS style paths that start with a drive letter will have \ as the
   * separator.</li>
   * </ul>
   * <p>
   * Unlike {@link File#getCanonicalPath()} this method specifically does not
   * resolve symbolic links.
   * </p>
   *
   * <p>
   * If the path tries to go beyond the file system root (i.e. it contains more
   * ".." segments than can be travelled up) the method will return the original
   * path unchanged.
   * </p>
   *
   * @param path the path to be normalized.
   * @return the normalized version of the path.
   *
   * @throws java.lang.NullPointerException if path is null.
   */
  public File normalize(final String path)
  {
    Stack<String> s = new Stack<>();
    String[] dissect = dissect(path);
    s.push(dissect[0]);

    StringTokenizer tok = new StringTokenizer(dissect[1], File.separator);
    while (tok.hasMoreTokens())
    {
      String thisToken = tok.nextToken();
      if (".".equals(thisToken))
      {
        continue;
      }
      if ("..".equals(thisToken))
      {
        if (s.size() < 2)
        {
          // Cannot resolve it, so skip it.
          return new File(path);
        }
        s.pop();
      }
      else
      { // plain component
        s.push(thisToken);
      }
    }
    StringBuilder sb = new StringBuilder();
    final int size = s.size();
    for (int i = 0; i < size; i++)
    {
      if (i > 1)
      {
        // not before the filesystem root and not after it, since root
        // already contains one
        sb.append(File.separatorChar);
      }
      sb.append(s.elementAt(i));
    }
    return new File(sb.toString());
  }

  /**
   * Dissect the specified absolute path.
   * 
   * @param path the path to dissect.
   * @return String[] {root, remaining path}.
   * @throws java.lang.NullPointerException if path is null.
   * @since Ant 1.7
   */
  public String[] dissect(String path)
  {
    char sep = File.separatorChar;
    path = path.replace('/', sep).replace('\\', sep);

    // make sure we are dealing with an absolute path
    if (!isAbsolutePath(path))
    {
      throw new BuildException(path + " is not an absolute path");
    }
    String root;
    int colon = path.indexOf(':');
    if (colon > 0 && (ON_DOS || ON_NETWARE))
    {

      int next = colon + 1;
      root = path.substring(0, next);
      char[] ca = path.toCharArray();
      root += sep;
      // remove the initial separator; the root has it.
      next = (ca[next] == sep) ? next + 1 : next;

      StringBuffer sbPath = new StringBuffer();
      // Eliminate consecutive slashes after the drive spec:
      for (int i = next; i < ca.length; i++)
      {
        if (ca[i] != sep || ca[i - 1] != sep)
        {
          sbPath.append(ca[i]);
        }
      }
      path = sbPath.toString();
    }
    else if (path.length() > 1 && path.charAt(1) == sep)
    {
      // UNC drive
      int nextsep = path.indexOf(sep, 2);
      nextsep = path.indexOf(sep, nextsep + 1);
      root = (nextsep > 2) ? path.substring(0, nextsep + 1) : path;
      path = path.substring(root.length());
    }
    else
    {
      root = File.separator;
      path = path.substring(1);
    }
    return new String[]
    {
        root, path
    };
  }

  /**
   * Verifies that the specified filename represents an absolute path. Differs
   * from new java.io.File("filename").isAbsolute() in that a path beginning with
   * a double file separator--signifying a Windows UNC--must at minimum match
   * "\\a\b" to be considered an absolute path.
   * 
   * @param filename the filename to be checked.
   * @return true if the filename represents an absolute path.
   * @throws java.lang.NullPointerException if filename is null.
   * @since Ant 1.6.3
   */
  public static boolean isAbsolutePath(String filename)
  {
    if (filename.isEmpty())
    {
      return false;
    }
    int len = filename.length();
    char sep = File.separatorChar;
    filename = filename.replace('/', sep).replace('\\', sep);
    char c = filename.charAt(0);
    if (!ON_DOS && !ON_NETWARE)
    {
      return c == sep;
    }
    if (c == sep)
    {
      // CheckStyle:MagicNumber OFF
      if (!ON_DOS || len <= 4 || filename.charAt(1) != sep)
      {
        return false;
      }
      // CheckStyle:MagicNumber ON
      int nextsep = filename.indexOf(sep, 2);
      return nextsep > 2 && nextsep + 1 < len;
    }
    int colon = filename.indexOf(':');
    return (Character.isLetter(c) && colon == 1 && filename.length() > 2 && filename.charAt(2) == sep)
        || (ON_NETWARE && colon > 0);
  }

  /**
   * Interpret the filename as a file relative to the given file unless the
   * filename already represents an absolute filename. Differs from
   * <code>new File(file, filename)</code> in that the resulting File's path will
   * always be a normalized, absolute pathname. Also, if it is determined that
   * <code>filename</code> is context-relative, <code>file</code> will be
   * discarded and the reference will be resolved using available context/state
   * information about the filesystem.
   *
   * @param file     the "reference" file for relative paths. This instance must
   *                 be an absolute file and must not contain &quot;./&quot; or
   *                 &quot;../&quot; sequences (same for \ instead of /). If it is
   *                 null, this call is equivalent to
   *                 <code>new java.io.File(filename).getAbsoluteFile()</code>.
   *
   * @param filename a file name.
   *
   * @return an absolute file.
   * @throws java.lang.NullPointerException if filename is null.
   */
  public File resolveFile(File file, String filename)
  {
    if (!isAbsolutePath(filename))
    {
      char sep = File.separatorChar;
      filename = filename.replace('/', sep).replace('\\', sep);
      if (isContextRelativePath(filename))
      {
        file = null;
        // on cygwin, our current directory can be a UNC;
        // assume user.dir is absolute or all hell breaks loose...
        String udir = System.getProperty("user.dir");
        if (filename.charAt(0) == sep && udir.charAt(0) == sep)
        {
          filename = dissect(udir)[0] + filename.substring(1);
        }
      }
      filename = new File(file, filename).getAbsolutePath();
    }
    return normalize(filename);
  }

  /**
   * On DOS and NetWare, the evaluation of certain file specifications is
   * context-dependent. These are filenames beginning with a single separator
   * (relative to current root directory) and filenames with a drive specification
   * and no intervening separator (relative to current directory of the specified
   * root).
   * 
   * @param filename the filename to evaluate.
   * @return true if the filename is relative to system context.
   * @throws java.lang.NullPointerException if filename is null.
   * @since Ant 1.7
   */
  public static boolean isContextRelativePath(String filename)
  {
    if (!(ON_DOS || ON_NETWARE) || filename.isEmpty())
    {
      return false;
    }
    char sep = File.separatorChar;
    filename = filename.replace('/', sep).replace('\\', sep);
    char c = filename.charAt(0);
    int len = filename.length();
    return (c == sep && (len == 1 || filename.charAt(1) != sep))
        || (Character.isLetter(c) && len > 1 && filename.charAt(1) == ':' && (len == 2 || filename.charAt(2) != sep));
  }

}

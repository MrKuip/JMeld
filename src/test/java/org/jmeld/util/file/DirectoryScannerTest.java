package org.jmeld.util.file;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.jmeld.settings.FilterSettings;
import org.jmeld.settings.JMeldSettings;
import org.jmeld.settings.util.Filter;
import org.junit.jupiter.api.Test;

public class DirectoryScannerTest
{
  @Test
  public void testScanAll()
  {
    DirectoryScanner scanner;
    List<Path> fileList;
    List<Path> directoryList;
    Path userDir;
    
    userDir = Paths.get(System.getProperty("user.dir"));
    scanner = new DirectoryScanner(userDir);
    fileList = scanner.scan();
    directoryList = scanner.getDirectoryList();
    
    assertTrue(fileList.contains(userDir.resolve("src/main/java/org/jmeld/Main.java")));
    assertTrue(fileList.contains(userDir.resolve("src/test/java/org/jmeld/util/file/DirectoryScannerTest.java")));
    assertTrue(fileList.contains(userDir.resolve("src/main/resources/help/images/keyCTRL.png")));
    
    assertTrue(directoryList.contains(userDir.resolve("src/test")));
    assertTrue(directoryList.contains(userDir.resolve("src/main/java")));
    assertTrue(directoryList.contains(userDir.resolve(".git")));
    assertTrue(fileList.contains(userDir.resolve(".git/HEAD")));
    assertTrue(directoryList.contains(userDir.resolve("build")));
    
    assertTrue(fileList.stream().filter(file -> file.toString().endsWith(".png")).findFirst().isPresent());
    assertTrue(fileList.stream().filter(file -> file.toString().endsWith(".class")).findFirst().isPresent());
    assertTrue(fileList.stream().filter(file -> file.toString().endsWith(".xml")).findFirst().isPresent());
  }
  
  @Test
  public void testScanDefault()
  {
    DirectoryScanner scanner;
    List<Path> fileList;
    List<Path> directoryList;
    Path userDir;
    Filter filter;
    
    filter = JMeldSettings.getInstance().getFilter().getFilter(FilterSettings.DEFAULT);
    
    userDir = Paths.get(System.getProperty("user.dir"));
    scanner = new DirectoryScanner(userDir);
    scanner.setIncludes(filter.getIncludes());
    scanner.setExcludes(filter.getExcludes());
    fileList = scanner.scan();
    directoryList = scanner.getDirectoryList();
    
    assertTrue(fileList.contains(userDir.resolve("src/main/java/org/jmeld/Main.java")));
    assertTrue(fileList.contains(userDir.resolve("src/test/java/org/jmeld/util/file/DirectoryScannerTest.java")));
    assertFalse(fileList.contains(userDir.resolve("src/main/resources/help/images/keyCTRL.png")));
    
    assertTrue(directoryList.contains(userDir.resolve("src/test")));
    assertTrue(directoryList.contains(userDir.resolve("src/main/java")));
    assertFalse(directoryList.contains(userDir.resolve(".git")));
    assertFalse(directoryList.contains(userDir.resolve(".git/HEAD")));
    assertTrue(directoryList.contains(userDir.resolve("build")));
    
    assertFalse(fileList.stream().filter(file -> file.toString().endsWith(".png")).findFirst().isPresent());
    assertFalse(fileList.stream().filter(file -> file.toString().endsWith(".class")).findFirst().isPresent());
    assertTrue(fileList.stream().filter(file -> file.toString().endsWith(".xml")).findFirst().isPresent());
  }
  
  @Test
  public void testScanExcludes()
  {
    DirectoryScanner scanner;
    List<Path> fileList;
    List<Path> directoryList;
    Path userDir;
    Filter filter;
    
    filter = JMeldSettings.getInstance().getFilter().getFilter(FilterSettings.DEFAULT);
    
    userDir = Paths.get(System.getProperty("user.dir"));
    scanner = new DirectoryScanner(userDir);
    scanner.setIncludes(filter.getIncludes());
    scanner.setExcludes(filter.getExcludes());
    scanner.addExclude("**/test");
    scanner.addExclude("**/*.xml");
    fileList = scanner.scan();
    directoryList = scanner.getDirectoryList();
    
    assertFalse(directoryList.contains(userDir.resolve("src/test")));
    assertTrue(directoryList.contains(userDir.resolve("src/main/java")));
    assertTrue(fileList.contains(userDir.resolve("src/main/java/org/jmeld/Main.java")));
    assertFalse(fileList.contains(userDir.resolve("src/test/java/org/jmeld/util/file/DirectoryScannerTest.java")));
    assertFalse(fileList.contains(userDir.resolve("src/main/resources/help/images/keyCTRL.png")));
    
    assertFalse(directoryList.contains(userDir.resolve(".git")));
    assertFalse(directoryList.contains(userDir.resolve(".git/HEAD")));
    assertTrue(directoryList.contains(userDir.resolve("build")));
    
    assertFalse(fileList.stream().filter(file -> file.toString().endsWith(".png")).findFirst().isPresent());
    assertFalse(fileList.stream().filter(file -> file.toString().endsWith(".class")).findFirst().isPresent());
    assertFalse(fileList.stream().filter(file -> file.toString().endsWith(".xml")).findFirst().isPresent());
  }
}

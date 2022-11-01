package com.vizexplorer.dataadapter.admin.services;

import com.vizexplorer.dataadapter.admin.config.FileServerConfig;
import com.vizexplorer.dataadapter.admin.services.dto.FileInfo;
import com.vizexplorer.dataadapter.admin.services.dto.FileList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceImplTest {
  FileServerConfig fileServerConfig = new FileServiceImplTest.TestFileServerConfig(".");

  private FileService service = new FileServiceImpl(fileServerConfig);

  @Test
  void testGetFilesInfoWithBadPath() {
    Path filePath = Paths.get("bad-folder");
    assertThrows(IOException.class, () -> {
      service.getFilesInfo(filePath);
    }, "IOException was expected");
  }

  @Test
  void testGetFilesInfoWithWorkingPath() {
    String startingPath = "./src/test/resources";
    Path filePath = Paths.get(startingPath);
    try {
      FileList files = service.getFilesInfo(filePath);
      assertNotNull(files);
      assertEquals(startingPath, files.getPath());
      assertTrue(files.getFileInfo().size() > 0);
      FileInfo testFileInfo = files.getFileInfo()
              .stream()
              .filter(fileInfo -> "testfile.txt"
                      .equals(fileInfo.getFilePath())).findAny().orElse(null);
      assertNotNull(testFileInfo);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void saveFile() {
  }

  @Test
  void delete() {
  }

  @Test
  void createDirectory() {
  }

  public class TestFileServerConfig extends FileServerConfig {
    private String home;
    public TestFileServerConfig(String home) {
      this.home = home;
    }
    @Override
    public String getHome() {
      return home;
    }

  }
}

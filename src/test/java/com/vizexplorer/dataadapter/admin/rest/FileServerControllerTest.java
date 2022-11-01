package com.vizexplorer.dataadapter.admin.rest;

import com.vizexplorer.dataadapter.admin.services.FileService;
import com.vizexplorer.dataadapter.admin.services.dto.FileInfo;
import com.vizexplorer.dataadapter.admin.services.dto.FileList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileServerController.class)
class FileServerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  public FileService fileService;

  @Test
  void downloadFile() {
  }

  @Test
  void getFiles() {

  }

  @Test
  void testGetFilesWithNoParams() {
    try {
      MvcResult result = mockMvc.perform(get(FileServerController.URI_PREFIX + FileServerController.LIST_PREFIX))
              .andExpect(status().isOk())
              .andReturn();
      String expectedContent = String.format("");
      String content = result.getResponse().getContentAsString();
      assertEquals(expectedContent, content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testGetFilesPassingBadParam() {
    try {
      String folderName = "foo";
      String filename = "barnotfound";
      String expectedContent = String.format("");
      FileList files = new FileList(folderName);
      files.add(new FileInfo(filename));
      Mockito.when(fileService.getFilesInfo(Paths.get(folderName)))
              .thenThrow(new IOException("404"));
      MvcResult result = mockMvc.perform(get(FileServerController.URI_PREFIX + FileServerController.LIST_PREFIX + folderName))
              .andExpect(status().isInternalServerError())
              .andReturn();

      String content = result.getResponse().getContentAsString();
      assertEquals(expectedContent, content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  @Test
  void testGetFilesPassingOneParam() {
    try {
      String folderName = "foo";
      String filename = "bar";
      String expectedContent = String.format("{\"path\":\"%s\",\"fileInfo\":[{\"filePath\":\"%s\"}],\"directoryInfo\":[]}", folderName, filename);
      FileList files = new FileList(folderName);
      files.add(new FileInfo(filename));
      Mockito.when(fileService.getFilesInfo(Paths.get(folderName)))
              .thenReturn(files);
      MvcResult result = mockMvc.perform(get(FileServerController.URI_PREFIX + FileServerController.LIST_PREFIX + folderName))
              .andExpect(status().isOk())
              .andReturn();

      String content = result.getResponse().getContentAsString();
      assertEquals(expectedContent, content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void fileUpload() {
  }

  @Test
  void delete() {
  }

  @Test
  void createDirectory() {
  }
}

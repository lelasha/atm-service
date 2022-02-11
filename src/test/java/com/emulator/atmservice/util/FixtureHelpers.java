package com.emulator.atmservice.util;

import java.io.IOException;
import java.nio.file.Files;
import org.springframework.util.ResourceUtils;

public class FixtureHelpers {

  private static final String FIXTURES_PATH_TEMPLATE = "classpath:fixtures/%s";

  private FixtureHelpers() {}

  public static byte[] loadFixture(String name) throws IOException {
    return Files.readAllBytes(
        ResourceUtils.getFile(String.format(FIXTURES_PATH_TEMPLATE, name)).toPath());
  }
}
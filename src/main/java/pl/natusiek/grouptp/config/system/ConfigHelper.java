package pl.natusiek.grouptp.config.system;

import java.io.File;

public final class ConfigHelper {

  private ConfigHelper() {
  }

  public static Config create(File file, Class clazz) {
    return new Config(file, clazz);
  }

}

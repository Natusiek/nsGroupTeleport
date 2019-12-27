package pl.natusiek.grouptp.config.system;

public abstract class ConfigExtender {

  private static Configuration configuration;

  public ConfigExtender(Configuration configuration) {
    ConfigExtender.configuration = configuration;
  }

  public static void reloadConfig() {
    ConfigExtender.configuration.reload();
  }

  public static void saveConfig() {
    ConfigExtender.configuration.save();
  }

  public static void setConfiguration(Configuration configuration) {
    ConfigExtender.configuration = configuration;
  }

}

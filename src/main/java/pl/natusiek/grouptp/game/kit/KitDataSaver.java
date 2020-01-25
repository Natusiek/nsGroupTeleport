package pl.natusiek.grouptp.game.kit;

public interface KitDataSaver {

    Kit save(String name);

    Kit delete(String name);

    void load();
}

package pl.natusiek.grouptp.game.arena;

import net.minecraft.server.v1_8_R3.WorldBorder;
import pl.natusiek.grouptp.helper.BorderHelper;
import pl.natusiek.grouptp.helper.LocationHelper;

import java.util.List;
import java.util.UUID;

public interface Arena {

    void start();

    void restart();

    List<UUID> getPlayers();

    List<UUID> getSpectators();

    void addPlayer(UUID uuid);

    void removePlayer(UUID uuid);

    boolean isPlaying(UUID uuid);

    void addSpectators(UUID uuid);

    void removeSpectators(UUID uuid);

    boolean isSpectators(UUID uuid);

    String getName();

    LocationHelper getCenter();

    int getSize();

    int getState();

    void setState(int state);

    WorldBorder getBorder(UUID uuid);

    WorldBorder setBorder(UUID uuid, LocationHelper center, int size);

    final class ArenaStates {

        public static final int
                IN_GAME = 1,
                AVAILABLE = 2;
    }

}

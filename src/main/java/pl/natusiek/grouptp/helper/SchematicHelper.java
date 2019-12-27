package pl.natusiek.grouptp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.bukkit.Location;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;

public final class SchematicHelper {

  public static void pasteSchematic(File schematicFile, Location location, boolean withAir) {
    try {
      Vector pasteLocation = new Vector(location.getX(), location.getY(), location.getZ());
      World pasteWorld = new BukkitWorld(location.getWorld());
      WorldData pasteWorldData = pasteWorld.getWorldData();

      Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(pasteWorldData);
      ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);

      EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(pasteWorld, -1);

      Operation operation = clipboardHolder
              .createPaste(editSession, pasteWorldData)
              .to(pasteLocation)
              .ignoreAirBlocks(!withAir)
              .build();

      Operations.completeLegacy(operation);
    }
    catch (IOException | MaxChangedBlocksException ex) {
      ex.printStackTrace();
    }
  }

  private SchematicHelper() {
  }

}

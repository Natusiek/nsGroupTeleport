package pl.natusiek.grouptp.kit;

import java.util.List;
import org.bukkit.inventory.ItemStack;

//useless interface but helps with immutable and better practise xd
public interface Kit {

  String getName();

  List<ItemStack> getContent();

  ItemStack[] getArmorContent();

  ItemStack getIcon();

  int getRows();

  int getColumn();

}

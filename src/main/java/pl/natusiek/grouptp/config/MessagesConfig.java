package pl.natusiek.grouptp.config;

import org.omg.CORBA.PUBLIC_MEMBER;
import pl.natusiek.grouptp.config.system.ConfigExtender;
import pl.natusiek.grouptp.config.system.Configuration;

import java.util.Collections;
import java.util.List;

public class MessagesConfig extends ConfigExtender {

  public MessagesConfig(Configuration config) { super(config); }
  
  public static String ARENA$JOINED = "&aDolaczyles do areny: &6{ARENA}&a, grasz z: {PLAYERS}";
  public static String ARENA$END_ARENA = "&7Arene &f{ARENA} &7wygral &6{WINNER}";
  public static String ARENA$HP_OPPONENT = "&7{OPPONENT} zostalo mu: &c{HP}";
  public static String ARENA$CANNOT_JOIN$COOLDOWN = "&4Nie spamuj tak! &cWyszukiwac arene mozesz co &f10 sekund&c!";
  public static String ARENA$CANNOT_JOIN$NO_AVAILABLE_ARENAS = "&4Sory, &cale dobry serwer i nie ma wolnych aren!";
  public static String ARENA$CANNOT_JOIN$NO_PLAYERS_IN_RADIUS = "&4Sory, &c ale brak graczy w poblizu lub nie maja tego samego zestawu!";
  public static String ARENA$CANNOT_USE_PEARL_OUTSIDE_BORDER = "&4Co ty taki Stiv? &cale nie wyrzucamy perel za border!";
  public static String ARENA$NO_DROP = "&4Ej! Ej! Ej! &7Brudasie nie smiec na spawnie!";

  public static int ARENA$SEARCHING$COOLDOWN = 10;
  public static int ARENA$SEARCHING$RADIUS = 3;
  public static int ARENA$SEARCHING$MIN_PLAYERS_IN_RADIUS = 1;
  public static int ARENA$SEARCHING$MAX_PLAYERS = 4;

  public static String BUILD$IN_SPAWN = "&4Co ty? &cSpawna chcesz rozwalic?";
  public static String BUILD$ABOVE_CLOUD = "&4E, E, E, &cCo ty robisz, spadaj w dol!";
  public static boolean BUILD$ABOVE_CLOUDS = true;
  public static int BUILD$MAX_Y = 100;

  public static int BLOCK_COMMANDS$REMOVE_ITEMS = 100;

  public static String BUNGEE$SERVER = "lobby";
  public static String BUNGEE$CONNECT_SERVER = "&8>> &fPolaczono z serwerem {SERVER}";
  public static String BUNGEE$MESSAGE_LEAVE = "&aPaaaa, wbijesz jeszcze?";

  public static String KIT$USE = "&4Stuku puku &c,ale masz juz kit!";
  public static String KIT$TAKE = "&7Wybrales kit: &6{KIT}";
  public static String KIT$DONT_USE = "&4Co ty bez seta na bitke? Wybierz kit i dopiero idz!";
  public static String KIT$BLOCK_CHANGE_KIT = "&4Ej! Ej! Ej! &cBedziesz sprawiedliwy wobec innych osob?";


}

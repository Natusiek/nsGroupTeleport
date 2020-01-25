package pl.natusiek.grouptp.config;

import pl.natusiek.grouptp.config.system.ConfigExtender;
import pl.natusiek.grouptp.config.system.Configuration;

public class MessagesConfig extends ConfigExtender {

    public MessagesConfig(Configuration config) { super(config); }

    public static String ARENA$JOINED = "&aDolaczyles do areny: &6{ARENA}&a, grasz z: &f{PLAYERS}";
    public static String ARENA$END_ARENA = "&aGratki! &fArene &e{ARENA} &fwygral &e{WINNER}";
    public static String ARENA$HP_OPPONENT = "&7{OPPONENT} zostalo mu: &c{HP}";
    public static String ARENA$CANNOT_JOIN$COOLDOWN = "&4Nie spamuj tak! &cWyszukiwac arene mozesz co &f{TIME} sekund&c!";
    public static String ARENA$CANNOT_JOIN$NO_AVAILABLE_ARENAS = "&4Sory, &cale dobry serwer i nie ma wolnych aren!";
    public static String ARENA$CANNOT_JOIN$NO_PLAYERS_IN_RADIUS = "&4Sory, &c ale brak graczy w poblizu lub nie maja tego samego zestawu!";
    public static String ARENA$CANNOT_USE_PEARL_OUTSIDE_BORDER = "&4Co ty taki Stiv? &cale nie wyrzucamy perel za border!";

    public static String ARENA$COMPASS$COOLDOWN = "&cPoczekaj &f{TIME} &faby, uzyc kolejny raz! ";
    public static String ARENA$COMPASS$NO_FOUND_PLAYER = "&aNie znaleziono gracza w twoim poblizu.";
    public static String ARENA$COMPASS$NEAREST_PLAYER = "&aNajblizszy gracz: &f{RESULT} &aw odleglosci: {DISTANCE}";
    public static int ARENA$COMPASS$TIME_COOLDOWND = 3;

    public static int ARENA$SEARCHING$RADIUS = 3;
    public static int ARENA$SEARCHING$COOLDOWN = 10;
    public static int ARENA$SEARCHING$MAX_PLAYERS = 4;
    public static int ARENA$SEARCHING$MIN_PLAYERS_IN_RADIUS = 1;
    public static int ARENA$RADIUS$REMOVE_ITEMS = 100;

    public static String BUILD$ABOVE_CLOUD = "&4E, E, E, &cCo ty robisz, spadaj w dol!";
    public static boolean BUILD$ABOVE_CLOUDS = true;
    public static int BUILD$MAX_Y = 100;

    public static String BUNGEE$SERVER = "hub";
    public static String BUNGEE$CONNECT_SERVER = "&8>> &fPolaczono z serwerem {SERVER}";
    public static String BUNGEE$MESSAGE_LEAVE = "&aPaaaa, wbijesz jeszcze?";

    public static String SPECTATOR$ARENA_CLOSE = "&fArena zakonczyla sie zostales przeteleportowany na spawna";

    public static String COMMAND$NO_PERMISSION = "&4Upsik, ale nie masz permisji.";
    public static String COMMAND$NO_INT = "&4Upsik, podany argument nie jest liczba";

    public static String COMMAND$ADMIN_KIT$USE = "&4Uzycie: &f/adminkit (create/delete) (name) (rows number) (column number)";
    public static String COMMAND$ADMIN_KIT$CREATED = "&fUtowrzyles kit o nazwie: &6{KIT}";
    public static String COMMAND$ADMIN_KIT$ITEM_AIR = "&fNie trzymasz nic w rece.";
    public static String COMMAND$ADMIN_KIT$DELETE = "&fKit zostal usuniety";

    public static String COMMAND$SET_ARENA$USE = "&4Uzycie: &f/setarena (name) (size)";
    public static String COMMAND$SET_ARENA$ADD = "&4fArena zostala dodana pomyslnie";

    public static String KIT$USE = "&4Stuku puku &c,ale masz juz kit!";
    public static String KIT$TAKE = "&7Wybrales kit: &6{KIT}";
    public static String KIT$DONT_HAVE_KIT = "&4Co ty bez seta na bitke? Wybierz kit i dopiero idz!";
    public static String KIT$BLOCK_CHANGE_KIT = "&4Ej! Ej! Ej! &cBedziesz sprawiedliwy wobec innych osob?";

}
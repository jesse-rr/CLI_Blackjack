package code;

import code.models.Player;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class SaveManagement {
    private static final Pattern PLAYER_PATTERN = Pattern.compile(
            "player: \\[name: (\\w+), password: (\\S+), balance: R\\$ ([\\d.]+), " +
                    "winRate: ([\\d.]+)%, wins: (\\d+), loses: (\\d+)\\]"
    );

    private static List<Player> players = new ArrayList<>();
    private static boolean loaded = false;

    // Load all players from file at startup
    private static void loadPlayers() throws IOException {
        if (loaded) return;

        File file = new File("data.txt");
        if (!file.exists()) {
            loaded = true;
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Matcher matcher = PLAYER_PATTERN.matcher(line);
                if (matcher.matches()) {
                    Player player = new Player(
                            matcher.group(1), // name
                            matcher.group(2), // password (plain text)
                            Double.parseDouble(matcher.group(3)), // balance
                            Double.parseDouble(matcher.group(4)), // winRate
                            Integer.parseInt(matcher.group(5)), // wins
                            Integer.parseInt(matcher.group(6))  // loses
                    );
                    players.add(player);
                }
            }
            loaded = true;
        }
    }

    public static void savePlayerInfo(Player player) throws IOException {
        if (player.getName().equalsIgnoreCase("guest")) return;

        // Update in-memory list first
        boolean found = false;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(player.getName())) {
                players.set(i, player);
                found = true;
                break;
            }
        }
        if (!found) {
            players.add(player);
        }

        // Save all players to file
        saveAllPlayers();
    }

    private static void saveAllPlayers() throws IOException {
        try (FileWriter writer = new FileWriter("data.txt")) {
            for (Player player : players) {
                writer.write(formatPlayerInfo(player) + "\n");
            }
        }
    }

    public static Player findPlayer(String username, String password) throws IOException {
        loadPlayers(); // Ensure data is loaded

        for (Player player : players) {
            if (player.getName().equals(username) &&
                    player.getPassword().equals(password)) {
                return player;
            }
        }
        return null;
    }

    public static boolean playerExists(String username) throws IOException {
        loadPlayers(); // Ensure data is loaded

        for (Player player : players) {
            if (player.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void createPlayer(Player player) throws IOException {
        if (!playerExists(player.getName())) {
            players.add(player);
            saveAllPlayers();
        }
    }

    private static String formatPlayerInfo(Player player) {
        return String.format(
                "player: [name: %s, password: %s, balance: R$ %.2f, winRate: %.2f%%, wins: %d, loses: %d]",
                player.getName(),
                player.getPassword(), // Stored plain text
                player.getBalance(),
                player.getWinRate(),
                player.getWins(),
                player.getLoses()
        );
    }
}
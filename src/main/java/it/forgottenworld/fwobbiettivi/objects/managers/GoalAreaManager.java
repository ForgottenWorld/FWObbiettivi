package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.objects.*;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class GoalAreaManager {

    private static GoalAreaManager goalAreaManager;

    private HashMap<UUID, TownGoal> playerGoalAreaCreation;
    private HashMap<UUID, Treasury> playerTesAreaCreation;
    static HashMap<Chunk, List<TownGoal>> chunks = new HashMap<>();
    static HashMap<Chunk, Treasury> chunksTes = new HashMap<>();

    private GoalAreaManager() {
        this.playerGoalAreaCreation = new HashMap<>();
        this.playerTesAreaCreation = new HashMap<>();
    }

    public static GoalAreaManager getInstance() {
        if (goalAreaManager == null)
            goalAreaManager = new GoalAreaManager();

        return goalAreaManager;
    }

    public boolean isPlayerInCreationMode(Player player) {
        return this.playerGoalAreaCreation.containsKey(player.getUniqueId());
    }

    public boolean isPlayerInCreationModeTes(Player player) {
        return this.playerTesAreaCreation.containsKey(player.getUniqueId());
    }

    public void putGoalAreaCreation(UUID uuid, TownGoal townGoals){
        this.playerGoalAreaCreation.put(uuid, townGoals);
    }

    public void putTesAreaCreation(UUID uuid, Treasury treasury){
        this.playerTesAreaCreation.put(uuid, treasury);
    }

    public void setPlayerGoalAreaCreation(HashMap<UUID, TownGoal> playerGoalAreaCreation) {
        this.playerGoalAreaCreation = playerGoalAreaCreation;
    }

    public void setPlayerTesAreaCreation(HashMap<UUID, Treasury> playerTesAreaCreation) {
        this.playerTesAreaCreation = playerTesAreaCreation;
    }

    public HashMap<UUID, TownGoal> getPlayerGoalAreaCreation() {
        return playerGoalAreaCreation;
    }

    public HashMap<UUID, Treasury> getPlayerTesAreaCreation() {
        return playerTesAreaCreation;
    }

    public static void addChunk(Chunk chunk, TownGoal townGoal){
        if (getChunks().get(chunk) == null) {
            chunks.put(chunk, new ArrayList<>());
        }
        chunks.get(chunk).add(townGoal);

        save();
    }

    public static void addChunkTes(Chunk chunk, Treasury tes){
        if (!containsChunkTes(chunk, tes)) {
            chunksTes.put(chunk, tes);
        }

        save();
    }

    public static void removeChunk(Chunk chunk, TownGoal townGoal) {
        for (Map.Entry<Chunk, List<TownGoal>> c : getChunks().entrySet()){
            if (c.getKey().equals(chunk) && c.getValue().contains(townGoal)){
                chunks.get(c.getKey()).remove(townGoal);
            }
        }

        save();
    }

    public static void removeChunkTes(Chunk chunk, Treasury tes) {
        for (Map.Entry<Chunk, Treasury> c : getChunksTes().entrySet()){
            if (c.getKey().equals(chunk) && c.getValue().equals(tes)) {
                chunksTes.remove(chunk);
            }
        }

        save();
    }

    public static boolean containsChunk(Chunk chunk, TownGoal townGoal){
        for (Map.Entry<Chunk, List<TownGoal>> c : getChunks().entrySet()) {
            if (c.getKey().equals(chunk) && c.getValue().contains(townGoal))
                return true;
        }
        return false;
    }

    public static boolean containsChunkTes(Chunk chunk, Treasury tes){
        for (Map.Entry<Chunk, Treasury> c : getChunksTes().entrySet()) {
            if (c.getKey().equals(chunk) && c.getValue().equals(tes))
                return true;
        }
        return false;
    }

    public static HashMap<Chunk, List<TownGoal>> getChunks(){
        return chunks;
    }

    public static HashMap<Chunk, Treasury> getChunksTes(){
        return chunksTes;
    }

    public static List<Chunk> getChunksFromTownGoal(TownGoal tg){
        List<Chunk> app = new ArrayList<>();
        for (Map.Entry<Chunk, List<TownGoal>> entry : getChunks().entrySet()) {
            if (entry.getValue().contains(tg)) {
                app.add(entry.getKey());
            }
        }
        return app;
    }

    public static List<Chunk> getChunksFromTownTes(Treasury tes){
        List<Chunk> app = new ArrayList<>();
        for (Map.Entry<Chunk, Treasury> entry : getChunksTes().entrySet()) {
            if (tes.equals(entry.getValue())) {
                app.add(entry.getKey());
            }
        }
        return app;
    }

    public static List<TownGoal> getListTownGoalFromChunk(Chunk chunk){
        return getChunks().get(chunk) == null ? new ArrayList<>() : getChunks().get(chunk);
    }

    public static Treasury getTreasuryCurrentlyFromChunk(Chunk chunk){
        return getChunksTes().get(chunk);
    }

    public static boolean isOnTownGoal(Chunk chunk){
        return getChunks().containsKey(chunk);
    }

    public static boolean isOnTreasury(Chunk chunk){
        return getChunksTes().containsKey(chunk);
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        database.getFile().set("chunks", null);

        saveTes();
        saveGoal();
    }

    private static void saveGoal(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();

        for(Map.Entry<Chunk, List<TownGoal>> c: getChunks().entrySet()) {
            for (TownGoal tg : c.getValue()) {
                String path = "chunks." + tg.getTown().getName() + "." + tg.getGoal().getName();
                List<String> coord = new ArrayList<>();
                List<String> app = database.getFile().getStringList(path);

                if (!app.isEmpty()) {
                    coord.addAll(app);
                }

                if (!coord.contains(c.getKey().getX() + ";" + c.getKey().getZ()))
                    coord.add(c.getKey().getX() + ";" + c.getKey().getZ());

                database.getFile().set(path, coord);
            }
        }
        database.saveFile();
    }

    private static void saveTes(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();

        for(Map.Entry<Chunk, Treasury> c: getChunksTes().entrySet()) {
            Treasury tes = c.getValue();
            String path = "chunks." + tes.getTown().getName() + "." + tes.getName();
            List<String> coord = new ArrayList<>();
            List<String> app = database.getFile().getStringList(path);

            if (!app.isEmpty()) {
                coord.addAll(app);
            }

            if (!coord.contains(c.getKey().getX() + ";" + c.getKey().getZ()))
                coord.add(c.getKey().getX() + ";" + c.getKey().getZ());

            database.getFile().set(path, coord);
        }
        database.saveFile();
    }

    public static void load(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        chunks.clear();
        chunksTes.clear();

        if (database.getFile().getConfigurationSection("chunks") != null) {
            for (String town : database.getFile().getConfigurationSection("chunks").getKeys(false)) {
                for (String goal : database.getFile().getConfigurationSection("chunks." + town).getKeys(false)) {
                    String path = "chunks." + town + "." + goal;
                    List<Pair<Integer, Integer>> coord = new ArrayList<>();
                    List<String> app = database.getFile().getStringList(path);

                    for (String s : app) {
                        String[] valueCoord = s.split("\\;");
                        coord.add(new Pair<>(Integer.parseInt(valueCoord[0]), Integer.parseInt(valueCoord[1])));
                    }

                    if (goal.equals(ConfigUtil.getTreasuryName())) {
                        // Treasury
                        for (Pair<Integer, Integer> c : coord) {
                            Location loc = new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), c.getKey() * 16, 64, c.getValue() * 16);
                            chunksTes.put(loc.getChunk(), Treasuries.getFromTown(TownyUtil.getTownFromString(town)));
                        }
                    } else {
                        // TownGoal
                        for (Pair<Integer, Integer> c : coord) {
                            Location loc = new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), c.getKey() * 16, 64, c.getValue() * 16);
                            TownGoal tg = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(goal), TownyUtil.getTownFromString(town));
                            if (chunks.get(loc.getChunk()) == null) {
                                chunks.put(loc.getChunk(), new ArrayList<>());
                            }
                            chunks.get(loc.getChunk()).add(tg);
                        }
                    }
                }
            }
        }
    }

    /*
     * ==================================================================================
     * 										Particles
     * ==================================================================================
     */

    public static void spawnEffectAtBlock(Location loc) {
        loc.add(0.5,1.5,0.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 5);
        loc.getWorld().spawnParticle(Particle.REDSTONE,loc,10,dustOptions);
    }
}

package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.objects.*;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import it.forgottenworld.fwobbiettivi.utility.Util;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class GoalAreaManager {

    private static GoalAreaManager goalAreaManager;

    private HashMap<UUID, TownGoal> playerGoalAreaCreation;
    private HashMap<UUID, TownGoal> playerGoalAreaEdit;
    private HashMap<UUID, Integer> playerGoalAreaPlot;

    private HashMap<UUID, Treasury> playerTesAreaCreation;

    static HashMap<Long, List<TownGoal>> chunks = new HashMap<>();
    static HashMap<Long, Treasury> chunksTes = new HashMap<>();

    private GoalAreaManager() {
        this.playerGoalAreaCreation = new HashMap<>();
        this.playerGoalAreaEdit = new HashMap<>();
        this.playerGoalAreaPlot = new HashMap<>();
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

    public boolean isPlayerInEditMode(Player player) {
        return this.playerGoalAreaEdit.containsKey(player.getUniqueId());
    }

    public boolean isPlayerInCreationModeTes(Player player) {
        return this.playerTesAreaCreation.containsKey(player.getUniqueId());
    }

    public void putGoalAreaCreation(UUID uuid, TownGoal townGoals){
        this.playerGoalAreaCreation.put(uuid, townGoals);
    }

    public void putGoalAreaEdit(UUID uuid, TownGoal townGoals, int plot){
        this.playerGoalAreaEdit.put(uuid, townGoals);
        this.playerGoalAreaPlot.put(uuid, plot);
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

    public HashMap<UUID, TownGoal> getPlayerGoalAreaEdit() {
        return playerGoalAreaEdit;
    }

    public HashMap<UUID, Integer> getPlayerGoalAreaPlot() {
        return playerGoalAreaPlot;
    }

    public HashMap<UUID, Treasury> getPlayerTesAreaCreation() {
        return playerTesAreaCreation;
    }

    public static void addChunk(Long chunk, TownGoal townGoal){
        if (getChunks().get(chunk) == null) {
            chunks.put(chunk, new ArrayList<>());
        }
        chunks.get(chunk).add(townGoal);

        save();
    }

    public static void addChunkTes(Long chunk, Treasury tes){
        if (!containsChunkTes(chunk, tes)) {
            chunksTes.put(chunk, tes);
        }

        save();
    }

    public static void removeChunk(Long chunk, TownGoal townGoal) {
        for (Map.Entry<Long, List<TownGoal>> c : getChunks().entrySet()){
            if (c.getKey().equals(chunk) && c.getValue().contains(townGoal)){
                chunks.get(c.getKey()).remove(townGoal);
            }
        }

        save();
    }

    public static void removeChunkTes(Long chunk, Treasury tes) {
        for (Map.Entry<Long, Treasury> c : getChunksTes().entrySet()){
            if (c.getKey().equals(chunk) && c.getValue().equals(tes)) {
                chunksTes.remove(chunk);
            }
        }

        save();
    }

    public static boolean containsChunk(Long chunk, TownGoal townGoal){
        for (Map.Entry<Long, List<TownGoal>> c : getChunks().entrySet()) {
            if (c.getKey().equals(chunk) && c.getValue().contains(townGoal))
                return true;
        }
        return false;
    }

    public static boolean containsChunkTes(Long chunk, Treasury tes){
        for (Map.Entry<Long, Treasury> c : getChunksTes().entrySet()) {
            if (c.getKey().equals(chunk) && c.getValue().equals(tes))
                return true;
        }
        return false;
    }

    public static HashMap<Long, List<TownGoal>> getChunks(){
        return chunks;
    }

    public static HashMap<Long, Treasury> getChunksTes(){
        return chunksTes;
    }

    public static List<Long> getChunksFromTownGoal(TownGoal tg){
        List<Long> app = new ArrayList<>();
        for (Map.Entry<Long, List<TownGoal>> entry : getChunks().entrySet()) {
            if (entry.getValue().contains(tg)) {
                app.add(entry.getKey());
            }
        }
        return app;
    }

    public static List<Long> getChunksFromTownTes(Treasury tes){
        List<Long> app = new ArrayList<>();
        for (Map.Entry<Long, Treasury> entry : getChunksTes().entrySet()) {
            if (tes.equals(entry.getValue())) {
                app.add(entry.getKey());
            }
        }
        return app;
    }

    public static List<TownGoal> getListTownGoalFromChunk(Long chunk){
        return getChunks().get(chunk) == null ? new ArrayList<>() : getChunks().get(chunk);
    }

    public static Treasury getTreasuryCurrentlyFromChunk(Long chunk){
        return getChunksTes().get(chunk);
    }

    public static boolean isOnTownGoal(Long chunk){
        return getChunks().containsKey(chunk);
    }

    public static boolean isOnTreasury(Long chunk){
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

        for(Map.Entry<Long, List<TownGoal>> c: getChunks().entrySet()) {
            for (TownGoal tg : c.getValue()) {
                String path = "chunks." + tg.getTown().getName() + "." + tg.getGoal().getName();
                List<String> coord = new ArrayList<>();
                List<String> app = database.getFile().getStringList(path);

                if (!app.isEmpty()) {
                    coord.addAll(app);
                }

                if (!coord.contains(c.getKey()))
                    coord.add(c.getKey() + "");

                database.getFile().set(path, coord);
            }
        }
        database.saveFile();
    }

    private static void saveTes(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();

        for(Map.Entry<Long, Treasury> c: getChunksTes().entrySet()) {
            Treasury tes = c.getValue();
            String path = "chunks." + tes.getTown().getName() + "." + tes.getName();
            List<String> coord = new ArrayList<>();
            List<String> app = database.getFile().getStringList(path);

            if (!app.isEmpty()) {
                coord.addAll(app);
            }

            if (!coord.contains(c.getKey()))
                coord.add(c.getKey() + "");

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
                        coord.add(Util.getPairFromKey(Long.parseLong(valueCoord[0])));
                    }

                    if (goal.equals(ConfigUtil.getTreasuryName())) {
                        // Treasury
                        for (Pair<Integer, Integer> c : coord) {
                            Location loc = new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), c.getKey() * 16, 64, c.getValue() * 16);
                            chunksTes.put(loc.getChunk().getChunkKey(), Treasuries.getFromTown(TownyUtil.getTownFromString(town)));
                        }
                    } else {
                        // TownGoal
                        for (Pair<Integer, Integer> c : coord) {
                            Location loc = new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), c.getKey() * 16, 64, c.getValue() * 16);
                            TownGoal tg = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(goal), TownyUtil.getTownFromString(town));
                            if (chunks.get(loc.getChunk().getChunkKey()) == null) {
                                chunks.put(loc.getChunk().getChunkKey(), new ArrayList<>());
                            }
                            chunks.get(loc.getChunk().getChunkKey()).add(tg);
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

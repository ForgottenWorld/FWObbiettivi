package it.forgottenworld.fwobbiettivi.objects.managers;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.Goals;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.utility.FWLocation;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import javafx.util.Pair;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class GoalAreaManager {

    private static GoalAreaManager goalAreaManager;

    private HashMap<UUID, TownGoal> playerGoalAreaCreation;
    static HashMap<Pair<Integer, Integer>, TownGoal> chunks = new HashMap<Pair<Integer, Integer>, TownGoal>();

    private GoalAreaManager() {
        this.playerGoalAreaCreation = new HashMap<>();
    }

    public static GoalAreaManager getInstance() {
        if (goalAreaManager == null)
            goalAreaManager = new GoalAreaManager();

        return goalAreaManager;
    }

    public boolean isPlayerInCreationMode(Player player) {
        return this.playerGoalAreaCreation.containsKey(player.getUniqueId());
    }

    public void putGoalAreaCreation(UUID uuid, TownGoal townGoals){
        this.playerGoalAreaCreation.put(uuid, townGoals);
    }

    public void setPlayerGoalAreaCreation(HashMap<UUID, TownGoal> playerGoalAreaCreation) {
        this.playerGoalAreaCreation = playerGoalAreaCreation;
    }

    public HashMap<UUID, TownGoal> getPlayerGoalAreaCreation() {
        return playerGoalAreaCreation;
    }

    public static void addChunk(Location location, TownGoal townGoal){
        Pair<Integer, Integer> chunk = new Pair<>(location.getChunk().getX(), location.getChunk().getZ());
        if (!containsChunk(location)) {
            chunks.put(chunk, townGoal);
        }

        save();
    }

    public static void removeChunk(Location location) {
        Pair<Integer, Integer> chunk = new Pair<>(location.getChunk().getX(), location.getChunk().getZ());
        chunks.remove(chunk);

        save();
    }

    public static boolean containsChunk(Location location){
        Pair<Integer, Integer> chunk = new Pair<>(location.getChunk().getX(), location.getChunk().getZ());
        for (Map.Entry<Pair<Integer, Integer>, TownGoal> entry : chunks.entrySet()) {
            if (chunk.equals(entry.getKey()))
                return true;
        }
        return false;
    }

    public static HashMap<Pair<Integer, Integer>, TownGoal> getChunks(){
        return chunks;
    }

    public static HashMap<Pair<Integer, Integer>, TownGoal> getChunksFromTownGoal(TownGoal tg){
        HashMap<Pair<Integer, Integer>, TownGoal> app = new HashMap<>();
        for (Map.Entry<Pair<Integer, Integer>, TownGoal> entry : getChunks().entrySet()) {
            if (tg.equals(entry.getValue())) {
                app.put(entry.getKey(), entry.getValue());
            }
        }
        return app;
    }

    public static TownGoal getTownGoalCurrentlyAre(Location location){
        Pair<Integer, Integer> chunk = new Pair<>(location.getChunk().getX(), location.getChunk().getZ());
        for (Map.Entry<Pair<Integer, Integer>, TownGoal> entry : getChunks().entrySet()) {
            if (chunk.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static boolean isOnTownGoal(Location location){
        Pair<Integer, Integer> chunk = new Pair<>(location.getChunk().getX(), location.getChunk().getZ());
        for (Map.Entry<Pair<Integer, Integer>, TownGoal> entry : getChunks().entrySet()) {
            if (chunk.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save(){
        StringBuilder sb = new StringBuilder();

        for (Pair<Integer, Integer> pair: chunks.keySet()){
            TownGoal tg = chunks.get(pair);
            sb.append(pair.getKey()).append("*");
            sb.append(pair.getValue()).append("*");
            sb.append(tg.getTown().getUuid().toString()).append("*");
            sb.append(tg.getGoal().getName()).append("*");
            sb.append(FWLocation.getStringFromLocation(tg.getLocation())).append("*");
            sb.append(tg.isActive());
            sb.append("|");
        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        try {
            FileWriter writer = new FileWriter("plugins/FWObbiettivi/chunks.markus");
            if(sb.length() == 0){
                writer.write("");
            }else {
                writer.write(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("plugins/FWObbiettivi/chunks.markus");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            inputStream.close();

            if(sb.length() != 0){
                List<String> file = Arrays.asList(sb.toString().split("\\|"));

                for (String s: file){
                    String[] valueString = s.split("\\*");
                    TownGoal tg = new TownGoal();
                    TownyDataSource tds = TownyUniverse.getInstance().getDataSource();
                    tg.setTown(tds.getTown(UUID.fromString(valueString[2])));

                    for(Goal g : Goals.getObbiettivi()){
                        if(g.getName().equals(valueString[3])){
                            tg.setGoal(g);
                            break;
                        }
                    }

                    tg.setLocation(FWLocation.getLocationFromString(valueString[4]));

                    tg.setActive(Boolean.valueOf(valueString[5]));

                    chunks.put(new Pair<>(Integer.valueOf(valueString[0]), Integer.valueOf(valueString[1])), tg);
                }
            }

        } catch (FileNotFoundException e) {
            FWObbiettivi.info(Messages.NO_EXISTING_FILE_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GoalAreaManager {

    private static GoalAreaManager goalAreaManager;

    private HashMap<UUID, TownGoals> playerGoalAreaCreation;

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

    public void putGoalAreaCreation(UUID uuid, TownGoals townGoals){
        this.playerGoalAreaCreation.put(uuid, townGoals);
    }

    public void setPlayerGoalAreaCreation(HashMap<UUID, TownGoals> playerGoalAreaCreation) {
        this.playerGoalAreaCreation = playerGoalAreaCreation;
    }

    public HashMap<UUID, TownGoals> getPlayerGoalAreaCreation() {
        return playerGoalAreaCreation;
    }
}

package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GoalAreaCreationListener implements Listener {

    static HashMap<UUID, Integer> numClick = new HashMap<>();

    @EventHandler
    public void onRightClickSelection(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (e.getItem() != null)
            return;

        Player player = e.getPlayer();
        if (!GoalAreaManager.getInstance().isPlayerInCreationMode(player))
            return;

        if ((numClick.get(e.getPlayer().getUniqueId()) == null) || (numClick.get(e.getPlayer().getUniqueId()) % 2 == 0)) {
            numClick.put(e.getPlayer().getUniqueId(), 1);
        } else {
            numClick.remove(e.getPlayer().getUniqueId());
            return;
        }

        if (!ConfigUtil.MULTI_GOAL) {
            if (GoalAreaManager.getChunks().containsKey(e.getClickedBlock().getChunk().getChunkKey()) && GoalAreaManager.getChunks().get(e.getClickedBlock().getChunk().getChunkKey()).size() == 1) {
                e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_PLOT_ALREADY_SET) + " " + ChatFormatter.formatWarningMessageNoPrefix(GoalAreaManager.getListTownGoalFromChunk(e.getClickedBlock().getLocation().getChunk().getChunkKey()).get(0).getGoal().getName()));
                return;
            }

            if (GoalAreaManager.getChunksTes().containsKey(e.getClickedBlock().getChunk().getChunkKey())) {
                e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.TREASURY_PLOT_ALREADY_SET));
                return;
            }
        } else {
            List<TownGoal> townGoals = GoalAreaManager.getListTownGoalFromChunk(e.getClickedBlock().getLocation().getChunk().getChunkKey());
            if (townGoals.isEmpty()) {
                e.getPlayer().sendMessage(ChatFormatter.formatSuccessMessage(Messages.NO_GOAL_LOC));
            } else {
                e.getPlayer().sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOALS_PRESENT));
                for (TownGoal tg:townGoals){
                    e.getPlayer().sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + tg.getGoal().getName()));
                }
            }
        }

        List<Pair<Integer, Integer>> chunksList = new ArrayList<>();
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX() + 1, e.getClickedBlock().getChunk().getZ()));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX() - 1, e.getClickedBlock().getChunk().getZ()));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ() + 1));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ() - 1));

        for (Pair<Integer, Integer> chunk : chunksList){
            Location loc = new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), chunk.getKey() * 16, 64, chunk.getValue() * 16);
            TownGoal tg = GoalAreaManager.getInstance().getPlayerGoalAreaCreation().get(e.getPlayer().getUniqueId());
            if (!GoalAreaManager.getChunksFromTownGoal(tg).contains(loc.getChunk().getChunkKey())){
                int maxPlot = tg.getGoal().getNumPlot();
                long chunkCount = GoalAreaManager.getChunksFromTownGoal(tg).size();

                if (GoalAreaManager.getChunks().get(e.getClickedBlock().getLocation().getChunk().getChunkKey()) != null) {
                    if (GoalAreaManager.getListTownGoalFromChunk(e.getClickedBlock().getLocation().getChunk().getChunkKey()).size() + (GoalAreaManager.getTreasuryCurrentlyFromChunk(e.getClickedBlock().getLocation().getChunk().getChunkKey()) == null ? 0 : 1) >= ConfigUtil.MAX_GOAL_IN_CHUNK) {
                        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.MAX_GOAL_IN_CHUNK));
                        return;
                    }
                }

                if (chunkCount < maxPlot){

                    if (!TownyUtil.isInTown(e.getClickedBlock().getLocation())) {
                        // Not in a town
                        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        return;
                    }

                    TownyUtil.renamePlot(e.getClickedBlock().getLocation(), tg.getGoal().getName(), false);
                    GoalAreaManager.addChunk(e.getPlayer().getLocation().getChunk().getChunkKey(), tg);

                    e.getPlayer().sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix((chunkCount + 1) + "/" + maxPlot));

                    if (e.getPlayer().getTargetBlockExact(5) == null)
                        chunkCount--;
                    else
                        e.getPlayer().getTargetBlockExact(5).getWorld().playSound(e.getPlayer().getTargetBlockExact(5).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);

                    GoalAreaManager.spawnEffectAtBlock(e.getPlayer().getTargetBlockExact(5).getLocation());

                    if ((chunkCount + 1) == maxPlot) {
                        Treasury tes = Treasuries.getFromTown(tg.getTown());
                        // Get objects
                        if (!tg.getGoal().getRequiredObjects().isEmpty()) {
                            for (ItemStack is : tg.getGoal().getRequiredObjects()) {
                                int quantity = is.getAmount();
                                for (int i = 0; i < tes.getTreasuryChestInventory().getSize(); i++) {
                                    if (quantity > 0 && tes.getTreasuryChestInventory().getItem(i) != null && tes.getTreasuryChestInventory().getItem(i).getType() == is.getType())
                                        if (quantity >= tes.getTreasuryChestInventory().getItem(i).getAmount()) {
                                            quantity -= tes.getTreasuryChestInventory().getItem(i).getAmount();
                                            tes.getTreasuryChestInventory().setItem(i, null);
                                        } else {
                                            int newAmount = tes.getTreasuryChestInventory().getItem(i).getAmount() - quantity;
                                            quantity = 0;
                                            tes.getTreasuryChestInventory().setItem(i, new ItemStack(is.getType(), newAmount));
                                        }
                                }
                            }
                            player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TOWN_OBJECTS_WITHDRAWN));
                        }

                        // Withdraw zenar
                        if (tg.getGoal().getRequiredZenar() > 0) {
                            try {
                                tg.getTown().getAccount().withdraw(tg.getGoal().getRequiredZenar(), "Goal creation payment.");
                                player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TOWN_MONEY_WITHDRAWN));
                            } catch (EconomyException ex) {
                                ex.printStackTrace();
                            }
                        }

                        GoalAreaManager.getInstance().getPlayerGoalAreaCreation().remove(e.getPlayer().getUniqueId());
                    }
                }
                return;
            }
        }

        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_PLOT_NOT_NEAR));

    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if (!GoalAreaManager.getInstance().isPlayerInCreationMode(player))
            return;

        TownGoal tg = GoalAreaManager.getInstance().getPlayerGoalAreaCreation().get(e.getPlayer().getUniqueId());

        // Removing Goal to that Town
        TownGoals.removeTownGoal(tg);
        FWObbiettivi.info(Messages.ABORT_ADD_GOAL);

        GoalAreaManager.getInstance().getPlayerGoalAreaCreation().remove(e.getPlayer().getUniqueId());
    }

}
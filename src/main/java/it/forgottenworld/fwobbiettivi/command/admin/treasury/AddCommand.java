package it.forgottenworld.fwobbiettivi.command.admin.treasury;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.utility.*;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandNames;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends SubCommand {
    @Override
    public String getName() {
        return TreasuryCommandNames.ADD_CMD;
    }

    @Override
    public String getDescription() {
        return TreasuryCommandDescriptions.ADD_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_TREASURY_ADD;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // Save location of chest
        Block b = player.getTargetBlock(null, 5);
        if (b.getType() != Material.CHEST) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_CHEST));
        } else {
            Chest chest = (Chest) b.getState();
            if (!(chest.getInventory() instanceof DoubleChestInventory)) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_SINGLE_CHEST));
                return;
            }

            if (!TownyUtil.isInTown(b.getLocation())){
                // Not in a town
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                return;
            }

            Chest right = (Chest)((DoubleChest) chest.getInventory().getHolder()).getRightSide();
            Chest left = (Chest)((DoubleChest) chest.getInventory().getHolder()).getLeftSide();

            if (!Treasuries.isSameChunk(right.getLocation(), left.getLocation())){
                // Not same location
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_SAME_LOC));
                return;
            }

            Town townTes = TownyUtil.getTownFromLocation(b.getLocation());
            if (townTes == null) {
                // Not in a town
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                return;
            }

            // Check if the Treasury exist
            // Check if the Goal is alredy present in Town
            if (Treasuries.containsTreasury(TownyUtil.getTownFromLocation(player.getLocation()))) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.TREASURY_ALREADY_PRESENT));
                return;
            }

            // Adding Treaury to that Town
            Treasury tes = new Treasury(ConfigUtil.getTreasuryName(), townTes, right.getLocation(), left.getLocation(), ConfigUtil.getTreasuryNumPlot());
            Treasuries.addTreasury(tes);
            // Adding chunk to the chunk control system
            GoalAreaManager.addChunkTes(right.getLocation().getChunk(), tes);
            GoalAreaManager.getInstance().putTesAreaCreation(player.getUniqueId(), tes);

            // Rename plot to Treasury name
            TownyUtil.renamePlot(b.getLocation(), tes.getName(), false);

            player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_ADDED) + " " + ChatFormatter.formatWarningMessageNoPrefix(townTes.getName()));

            b.getWorld().playSound(b.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            GoalAreaManager.spawnEffectAtBlock(b.getLocation());

            if (tes.getNumPlot() > 1)
                player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix("1/" + tes.getNumPlot()));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}

package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Treasury {

    private Town town;
    private Location location;

    public Treasury(Town town, Location location){
        this.town = town;
        this.location = location;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location chest) {
        this.location = chest;
    }

    public void openChest(Player player){
        if (location.getBlock().getType().equals(Material.CHEST)){
            if (TownyUniverse.getTownBlock(location) == null)
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));

            player.openInventory(((Chest) location.getBlock().getState()).getBlockInventory());
        } else {
            // TODO messages
            player.sendMessage(ChatColor.RED + "Attenzione, la chest non è stata trovata.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treasury)) return false;
        Treasury treasury = (Treasury) o;
        return Objects.equals(getTown(), treasury.getTown()) &&
                Objects.equals(getLocation(), treasury.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTown(), getLocation());
    }

    @Override
    public String toString() {
        return "Treasury{" +
                "town=" + town +
                ", chest=" + location +
                '}';
    }

}

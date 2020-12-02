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
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class Treasury {

    private String name;
    private Town town;
    private Location chestRight;
    private Location chestLeft;
    private int numPlot;

    public Treasury(String name, Town town, Location chestRight, Location chestLeft, int numPlot){
        this.name = name;
        this.town = town;
        this.chestRight = chestRight;
        this.chestLeft = chestLeft;
        this.numPlot = numPlot;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Location getLocationChestRight() {
        return chestRight;
    }

    public void setLocationChestRight(Location chest) {
        this.chestRight = chest;
    }

    public Location getLocationChestLeft() {
        return chestLeft;
    }

    public void setLocationChestLeft(Location chest) {
        this.chestLeft = chest;
    }

    public void setNumPlot(int numPlot) {
        this.numPlot = numPlot;
    }

    public int getNumPlot() {
        return numPlot;
    }

    public void openChest(Player player){
        if (chestRight.getBlock().getType().equals(Material.CHEST)){
            if (TownyUniverse.getTownBlock(chestRight) == null)
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));

            player.openInventory(getTreasuryChestInventory());
        } else {
            // TODO messages
            player.sendMessage(ChatColor.RED + "Attenzione, la chest non è stata trovata.");
        }
    }

    public Inventory getTreasuryChestInventory(){
        Chest chest = (Chest) chestRight.getBlock().getState();
        return chest.getInventory().getHolder().getInventory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treasury)) return false;
        Treasury treasury = (Treasury) o;
        return Objects.equals(getName(), treasury.getName()) &&
                Objects.equals(getTown(), treasury.getTown()) &&
                Objects.equals(chestRight, treasury.chestRight) &&
                Objects.equals(chestLeft, treasury.chestLeft) &&
                Objects.equals(getNumPlot(), treasury.getNumPlot());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTown(), chestRight, chestLeft, getNumPlot());
    }

    @Override
    public String toString() {
        return "Treasury{" +
                "name='" + name + '\'' +
                ", town=" + town +
                ", chestRight=" + chestRight +
                ", chestLeft=" + chestLeft +
                ", numPlot=" + numPlot +
                '}';
    }
}

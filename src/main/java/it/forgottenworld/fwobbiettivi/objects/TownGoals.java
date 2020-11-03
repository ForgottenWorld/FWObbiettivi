package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * Class that manages a single objective. This class is created
 * for each chest that needs to convert an objective.
 */
public class TownGoals {

    private Town town;
    private Goal goal;
    private Location location;
    private boolean active;

    /**
     * Constructor
     */
    public TownGoals(){
        this.active = true;
    }

    /**
     * Constructor
     * @param town Town object containing the town to which the goals belong.
     * @param goal Goal object which contains all the information about the goal.
     * @param location Position of the FWChest associated with this goal.
     */
    public TownGoals(Town town, Goal goal, Location location){
        this.town = town;
        this.goal = goal;
        this.location = location;
        this.active = true;
    }

    /**
     * Constructor
     * @param town Town object containing the town to which the goals belong.
     * @param goal Goal object which contains all the information about the goal.
     * @param location Position of the FWChest associated with this goal.
     * @param active It defines a new goal differently, by default it is enabled.
     */
    public TownGoals(Town town, Goal goal, Location location, boolean active){
        this.town = town;
        this.goal = goal;
        this.location = location;
        this.active = active;
    }

    /*
     * ==================================================================================
     * 									Getters & Setters
     * ==================================================================================
     */

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /* ==================================================================================
     *  								   CONVERSION
     * ==================================================================================
     */

    public boolean pay(){
        convert(((Chest) this.getLocation().getBlock().getState()).getBlockInventory());
        return true;
    }

    private void convert(Inventory inv) {
        boolean hasEnoughtItems = true;
        List<ItemStack> inputStacks = this.getGoal().getPayment();
        List<ItemStack> outputStacks = this.getGoal().getReward();

        while (hasEnoughtItems){
            for (ItemStack is : inputStacks)
                hasEnoughtItems &= containsAtLeast(inv, is.getType(), is.getAmount());

            if (hasEnoughtItems){
                for (ItemStack is : inputStacks)
                    removeExactly(inv, is.getType(), is.getAmount());

                for (ItemStack is : outputStacks)
                    inv.addItem(is);
            }
        }
    }

    private boolean containsAtLeast(Inventory inv, Material material, int quantity) {
        int totalQuantity = 0;
        for (ItemStack is : inv){
            if (is != null && is.getType() == material ){
                totalQuantity += is.getAmount();
            }
        }
        return totalQuantity >= quantity;
    }

    private void removeExactly(Inventory inv, Material material, int quantity) {
        for (int i=0; i<inv.getSize(); i++){
            System.out.println(inv.getSize());
            if (quantity > 0 && inv.getItem(i) != null && inv.getItem(i).getType() == material)
                if (quantity >= inv.getItem(i).getAmount()) {
                    System.out.println("remove Item");
                    quantity -= inv.getItem(i).getAmount();
                    inv.setItem(i, null);
                } else {
                    System.out.println("refuse item");
                    int newAmount = inv.getItem(i).getAmount() - quantity;
                    quantity -= newAmount;
                    inv.setItem(i, new ItemStack(material, newAmount));
                }
        }
    }

    /* ==================================================================================
     *  								   	  UTILS
     * ==================================================================================
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TownGoals)) return false;
        TownGoals townGoals = (TownGoals) o;
        return isActive() == townGoals.isActive() &&
                Objects.equals(getTown(), townGoals.getTown()) &&
                Objects.equals(getGoal(), townGoals.getGoal()) &&
                Objects.equals(getLocation(), townGoals.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTown(), getGoal(), getLocation(), isActive());
    }

    @Override
    public String toString() {
        return "TownGoals{" +
                "town=" + town +
                ", goal=" + goal +
                ", location=" + location +
                ", active=" + active +
                '}';
    }
}

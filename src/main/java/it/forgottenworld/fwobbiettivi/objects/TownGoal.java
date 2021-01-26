package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
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
public class TownGoal {

    private Town town;
    private Goal goal;
    private Location location;
    private boolean active;

    /**
     * Constructor
     */
    public TownGoal(){
        this.active = true;
    }

    /**
     * Constructor
     * @param town Town object containing the town to which the goals belong.
     * @param goal Goal object which contains all the information about the goal.
     * @param location Position of the FWChest associated with this goal.
     */
    public TownGoal(Town town, Goal goal, Location location){
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
    public TownGoal(Town town, Goal goal, Location location, boolean active){
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
        boolean response = this.active;
        for (String s : this.goal.getRequiredGoals()){
            if(Goals.getGoalFromString(s) != null)
                response &= TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(s), this.town).isActive();
        }

        return response;
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
        try {
            town.collect(goal.getRewardZenar());
        } catch (EconomyException e) {
            e.printStackTrace();
        }
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
            if (quantity > 0 && inv.getItem(i) != null && inv.getItem(i).getType() == material)
                if (quantity >= inv.getItem(i).getAmount()) {
                    quantity -= inv.getItem(i).getAmount();
                    inv.setItem(i, null);
                } else {
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
        if (!(o instanceof TownGoal)) return false;
        TownGoal townGoals = (TownGoal) o;
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

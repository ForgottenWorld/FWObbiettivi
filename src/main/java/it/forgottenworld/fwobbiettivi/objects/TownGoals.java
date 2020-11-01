package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TownGoals {

    private Town town;
    private Goal goal;
    private Location location;

    public TownGoals(){}

    public TownGoals(Town t, Goal g, Location l){
        this.town = t;
        this.goal = g;
        this.location = l;
    }

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

    public boolean pay(){
        convert(((Chest) this.getLocation().getBlock().getState()).getBlockInventory());
        return true;
    }

    private void convert(Inventory inv) {
        boolean hasEnoughtItems = true;
        List<ItemStack> inputStacks = this.getGoal().getPayment();
        List<ItemStack> outputStacks = this.getGoal().getReward();

        while (hasEnoughtItems){
            for (ItemStack is : inputStacks) {
                hasEnoughtItems &= containsAtLeast(inv, is.getType(), is.getAmount());
                System.out.println("hasEnoughtItems step");
            }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TownGoals)) return false;
        TownGoals townGoals = (TownGoals) o;
        return Objects.equals(getTown(), townGoals.getTown()) &&
                Objects.equals(getGoal(), townGoals.getGoal()) &&
                Objects.equals(getLocation(), townGoals.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTown(), getGoal(), getLocation());
    }

    @Override
    public String toString() {
        return "TownGoals{" +
                "town=" + town +
                ", goal=" + goal +
                ", location=" + location +
                '}';
    }
}

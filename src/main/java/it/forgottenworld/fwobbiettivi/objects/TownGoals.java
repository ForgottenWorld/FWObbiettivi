package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;

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

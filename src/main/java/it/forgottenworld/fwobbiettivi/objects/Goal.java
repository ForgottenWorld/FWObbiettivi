package it.forgottenworld.fwobbiettivi.objects;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Goal {

    /**
     * name = Nome dell'Obbiettivo Cittadino
     * branch = Ramo di obbiettivi al quale appartiene
     * requiredGoals = Obbiettivi "Padri"
     * payment = Pagamento richiesto per un determinato beneficio (opzionale)
     * reward = Ricompensa a seguito di un pagamento o pro-bono
     */
    private String name;
    private Branch branch;
    private int numPlot;
    private List<String> requiredGoals;
    private double requiredZenar;
    private List<ItemStack>  payment;
    private List<ItemStack>  reward;
    private double rewardZenar;
    private List<String> descrizione;

    public Goal(String name, Branch branch, int numPlot, List<String> requiredGoals, double requiredZenar, List<ItemStack> payment, List<ItemStack> reward, double rewardZenar, List<String> descrizione){
        this.name = name;
        this.branch = branch;
        this.numPlot = numPlot;
        this.requiredGoals = requiredGoals;
        this.requiredZenar = requiredZenar;
        this.payment = payment;
        this.reward = reward;
        this.rewardZenar = rewardZenar;
        this.descrizione = descrizione;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getNumPlot() {
        return numPlot;
    }

    public void setNumPlot(int numPlot) {
        this.numPlot = numPlot;
    }

    public List<String> getRequiredGoals() {
        return requiredGoals;
    }

    public void setRequiredGoals(List<String> requiredGoals) {
        this.requiredGoals = requiredGoals;
    }

    public double getRequiredZenar() {
        return requiredZenar;
    }

    public void setRequiredZenar(double requiredZenar) {
        this.requiredZenar = requiredZenar;
    }

    public List<ItemStack> getPayment() {
        return payment;
    }

    public void setPayment(List<ItemStack> payment) {
        this.payment = payment;
    }

    public List<ItemStack> getReward() {
        return reward;
    }

    public void setReward(List<ItemStack> reward) {
        this.reward = reward;
    }

    public double getRewardZenar() {
        return rewardZenar;
    }

    public void setRewardZenar(double rewardZenar) {
        this.rewardZenar = rewardZenar;
    }

    public List<String> getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(List<String> descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal)) return false;
        Goal goal = (Goal) o;
        return getNumPlot() == goal.getNumPlot() &&
                Double.compare(goal.getRequiredZenar(), getRequiredZenar()) == 0 &&
                Double.compare(goal.getRewardZenar(), getRewardZenar()) == 0 &&
                Objects.equals(getName(), goal.getName()) &&
                Objects.equals(getBranch(), goal.getBranch()) &&
                Objects.equals(getRequiredGoals(), goal.getRequiredGoals()) &&
                Objects.equals(getPayment(), goal.getPayment()) &&
                Objects.equals(getReward(), goal.getReward()) &&
                Objects.equals(getDescrizione(), goal.getDescrizione());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBranch(), getNumPlot(), getRequiredGoals(), getRequiredZenar(), getPayment(), getReward(), getRewardZenar(), getDescrizione());
    }

    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", branch=" + branch +
                ", numPlot=" + numPlot +
                ", requiredGoals=" + requiredGoals +
                ", requiredZenar=" + requiredZenar +
                ", payment=" + payment +
                ", reward=" + reward +
                ", rewardZenar=" + rewardZenar +
                ", descrizione=" + descrizione +
                '}';
    }
}

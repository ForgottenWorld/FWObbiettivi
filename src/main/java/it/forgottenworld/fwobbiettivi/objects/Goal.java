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
    private List<String> requiredGoals;
    private List<ItemStack>  payment;
    private List<ItemStack>  reward;
    private List<String> descrizione;

    public Goal(String name, Branch branch, List<String> requiredGoals, List<ItemStack> payment, List<ItemStack> reward, List<String> descrizione){
        this.name = name;
        this.branch = branch;
        this.requiredGoals = requiredGoals;
        this.payment = payment;
        this.reward = reward;
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

    public List<String> getRequiredGoals() {
        return requiredGoals;
    }

    public void setRequiredGoals(List<String> requiredGoals) {
        this.requiredGoals = requiredGoals;
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
        return Objects.equals(getName(), goal.getName()) &&
                Objects.equals(getBranch(), goal.getBranch()) &&
                Objects.equals(getRequiredGoals(), goal.getRequiredGoals()) &&
                Objects.equals(getPayment(), goal.getPayment()) &&
                Objects.equals(getReward(), goal.getReward()) &&
                Objects.equals(getDescrizione(), goal.getDescrizione());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getBranch(), getRequiredGoals(), getPayment(), getReward(), getDescrizione());
        return result;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", branch=" + branch +
                ", requiredGoals=" + requiredGoals +
                ", payment=" + payment +
                ", reward=" + reward +
                ", descrizione=" + descrizione +
                '}';
    }
}

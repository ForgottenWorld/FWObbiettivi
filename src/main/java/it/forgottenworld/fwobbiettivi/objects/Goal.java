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
    private String[] requiredGoals;
    private ItemStack[] payment;
    private ItemStack[] reward;
    private List<String> descrizione;

    public Goal(String name, Branch branch, String[] requiredGoals, ItemStack[] payment, ItemStack[] reward, List<String> descrizione){
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

    public String[] getRequiredGoals() {
        return requiredGoals;
    }

    public void setRequiredGoals(String[] requiredGoals) {
        this.requiredGoals = requiredGoals;
    }

    public ItemStack[] getPayment() {
        return payment;
    }

    public void setPayment(ItemStack[] payment) {
        this.payment = payment;
    }

    public ItemStack[] getReward() {
        return reward;
    }

    public void setReward(ItemStack[] reward) {
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
                Arrays.equals(getRequiredGoals(), goal.getRequiredGoals()) &&
                Arrays.equals(getPayment(), goal.getPayment()) &&
                Arrays.equals(getReward(), goal.getReward()) &&
                Objects.equals(getDescrizione(), goal.getDescrizione());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getBranch(), getDescrizione());
        result = 31 * result + Arrays.hashCode(getRequiredGoals());
        result = 31 * result + Arrays.hashCode(getPayment());
        result = 31 * result + Arrays.hashCode(getReward());
        return result;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", branch=" + branch +
                ", requiredGoals=" + Arrays.toString(requiredGoals) +
                ", payment=" + Arrays.toString(payment) +
                ", reward=" + Arrays.toString(reward) +
                ", descrizione=" + descrizione +
                '}';
    }
}

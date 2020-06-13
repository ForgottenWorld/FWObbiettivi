package it.forgottenworld.fwobbiettivi.goals;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class Obbiettivo {

    /**
     * name = Nome dell'Obbiettivo Cittadino
     * branch = Ramo di obbiettivi al quale appartiene
     * requiredGoals = Obbiettivi "Padri"
     * payment = Pagamento richiesto per un determinato beneficio (opzionale)
     * reward = Ricompensa a seguito di un pagamento o pro-bono
     */
    private String name;
    private Ramo branch;
    private String[] requiredGoals;
    private ItemStack[] payment;
    private ItemStack[] reward;

    public Obbiettivo(String name, Ramo branch, String[] requiredGoals, ItemStack[] payment, ItemStack[] reward){
        this.name = name;
        this.branch = branch;
        this.requiredGoals = requiredGoals;
        this.payment = payment;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ramo getBranch() {
        return branch;
    }

    public void setBranch(Ramo branch) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Obbiettivo)) return false;
        Obbiettivo that = (Obbiettivo) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getBranch(), that.getBranch()) &&
                Arrays.equals(getRequiredGoals(), that.getRequiredGoals()) &&
                Arrays.equals(getPayment(), that.getPayment()) &&
                Arrays.equals(getReward(), that.getReward());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getBranch());
        result = 31 * result + Arrays.hashCode(getRequiredGoals());
        result = 31 * result + Arrays.hashCode(getPayment());
        result = 31 * result + Arrays.hashCode(getReward());
        return result;
    }

    @Override
    public String toString() {
        return "Obbiettivo{" +
                "name='" + name + '\'' +
                ", branch=" + branch +
                ", requiredGoals=" + Arrays.toString(requiredGoals) +
                ", payment=" + Arrays.toString(payment) +
                ", reward=" + Arrays.toString(reward) +
                '}';
    }
}

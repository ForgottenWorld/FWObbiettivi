package it.forgottenworld.fwobbiettivi.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Branch{

    private String name;
    private ItemStack icon;
    private List<String> descrizione;

    public Branch(String name, ItemStack icon, List<String> descrizione){
        this.name = name;
        this.icon = icon;
        this.descrizione = descrizione;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
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
        if (!(o instanceof Branch)) return false;
        Branch ramo = (Branch) o;
        return Objects.equals(getName(), ramo.getName()) &&
                Objects.equals(getIcon(), ramo.getIcon()) &&
                Objects.equals(getDescrizione(), ramo.getDescrizione());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIcon(), getDescrizione());
    }

    @Override
    public String toString() {
        return "Ramo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", descrizione=" + descrizione +
                '}';
    }
}

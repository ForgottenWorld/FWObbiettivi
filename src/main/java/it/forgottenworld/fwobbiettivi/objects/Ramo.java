package it.forgottenworld.fwobbiettivi.objects;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Ramo {

    private String name;
    private ItemStack item;

    public Ramo (String name, ItemStack item){
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ramo)) return false;
        Ramo ramo = (Ramo) o;
        return Objects.equals(getName(), ramo.getName()) &&
                Objects.equals(getItem(), ramo.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getItem());
    }

    @Override
    public String toString() {
        return "Ramo{" +
                "name='" + name + '\'' +
                ", item=" + item +
                '}';
    }
}

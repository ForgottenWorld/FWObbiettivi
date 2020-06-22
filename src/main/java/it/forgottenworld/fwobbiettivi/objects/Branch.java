package it.forgottenworld.fwobbiettivi.objects;

import org.bukkit.Material;

import java.util.List;
import java.util.Objects;

public class Branch {

    private String name;
    private Material material;
    private List<String> descrizione;

    public Branch(String name, Material material, List<String> descrizione){
        this.name = name;
        this.material = material;
        this.descrizione = descrizione;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
                Objects.equals(getMaterial(), ramo.getMaterial()) &&
                Objects.equals(getDescrizione(), ramo.getDescrizione());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMaterial(), getDescrizione());
    }

    @Override
    public String toString() {
        return "Ramo{" +
                "name='" + name + '\'' +
                ", material=" + material +
                ", descrizione=" + descrizione +
                '}';
    }
}

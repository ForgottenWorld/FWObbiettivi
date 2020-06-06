package it.forgottenworld.fwobbiettivi.goals;

public class Obbiettivi {

    private String name;
    private Ramo branch;
    private String[] required;

    public Obbiettivi(String name, Ramo branch, String[] required){
        this.name = name;
        this.branch = branch;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public Ramo getBranch() {
        return branch;
    }

    public String[] getRequired() {
        return required;
    }
}

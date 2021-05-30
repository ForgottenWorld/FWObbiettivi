package it.forgottenworld.fwobbiettivi.utility;

import javafx.util.Pair;

public class Util {

    public static Pair<Integer, Integer> getPairFromKey(long key){
        int x = (int) key;
        int z = (int) (key >> 32);

        Pair<Integer, Integer> chunkCoords = new Pair<>(x, z);

        return chunkCoords;
    }

}

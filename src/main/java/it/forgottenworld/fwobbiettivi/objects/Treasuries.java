package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.utility.FWLocation;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Treasuries {

    static List<Treasury> treasuries = new ArrayList<>();

    /**
     *
     * @param tes
     */
    public static void addTreasury(Treasury tes) {
        if (!containsTreasury(tes.getTown()))
            treasuries.add(tes);
    }

    /**
     *
     * @param tes
     */
    public static void removeTreasury(Treasury tes) {
        treasuries.remove(tes);
    }

    /**
     *
     * @param town
     * @return
     */
    public static boolean containsTreasury(Town town) {
        for (Treasury t : treasuries)
            if (t.getTown().equals(town))
                return true;
        return false;
    }

    /**
     *
     * @param town
     * @return
     */
    public static Treasury getFromTown(Town town) {
        for (Treasury t : treasuries)
            if (t.getTown().equals(town))
                return t;
        return null;
    }

    /**
     * Returns a printable list of all objectives present
     */
    public static String getPrintableList() {
        String out = "";
        for (Treasury t : treasuries)
            out = out + "* " + t.toString() + "\n";
        return out;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    /**
     *
     */
    public static void save(){
        StringBuilder sb = new StringBuilder();

        for(Treasury tes: treasuries){
            sb.append(tes.getTown().getUuid().toString()).append("*");
            sb.append(FWLocation.getStringFromLocation(tes.getLocation()));
            sb.append("|");
        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        try {
            FileWriter writer = new FileWriter("plugins/FWObbiettivi/treasuries.markus");
            if(sb.length() == 0){
                writer.write("");
            }else {
                writer.write(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO

    /**
     *
     */
    public static void load(){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("plugins/FWObbiettivi/treasuries.markus");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            inputStream.close();

            if(sb.length() != 0){
                List<String> file = Arrays.asList(sb.toString().split("\\|"));

                for (String s: file){
                    String[] valueString = s.split("\\*");
                    Treasury tes = new Treasury(TownyUtil.getTownFromUUID(UUID.fromString(valueString[0])), FWLocation.getLocationFromString(valueString[1]));

                    Block b = tes.getLocation().getBlock();
                    Chest chestState = (Chest) b.getState();
                    chestState.setCustomName("FWChest");
                    b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

                    treasuries.add(tes);
                }
            }

        } catch (FileNotFoundException e) {
            FWObbiettivi.info(Messages.NO_EXISTING_FILE_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

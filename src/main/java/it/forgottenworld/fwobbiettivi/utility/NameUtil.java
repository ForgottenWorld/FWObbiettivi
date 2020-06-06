package it.forgottenworld.fwobbiettivi.utility;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NameUtil {

    public static List<String> filterByStart(List<String> list, String startingWith) {
        if (list == null || startingWith == null) {
            return Collections.emptyList();
        }
        return list.stream().filter(name -> name.toLowerCase().startsWith(startingWith.toLowerCase())).collect(Collectors.toList());
    }

}

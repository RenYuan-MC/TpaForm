package ltd.rymc.form.tpa.utils;

import java.util.Arrays;

@SuppressWarnings("unused")
public class ArraysUtils {
    public static String[] rotate(String[] original, int num){
        String[] newList = new String[original.length + num];
        System.arraycopy(original, 0, newList, num, original.length);
        return newList;
    }

    public static String[] arraysFilter(String[] arr, String fil) {
        return Arrays.stream(arr).filter(s -> !fil.equals(s)).toArray(String[]::new);
    }

}

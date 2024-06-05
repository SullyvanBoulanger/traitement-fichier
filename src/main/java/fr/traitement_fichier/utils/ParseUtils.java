package fr.traitement_fichier.utils;

public class ParseUtils {
    
    public static int parseInt(String label) {
        try {
            return Integer.parseInt(label);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
}

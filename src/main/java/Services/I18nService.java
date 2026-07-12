package Services; // <- Ini bagian yang kemungkinan besar terhapus sebelumnya

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.WeakHashMap;

public class I18nService {
    private static ResourceBundle bundle;
    private static Locale currentLocale;
    
    // Interface untuk mendaftarkan UI yang ingin mendengarkan perubahan bahasa
    public interface I18nChangeListener {
        void onLanguageChanged();
    }
    
    // MENGGUNAKAN WEAK HASH MAP AGAR TIDAK TERJADI PENUMPUKAN MEMORI (LAG)
    private static final Set<I18nChangeListener> listeners = Collections.newSetFromMap(new WeakHashMap<>());
    
    // Blok inisialisasi default agar tidak NullPointerException di awal aplikasi
    static {
        setLocale(Locale.of("id")); 
    }
    
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
        notifyListeners();
    }
    
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException | NullPointerException e) {
            return "!" + key + "!"; 
        }
    }
    
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    // --- MANAJEMEN LISTENER (OBSERVER) ---
    
    public static synchronized void registerListener(I18nChangeListener listener) {
        listeners.add(listener);
    }
    
    public static synchronized void unregisterListener(I18nChangeListener listener) {
        listeners.remove(listener);
    }
    
    private static void notifyListeners() {
        List<I18nChangeListener> activeListeners;
        synchronized (I18nService.class) {
            activeListeners = new ArrayList<>(listeners);
        }

        for (I18nChangeListener listener : activeListeners) {
            if (listener != null) {
                listener.onLanguageChanged();
            }
        }
    }
}
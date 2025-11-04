package attestation.attestation03.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    /**
     * Проверка валидности телефона
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Проверка валидности имени
     */
    public static boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.length() >= 2;
    }

    /**
     * Форматирование телефона
     */
    public static String formatPhone(String phone) {
        if (phone == null) {
            return null;
        }
        return phone.replaceAll("[^0-9+]", "");
    }
}
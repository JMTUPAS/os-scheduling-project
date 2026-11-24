// InputCheck.java
// Reads a number typed by the user and shows a clear message if it is wrong.
// Catches: empty, not a number (letters, decimals), negative and out of range.
// Written by: John Michael Tupas (Testing Lead)

import java.awt.Component;
import javax.swing.JOptionPane;

public class InputCheck {

    // returns the number, or -1 if the input is not valid (an error message is shown)
    public static int readNumber(Component parent, Object value, String name, int min, int max) {
        String text = "";
        if (value != null) {
            text = value.toString().trim();
        }

        if (text.equals("")) {
            showError(parent, name + " is empty.");
            return -1;
        }

        int number;
        try {
            number = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showError(parent, name + " must be a whole number. You typed: " + text);
            return -1;
        }

        if (number < 0) {
            showError(parent, name + " cannot be negative.");
            return -1;
        }
        if (number < min || number > max) {
            showError(parent, name + " must be from " + min + " to " + max + ".");
            return -1;
        }
        return number;
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Invalid input", JOptionPane.ERROR_MESSAGE);
    }
}

import javax.swing.*;
import java.awt.*;

public class TimePicker extends JPanel {

    private JComboBox<Integer> hourComboBox;
    private JComboBox<Integer> minuteComboBox;

    public TimePicker() {
        setLayout(new FlowLayout());

        // Crée un modèle pour les heures (0-23)
        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = i;
        }
        hourComboBox = new JComboBox<>(hours);

        // Crée un modèle pour les minutes (0-59)
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 60; i++) {
            minutes[i] = i;
        }
        minuteComboBox = new JComboBox<>(minutes);

        add(new JLabel("Heure:"));
        add(hourComboBox);
        add(new JLabel("Minute:"));
        add(minuteComboBox);
    }

    public int getSelectedHour() {
        return (Integer) hourComboBox.getSelectedItem();
    }

    public int getSelectedMinute() {
        return (Integer) minuteComboBox.getSelectedItem();
    }
}
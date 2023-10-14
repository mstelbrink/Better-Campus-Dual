import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    public StatsPanel(double averageGrade) {
        setSize(800, 600);
        add(new Label("Average Grade: " + averageGrade));
        setVisible(true);
    }
}

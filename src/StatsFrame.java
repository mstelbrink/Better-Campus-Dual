import javax.swing.*;

public class StatsFrame extends JFrame {

    public StatsFrame(StatsPanel statsPanel) {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(statsPanel);
        //pack();
        setVisible(true);
    }
}

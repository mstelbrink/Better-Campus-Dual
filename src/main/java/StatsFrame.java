import javax.swing.*;
import java.awt.*;

public class StatsFrame extends JFrame {

    public StatsFrame(AverageGradePanel averageGradePanel, GradeDistributionPanel gradeDistributionPanel) {
        setSize(800, 600);
        setLayout(new GridLayout(0, 2));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(averageGradePanel);
        add(gradeDistributionPanel.getPanel());
        //pack();
        setVisible(true);
    }
}

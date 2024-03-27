import javax.swing.*;
import java.awt.*;

public class AverageGradePanel extends JPanel {

    public AverageGradePanel(double averageGrade, double firstSemesterAverage, double secondSemesterAverage,
                             double thirdSemesterAverage, double fourthSemesterAverage, double fifthSemesterAverage,
                             double sixthSemesterAverage) {
        setLayout(new GridLayout(7, 0));
        add(new Label("Average grade: " + averageGrade));
        add(new Label("1st semester: " + firstSemesterAverage));
        add(new Label("2nd semester: " + secondSemesterAverage));
        add(new Label("3rd semester: " + thirdSemesterAverage));
        add(new Label("4th semester: " + fourthSemesterAverage));
        add(new Label("5th semester: " + fifthSemesterAverage));
        add(new Label("6th semester: " + sixthSemesterAverage));
        setVisible(true);
    }
}

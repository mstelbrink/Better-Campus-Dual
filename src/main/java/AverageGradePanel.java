import javax.swing.*;
import java.awt.*;

public class AverageGradePanel extends JPanel {

    public AverageGradePanel(double averageGrade, double firstSemesterAverage, double secondSemesterAverage,
                             double thirdSemesterAverage, double fourthSemesterAverage, double fifthSemesterAverage,
                             double sixthSemesterAverage) {
        setLayout(new GridLayout(7, 0));
        add(new Label("Average Grade: " + averageGrade));
        add(new Label("First Semester: " + firstSemesterAverage));
        add(new Label("Second Semester: " + secondSemesterAverage));
        add(new Label("Third Semester: " + thirdSemesterAverage));
        add(new Label("Fourth Semester: " + fourthSemesterAverage));
        add(new Label("Fifth Semester: " + fifthSemesterAverage));
        add(new Label("Sixth Semester: " + sixthSemesterAverage));
        setVisible(true);
    }
}

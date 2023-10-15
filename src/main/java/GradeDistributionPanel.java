import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GradeDistributionPanel extends JPanel {

    private ChartPanel panel;

    public GradeDistributionPanel(HashMap<Integer, Integer> distributionMap) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(distributionMap.get(1), "Amount", "1");
        dataset.setValue(distributionMap.get(2), "Amount", "2");
        dataset.setValue(distributionMap.get(3), "Amount", "3");
        dataset.setValue(distributionMap.get(4), "Amount", "4");
        dataset.setValue(distributionMap.get(5), "Amount", "5");

        JFreeChart chart = ChartFactory.createBarChart("Grade distribution", "Grade",
                "Amount", dataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);
        panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(400, 500));
    }

    public ChartPanel getPanel() {
        return panel;
    }
}

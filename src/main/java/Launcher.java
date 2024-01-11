import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Launcher implements ActionListener {

    private JFrame frame;
    private JTextField tf;
    private JPasswordField pf;
    private String username;
    private char[] password;
    private double averageGrade;
    private double firstSemesterAverage;
    private double secondSemesterAverage;
    private double thirdSemesterAverage;
    private double fourthSemesterAverage;
    private double fifthSemesterAverage;
    private double sixthSemesterAverage;
    private HashMap<Integer, Integer> distributionMap;



    public static void main( String[] args ) {

        Launcher l = new Launcher();
        l.createWindow();
    }

    public void createWindow() {
        frame = new JFrame("Campus-Dual");
        frame.setLayout(new GridLayout(3, 0));
        frame.setSize(250, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tf = new JTextField();
        pf = new JPasswordField();
        JButton btn = new JButton("Login");
        btn.addActionListener(this);

        frame.add(tf);
        frame.add(pf);
        frame.add(btn);

        frame.setVisible(true);
    }

    public void login() throws IOException {

        String loginURL = "https://erp.campus-dual.de/sap/bc/webdynpro/sap/zba_initss?sap-client=100&sap-language=" +
                "de&uri=https://selfservice.campus-dual.de/index/login";
        Connection.Response loginForm = Jsoup.connect(loginURL)
                .method(Connection.Method.GET)
                .execute();

        Document loginDoc = loginForm.parse();

        String xsrf_token = loginDoc.select("#SL__FORM > input:nth-child(9)").first().attr("value");

        Connection.Response loginResponse = Jsoup.connect(loginURL)
                .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:102.0) Gecko/20100101 Firefox/102.0")
                .cookies(loginForm.cookies())
                .data("sap-login-XSRF", xsrf_token)
                .data("sap-user", username)
                .data("sap-password", new String(password))
                .method(Method.POST)
                .execute();

        String gradesURL = "https://selfservice.campus-dual.de/acwork/index";
        Document doc = Jsoup.connect(gradesURL)
                    .cookies(loginResponse.cookies())
                    .get();

        Arrays.fill(password, (char) 0);

        HashMap<String, Double> grades = new HashMap<>();
        HashMap<String, Integer> credits = new HashMap<>();
        HashMap<String, String> periods = new HashMap<>();
        ArrayList<String> periodNames = new ArrayList<>();
        distributionMap = new HashMap<>();
        distributionMap.put(1, 0);
        distributionMap.put(2, 0);
        distributionMap.put(3, 0);
        distributionMap.put(4, 0);
        distributionMap.put(5, 0);

        for (int i = 1; i < 100; i++) {
            String module = doc.select("#node-" + i + " > td:nth-of-type(1)").text();
            String grade = doc.select("#node-" + i + " > td:nth-of-type(2)").text();
            if (!grade.matches("[0-9],[0-9]")) {
                break;
            }
            String credit = doc.select("#node-" + i + " > td:nth-of-type(4)").text();
            String period = doc.select("#node-" + i + " > td:nth-of-type(8)").text();
            if (module.isEmpty() || grade.isEmpty()) {
                break;
            }
            grade = grade.replace(',','.');
            grades.put(module, Double.valueOf(grade));
            credits.put(module, Integer.parseInt(credit));
            periods.put(module, period);
            if (!(periodNames.contains(period))) {
                periodNames.add(period);
            }

            double gradeValue = Double.parseDouble(grade);
            if (gradeValue >= 1 && gradeValue <= 1.5) {
                distributionMap.put(1, distributionMap.get(1) + 1);
            } else if (gradeValue > 1.5 && gradeValue <= 2.5) {
                distributionMap.put(2, distributionMap.get(2) + 1);
            } else if (gradeValue > 2.5 && gradeValue <= 3.5) {
                distributionMap.put(3, distributionMap.get(3) + 1);
            } else if (gradeValue > 3.5 && gradeValue <= 4) {
                distributionMap.put(4, distributionMap.get(4) + 1);
            } else if (gradeValue > 4) {
                distributionMap.put(5, distributionMap.get(5) + 1);
            }
        }

        double weightedGrade;
        double weightedGradeSum = 0;
        int cpSum = 0;

        double firstSemesterWeighted = 0;
        double secondSemesterWeighted = 0;
        double thirdSemesterWeighted = 0;
        double fourthSemesterWeighted = 0;
        double fifthSemesterWeighted = 0;
        double sixthSemesterWeighted = 0;

        int firstSemesterCPSum = 0;
        int secondSemesterCPSum = 0;
        int thirdSemesterCPSum = 0;
        int fourthSemesterCPSum = 0;
        int fifthSemesterCPSum = 0;
        int sixthSemesterCPSum = 0;

        for (String key : grades.keySet()) {
            weightedGrade = grades.get(key) * credits.get(key);
            weightedGradeSum += weightedGrade;
            cpSum += credits.get(key);

            if (periods.get(key).equals(periodNames.get(0))) {
                firstSemesterWeighted += weightedGrade;
                firstSemesterCPSum += credits.get(key);
            } else if (periods.get(key).equals(periodNames.get(1))) {
                secondSemesterWeighted += weightedGrade;
                secondSemesterCPSum += credits.get(key);
            } else if (periods.get(key).equals(periodNames.get(2))) {
                thirdSemesterWeighted += weightedGrade;
                thirdSemesterCPSum += credits.get(key);
            } else if (periods.get(key).equals(periodNames.get(3))) {
                fourthSemesterWeighted += weightedGrade;
                fourthSemesterCPSum += credits.get(key);
            } else if (periods.get(key).equals(periodNames.get(4))) {
                fifthSemesterWeighted += weightedGrade;
                fifthSemesterCPSum += credits.get(key);
            } else if (periods.get(key).equals(periodNames.get(5))) {
                sixthSemesterWeighted += weightedGrade;
                sixthSemesterCPSum += credits.get(key);
            } else {
                System.out.println("Could not add semester-specific grade.");
            }
        }

        averageGrade = Math.round((weightedGradeSum / cpSum) * 100.0) / 100.0;
        firstSemesterAverage = Math.round((firstSemesterWeighted / firstSemesterCPSum) * 100.0) / 100.0;
        secondSemesterAverage = Math.round((secondSemesterWeighted / secondSemesterCPSum) * 100.0) / 100.0;
        thirdSemesterAverage = Math.round((thirdSemesterWeighted / thirdSemesterCPSum) * 100.0) / 100.0;
        fourthSemesterAverage = Math.round((fourthSemesterWeighted / fourthSemesterCPSum) * 100.0) / 100.0;
        fifthSemesterAverage = Math.round((fifthSemesterWeighted / fifthSemesterCPSum) * 100.0) / 100.0;
        sixthSemesterAverage = Math.round((sixthSemesterWeighted / sixthSemesterCPSum) * 100.0) / 100.0;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        username = tf.getText();
        if ( e.getActionCommand().equals("Login")) {
            password = pf.getPassword();
        }
        try {
            login();
            frame.setVisible(false);
            new StatsFrame(new AverageGradePanel(averageGrade, firstSemesterAverage, secondSemesterAverage,
                    thirdSemesterAverage, fourthSemesterAverage, fifthSemesterAverage, sixthSemesterAverage),
                    new GradeDistributionPanel(distributionMap));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
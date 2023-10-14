import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;


public class Launcher implements ActionListener {

    private JFrame frame;
    private JTextField tf;
    private JPasswordField pf;
    private String username;
    private char[] password;
    private String loginURL = "https://erp.campus-dual.de/sap/bc/webdynpro/sap/zba_initss?sap-client=100&sap-language=" +
            "de&uri=https://selfservice.campus-dual.de/index/login";
    private String gradesURL = "https://selfservice.campus-dual.de/acwork/index";

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

        Document doc = Jsoup.connect(gradesURL)
                    .cookies(loginResponse.cookies())
                    .get();

        Arrays.fill(password, (char) 0);

        for (int i = 1; i < 100; i++) {
            if (doc.select("tr#node-" + i).text().isEmpty()) break;
            String tr = doc.select("tr#node-" + i).text();
            System.out.println(tr);
        }
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
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
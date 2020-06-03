import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginForm extends JDialog {
    private Window frame;
    private String userName;

    public LoginForm(Window frame) {
        super(frame, "Login");
        this.frame = frame;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(400, 200));
        getContentPane().add(createUI());
        setResizable(false);
        setVisible(true);
    }

    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel name = new JPanel();
        name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Login:         ");
        name.add(nameLabel);
        name.add(Box.createHorizontalStrut(12));
        JTextField login = new JTextField(15);
        name.add(login);
        JPanel password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
        JLabel passwrdLabel = new JLabel("Password:");
        password.add(passwrdLabel);
        password.add(Box.createHorizontalStrut(12));
        JTextField passwd = new JTextField(15);
        password.add(passwd);
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0));
        JButton loginButton = new JButton("Log in");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.sendForm("login::" + login.getText() + "::" + passwd.getText());
                userName = login.getText();
            }
        });
        JButton signupButton = new JButton("Sign up");
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.sendForm("signup::" + login.getText() + "::" + passwd.getText());
                userName = login.getText();
            }
        });
        grid.add(loginButton);
        grid.add(signupButton);
        flow.add(grid);
        panel.add(name);
        panel.add(Box.createVerticalStrut(12));
        panel.add(password);
        panel.add(Box.createVerticalStrut(17));
        panel.add(flow);
        return panel;
    }

    public void successfullyEntered() {
        setVisible(false);
        frame.getMessages(userName);
        frame.setVisible(true);
    }
}

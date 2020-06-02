import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class UserButton extends JButton {
    public UserButton(String userName, MyFrame frame) {
        super(userName);
        setPreferredSize(new Dimension(181, 100));
        setMaximumSize(new Dimension(181, 100));
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setUser(userName);
            }
        });
    }
}

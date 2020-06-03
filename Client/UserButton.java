import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class UserButton extends JButton {
    /**
     *
     */
    private static final long serialVersionUID = 6707072803214983264L;

    public UserButton(String userName, Window frame) {
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

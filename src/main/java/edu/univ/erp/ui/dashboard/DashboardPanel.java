import edu.univ.erp.ui.util.Theme;
import edu.univ.erp.ui.util.RoundedButton;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY_WHITE);

        JLabel title = new JLabel("Welcome to ERP Dashboard", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.DEEP_SEA);

        RoundedButton logout = new RoundedButton("Logout");
        logout.addActionListener(e -> firePropertyChange("logout", false, true));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.PRIMARY_WHITE);
        top.add(logout, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(title, BorderLayout.CENTER);
    }
}

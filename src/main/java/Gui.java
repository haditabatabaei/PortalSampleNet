import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Gui extends JFrame {
    private JPanel topPanel;
    private JPanel passPanel;
    private JPanel captchaPanel;
    private JPanel usernamePanel;
    private JLabel usernameLabel;
    private JPanel rightPanel;
    private JLabel passLabel;
    private JLabel captchaLabel;
    private JLabel captchaImageAsLabel;
    private JTextField usernameField;
    private JTextField passField;
    private JTextField captchaField;
    private JButton loginButton;
    private BufferedImage captchaImage;
    private GuiHandler handler;

    public Gui() {
        initFrame();
        initEntities();
        addEntities();
        configHandler();
    }

    private void initEntities() {
        topPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        usernamePanel = new JPanel(new BorderLayout());
        usernameLabel = new JLabel("Username: ");
        passLabel = new JLabel("Password: ");
        captchaLabel = new JLabel("Captcha :");
        usernameField = new JTextField();
        passField = new JTextField();
        captchaField = new JTextField();
        loginButton = new JButton("Login");
        passPanel = new JPanel(new BorderLayout());
        captchaPanel = new JPanel(new BorderLayout());
        captchaImageAsLabel = new JLabel();
        rightPanel = new JPanel(new BorderLayout());
    }

    private void addEntities() {
        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passField, BorderLayout.CENTER);

        captchaPanel.add(captchaLabel, BorderLayout.WEST);
        captchaPanel.add(captchaField, BorderLayout.CENTER);
        captchaPanel.add(captchaImageAsLabel, BorderLayout.EAST);

        topPanel.add(usernamePanel);
        topPanel.add(passPanel);
        topPanel.add(captchaPanel);

        rightPanel.add(loginButton, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);
    }

    private void configHandler() {
        handler = new GuiHandler();

        loginButton.setActionCommand("login");
        loginButton.addActionListener(handler);
    }

    private void initFrame() {
        setTitle("Samad Login");
        setLocationRelativeTo(null);
        setSize(500, 170);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public void makeVisible() {
        setVisible(true);
    }

    public void makeHidden() {
        setVisible(false);
    }

    public JLabel getCaptchaImageAsLabel() {
        return captchaImageAsLabel;
    }

    public GuiHandler getHandler() {
        return handler;
    }
}

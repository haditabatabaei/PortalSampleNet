import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/*
 * Created by JFormDesigner on Mon Aug 27 21:12:05 IRDT 2018
 */


/**
 * @author Hadi
 */
public class PortalGui extends JFrame {
    public PortalGui() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        customAccount = new JCheckBox();
        userLabel = new JLabel();
        passLabel = new JLabel();
        captchaLabel = new JLabel();
        userField = new JTextField();
        passField = new JTextField();
        captchaField = new JTextField();
        captchaImageField = new JLabel();
        loginButton = new JButton();
        nameLabel = new JLabel();
        emailLabel = new JLabel();
        idNumLabel = new JLabel();
        phoneLabel = new JLabel();
        nameLabelInfo = new JLabel();
        emailLabelInfo = new JLabel();
        idNumLabelInfo = new JLabel();
        phoneLabelInfo = new JLabel();
        scrollPane1 = new JScrollPane();
        panel1 = new JPanel();
        studentNumToName = new JLabel();
        studentNumToName2 = new JLabel();
        studentIdField = new JTextField();
        separator1 = new JSeparator();
        findResultLabel = new JLabel();
        findResultStudent = new JLabel();
        findButton = new JButton();
        samadCaptchaLabel = new JLabel();
        samadCaptchaLabelImage = new JLabel();
        samadCaptchaField = new JTextField();

        //======== this ========
        setResizable(false);
        setTitle("Portal Simpler-By Hadi");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- customAccount ----
        customAccount.setText("Custom Account");
        contentPane.add(customAccount);
        customAccount.setBounds(new Rectangle(new Point(15, 10), customAccount.getPreferredSize()));

        //---- userLabel ----
        userLabel.setText("Username :");
        contentPane.add(userLabel);
        userLabel.setBounds(new Rectangle(new Point(20, 50), userLabel.getPreferredSize()));

        //---- passLabel ----
        passLabel.setText("Password :");
        contentPane.add(passLabel);
        passLabel.setBounds(20, 75, 65, 16);

        //---- captchaLabel ----
        captchaLabel.setText("Captcha :");
        contentPane.add(captchaLabel);
        captchaLabel.setBounds(20, 100, 65, 16);
        contentPane.add(userField);
        userField.setBounds(100, 50, 220, userField.getPreferredSize().height);
        contentPane.add(passField);
        passField.setBounds(100, 75, 220, 20);
        contentPane.add(captchaField);
        captchaField.setBounds(100, 100, 220, 20);
        contentPane.add(captchaImageField);
        captchaImageField.setBounds(75, 125, 160, 45);

        //---- loginButton ----
        loginButton.setText("Login");
        contentPane.add(loginButton);
        loginButton.setBounds(240, 130, 81, loginButton.getPreferredSize().height);

        //---- nameLabel ----
        nameLabel.setText("\u0646\u0627\u0645 \u0648 \u0646\u0627\u0645 \u062e\u0627\u0646\u0648\u0627\u062f\u06af\u06cc");
        contentPane.add(nameLabel);
        nameLabel.setBounds(new Rectangle(new Point(30, 200), nameLabel.getPreferredSize()));

        //---- emailLabel ----
        emailLabel.setText("\u0627\u06cc\u0645\u06cc\u0644");
        contentPane.add(emailLabel);
        emailLabel.setBounds(30, 225, 82, 14);

        //---- idNumLabel ----
        idNumLabel.setText("\u06a9\u062f \u0645\u0644\u06cc");
        contentPane.add(idNumLabel);
        idNumLabel.setBounds(30, 255, 82, 14);

        //---- phoneLabel ----
        phoneLabel.setText("\u0645\u0648\u0628\u0627\u06cc\u0644");
        contentPane.add(phoneLabel);
        phoneLabel.setBounds(30, 285, 82, 14);
        contentPane.add(nameLabelInfo);
        nameLabelInfo.setBounds(125, 200, 215, 20);
        contentPane.add(emailLabelInfo);
        emailLabelInfo.setBounds(120, 225, 215, 25);
        contentPane.add(idNumLabelInfo);
        idNumLabelInfo.setBounds(120, 255, 215, 20);
        contentPane.add(phoneLabelInfo);
        phoneLabelInfo.setBounds(120, 285, 215, 20);

        //======== scrollPane1 ========
        {
            scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            //======== panel1 ========
            {
                panel1.setLayout(new GridLayout());
            }
            scrollPane1.setViewportView(panel1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(355, 35, 400, 495);

        //---- studentNumToName ----
        studentNumToName.setText("\u0634\u0645\u0627\u0631\u0647 \u062f\u0627\u0646\u0634\u062c\u0648\u06cc\u06cc \u0628\u0632\u0646 \u0627\u0633\u0645 \u0628\u06af\u06cc\u0631");
        contentPane.add(studentNumToName);
        studentNumToName.setBounds(145, 440, 195, 14);

        //---- studentNumToName2 ----
        studentNumToName2.setText("\u0634\u0645\u0627\u0631\u0647 \u062f\u0627\u0646\u0634\u062c\u0648\u06cc\u06cc");
        contentPane.add(studentNumToName2);
        studentNumToName2.setBounds(15, 470, 95, 20);
        contentPane.add(studentIdField);
        studentIdField.setBounds(110, 470, 230, 20);
        contentPane.add(separator1);
        separator1.setBounds(30, 360, 310, 10);

        //---- findResultLabel ----
        findResultLabel.setText(": \u0646\u062a\u06cc\u062c\u0647");
        contentPane.add(findResultLabel);
        findResultLabel.setBounds(15, 530, 45, 20);
        contentPane.add(findResultStudent);
        findResultStudent.setBounds(65, 530, 185, 30);

        //---- findButton ----
        findButton.setText("Find");
        contentPane.add(findButton);
        findButton.setBounds(260, 500, 81, 23);

        //---- samadCaptchaLabel ----
        samadCaptchaLabel.setText("captcha :");
        contentPane.add(samadCaptchaLabel);
        samadCaptchaLabel.setBounds(15, 500, 60, 20);
        contentPane.add(samadCaptchaLabelImage);
        samadCaptchaLabelImage.setBounds(195, 495, 55, 30);
        contentPane.add(samadCaptchaField);
        samadCaptchaField.setBounds(75, 500, 115, 20);

        contentPane.setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        NetHandler handler = new NetHandler(this);
        userField.setText("Predefined Account");
        userField.setEnabled(false);
        passField.setEnabled(false);
        //samadCaptchaField.setEnabled(false);
        loginButton.setActionCommand(NetHandler.COMMAND_LOGIN);
        customAccount.setActionCommand(NetHandler.COMMAND_CUSTOM_ACC);
        findButton.setActionCommand(NetHandler.COMMAND_FIND);
        customAccount.addActionListener(handler);
        loginButton.addActionListener(handler);
        findButton.addActionListener(handler);
        panelGridLayout = (GridLayout) panel1.getLayout();

        setVisible(true);
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JCheckBox customAccount;
    private JLabel userLabel;
    private JLabel passLabel;
    private JLabel captchaLabel;
    private JTextField userField;
    private JTextField passField;
    private JTextField captchaField;
    private JLabel captchaImageField;
    private JButton loginButton;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel idNumLabel;
    private JLabel phoneLabel;
    private JLabel nameLabelInfo;
    private JLabel emailLabelInfo;
    private JLabel idNumLabelInfo;
    private JLabel phoneLabelInfo;
    private JScrollPane scrollPane1;
    private JPanel panel1;
    private JLabel studentNumToName;
    private JLabel studentNumToName2;
    private JTextField studentIdField;
    private JSeparator separator1;
    private JLabel findResultLabel;
    private JLabel findResultStudent;
    private JButton findButton;
    private JLabel samadCaptchaLabel;
    private JLabel samadCaptchaLabelImage;
    private JTextField samadCaptchaField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private GridLayout panelGridLayout;

    public void insertScores(ArrayList<String> coursesNames, ArrayList<Float> scores) {
        for (int i = 0; i < coursesNames.size(); i++) {
            JPanel tempPanel = new JPanel(new BorderLayout());
            JLabel courseName = new JLabel(coursesNames.get(i));
            JLabel courseScore = new JLabel(scores.get(i) + "");
            tempPanel.add(courseName, BorderLayout.EAST);
            tempPanel.add(courseScore, BorderLayout.WEST);
            tempPanel.add(new JLabel("---------------------------------------------"), BorderLayout.SOUTH);
            panelGridLayout.setRows(panelGridLayout.getRows() + 1);
            panel1.add(tempPanel);
            revalidate();
        }
    }



    public void clearScoreBoard() {
        panel1.removeAll();
        panelGridLayout.setRows(1);
        revalidate();
    }

    public JCheckBox getCustomAccount() {
        return customAccount;
    }

    public void setCustomAccount(JCheckBox customAccount) {
        this.customAccount = customAccount;
    }

    public JLabel getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(JLabel userLabel) {
        this.userLabel = userLabel;
    }

    public JTextField getStudentIdField() {
        return studentIdField;
    }

    public JLabel getFindResultStudent() {
        return findResultStudent;
    }

    public JLabel getSamadCaptchaLabelImage() {
        return samadCaptchaLabelImage;
    }

    public JTextField getSamadCaptchaField() {
        return samadCaptchaField;
    }

    public JLabel getPassLabel() {
        return passLabel;
    }

    public void setPassLabel(JLabel passLabel) {
        this.passLabel = passLabel;
    }

    public JLabel getCaptchaLabel() {
        return captchaLabel;
    }

    public void setCaptchaLabel(JLabel captchaLabel) {
        this.captchaLabel = captchaLabel;
    }

    public JTextField getUserField() {
        return userField;
    }

    public void setUserField(JTextField userField) {
        this.userField = userField;
    }

    public JTextField getPassField() {
        return passField;
    }

    public void setPassField(JTextField passField) {
        this.passField = passField;
    }

    public JTextField getCaptchaField() {
        return captchaField;
    }

    public void setCaptchaField(JTextField captchaField) {
        this.captchaField = captchaField;
    }

    public JLabel getCaptchaImageField() {
        return captchaImageField;
    }

    public void setCaptchaImageField(JLabel captchaImageField) {
        this.captchaImageField = captchaImageField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(JLabel nameLabel) {
        this.nameLabel = nameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

    public void setEmailLabel(JLabel emailLabel) {
        this.emailLabel = emailLabel;
    }

    public JLabel getIdNumLabel() {
        return idNumLabel;
    }

    public void setIdNumLabel(JLabel idNumLabel) {
        this.idNumLabel = idNumLabel;
    }

    public JLabel getPhoneLabel() {
        return phoneLabel;
    }

    public void setPhoneLabel(JLabel phoneLabel) {
        this.phoneLabel = phoneLabel;
    }

    public JLabel getNameLabelInfo() {
        return nameLabelInfo;
    }

    public void setNameLabelInfo(JLabel nameLabelInfo) {
        this.nameLabelInfo = nameLabelInfo;
    }

    public JLabel getEmailLabelInfo() {
        return emailLabelInfo;
    }

    public void setEmailLabelInfo(JLabel emailLabelInfo) {
        this.emailLabelInfo = emailLabelInfo;
    }

    public JLabel getIdNumLabelInfo() {
        return idNumLabelInfo;
    }

    public void setIdNumLabelInfo(JLabel idNumLabelInfo) {
        this.idNumLabelInfo = idNumLabelInfo;
    }

    public JLabel getPhoneLabelInfo() {
        return phoneLabelInfo;
    }

    public void setPhoneLabelInfo(JLabel phoneLabelInfo) {
        this.phoneLabelInfo = phoneLabelInfo;
    }
}

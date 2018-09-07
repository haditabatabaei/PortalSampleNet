import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetHandler implements ActionListener {
    public static final String COMMAND_LOGIN = "login";
    public static final String COMMAND_CUSTOM_ACC = "customAcc";
    public static final String COMMAND_FIND = "find";
    private PortalGui gui;
    private ExecutorService executorService;
    private PortalNetHunter portalNetHunter;

    public PortalNetHunter getPortalNetHunter() {
        return portalNetHunter;
    }

    public NetHandler(PortalGui gui) {
        this.gui = gui;
        executorService = Executors.newCachedThreadPool();
        portalNetHunter = new PortalNetHunter("9631049", "0371964660");
//        try {
//            portalNetHunter.startSamad();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            portalNetHunter.init();
      //      System.exit(0);
            gui.getCaptchaImageField().setIcon(new ImageIcon("captcha.jpg"));
            gui.getSamadCaptchaLabelImage().setIcon(new ImageIcon("sCaptcha.jpg"));
            gui.revalidate();
            gui.getNameLabelInfo().setText(portalNetHunter.getPortalFulLName());
            gui.getEmailLabelInfo().setText(portalNetHunter.getEmail());
            gui.getIdNumLabelInfo().setText(portalNetHunter.getIdNum());
            gui.getPhoneLabelInfo().setText(portalNetHunter.getPhone());
            gui.revalidate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case COMMAND_LOGIN:
                portalNetHunter.getScores().clear();
                portalNetHunter.getCoursesName().clear();
                gui.clearScoreBoard();
                if (gui.getCustomAccount().isSelected()) {
                    portalNetHunter.setUsername(gui.getUserField().getText());
                    portalNetHunter.setPassword(gui.getPassField().getText());
                }
                portalNetHunter.setCaptcha(gui.getCaptchaField().getText());
                try {
                    portalNetHunter.start();
                    gui.getNameLabelInfo().setText(portalNetHunter.getPortalFulLName());
                    gui.getEmailLabelInfo().setText(portalNetHunter.getEmail());
                    gui.getIdNumLabelInfo().setText(portalNetHunter.getIdNum());
                    gui.getPhoneLabelInfo().setText(portalNetHunter.getPhone());
                    gui.revalidate();
                    portalNetHunter.getChooseCourses();
                    StringBuilder message = new StringBuilder();
                    for (int i = 0; i < portalNetHunter.getCoursesName().size(); i++) {
                        //     message.append(portalNetHunter.getCoursesName().get(i) + " | Score : " + portalNetHunter.getScores().get(i) + "\n");
                    }
                    //      gui.insertScores(portalNetHunter.getCoursesName(), portalNetHunter.getScores());
                    gui.insertInfo(portalNetHunter.getCoursesName(), portalNetHunter.getProfessors(), portalNetHunter.getFirstTimes(), portalNetHunter.getSecondTimes(), portalNetHunter.getExamTimes());
                    //  JOptionPane.showMessageDialog(null, message.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //executorService.execute(portalNetHunter);
                System.out.println("Login Command");
                break;
            case COMMAND_CUSTOM_ACC:
                JCheckBox clickedCheckBox = (JCheckBox) e.getSource();
                if (clickedCheckBox.isSelected()) {
                    gui.getUserField().setEnabled(true);
                    gui.getPassField().setEnabled(true);
                    gui.getUserField().setText("");
                    portalNetHunter.setUsername("");
                    portalNetHunter.setPassword("");
                    System.out.println("This is for custom");
                } else {
                    portalNetHunter.setUsername("9631049");
                    portalNetHunter.setPassword("0371964660");
                    gui.getUserField().setEnabled(false);
                    gui.getPassField().setEnabled(false);
                    gui.getUserField().setText("Predefined Account");
                    System.out.println("This is for predefined.");
                }
                break;
            case COMMAND_FIND:
                portalNetHunter.setSamadCaptcha(gui.getSamadCaptchaField().getText());
                try {
                    portalNetHunter.findStudent(gui.getStudentIdField().getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }
}

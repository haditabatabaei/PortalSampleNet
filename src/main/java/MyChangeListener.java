import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

public class MyChangeListener implements ItemListener {
    private PortalGui gui;
    private PortalNetHunter portalNetHunter;

    public MyChangeListener(PortalGui gui, PortalNetHunter portalNetHunter) {
        this.gui = gui;
        this.portalNetHunter = portalNetHunter;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object Item = e.getItem();
            gui.clearScoreBoard();
            try {
                portalNetHunter.handleComboSelection(gui.getComboBox1().getSelectedIndex());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            gui.insertInfo(portalNetHunter.getCoursesName(), portalNetHunter.getProfessors(), portalNetHunter.getFirstTimes(), portalNetHunter.getSecondTimes(), portalNetHunter.getExamTimes());

        }
    }
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GuiHandler implements ActionListener {
    private NetHandler netHandler;

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().endsWith("login")) {
            try {
                netHandler.login();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}

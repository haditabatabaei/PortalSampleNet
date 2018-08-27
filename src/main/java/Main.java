import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /*NetHandler handler = new NetHandler();
        Gui gui = new Gui();
        handler.setGui(gui);*/
        Gui gui = new Gui();
        gui.makeVisible();
        PortalNetHunter portalNetHunter = new PortalNetHunter("9631049", "0371964660");

        portalNetHunter.start();

       /* try {
            handler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

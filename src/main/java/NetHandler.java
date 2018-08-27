import sun.net.www.protocol.http.HttpURLConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class NetHandler {
    private final String samadHost = "http://samad.aut.ac.ir";
    private final String portalHost = "http//portal.aut.ac.ir";
    private final String USER_AGENT = "Mozilla/5.0";
    private String sessionId;
    private String username = "9631049";
    private String password = "0371964660";
    private String captcha;
    private Gui gui;


    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void start() throws IOException {
        URL samadUrl = new URL(samadHost + "/index.rose");
        HttpURLConnection samadConnection = (HttpURLConnection) samadUrl.openConnection();
        samadConnection.setRequestProperty("User-Agent", USER_AGENT);
        int samadConnectionResponseCode = samadConnection.getResponseCode();
        if (samadConnectionResponseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Index rose connection ok.");
            System.out.println(samadConnection.getHeaderFields());
            sessionId = samadConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
            System.out.println("Session id : " + sessionId);
            URL samadCaptcha = new URL(samadHost + "/captcha.jpg");
            HttpURLConnection samadCaptchaConnection = (HttpURLConnection) samadCaptcha.openConnection();

            samadCaptchaConnection.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            samadCaptchaConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            samadCaptchaConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            samadCaptchaConnection.setRequestProperty("Connection", "keep-alive");
            samadCaptchaConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798869775.1529914308; JSESSIONID=" + sessionId);
            samadCaptchaConnection.setRequestProperty("Host", "samad.aut.ac.ir");
            samadCaptchaConnection.setRequestProperty("Referer", samadHost + "/index.rose");
            samadCaptchaConnection.setRequestProperty("User-Agent", USER_AGENT);

            int samadCaptchaResponseCode = samadCaptchaConnection.getResponseCode();
            if (samadCaptchaResponseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Captcha response code : ok");
                System.out.println(samadCaptchaConnection.getHeaderFields());
                BufferedImage image;
                image = ImageIO.read(samadCaptchaConnection.getInputStream());
                System.out.println("Image received : height : " + image.getHeight() + " width : " + image.getWidth());
                File resultImage = new File("result.jpg");
                ImageIO.write(image, "jpg", resultImage);
                Scanner scanner = new Scanner(System.in);
                gui.getCaptchaImageAsLabel().setIcon(new ImageIcon("result.jpg"));
                gui.getCaptchaImageAsLabel().revalidate();
                gui.makeVisible();
                gui.getHandler().setNetHandler(this);
            }

        } else {
            System.out.println("Error with connection. response code : " + samadConnectionResponseCode);
        }
    }

    public void login() throws IOException {
        URL samadLogin = new URL(samadHost + "/j_security_check");
        HttpURLConnection samadLoginConnection = (HttpURLConnection) samadLogin.openConnection();
        samadLoginConnection.setRequestMethod("POST");
        samadLoginConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        samadLoginConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        samadLoginConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        samadLoginConnection.setRequestProperty("Cache-Control", "max-age=0");
        samadLoginConnection.setRequestProperty("Connection", "keep-alive");
        samadLoginConnection.setRequestProperty("Content-Length", "128");
        samadLoginConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        samadLoginConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798869775.1529914308; JSESSIONID=" + sessionId);
        samadLoginConnection.setRequestProperty("Host", "samad.aut.ac.ir");
        samadLoginConnection.setRequestProperty("Origin", "http://samad.aut.ac.ir");
        samadLoginConnection.setRequestProperty("Referer", "http://samad.aut.ac.ir/index.rose");
        samadLoginConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadLoginConnection.setRequestProperty("User-Agent", USER_AGENT);
        String params = "_csrf=2b700cd3-d823-4d60-ad31-5963efb62703&" + "username=" + username + "&password=" + password + "&captcha_input=" + captcha + "&login=%D9%88%D8%B1%D9%88%D8%AF";
        samadLoginConnection.setDoOutput(true);
        samadLoginConnection.getOutputStream().write(params.getBytes());
        samadLoginConnection.getOutputStream().flush();
        samadLoginConnection.getOutputStream().close();

        System.out.println(samadLoginConnection.getResponseCode());

        String line;
        StringBuilder response = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(samadLoginConnection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            response.append(line);


            HttpURLConnection samadLastConnection = (HttpURLConnection) (new URL(samadHost + "/home.rose")).openConnection();
            samadLastConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            samadLastConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            samadLastConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            samadLastConnection.setRequestProperty("Cache-Control", "max-age=0");
            samadLastConnection.setRequestProperty("Connection", "keep-alive");
            samadLastConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798869775.1529914308; JSESSIONID=" + sessionId);
            samadLastConnection.setRequestProperty("Host", "samad.aut.ac.ir");
            samadLastConnection.setRequestProperty("Referer", "http://samad.aut.ac.ir/index.rose");
            samadLastConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            samadLastConnection.setRequestProperty("User-Agent", USER_AGENT);
            int samadLastConnectionResponseCode = samadLastConnection.getResponseCode();
//        if (samadLastConnectionResponseCode == HttpURLConnection.HTTP_OK) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(samadLastConnection.getInputStream()));
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//        }
                /*int samadLoginConnectionResponseCode = samadLoginConnection.getResponseCode();
                if (samadLoginConnectionResponseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(samadLoginConnection.getInputStream()));
                    while ((line = reader.readLine()) != null)
                        response.append(line);
                }
*/
            File resultFile = new File("result.html");
            resultFile.createNewFile();
            FileWriter fileWriter = new FileWriter(resultFile);
            fileWriter.write(response.toString());
            fileWriter.close();
            System.out.println("DONE");

        }
    }
}

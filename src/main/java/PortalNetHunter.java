import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class PortalNetHunter {
    private final String PORTAL_HOST = "portal.aut.ac.ir";
    private final String PORTAL_HOST_HTTPS = "https://portal.aut.ac.ir";
    private String sessionId;
    private String username;
    private String password;
    private String captcha;
    private String portalFulLName;

    public PortalNetHunter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void start() throws IOException {
        getSessionId();
        getCaptcha();
        sendLogin();
        getTopStudent();
    }

    private void getTopStudent() throws IOException {
        URL portalLogin = new URL(PORTAL_HOST_HTTPS + "/aportal/regadm/style/top/top.student.jsp");
        HttpsURLConnection postConnection = (HttpsURLConnection) portalLogin.openConnection();
        postConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        File testTop = new File("testTop.jsp");
        File testTopHtml = new File("testTopHtml.html");
        testTopHtml.createNewFile();
        testTop.createNewFile();
        int response = postConnection.getResponseCode();
        System.out.println("Top Student response code : " + response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
        String line;
        StringBuilder responseT = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            responseT.append(line);
            if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }
        }


        System.out.println(responseT.toString());
        FileWriter writer = new FileWriter(testTop);
        FileWriter htmlWriter = new FileWriter(testTopHtml);
        writer.write(responseT.toString());
        htmlWriter.write(responseT.toString());
        writer.close();
        htmlWriter.close();
        Document nameDoc = Jsoup.parse(testTopHtml, "UTF-8");
        Element selectedElement = nameDoc.select("td").first();
        portalFulLName = selectedElement.text();
        System.out.println("Portal Full Name : " + portalFulLName);

    }

    private void sendLogin() throws IOException {
        URL portalLogin = new URL(PORTAL_HOST_HTTPS + "/aportal/login.jsp");
        HttpsURLConnection postConnection = (HttpsURLConnection) portalLogin.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        String params = "username=" + username + "&password=" + password + "&passline=" + captcha + "&login=%D9%88%D8%B1%D9%88%D8%AF+%D8%A8%D9%87+%D9%BE%D9%88%D8%B1%D8%AA%D8%A7%D9%84";
        postConnection.getOutputStream().write(params.getBytes());
        postConnection.getOutputStream().flush();
        postConnection.getOutputStream().close();
        int sendPostResponse = postConnection.getResponseCode();
        System.out.println("Send Login Response code : " + sendPostResponse);
    }

    private void getCaptcha() throws IOException {
        URL portalCaptcha = new URL(PORTAL_HOST_HTTPS + "/aportal/PassImageServlet");
        HttpsURLConnection portalCaptchaConnection = (HttpsURLConnection) portalCaptcha.openConnection();
        portalCaptchaConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        int captchaResponse = portalCaptchaConnection.getResponseCode();
        if (captchaResponse == HttpsURLConnection.HTTP_OK) {
            System.out.println("Captcha response ok");
            BufferedImage bufferedImage = ImageIO.read(portalCaptchaConnection.getInputStream());
            File imageFile = new File("captcha.jpg");
            imageFile.createNewFile();
            ImageIO.write(bufferedImage, "jpg", imageFile);
            System.out.println("Captcha Received.");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Check and enter captcha :");
            captcha = scanner.next();
            System.out.println("Captcha Entered :" + captcha);
            System.out.println("Done");
        }
    }

    private void getSessionId() throws IOException {
        URL portalSessionURL = new URL(PORTAL_HOST_HTTPS + "/aportal/");
        HttpsURLConnection portalSessionConnection = (HttpsURLConnection) portalSessionURL.openConnection();
        int sessionResponseCode = portalSessionConnection.getResponseCode();
        if (sessionResponseCode == HttpsURLConnection.HTTP_OK) {
            System.out.println("Session response code is ok");
            System.out.println(portalSessionConnection.getHeaderFields());
            sessionId = portalSessionConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
            System.out.println("Session id : " + sessionId);
        }
    }
}

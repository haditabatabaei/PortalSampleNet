import com.sun.jndi.toolkit.url.Uri;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.net.HttpURLConnection.HTTP_OK;

public class PortalNetHunter implements Runnable {
    private final String PORTAL_HOST = "portal.aut.ac.ir";
    private final String PORTAL_HOST_HTTPS = "https://portal.aut.ac.ir";
    private String sessionId;
    private String samadSession;
    private String samadCaptcha;
    private String username;
    private String password;
    private String captcha;
    private String portalFulLName;
    private String email;
    private String idNum;
    private String phone;
    private ArrayList<String> coursesName;
    private ArrayList<Float> scores;
    private ArrayList<String> professors;
    private ArrayList<String> firstTimes;
    private ArrayList<String> secondTimes;
    private ArrayList<String> examTimes;

    public PortalNetHunter(String username, String password) {
        this.username = username;
        this.password = password;
        coursesName = new ArrayList<>();
        scores = new ArrayList<>();
        professors = new ArrayList<>();
        firstTimes = new ArrayList<>();
        secondTimes = new ArrayList<>();
        examTimes = new ArrayList<>();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSamadSession() {
        return samadSession;
    }

    public void setSamadSession(String samadSession) {
        this.samadSession = samadSession;
    }

    public String getSamadCaptcha() {
        return samadCaptcha;
    }

    public void setSamadCaptcha(String samadCaptcha) {
        this.samadCaptcha = samadCaptcha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPortalFulLName() {
        return portalFulLName;
    }

    public void setPortalFulLName(String portalFulLName) {
        this.portalFulLName = portalFulLName;
    }

    public void start() throws IOException {
        sendLogin();
        //getTopStudent();
        // getPersonal();
    }

    public void init() throws IOException {
        getSessionId();
        getCaptcha();

    }

    public void getCourses() throws IOException {
        URL personalUrl = new URL(PORTAL_HOST_HTTPS + "/aportal/regadm/student.portal/student.portal.jsp?action=edit&st_info=sem_962&st_sub_info=courses");
        HttpsURLConnection personalConnection = (HttpsURLConnection) personalUrl.openConnection();
        personalConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        File personalFile = new File("courses.jsp");
        File personalFileHtml = new File("coursesHtml.html");
        personalFile.createNewFile();
        personalFileHtml.createNewFile();
        int responseCode = personalConnection.getResponseCode();
        System.out.println("Personal response code : " + responseCode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(personalConnection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            //    System.out.println(line);
            response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
        }
        FileWriter personalWriter = new FileWriter(personalFile);
        FileWriter personalHtmlWriter = new FileWriter(personalFileHtml);
        personalWriter.write(response.toString());
        personalHtmlWriter.write(response.toString());
        personalWriter.close();
        personalHtmlWriter.close();
        Document coursesDoc = Jsoup.parse(personalFileHtml, "UTF-8");
        Elements elements = coursesDoc.getElementsByAttributeValue("class", "gridtr");
        for (int i = 0; i < elements.size(); i++) {
            Element onHoldElement = elements.get(i);
            String course = onHoldElement.select("td.gridtic").get(1).text();
            float courseScore = Float.parseFloat(onHoldElement.select("td.gridtic").get(8).text());
            coursesName.add(course);
            scores.add(courseScore);
        }
        // getChooseCourses();
    }

    public ArrayList<Float> getScores() {
        return scores;
    }

    public ArrayList<String> getCoursesName() {
        return coursesName;
    }

    public ArrayList<String> getProfessors() {
        return professors;
    }

    public ArrayList<String> getFirstTimes() {
        return firstTimes;
    }

    public ArrayList<String> getSecondTimes() {
        return secondTimes;
    }

    public ArrayList<String> getExamTimes() {
        return examTimes;
    }

    private void clearAllLists() {
        coursesName.clear();
        professors.clear();
        firstTimes.clear();
        secondTimes.clear();
        examTimes.clear();
    }

    public void handleComboSelection(int selectionNum) throws IOException {
        clearAllLists();
        switch (selectionNum) {
            case 0:
                System.out.println("fuck 0");
                handleRequest("u_mine_all");
                break;
            case 1:
                System.out.println("fuck 1");
                handleRequest("u_pre_register");
                break;
            case 2:
                System.out.println("fuck 2");
                handleRequest("u_math");
                break;
            case 3:
                System.out.println("fuck 3");
                handleRequest("u_phys");
                break;
            case 4:
                System.out.println("fuck 4");
                handleRequest("u_physlab1");

                break;
            case 5:
                System.out.println("fuck 5");
                handleRequest("u_physlab2");

                break;
            case 6:
                System.out.println("fuck 6");
                handleRequest("u_serv");

                break;
            case 7:
                System.out.println("fuck 7");
                handleRequest("u_english");

                break;
            case 8:
                System.out.println("fuck 8");
                handleRequest("u_history");

                break;
            case 9:
                System.out.println("fuck 9");
                handleRequest("u_andishe");

                break;
            case 10:
                System.out.println("fuck 10");
                handleRequest("u_persian");

                break;
            case 11:
                System.out.println("fuck 11");
                handleRequest("u_akhlagh");

                break;
            case 12:
                System.out.println("fuck 12");
                handleRequest("u_revel");

                break;
            case 13:
                System.out.println("fuck 13");
                handleRequest("u_tafsir");

                break;
            case 14:
                System.out.println("fuck 14");
                handleRequest("u_phyedu1");
                break;
            case 15:
                System.out.println("fuck 15");
                handleRequest("u_phyedu2");
                break;
        }
    }

    private void handleRequest(String portalSubInfo) throws IOException {
        URL personalUrl = new URL(PORTAL_HOST_HTTPS + "/aportal/regadm/student.portal/student.portal.jsp?action=edit&st_info=register&st_sub_info=" + portalSubInfo);
        HttpsURLConnection personalConnection = (HttpsURLConnection) personalUrl.openConnection();
        personalConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        File personalFile = new File("coursesChoose.jsp");
        File personalFileHtml = new File("coursesHtmlChoose.html");
        personalFile.createNewFile();
        personalFileHtml.createNewFile();
        int responseCode = personalConnection.getResponseCode();
        System.out.println("Personal response code : " + responseCode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(personalConnection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
                System.out.println(line);
            response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
        }
        FileWriter personalWriter = new FileWriter(personalFile);
        FileWriter personalHtmlWriter = new FileWriter(personalFileHtml);
        personalWriter.write(response.toString());
        personalHtmlWriter.write(response.toString());
        personalWriter.close();
        personalHtmlWriter.close();
        Document coursesDoc = Jsoup.parse(personalFileHtml, "UTF-8");
        Elements elements = coursesDoc.getElementsByAttributeValue("class", "gridtr");
        for (int i = 0; i < elements.size(); i++) {
            Element onHoldElement = elements.get(i);
            String course = onHoldElement.select("td.gridtic").get(1).text();
            //  float courseScore = Float.parseFloat(onHoldElement.select("td.gridtic").get(8).text());
            String professor = onHoldElement.select("td.gridtic").get(7).text();
            String firstTime = onHoldElement.select("td.gridtic").get(8).text();
            String secondTime = onHoldElement.select("td.gridtic").get(9).text();
            String examTime = onHoldElement.select("td.gridtic").get(11).text();
            coursesName.add(course);
            professors.add(professor);
            firstTimes.add(firstTime);
            secondTimes.add(secondTime);
            examTimes.add(examTime);
            //scores.add(courseScore);
        }
    }

    public void getChooseCourses() throws IOException {
        URL personalUrl = new URL(PORTAL_HOST_HTTPS + "/aportal/regadm/student.portal/student.portal.jsp?action=edit&st_info=register&st_sub_info=u_mine_all");
        HttpsURLConnection personalConnection = (HttpsURLConnection) personalUrl.openConnection();
        personalConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        File personalFile = new File("coursesChoose.jsp");
        File personalFileHtml = new File("coursesHtmlChoose.html");
        personalFile.createNewFile();
        personalFileHtml.createNewFile();
        int responseCode = personalConnection.getResponseCode();
        System.out.println("Personal response code : " + responseCode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(personalConnection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            //    System.out.println(line);
            response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
        }
        FileWriter personalWriter = new FileWriter(personalFile);
        FileWriter personalHtmlWriter = new FileWriter(personalFileHtml);
        personalWriter.write(response.toString());
        personalHtmlWriter.write(response.toString());
        personalWriter.close();
        personalHtmlWriter.close();
        Document coursesDoc = Jsoup.parse(personalFileHtml, "UTF-8");
        Elements elements = coursesDoc.getElementsByAttributeValue("class", "gridtr");
        for (int i = 0; i < elements.size(); i++) {
            Element onHoldElement = elements.get(i);
            String course = onHoldElement.select("td.gridtic").get(1).text();
            //  float courseScore = Float.parseFloat(onHoldElement.select("td.gridtic").get(8).text());
            String professor = onHoldElement.select("td.gridtic").get(7).text();
            String firstTime = onHoldElement.select("td.gridtic").get(8).text();
            String secondTime = onHoldElement.select("td.gridtic").get(9).text();
            String examTime = onHoldElement.select("td.gridtic").get(11).text();
            coursesName.add(course);
            professors.add(professor);
            firstTimes.add(firstTime);
            secondTimes.add(secondTime);
            examTimes.add(examTime);
            //scores.add(courseScore);
        }
    }

    private void getPersonal() throws IOException {
        URL personalUrl = new URL(PORTAL_HOST_HTTPS + "/aportal/regadm/student.portal/student.portal.jsp?action=edit&st_info=personal&st_sub_info=0");
        HttpsURLConnection personalConnection = (HttpsURLConnection) personalUrl.openConnection();
        personalConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        File personalFile = new File("personal.jsp");
        File personalFileHtml = new File("personalHtml.html");
        personalFile.createNewFile();
        personalFileHtml.createNewFile();
        int responseCode = personalConnection.getResponseCode();
        System.out.println("Personal response code : " + responseCode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(personalConnection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
        }
        FileWriter personalWriter = new FileWriter(personalFile);
        FileWriter personalHtmlWriter = new FileWriter(personalFileHtml);
        personalWriter.write(response.toString());
        personalHtmlWriter.write(response.toString());
        personalWriter.close();
        personalHtmlWriter.close();
        Document personalDoc = Jsoup.parse(personalFileHtml, "UTF-8");
        Element firstNameElement = personalDoc.select("input[name='st_firstname']").first();
        Element lastNameElement = personalDoc.selectFirst("input[name='st_lastname']");
        Element emailElement = personalDoc.select("input[name='st_email']").first();
        Element idNumElement = personalDoc.select("input[name='st_nationalid']").first();
        Element phoneElement = personalDoc.select("input[name='st_cellphone']").first();
        portalFulLName = firstNameElement.val() + " " + lastNameElement.val();
        email = emailElement.val();
        idNum = idNumElement.val();
        phone = phoneElement.val();
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
        portalCaptchaConnection.setRequestProperty("User-Agent","Mozilla/5.0");
        int captchaResponse = portalCaptchaConnection.getResponseCode();
        if (captchaResponse == HTTP_OK) {
            System.out.println("Captcha response ok");
            BufferedImage bufferedImage = ImageIO.read(portalCaptchaConnection.getInputStream());
            File imageFile = new File("captcha.jpg");
            imageFile.createNewFile();
            ImageIO.write(bufferedImage, "jpg", imageFile);
            System.out.println("Captcha Received.");
            System.out.println("Done");
        }else{
            System.out.println("CAPTCHA JAVA RESPONSE NOT OK " + portalCaptchaConnection.getResponseCode());
        }
    }

    private void getSessionId() throws IOException {
        URL portalSessionURL = new URL(PORTAL_HOST_HTTPS + "/aportal/");
        HttpsURLConnection portalSessionConnection = (HttpsURLConnection) portalSessionURL.openConnection();
        int sessionResponseCode = portalSessionConnection.getResponseCode();
        if (sessionResponseCode == HTTP_OK) {
            System.out.println("Session response code is ok");
            System.out.println(portalSessionConnection.getHeaderFields());
            sessionId = portalSessionConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
            System.out.println("Session id : " + sessionId);
            /* Handling captcha using python */
            System.out.println("python hadi.py " + sessionId + " 20");
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"python hadi.py " + sessionId + " 20\"");
            System.out.println("Python Started");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("WOKE UP");
            BufferedReader reader = new BufferedReader(new FileReader("captcha.txt"));
            captcha = reader.readLine();
            System.out.println("READ CAPTCHA : " + captcha);
          //  getCaptcha();
/*            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            sendLogin();
        /*    try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        //    getTopStudent();
            getPersonal();
            handleRequest("u_math");
            //System.exit(0);
        }
    }


    private void dawnCaptcha(String captchaName) throws IOException {
        URL portalCaptcha = new URL(PORTAL_HOST_HTTPS + "/aportal/PassImageServlet");
        HttpsURLConnection portalCaptchaConnection = (HttpsURLConnection) portalCaptcha.openConnection();
        portalCaptchaConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId + "; _ga=GA1.3.1786734947.1535188347; _gid=GA1.3.1828509133.1535188347");
        int captchaResponse = portalCaptchaConnection.getResponseCode();
        if (captchaResponse == HTTP_OK) {
            System.out.println("Captcha response ok");
            BufferedImage bufferedImage = ImageIO.read(portalCaptchaConnection.getInputStream());
            File imageFile = new File(captchaName + ".jpg");
            imageFile.createNewFile();
            ImageIO.write(bufferedImage, "jpg", imageFile);
            System.out.println("Captcha Received.");
            System.out.println("Done");
        }
    }

    public void projectDawn() throws IOException {
        for (int i = 300; i < 450; i++) {
            getSessionId();
            dawnCaptcha("CaptchaNum" + i);
        }
    }


    public void startSamad() throws IOException {
        URL samadSessionUrl = new URL("http://samad.aut.ac.ir/index.rose");
        HttpURLConnection samadSessionConnection = (HttpURLConnection) samadSessionUrl.openConnection();
        samadSessionConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        samadSessionConnection.setRequestProperty("Conneciton", "keep-alive");
        samadSessionConnection.setRequestProperty("Referer", "http://samad.aut.ac.ir/index/index.rose");
        samadSessionConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        int samadSessionResponse = samadSessionConnection.getResponseCode();
        if (samadSessionResponse == HTTP_OK) {
            System.out.println(samadSessionConnection.getHeaderFields());
            samadSession = samadSessionConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
            System.out.println("Samad Session : " + samadSession);

            URL samadCheck = new URL("http://samad.aut.ac.ir/j_security_check");
            HttpURLConnection samadConnection = (HttpURLConnection) samadCheck.openConnection();
            samadConnection.setRequestMethod("POST");
            samadConnection.setDoOutput(true);
            samadConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
            samadConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            samadConnection.setRequestProperty("Connection", "keep-alive");
            samadConnection.setRequestProperty("Cache-Control", "max-age=0");
            samadConnection.setRequestProperty("Content-Length", "110");
            samadConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            samadConnection.setRequestProperty("Referer", "http://samad.aut.ac.ir/index.rose");
            samadConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            String params = "_csrf=4b52b387-a7ce-4d89-bb93-d12a0b95242a&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF";
            samadConnection.getOutputStream().write(params.getBytes());
            int samadLoginResponse = samadConnection.getResponseCode();
            if (samadLoginResponse == HTTP_OK) {
                System.out.println("Samad login completed.");


                HttpURLConnection samadCheck2 = (HttpURLConnection) new URL("http://samad.aut.ac.ir/index/index.rose").openConnection();
                samadCheck2.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
                samadCheck2.setRequestProperty("User-Agent", "Mozilla/5.0");
                samadCheck2.setRequestProperty("Connection", "keep-alive");
                samadCheck2.setRequestProperty("Cache-Control", "max-age=0");
                samadCheck2.setRequestProperty("Content-Length", "110");
                //      samadCheck.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                samadCheck2.setRequestProperty("Referer", "http://samad.aut.ac.ir/index.rose");
                samadCheck2.setRequestProperty("Upgrade-Insecure-Requests", "1");


                if (samadCheck2.getResponseCode() == HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(samadCheck2.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        //System.out.println(line);
                        response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
                    }
                    File studentResult2 = new File("studentResult-new.jsp");
                    File studentResultHtml2 = new File("studentResult-new.html");
                    studentResult2.createNewFile();
                    studentResultHtml2.createNewFile();
                    FileWriter personalWriter = new FileWriter(studentResult2);
                    FileWriter personalHtmlWriter = new FileWriter(studentResultHtml2);
                    personalWriter.write(response.toString());
                    personalHtmlWriter.write(response.toString());
                    personalWriter.close();
                    personalHtmlWriter.close();
                } else {
                    System.out.println(samadCheck2.getResponseCode() + " THIS IS SAMAD TEST RESPONSE CODE");
                }

/*                URL samadCaptchaUrl = new URL("http://samad.aut.ac.ir/captcha.jpg");
                HttpURLConnection samadCaptchaConnection = (HttpURLConnection) samadCaptchaUrl.openConnection();
                samadCaptchaConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
                samadCaptchaConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                int samadCaptchaResponse = samadCaptchaConnection.getResponseCode();
                if (samadCaptchaResponse == HTTP_OK) {
                    File samadCaptcha = new File("sCaptcha.jpg");
                    samadCaptcha.createNewFile();
                    BufferedImage bufferedCaptcha = ImageIO.read(samadCaptchaConnection.getInputStream());
                    ImageIO.write(bufferedCaptcha, "jpg", samadCaptcha);
                }
            } else {
                System.out.println("SAMAD LOGIN RESPONSE POST RESPONSE : IMPORTANT : " + samadLoginResponse);
            }*/
            }
        }
    }

    public String findStudent(String studentId) throws IOException {


        HttpURLConnection samadTestGet = (HttpURLConnection) new URL("http://samad.aut.ac.ir/nurture/user/credit/inputTransferCreditInfo.rose").openConnection();
        samadTestGet.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
        samadTestGet.setRequestProperty("User-Agent", "Mozilla/5.0");
        samadTestGet.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        samadTestGet.setRequestProperty("Referer", "http://samad.aut.ac.ir/index/index.rose");
        samadTestGet.setRequestProperty("Upgrade-Insecure-Requests", "1");
        int samadTestResponseCode = samadTestGet.getResponseCode();
        if (samadTestResponseCode == HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(samadTestGet.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
            }
            File studentResult2 = new File("studentResult-new.jsp");
            File studentResultHtml2 = new File("studentResult-new.html");
            studentResult2.createNewFile();
            studentResultHtml2.createNewFile();
            FileWriter personalWriter = new FileWriter(studentResult2);
            FileWriter personalHtmlWriter = new FileWriter(studentResultHtml2);
            personalWriter.write(response.toString());
            personalHtmlWriter.write(response.toString());
            personalWriter.close();
            personalHtmlWriter.close();
        } else {
            System.out.println(samadTestResponseCode + " THIS IS SAMAD TEST RESPONSE CODE");
        }


        URL finalPost = new URL("http://samad.aut.ac.ir/nurture/user/credit/inputTransferCreditInfoFinal.rose");
        HttpURLConnection finalPostConnection = (HttpURLConnection) finalPost.openConnection();
        finalPostConnection.setRequestMethod("POST");
        String params = "maxTransferableCreditAmount=19983&minTransferableCreditAmount=0&targetUserName=" + studentId + "&transferCreditAmount=1&captcha_input=" + samadCaptcha + "&_csrf=82a7d2af-0fbe-4306-a7a6-ae7fb8be2917";
        finalPostConnection.setDoOutput(true);
        finalPostConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
        finalPostConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        finalPostConnection.getOutputStream().write(params.getBytes());
        if (finalPostConnection.getResponseCode() == HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(finalPostConnection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                response.append(line);
       /*     if (line.startsWith("<td class=toptrc nowrap><font color=white size=\"+1\">")) {
                System.out.println("----------------\n" + line + "------------\n");
                portalFulLName = line.replace("<td class=toptrc nowrap><font color=white size=\"+1\">", "").replace("</font></td>", "");
            }*/
            }
            File studentResult = new File("studentResult.jsp");
            File studentResultHtml = new File("studentResult.html");
            studentResult.createNewFile();
            studentResultHtml.createNewFile();
            FileWriter personalWriter = new FileWriter(studentResult);
            FileWriter personalHtmlWriter = new FileWriter(studentResultHtml);
            personalWriter.write(response.toString());
            personalHtmlWriter.write(response.toString());
            personalWriter.close();
            personalHtmlWriter.close();
        }
        return null;
    }

    @Override
    public void run() {

    }
}


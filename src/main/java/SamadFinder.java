import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class SamadFinder implements Runnable {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
    private String samadSession;
    private String firstSession;
    private String csrf;
    private final String SAMAD_HOST_HTTP = "http://samad.aut.ac.ir";
    private final String SAMAD_HOST = "samad.aut.ac.ir";
    private final String TEST_LOGIN_USERNAME = "9631049";
    private final String TEST_LOGIN_PASS = "0371964660";
    private String inputCaptcha;
    private String searchUsername = "9631050";
    private int counter;

    public SamadFinder() {
        counter = 0;
    }

    private void getStartSessionAndCsrf() throws IOException {

        URL samadURL = new URL(SAMAD_HOST_HTTP + "/index.rose");
        HttpURLConnection samadIndexConnection = (HttpURLConnection) samadURL.openConnection();
        samadIndexConnection.setRequestProperty("Host", SAMAD_HOST);
        samadIndexConnection.setRequestProperty("Connection", "keep-alive");
        samadIndexConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadIndexConnection.setRequestProperty("User-Agent", USER_AGENT);
        samadIndexConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        samadIndexConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
        samadIndexConnection.setRequestProperty("Accept-Encoding", "gzip, deflat");
        samadIndexConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        //samadIndexConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        //samadIndexConnection.setRequestProperty("Cache-Control", "max-age=0");
        //samadIndexConnection.setRequestProperty("Content-Length", "110");
        // samadSecurityConnection.setRequestProperty("Origin", SAMAD_HOST_HTTP);
        //  samadIndexConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        System.out.println(samadIndexConnection.getHeaderFields());
        firstSession = samadIndexConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
        System.out.println("First Session=" + firstSession);
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(samadIndexConnection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            if (line.contains("X-CSRF-TOKEN")) {
                reader.close();
                break;
            }
        }
        csrf = line.substring(line.indexOf(':') + 3, line.lastIndexOf("'"));
        System.out.println("First CSRF=" + csrf);
    }

    private void sendLogin() throws IOException {
        URL samadSecurityURL = new URL(SAMAD_HOST_HTTP + "/j_security_check");
        HttpURLConnection samadSecurityConnection = (HttpURLConnection) samadSecurityURL.openConnection();
        samadSecurityConnection.setRequestMethod("POST");
        samadSecurityConnection.setRequestProperty("Host", SAMAD_HOST);
        samadSecurityConnection.setRequestProperty("Connection", "keep-alive");
        samadSecurityConnection.setRequestProperty("Content-Length", "110");
        samadSecurityConnection.setRequestProperty("Cache-Control", "max-age=0");
        samadSecurityConnection.setRequestProperty("Origin", SAMAD_HOST_HTTP);
        samadSecurityConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadSecurityConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        samadSecurityConnection.setRequestProperty("User-Agent", USER_AGENT);
        samadSecurityConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        samadSecurityConnection.setInstanceFollowRedirects(true);
        samadSecurityConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
        samadSecurityConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        samadSecurityConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        samadSecurityConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + firstSession);
        samadSecurityConnection.setDoOutput(true);
        String params = "_csrf=" + csrf + "&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF";
        samadSecurityConnection.getOutputStream().write(params.getBytes());
        samadSecurityConnection.getOutputStream().flush();
        samadSession = samadSecurityConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=","");
        System.out.println("SAmad security response : " + samadSecurityConnection.getResponseCode());


    }

    private void getPage() throws IOException{
        URL samadURL = new URL(SAMAD_HOST_HTTP + "/index/index.rose");
        HttpURLConnection samadIndexConnection = (HttpURLConnection) samadURL.openConnection();
        samadIndexConnection.setRequestProperty("Host", SAMAD_HOST);
        samadIndexConnection.setRequestProperty("Connection", "keep-alive");
        samadIndexConnection.setRequestProperty("Cache-Control", "max-age=0");
        samadIndexConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadIndexConnection.setRequestProperty("User-Agent", USER_AGENT);
        samadIndexConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        samadIndexConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
        samadIndexConnection.setRequestProperty("Accept-Encoding", "gzip, deflat");
        samadIndexConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        samadIndexConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);

        System.out.println("Samad Account Response : " + samadIndexConnection.getResponseCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(samadIndexConnection.getInputStream()));

        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line);
        }
    }
    // @Override
    public void run() {
        try {
            getStartSessionAndCsrf();
            sendLogin();
            getPage();
        } catch (IOException e) {
            e.printStackTrace();
        }

//            System.out.println("FIRST SAMAD RESPONSE CODE : SAMAD/INDEX.ROSE : " + samadIndexResponseCode);
//            if (samadIndexResponseCode == HTTP_OK) {
//                System.out.println("SAMAD first Index Headers :" + samadIndexConnection.getHeaderFields());
//                firstSession = samadIndexConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
//                System.out.println("Samad first Session id : " + firstSession);
//
//                /* Handling X-CSRF-TOKEN */
//                String linesRead;
//                BufferedReader firstReader = new BufferedReader(new InputStreamReader(samadIndexConnection.getInputStream()));
//                while ((linesRead = firstReader.readLine()) != null) {
//                    if (linesRead.contains("X-CSRF-TOKEN"))
//                        csrf = linesRead;
//                }
//                csrf = csrf.substring(csrf.indexOf(':') + 3, csrf.lastIndexOf("'"));
//                System.out.println("CSRF :" + csrf);
//                URL samadSecurityURL = new URL(SAMAD_HOST_HTTP + "/j_security_check");
//                HttpURLConnection samadSecurityConnection = (HttpURLConnection) samadSecurityURL.openConnection();
//                samadSecurityConnection.setRequestMethod("POST");
//                samadSecurityConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//                samadSecurityConnection.setInstanceFollowRedirects(true);
//                samadSecurityConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//                samadSecurityConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
//                samadSecurityConnection.setRequestProperty("Cache-Control", "max-age=0");
//                samadSecurityConnection.setRequestProperty("Connection", "keep-alive");
//                samadSecurityConnection.setRequestProperty("Content-Length", "110");
//                samadSecurityConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                samadSecurityConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + firstSession);
//                samadSecurityConnection.setRequestProperty("Host", SAMAD_HOST);
//                samadSecurityConnection.setRequestProperty("Origin", SAMAD_HOST_HTTP);
//                samadSecurityConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
//                samadSecurityConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
//                samadSecurityConnection.setRequestProperty("User-Agent", USER_AGENT);
//                samadSecurityConnection.setAllowUserInteraction(true);
//                samadSecurityConnection.setInstanceFollowRedirects(true);
//                //samadSession = samadSecurityConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
//                String parameters = "_csrf=" + csrf + "&username=" + TEST_LOGIN_USERNAME + "&password=" + TEST_LOGIN_PASS + "&login=%D9%88%D8%B1%D9%88%D8%AF";
//                System.out.println("Parameters : " + parameters);
//                samadSecurityConnection.setDoOutput(true);
//                samadSecurityConnection.getOutputStream().write(parameters.getBytes());
//                samadSecurityConnection.getOutputStream().flush();
//                //     samadSecurityConnection.getOutputStream().close();
//                System.out.println(samadSecurityConnection.getRequestMethod());
//                System.out.println(samadSecurityConnection.getHeaderFields());
//                int samadSecurityResponse = samadSecurityConnection.getResponseCode();
//                System.out.println("Samad J Security Response : " + samadIndexResponseCode);
//                if (samadSecurityResponse == HTTP_OK) {
//                    HttpURLConnection testSec = (HttpURLConnection) samadSecurityURL.openConnection();
//                    testSec.setInstanceFollowRedirects(true);
//                    HttpURLConnection.setFollowRedirects(true);
//                    System.out.println(testSec.getResponseCode() + " MESSAGE : " + testSec.getResponseMessage() + " Headers : " + testSec.getHeaderFields());
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(samadSecurityConnection.getInputStream()));
//                    String line;
//                    StringBuilder response = new StringBuilder();
//                    while ((line = reader.readLine()) != null) {
//                        //   System.out.println(line);
//                        response.append(line);
//                    }
//                    File indexRose = new File("index2345.rose");
//                    File indexRoseHtml = new File("indexRose2345.html");
//                    indexRose.createNewFile();
//                    indexRoseHtml.createNewFile();
//                    FileWriter writer = new FileWriter(indexRose);
//                    FileWriter htmlWriter = new FileWriter(indexRoseHtml);
//
//                    writer.write(response.toString());
//                    htmlWriter.write(response.toString());
//
//                    writer.close();
//                    htmlWriter.close();
//                    System.out.println("Samad J Security Get Headers : " + samadSecurityConnection.getHeaderFields());
//                    System.out.println("TEST PRINTS : " + samadSecurityConnection.getHeaderFields());
//                    System.out.println("Samad Security OK");
//                    System.out.println("Samad J Security Headers : " + samadSecurityConnection.getHeaderFields());
//                    samadSession = samadSecurityConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
//                    System.out.println("Samad new active session : " + samadSession);
//
//
//                    URL samadIndexAccountURL = new URL("http://185.211.88.67" + "/index/index.rose");
//                    HttpURLConnection samadIndexAccountConnection = (HttpURLConnection) samadIndexAccountURL.openConnection();
//                    samadIndexAccountConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//                    samadIndexAccountConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//                    samadIndexAccountConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
//                    samadIndexAccountConnection.setRequestProperty("Cache-Control", "max-age=0");
//                    samadIndexAccountConnection.setRequestProperty("Connection", "keep-alive");
//                    samadIndexAccountConnection.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
//                    samadIndexAccountConnection.setRequestProperty("Host", SAMAD_HOST);
//                    samadIndexAccountConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
//                    samadIndexAccountConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
//                    samadIndexAccountConnection.setRequestProperty("User-Agent", USER_AGENT);
//                    int samadIndexAccountResponse = samadIndexAccountConnection.getResponseCode();
//
//                    System.out.println("SAMAD ACTION = HOME RESPONSE :  " + samadIndexAccountResponse);
//                    if (samadIndexAccountResponse == HTTP_OK) {
//                        System.out.println("Samad Index Account OK");
//                        System.out.println("Samad Login Index Account Headers : " + samadIndexAccountConnection.getHeaderFields());
//                        System.out.println("Getting page...");
//                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(samadIndexAccountConnection.getInputStream()));
//                        String line2;
//                        StringBuilder response2 = new StringBuilder();
//                        while ((line = reader.readLine()) != null) {
//                            //   System.out.println(line);
//                            response.append(line);
//                        }
//                        File indexRose2 = new File("index23.rose");
//                        File indexRoseHtml2 = new File("indexRose23.html");
//                        indexRose.createNewFile();
//                        indexRoseHtml.createNewFile();
//                        FileWriter writer2 = new FileWriter(indexRose2);
//                        FileWriter htmlWriter2 = new FileWriter(indexRoseHtml2);
//
//                        writer2.write(response.toString());
//                        htmlWriter2.write(response.toString());
//
//                        writer2.close();
//                        htmlWriter2.close();
//                    }
//
//                }
//
//
//            }

    }
}

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class SamadFinder implements Runnable {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
    private static final String MOZILLA_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0";
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
        // samadIndexConnection.setInstanceFollowRedirects(true);
        //  HttpURLConnection.setFollowRedirects(true);
        samadIndexConnection.setRequestProperty("Host", SAMAD_HOST);
        samadIndexConnection.setRequestProperty("Connection", "keep-alive");
        samadIndexConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadIndexConnection.setRequestProperty("User-Agent", MOZILLA_USER_AGENT);
        samadIndexConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        samadIndexConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
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
        samadSecurityConnection.setInstanceFollowRedirects(true);
        HttpURLConnection.setFollowRedirects(true);
        samadSecurityConnection.setDoOutput(true);
        samadSecurityConnection.setRequestProperty("Host", SAMAD_HOST);
        samadSecurityConnection.setRequestProperty("Connection", "keep-alive");
        samadSecurityConnection.setRequestProperty("Content-Length", "110");
        samadSecurityConnection.setRequestProperty("Cache-Control", "max-age=0");
        samadSecurityConnection.setRequestProperty("Origin", SAMAD_HOST_HTTP);
        samadSecurityConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadSecurityConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        samadSecurityConnection.setRequestProperty("User-Agent", MOZILLA_USER_AGENT);
        samadSecurityConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //samadSecurityConnection.setInstanceFollowRedirects(true);
        samadSecurityConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
        samadSecurityConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        samadSecurityConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        samadSecurityConnection.setRequestProperty("Cookie", "JSESSIONID=" + firstSession);
        String params = "_csrf=" + csrf + "&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF";
        samadSecurityConnection.getOutputStream().write(params.getBytes());
        samadSecurityConnection.getOutputStream().flush();
        samadSecurityConnection.getOutputStream().close();
        System.out.println("Samad JSecurity headers :" + samadSecurityConnection.getHeaderField("Location"));
        System.out.println("Samad JSecurity session : " + (samadSession = samadSecurityConnection.getHeaderField("Set-Cookie").split(";")[0].replace("JSESSIONID=", "")));
        System.out.println("Samad new Session : " + samadSession);
        System.out.println("SAmad security response : " + samadSecurityConnection.getResponseCode());
    }

    private void getPage() throws IOException {
        URL samadURL = new URL(SAMAD_HOST_HTTP + "/index/index.rose");
        HttpURLConnection samadIndexConnection = (HttpURLConnection) samadURL.openConnection();
        // samadIndexConnection.setInstanceFollowRedirects(true);
        samadIndexConnection.setRequestProperty("Host", SAMAD_HOST);
        samadIndexConnection.setRequestProperty("Connection", "keep-alive");
        samadIndexConnection.setRequestProperty("Cache-Control", "max-age=0");
        samadIndexConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        samadIndexConnection.setRequestProperty("User-Agent", USER_AGENT);
        samadIndexConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        samadIndexConnection.setRequestProperty("Referer", SAMAD_HOST_HTTP + "/index.rose");
        samadIndexConnection.setRequestProperty("Accept-Encoding", "gzip, deflat");
        samadIndexConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        samadIndexConnection.setRequestProperty("Cookie", "JSESSIONID=" + samadSession);

        System.out.println("Samad Account Response : " + samadIndexConnection.getResponseCode());
        System.out.println("samad Account headers : " + samadIndexConnection.getHeaderFields());
        BufferedReader reader = new BufferedReader(new InputStreamReader(samadIndexConnection.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


    private void getStartSessionUsingClient() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://samad.aut.ac.ir/index.rose").build();
        Response response = client.newCall(request).execute();
        firstSession = response.header("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
//        System.out.println("first Session : " + firstSession);
        String responseBody = response.body().string();
        Document doc = Jsoup.parse(responseBody);
        Element input = doc.select("input[name='_csrf']").first();
        csrf = input.val();
        System.out.println("first session : " + firstSession + " csrf :" + csrf);
        //return (response.headers() + "\n" +response.body().string());
    }

    private void sendLoginUsingClient() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(null, "_csrf=" + csrf + "&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF");
        Request request = new Request.Builder().url(new URL("http://samad.aut.ac.ir/j_security_check"))
                .header("Cookie", "JSESSIONID=" + firstSession)
                .header("User-Agent", "Mozilla/5.0")
                .post(requestBody)
                .build();
        System.out.println(request.headers());
        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        System.out.println("Response Headers : " + response.headers() + "\n\n\n" + response.body());
    }

    private void postManFirstSession() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://samad.aut.ac.ir/index.rose")
                .get()
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "24cc8629-da25-46b6-884e-ce8a9232ea83")
                .build();

        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Element element = doc.selectFirst("script");
        String data = doc.selectFirst("script").data();
        csrf = data.substring(data.indexOf(":") + 3, data.lastIndexOf("'"));
        samadSession = response.header("Set-Cookie").split(";")[0].replace("JSESSIONID=", "");
        System.out.println(csrf);
        System.out.println(samadSession);
        // OkHttpClient client2 =
    }

    private void getInfo() throws IOException, UnirestException {
        HttpURLConnection samadConn = (HttpURLConnection) (new URL("http://samad.aut.ac.ir/index.rose")).openConnection();
        System.out.println("samad index.rose connection : " + samadConn.getResponseCode());
        System.out.println("samad index.rose connection headers : " + samadConn.getHeaderFields());
        System.out.println("samad first : " + (samadSession = samadConn.getHeaderField("Set-Cookie").split(";")[0]));

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(samadConn.getInputStream()));

        while ((line = reader.readLine()) != null) {
            if (line.contains("csrf"))
                break;
        }
        reader.close();
        csrf = line.substring(line.indexOf(":") + 3, line.lastIndexOf("'"));
        System.out.println("samad csrf =" + csrf);
        HttpURLConnection samadPost = (HttpURLConnection) (new URL("http://samad.aut.ac.ir/j_security_check?_csrf=" + csrf + "&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF")).openConnection();
        samadPost.setRequestMethod("POST");
        samadPost.setRequestProperty("Cookie", samadSession);
        reader = new BufferedReader(new InputStreamReader(samadPost.getInputStream()));
        System.out.println(samadPost.getHeaderFields());
        System.out.println(samadPost.getResponseCode());
        while((line = reader.readLine()) != null)
            System.out.println(line);

        reader.close();
        samadSession = samadPost.getHeaderField("Set-Cookie").split(";")[0];
        HttpURLConnection samadAcc = (HttpURLConnection) (new URL("http://samad.aut.ac.ir/index/index.rose")).openConnection();
//        samadPost.setRequestMethod("POST");
        samadAcc.setRequestProperty("Cookie", samadSession);
        reader = new BufferedReader(new InputStreamReader(samadPost.getInputStream()));
        System.out.println(samadPost.getHeaderFields());
        System.out.println(samadPost.getResponseCode());
        reader = new BufferedReader(new InputStreamReader(samadAcc.getInputStream()));
        while((line = reader.readLine()) != null)
            System.out.println(line);

        reader.close();


        //        HttpResponse<String> response = Unirest.get("http://samad.aut.ac.ir/accessMgmt/action/logout.rose")
//                .header("Cache-Control", "no-cache")
//                .asString();
//
//
//        System.out.println(response5.getBody());
//        Document doc = Jsoup.parse(response5.getBody());
//        Element element1 = doc.selectFirst("script");
//        String data = doc.selectFirst("script").data();
//        csrf = data.substring(data.indexOf(":") + 3, data.lastIndexOf("'"));
//        String resCookie = response5.getHeaders().get("Set-Cookie").get(0);
//        samadSession = resCookie.substring(resCookie.indexOf("=") + 1, resCookie.indexOf(";"));
//        System.out.println(csrf);
//        System.out.println(samadSession);
//        //System.out.println(response5.getBody());
//        //System.out.println(response5.getHeaders());
//
//
//        System.out.println("---------------------------------------");
//        HttpResponse<String> response = Unirest.post("http://samad.aut.ac.ir/j_security_check?_csrf=" + csrf + "&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF")
//                .header("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession)
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Cache-Control", "no-cache")
//                .header("Postman-Token", "bfe53835-494d-4f6f-8a7a-6e43c30152b4")
//                .asString();
//
//        System.out.println("post respond : " + response.getHeaders());
//        String resCookie1 = response.getHeaders().get("Set-Cookie").get(0);
//        samadSession = resCookie1.substring(resCookie1.indexOf("=") + 1, resCookie.indexOf(";"));
//        System.out.println(samadSession);
//
//        HttpResponse<String> response2 = Unirest.post("http://samad.aut.ac.ir/index.rose")
//                .header("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession)
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Cache-Control", "no-cache")
//                .header("Postman-Token", "bfe53835-494d-4f6f-8a7a-6e43c30152b4")
//                .asString();
//        System.out.println(response2.getHeaders());
//        System.out.println(response2.getStatus());
//
//        HttpResponse<String> response3 = Unirest.post("http://samad.aut.ac.ir/home.rose?actionName=home")
//                .header("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession)
//                //.header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Cache-Control", "no-cache")
//                .header("Postman-Token", "bfe53835-494d-4f6f-8a7a-6e43c30152b4")
//                .header("Referer","http://samad.aut.ac.ir/index.rose")
//                .asString();
//        System.out.println(response3.getHeaders());
//        System.out.println("response ?home : " + response3.getStatus());
//
//        HttpResponse<String> response4 = Unirest.post("http://samad.aut.ac.ir/index/index.rose")
//                .header("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession)
//               // .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Cache-Control", "no-cache")
//                .header("Postman-Token", "bfe53835-494d-4f6f-8a7a-6e43c30152b4")
//                .asString();
//
//        System.out.println(response4.getBody());
/*
        HttpURLConnection samadConn = (HttpURLConnection) (new URL("http://samad.aut.ac.ir/j_security_check?_csrf=\" + csrf + \"&username=9631049&password=0371964660&login=%D9%88%D8%B1%D9%88%D8%AF")).openConnection();
        samadConn.setRequestMethod("POST");
        samadConn.setRequestProperty("Cookie", "_ga=GA1.3.1798076556.1532371231; JSESSIONID=" + samadSession);
        samadConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        samadConn.setRequestProperty("Cache-Control", "no-cache");
        samadConn.setRequestProperty("Postman-Token", "a43aa4ef-0e20-4770-8563-c11d25ab8930");
        BufferedReader reader = new BufferedReader(new InputStreamReader(samadConn.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            System.out.println(line);
*/

    }

    // @Override
    public void run() {
        try {
            // getStartSessionUsingClient();
            //  sendLoginUsingClient();
            //  System.setProperty("http.agent", "");
            //   postManFirstSession();
            getInfo();
            //getStartSessionAndCsrf();
            //   sendLogin();
            //   getPage();
            //   getPage();
            //    getPage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
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

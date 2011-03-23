/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bilanpack.net.vkscaner;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Начнм работать!
 * @author Pavlyha
 */
public class VKScaner implements Runnable{
    public static final String MARK_ONLINE = "<h1 id=\"title\"><b class=\"fl_r\">";
    public static final String MARK_END_PAGE = "</html>";
    public static final String MARK_NEAR_NAME = "<h1 id=\"title\">";
    public static final String MARK_NEAR_STATUS = "<div class=\"clear\" id=\"profile_current_info\">";
    public static final String URL_TEMPLATE = "http://vkontakte.ru/@ID@";
    public static final int HTTP_PORT = 80;

    private String vkID;
    private Proxy proxy;
    private Logger log;
    private URL url;
    private int sleepSeconds = 60;
    private String logPath = "D:\\JAVA\\pr\\logVK";
    private boolean connectFailed = false;
    private boolean isOnline = false;
    
    public VKScaner(){
    }

    public VKScaner(String id, String logPath, int sleep, String proxy){
        this.vkID = id;
        this.sleepSeconds = sleep;
    	this.proxy = new Proxy(proxy);
        if(logPath != null)
            this.logPath = logPath;
    }
    /**
     * Возвращает весь ответ в одной стринге, если трабл то null
     * @param url
     * @return
     */
    private String getRequestedPage(URL url, Proxy proxy){
        if(url == null || proxy == null)
            return null;

        Socket sock = null;
        BufferedReader br = null;
//        InputStreamReader isr = null;
        try {
        	if(proxy.isRequired())
        		sock = new Socket(proxy.getHost(), proxy.getPort());
        	else	
        		sock = new Socket(url.getHost(), HTTP_PORT);
            if(connectFailed == true){
                log.log("NETWORK was resumed. Account is "+(isOnline ? "ONLINE":"OFFLINE"));
                connectFailed = false;
            }
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), false);
            pw.print("POST " + url.toString() + " HTTP/1.1\r\n");
            pw.print("Host: " + url.getHost() + "\r\n");
            pw.print("Content-Type: application/x-www-form-urlencoded\r\n");
            if(proxy.isRequired())
            	pw.print("Proxy-Authorization: Basic "+Base64Coder.encodeString(proxy.getLogin()+":"+proxy.getPswd())+"\r\n");
            pw.print("\r\n");
            pw.print("\r\n");
            pw.flush();
            
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            
            StringBuilder sb = new StringBuilder();
            String line = null;
            char[] buf = new char[10*1024];
            while (br.read(buf) > 0) {
                sb.append(buf);
                if(new String(buf).contains(MARK_END_PAGE)){
                    br.close();
                    break;
                }
            }
            return sb.toString();
        } catch (ConnectException ex) {
            if(log != null){
                log.log("ERROR ConnectException: " + ex );
            }else{
                ex.printStackTrace();
            }
        } catch (UnknownHostException ex) {
            if(log != null && !connectFailed){
                log.log("ERROR UnknownHostException: " + ex.getMessage() );
            }
            connectFailed = true;
        } catch (SocketException ex){
            if(log != null && !connectFailed){
                log.log("ERROR SocketException: " + ex.getMessage() );
            }
            connectFailed = true;
        }catch (IOException ex) {
            if(log != null){
                log.log("ERROR IOException: " + ex );
            }else{
                ex.printStackTrace();
            }
        }
        return null;
    }

    private String extractName(String page){
        if(page == null){
            System.out.println("ERROR !!! page = null, may be network is unreachble");
            System.exit(0);
        }
        int q = page.indexOf(MARK_NEAR_NAME);
        q += MARK_NEAR_NAME.length();
        if(page.charAt(q) == '<'){
            q = page.indexOf("</b>", q) + 4;
        }

        return page.substring(q, page.indexOf("</h1>")).trim();

    }

    private static String extractStatus(String page){
        if(page == null){
            System.out.println("ERROR !!! page = null, may be network is unreachble");
            System.exit(0);
        }
        int q = page.indexOf(MARK_NEAR_STATUS);
        if(q == -1)
            return "none";
        q += MARK_NEAR_STATUS.length();
        return page.substring(q, page.indexOf("</div>",q)).trim();
    }

    public void run() {
        try {
            String stUrl = URL_TEMPLATE.replace("@ID@", vkID);
            url = new URL(stUrl);
        } catch (MalformedURLException ex) {
            log.log(ex.getMessage());
            ex.printStackTrace();
            return;
        }

        String page = this.getRequestedPage(url, proxy);
        String name = extractName(page);
        String status = extractStatus(page);

        this.log = new Logger(logPath, name);
        log.log("=====================================================================");
        log.log("Start LOGGING for "+name+". Status: "+status);
        System.out.println("Start logger for: "+vkID);
        long startOnline = 0L;
        while (true) {
            page = this.getRequestedPage(url, proxy);
            if(page == null){
//                try {Thread.sleep(5*sleepSeconds*1000);} catch (InterruptedException ex) {log.log(ex.getMessage()); ex.printStackTrace();}
                try {Thread.sleep(1000);} catch (InterruptedException ex) {log.log(ex.getMessage()); ex.printStackTrace();}
                continue;
            }
            if(page.contains(MARK_ONLINE)){
                if(!isOnline){
                    status = extractStatus(page);
                    log.log("ONLINE. Status: '"+status+"'");
                    isOnline = true;
                    startOnline = System.currentTimeMillis();
                }
            }else{
                if(isOnline){
                    status = extractStatus(page);
                    long duration = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-startOnline);
                    log.log("offline, spend: " + duration +" min. Status: '"+status+"'");
                    isOnline = false;
                }
            }
            try {Thread.sleep(sleepSeconds*1000);} catch (InterruptedException ex) {log.log(ex.getMessage()); ex.printStackTrace();}
        }
    }


    
    

	  public static void main(String[] args) {
	//System.out.println("start \n");
	//String jeka = "id128174932";
	//String yula = "id10664661";
	//String pedan = "id75567015";
	//String bilan = "id8254758";
	//String maksimenko = "id10889993";
	//String kornejchuk = "andrey_korneychuk";
	//VKScaner s = new VKScaner(kornejchuk);
	//new Thread(s).start();
	
	
	//System.out.println(extractName("<div id=\"header_wrap1\">          <div id=\"header\" style=\"\">            <h1 id=\"title\">Yuliya Kolupajlo</h1>          </div>"));
	//System.out.println(extractStatus("<span class=\"fl_r\"></span>    Евгений Павловский    <div class=\"clear\" id=\"profile_current_info\">носим ношенное, ебём брошенное...</div>  </h4>"));
	//
	//URL u;
	//try {
	//	
	//	System.out.println(u.getHost());
	//} catch (MalformedURLException e) {
	//	// TODO Auto-generated catch block
	//	e.printStackTrace();
	//}
	//
	//
	
	
	 Socket sock = null;
	    BufferedReader br = null;
	    try {
	//        sock = new Socket(url.getHost(), HTTP_PORT);
	    	URL url = new URL("http://vkontakte.ru/id1");
	        sock = new Socket("proxy.pbank.com.ua", 8080);
	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), false);
	        pw.print("POST " + url.toString() + " HTTP/1.1\r\n");
	        pw.print("Host: " + url.getHost() + "\r\n");
	        pw.print("Content-Type: application/x-www-form-urlencoded\r\n");
	        pw.print("Proxy-Authorization: Basic "+Base64Coder.encodeString("dn120788bpn:deavew")+"\r\n");
	        pw.print("Accept-Language: ru, en\r\n");
	        pw.print("Accept-Charset: UTF-8\r\n");
	        pw.print("\r\n");
	        pw.print("\r\n");
	        pw.flush();
	        
	        br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	        
	        StringBuilder sb = new StringBuilder();
	        char[] buf = new char[10*1024];
	        int i =0;
	        while (br.read(buf) > 0) {
	            sb.append(buf);
	            if(i<2)
//	            	System.out.println(new String(buf.toString().getBytes(), "utf-8"));
	            	System.out.println(new String(buf.toString().getBytes()));
	            i++;
	            if(new String(buf).contains(MARK_END_PAGE)){
	                br.close();
	                break;
	            }
	        }
	//        System.out.println(new String(sb.toString().getBytes(), "UTF8"));
	    } catch (ConnectException ex) {
	        ex.printStackTrace();
	    } catch (UnknownHostException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	
	
	//Accept-Language: ru, en
	//Accept-Charset: windows-1251, KOI8-R
	}
}

//https://vkscanner.svn.sourceforge.net/svnroot/vkscanner
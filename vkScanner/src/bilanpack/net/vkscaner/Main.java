/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bilanpack.net.vkscaner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pavlyha
 */
public class Main {
    public static final String jeka = "id128174932";
    public static final String yula = "id10664661";
    public static final String pedan = "id75567015";
    public static final String bilan = "id8254758";
    public static final String maksimenko = "id10889993";
    public static final String kornejchuk = "andrey_korneychuk";
    public static final String sokolov = "id1444942";
    public static final String myzuka = "id9182900";

    public static final String COMAND_REFRESH_TIME = "-r";
    public static final String COMAND_LOG_PATH = "-log";
    public static final String COMAND_ID = "-id";
    public static final String COMAND_HELP = "-h";
    public static final String COMAND_PROXY = "-proxy";
//    public static final String COMAND_ = "";
//    public static final String COMAND_ = "";

    /** 
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        List<String> listId = new ArrayList<String>();
        String log = null;
        String proxy = null;
        int time = 0;
        int i = 0;
        while( i < args.length){
            if(args[i].equals(COMAND_HELP)){
                String msg ="Input arguments:\n\t"+COMAND_LOG_PATH+"path for log file\n\t"+COMAND_REFRESH_TIME+" seconds between updates\n\t"+COMAND_ID+" set of users id\n\t"+COMAND_HELP+" help";
                try {
                    System.out.println(new String(msg.getBytes(), "windows-1251"));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                return;
            }else
            if(args[i].equals(COMAND_REFRESH_TIME)){
                i++;
                time = Integer.valueOf(args[i]);
                i++;
                continue;
            }else
            if(args[i].equals(COMAND_LOG_PATH)){
                i++;
                log = args[i];
                i++;
                continue;
            }else
        	if(args[i].equals(COMAND_PROXY)){
                i++;
                proxy = args[i];
                i++;
                continue;
            }else
            if(args[i].equals(COMAND_ID)){
                i++;
                while( (i < args.length) && (args[i].charAt(0) != '-') ){
                    listId.add(args[i]);
                    i++;
                }
                continue;
            }else{
                System.out.println("Unknown argument: "+args[i]);
                return;
            }
        }

        for(String id: listId){
            new Thread(new VKScaner(id, log, time, proxy)).start();
        }
//
//        System.out.println("start\n");
//        VKScaner s = new VKScaner(kornejchuk);
//        new Thread(new VKScaner(kornejchuk)).start();

    }

}




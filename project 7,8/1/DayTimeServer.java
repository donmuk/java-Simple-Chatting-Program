import java.io.*;
import java.net.*;
import java.util.Date;
public class DayTimeServer
{
   public final static int daytimeport=13;
   public static void main(String args[]){
      ServerSocket theServer;
      
      Socket theSocket = null;

      try{
         theServer = new ServerSocket(daytimeport,5); 
 // 13번 포트에서 클라이언트의 접속 요청을 기다리는 서버소켓 객체를 생성한다.
         while(true) {
            try{           
               theSocket = theServer.accept();  // 클라이언트의 접속요청을
 // 기다리고 클라이언트의 소켓과 연결된 서버 측의 소켓(theSocket)을 생성한다.
               ServerThread thread = new ServerThread(theSocket);
               thread.start();
            }catch(IOException e){
               System.out.println(e);
            }
         }
      }catch(IOException e){
         System.out.println(e);
      }
   }
}
class ServerThread extends Thread
{
    BufferedReader reader;
    BufferedWriter writer;
    Socket theSocket=null;
    public ServerThread(Socket theSocket1) {
    	theSocket=theSocket1;
    }
    public void run() {
    	try {
    		OutputStream os;
			os = theSocket.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os));
			// 클라이언트에 데이터를 전송하는 BufferedWriter 객체를 생성한다.
			Date now = new Date(); // 날짜를 구한다.                
			writer.write(now.toString()+"\r\n"); // 날짜를 전송한다.
			writer.flush();
			InputStream in = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader (in));
			System.out.println(reader.readLine());
		} catch (IOException e) {
			System.out.println(e);
		}
    }
}

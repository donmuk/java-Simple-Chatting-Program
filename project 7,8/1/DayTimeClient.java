import java.io.*;
import java.net.*;
public class DayTimeClient
{
   public static void main(String args[]){
      Socket theSocket = null;
      String host;
      InputStream is;
      OutputStream out;
      BufferedReader reader;
      BufferedWriter writer;
      if(args.length>0){
         host=args[0]; // 원격 호스트를 입력받음
      }else{
         host="localhost"; // 로컬 호스트를 원격 호스트로 사용
      }
      try{
         theSocket = new Socket(host, 13); // daytime 서버에 접속한다.
         is = theSocket.getInputStream();
         out = theSocket.getOutputStream();
         reader = new BufferedReader(new InputStreamReader(is));
         writer = new BufferedWriter(new OutputStreamWriter(out));
         String theTime = reader.readLine(); // 날짜를 읽는다
         System.out.println("호스트의 시간은 "+theTime+"이다");
         writer.write("Thank You!\r\n");
         writer.flush();
         while(true)
        	 ;
      }catch(UnknownHostException e){
         System.err.println(args[0]+" 호스트를 찾을 수 없습니다.");
      }catch(IOException e){
         System.err.println(e);
      }
   }
}
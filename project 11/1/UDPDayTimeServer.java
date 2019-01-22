import java.io.*;
import java.net.*;
import java.util.Date;
public class UDPDayTimeServer extends Thread
{
   public final static int daytimeport=13;
   public final static int BUFFER_SIZE=1024;
   protected DatagramSocket ds;
   public UDPDayTimeServer() throws SocketException {
	   ds= new DatagramSocket(daytimeport);
   }
   public void run(){
	      byte[] buffer = new byte[BUFFER_SIZE];
	      while(true){
	         DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
	       // 수신용 데이터그램을 생성한다.
	         try{
	            ds.receive(incoming); // 데이터그램을 수신한다.
	            String recData = new String(incoming.getData(), 0, incoming.getLength());
	       // 수신한 바이트 데이터를 문자열로 변환한다.
	            System.out.println(recData);
	            Date now = new Date(); // 날짜를 구한다.
	            String snow = now.toString();
	            buffer = snow.getBytes();
	            DatagramPacket outgoing = new DatagramPacket(buffer, buffer.length, incoming.getAddress(), incoming.getPort());
	       // 송신용 데이터그램을 생성한다.
	            ds.send(outgoing); // 데이터그램을 보낸 호스트에 다시 전송한다.
	         }catch(IOException e){
	            System.out.println(e);
	         }
	      }
	   }
   public static void main(String args[]){
      try {
    	UDPDayTimeServer server = new UDPDayTimeServer();
    	server.start();
      }catch(SocketException se) {
    	  System.out.println(se);
      }
   }
}

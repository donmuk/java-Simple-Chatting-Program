import java.net.*;
import java.io.*;
public class UDPDaytimeClient
{
   public static final int PORT = 13;
   public static void main(String args[]){
      String hostname = "localhost";
      if(args.length > 0){
         hostname = args[0];
      }
      try{
         InetAddress ia = InetAddress.getByName(hostname);
         DatagramSocket theSocket = new DatagramSocket();
         Sender send = new Sender(ia, PORT, theSocket); 
      // 데이터를 송신하는 스레드 클래스로부터 객체를 생성한다.
         send.start(); // 데이터를 송신한다.
         Receiver receive = new Receiver(theSocket);
      // 데이터를 수신하는 스레드 클래스로부터 객체를 생성한다.
         receive.start(); // 데이터를 수신한다.
      }catch(UnknownHostException e){
         System.out.println(e);
      }catch(SocketException se){
         System.out.println(se);
      }
   }
}

class Sender extends Thread
{
   InetAddress server;
   int port ;
   DatagramSocket theSocket;
   public Sender(InetAddress ia, int port, DatagramSocket ds){
      server = ia;
      this.port = port;
      theSocket = ds;
   }
   public void run(){
      BufferedReader reader;
      String line;
      DatagramPacket packet;
      try{
         reader = new BufferedReader(new InputStreamReader(System.in));
         while(true){
            line = reader.readLine(); // 키보드로부터 데이터를 입력받는다.
            if(line.equals(".")) System.exit(0);
            else if(line.equals("time")) {
            	line = line.toString()+"\r\n";
            	byte[] data = line.getBytes("ASCII"); // 바이트 데이터로 변환한다.
            	packet = new DatagramPacket(data, data.length, server, port); 
  // server 및 port는 각각 상대방 호스트(서버)의 주소 및 포트번호이다.
            	theSocket.send(packet); // 데이터를 송신한다.
            	Thread.yield();
            }
         }
      }catch(IOException e){
         System.out.println(e);
      }
   }
}

class Receiver extends Thread
{
   DatagramSocket theSocket;
   protected DatagramPacket packet;
   public Receiver(DatagramSocket ds){
      theSocket = ds;
      byte[] buffer = new byte[65508];
      packet = new DatagramPacket(buffer, buffer.length);
   }
   public void run(){
      while(true){
         try{
            theSocket.receive(packet); // 데이터를 수신한다.
            String data = new String(packet.getData(), 0, packet.getLength());
            System.out.println(data);
            Thread.yield();
         }catch(IOException e){
            System.out.println(e);
         }
      }
   }
}
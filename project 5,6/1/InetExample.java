import java.io.*;
import java.net.*;
public class InetExample
{
   public static void main(String args[]){
      String hostname;
      BufferedReader br;
      printLocalAddress(); // 로컬 호스트의 이름 및 IP 주소 출력
      br = new BufferedReader(new InputStreamReader(System.in));
      try{
         do{
            System.out.println("호스트 이름 및 IP 주소를 입력하세요.");
            if((hostname = br.readLine())!= null)
               printRemoteAddress(hostname); // 원격 호스트의 주소 출력
         }while(hostname != null);
         System.out.println("프로그램을 종료합니다.");
      }catch(IOException ex){
         System.out.println("입력 에러!");
      }
   }
   static void printLocalAddress(){
      try{
         InetAddress myself = InetAddress.getLocalHost();
         System.out.println("로컬 호스트 이름 : "+myself.getCanonicalHostName());
         System.out.println("로컬 IP 주소 : "+InetAddress.getLoopbackAddress());
         System.out.println("로컬 호스트 class : "+ipClass(myself.getAddress()));
         System.out.println("로컬 호스트 InetAddress : "+myself.toString());
      }catch(UnknownHostException ex){
         System.out.println(ex);
      }
   }
   static void printRemoteAddress (String hostname){
      try{
         System.out.println("호스트를 찾고 있습니다. " + hostname +"....");
         InetAddress machine[] = InetAddress.getAllByName(hostname);
         for(int i=0;i<machine.length;i++) {
        	 System.out.println((i+1)+"번째 원격 호스트 이름 : "+machine[i].getCanonicalHostName());
         }
         System.out.println("원격 호스트 IP : "+machine[0].getHostAddress());
    	 System.out.println("원격 호스트 class : "+ipClass(machine[0].getAddress()));
    	 System.out.println("원격 호스트 InetAddress : "+machine[0].toString());
      }catch(UnknownHostException ex){
         System.out.println(ex);
      }
   }
   static char ipClass(byte[] ip){
      int highByte = 0xff & ip[0];
      return(highByte<128) ?  'A' :  (highByte<192) ?  'B' :  (highByte<224) ?   'C' : (highByte<240) ? 'D' : 'E';
   }
}
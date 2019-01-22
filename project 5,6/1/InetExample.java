import java.io.*;
import java.net.*;
public class InetExample
{
   public static void main(String args[]){
      String hostname;
      BufferedReader br;
      printLocalAddress(); // ���� ȣ��Ʈ�� �̸� �� IP �ּ� ���
      br = new BufferedReader(new InputStreamReader(System.in));
      try{
         do{
            System.out.println("ȣ��Ʈ �̸� �� IP �ּҸ� �Է��ϼ���.");
            if((hostname = br.readLine())!= null)
               printRemoteAddress(hostname); // ���� ȣ��Ʈ�� �ּ� ���
         }while(hostname != null);
         System.out.println("���α׷��� �����մϴ�.");
      }catch(IOException ex){
         System.out.println("�Է� ����!");
      }
   }
   static void printLocalAddress(){
      try{
         InetAddress myself = InetAddress.getLocalHost();
         System.out.println("���� ȣ��Ʈ �̸� : "+myself.getCanonicalHostName());
         System.out.println("���� IP �ּ� : "+InetAddress.getLoopbackAddress());
         System.out.println("���� ȣ��Ʈ class : "+ipClass(myself.getAddress()));
         System.out.println("���� ȣ��Ʈ InetAddress : "+myself.toString());
      }catch(UnknownHostException ex){
         System.out.println(ex);
      }
   }
   static void printRemoteAddress (String hostname){
      try{
         System.out.println("ȣ��Ʈ�� ã�� �ֽ��ϴ�. " + hostname +"....");
         InetAddress machine[] = InetAddress.getAllByName(hostname);
         for(int i=0;i<machine.length;i++) {
        	 System.out.println((i+1)+"��° ���� ȣ��Ʈ �̸� : "+machine[i].getCanonicalHostName());
         }
         System.out.println("���� ȣ��Ʈ IP : "+machine[0].getHostAddress());
    	 System.out.println("���� ȣ��Ʈ class : "+ipClass(machine[0].getAddress()));
    	 System.out.println("���� ȣ��Ʈ InetAddress : "+machine[0].toString());
      }catch(UnknownHostException ex){
         System.out.println(ex);
      }
   }
   static char ipClass(byte[] ip){
      int highByte = 0xff & ip[0];
      return(highByte<128) ?  'A' :  (highByte<192) ?  'B' :  (highByte<224) ?   'C' : (highByte<240) ? 'D' : 'E';
   }
}
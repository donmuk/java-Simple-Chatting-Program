import java.io.*;
public class ReadWriteAdd
{
   public static void main(String args[]){
      String buf;
      FileReader fin=null,fin2=null;
      FileWriter fout=null;
      if(args.length != 2){ // �μ��� �ҽ����ϸ� �� ������ϸ��� �Է��ؾ� �Ѵ�.
         System.out.println("�ҽ����� �� ��������� �����Ͻʽÿ�.");
         System.exit(1);
      }
      try{
         
    	  fin = new FileReader(args[0]); // �ҽ� ���ϰ� ����� �Է� ���� ��Ʈ��
    	  fin2 = new FileReader(args[1]); // ��� ���ϰ� ����� �Է� ���� ��Ʈ��
          fout = new FileWriter("test.txt");
         
      }catch(Exception e){
         System.out.println(e);
         System.exit(1);
      }
      LineNumberReader read = new LineNumberReader(fin);
      LineNumberReader read2 = new LineNumberReader(fin2);
      PrintWriter write = new PrintWriter(fout); // �⺻ fout ��½�Ʈ���� ����
      while(true){
         try{
            buf=read.readLine(); // �� ���� �����͸� �д´�.
            if(buf==null) {
            	break;
            }
         }catch(IOException e){
            System.out.println(e);
            break;
         }
         write.println(buf); // ������ ������ ���Ͽ� ����Ѵ�.
      }
      while(true){
          try{
             buf=read2.readLine(); // �� ���� �����͸� �д´�.
             if(buf==null) {
             	break;
             }
          }catch(IOException e){
             System.out.println(e);
             break;
          }
          write.println(buf); // ������ ������ ���Ͽ� ����Ѵ�.
       }
      try{
         fin.close();
         fout.close();
      }catch(IOException e){
         System.out.println(e);
      }
   }
}
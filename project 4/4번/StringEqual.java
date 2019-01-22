import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;
public class StringEqual
{
   public static void main(String args[]){
	   boolean flag=true;
      String buf,buf2;
      FileReader fin=null;
      FileReader fin2=null;
      File file1=null;
      File file2=null;
      if(args.length != 2){ // �μ��� �ҽ����ϸ� �� ������ϸ��� �Է��ؾ� �Ѵ�.
         System.out.println("�ҽ����� �� ��������� �����Ͻʽÿ�.");
         System.exit(1);
      }
      try{
         fin = new FileReader(args[0]); // �ҽ� ���ϰ� ����� �Է� ���� ��Ʈ��
         fin2 = new FileReader(args[1]);
      }catch(Exception e){
         System.out.println(e);
         System.exit(1);
      }
      LineNumberReader read = new LineNumberReader(fin);
      LineNumberReader read2 = new LineNumberReader(fin2);
      while(true){
         try{
            buf=read.readLine(); // �� ���� �����͸� �д´�.
            buf2=read2.readLine();
            if(buf==null&&buf2==null)
            	break;
         }catch(IOException e){
            System.out.println(e);
            break;
         }
         if(buf.equals(buf2)==false) {
        	 flag=false;
        	 break;
         }
      }
      try{
          fin.close();
          fin2.close();
       }catch(IOException e){
          System.out.println(e);
       }
      file1= new File(args[0]);
      file2= new File(args[1]);
      if(flag==true) {
    	  Long lastModified = file1.lastModified();
          Date date = new Date(lastModified);
          System.out.println(args[0]+" ���� ���� �ð� : "+date);
          lastModified=file2.lastModified();
          date = new Date(lastModified);
          System.out.println(args[1]+" ���� ���� �ð� : "+date);
      }
      else {
    	  if (file1.exists()) {
    	      long L = file1.length();
    	      System.out.println(args[0]+" ������ ���� : "+L + " bytes ");
    	    }
    	  else System.err.println(args[0]+"������ �����ϴ�.");
    	  if (file2.exists()) {
    	      long L = file2.length();
    	      System.out.println(args[1]+" ������ ���� : "+L + " bytes ");
    	    }
    	  else System.err.println(args[1]+"������ �����ϴ�.");
      }
      
   }
}
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
public class AttachLineNumberWrite
{
   public static void main(String args[]){
      String buf;
      FileReader fin=null;
      FileWriter fout=null;
      if(args.length != 2){ // 인수로 소스파일명 및 대상파일명을 입력해야 한다.
         System.out.println("소스파일 및 대상파일을 지정하십시오.");
         System.exit(1);
      }
      try{
         fin = new FileReader(args[0]); // 소스 파일과 연결된 입력 파일 스트림
         fout = new FileWriter(args[1]); // 대상 파일과 연결된 입력 파일 스트림
      }catch(Exception e){
         System.out.println(e);
         System.exit(1);
      }
      LineNumberReader read = new LineNumberReader(fin);
      PrintWriter write = new PrintWriter(fout); // 기본 fout 출력스트림에 연결
      int num=1;
      while(true){
         try{
            buf=read.readLine(); // 한 줄의 데이터를 읽는다.
            if(buf==null) break;
         }catch(IOException e){
            System.out.println(e);
            break;
         }
         buf = num + " : " + buf; // [번호 : 프로그램 내용] 형식으로 수정
         write.println(buf); // 수정된 내용을 파일에 출력한다.
         num++;
      }
      try{
         fin.close();
         fout.close();
      }catch(IOException e){
         System.out.println(e);
      }
   }
}
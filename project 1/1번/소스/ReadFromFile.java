
import java.io.*;
public class ReadFromFile
{
		public static void main(String args[]){
			int bytesRead;
			byte[] buffer = new byte[256];
			FileInputStream fin = null;
			for(int i=0;i<args.length;i++){
				try{
					fin = new FileInputStream(args[i]);
					while((bytesRead = fin.read(buffer)) >= 0){
						System.out.println("file name : "+args[i]);
						System.out.write(buffer, 0, bytesRead);
						System.out.println('\n');
					}
				}catch(IOException e){
					System.err.println("��Ʈ���Ƿκ��� �����͸� ���� �� �����ϴ�.");
				}finally{
					try{
						if(fin!=null) fin.close();
					}catch(IOException e){}
				}
			}
		}
}

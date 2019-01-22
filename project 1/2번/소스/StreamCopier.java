
import java.io.*;
public class StreamCopier
{
	public static void main(String args[]){
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try{
			fin = new FileInputStream(args[0]);
			fout = new FileOutputStream(args[1]);
			copy(fin, fout);
		}catch(IOException e){
			System.err.println("스트림으로부터 데이터를 읽을 수 없습니다.");
		}finally{
			try{
				if(fin!=null) fin.close();
				if(fout!=null) fout.close();
			}catch(IOException e){}
		}
	}
	public static void copy(InputStream in, OutputStream out) throws IOException{
		int bytesRead;
		byte[] buffer = new byte[256];
		synchronized(in){
			synchronized(out){
				while((bytesRead = in.read(buffer))>=0){
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}
}

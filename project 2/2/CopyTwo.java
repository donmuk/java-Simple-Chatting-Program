import java.io.*;
public class CopyTwo extends FileOutputStream{
	
	public CopyTwo(String args1,String args2,ByteArrayOutputStream buf) throws FileNotFoundException  {
		super(args1);
		// TODO Auto-generated constructor stub
		try {
			FileOutputStream fout1 = new FileOutputStream(args1); //������ ����
			FileOutputStream fout2 = new FileOutputStream(args2); //������ ����
			FilterOutputStream filterout1 = new FilterOutputStream(fout1);
			FilterOutputStream filterout2 = new FilterOutputStream(fout2);
			filterout1.write(buf.toByteArray()); //���ش�.
			filterout2.write(buf.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		try {
			FileInputStream fin = new FileInputStream(args[0]); //�о�� ������ �ִ� ����
			ByteArrayOutputStream buf = new ByteArrayOutputStream(); 
			copy(fin,buf); //����
			CopyTwo two = new CopyTwo(args[1],args[2],buf); //FileOutputStream�� ����� CopyTwo ����
		}catch(IOException e){
			System.err.println(e);
		}
	
	}
	public static void copy(InputStream in, OutputStream buffer) throws IOException{ //2.7���� �̿�
		synchronized(in) {
			synchronized(buffer) {
				BufferedInputStream bin = new BufferedInputStream(in);
				BufferedOutputStream bout = new BufferedOutputStream(buffer);
				while(true) {
					int data = bin.read();
					if(data==-1)break;
					bout.write(data);
				}
				bout.flush();
			}
		}
	}
}
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class WriteReadFromFileEvent extends Frame implements ActionListener {
	Label lfile,lfile2,ll;
	TextField tfile,tfile2;
	TextArea tadata;
	String filename,filename2;
	Button submit1,submit2;
	boolean check=false;
	public WriteReadFromFileEvent(String str) {
		super(str);
		setLayout(new FlowLayout());
		lfile = new Label("�Է�����");
		add(lfile);
		tfile = new TextField(20);
		tfile.addActionListener(this);
		submit1 = new Button("Ȯ��");
		submit1.addActionListener(this);
		add(tfile);
		add(submit1);
		lfile2 = new Label("�������");
		add(lfile2);
		tfile2 = new TextField(20);
		tfile2.addActionListener(this);
		submit2 = new Button("Ȯ��");
		submit2.addActionListener(this);
		add(tfile2);
		add(submit2);
		ll = new Label("���ϳ���");
		add(ll);
		tadata = new TextArea(11,42);
		add(tadata);
		addWindowListener(new WinListener());
	}
	public static void main(String args[]) {
		WriteReadFromFileEvent text = new WriteReadFromFileEvent("�����б�");
		text.setSize(370,300);
		text.show();
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
	public void actionPerformed(ActionEvent ae){
		if(!check) {
			filename=tfile.getText();
			try {
				FileInputStream fin = new FileInputStream(filename);
				tadata.setText(filename+" load ok\n");
				check=true;
				if(fin!=null) fin.close();
			}catch(IOException e) {
				System.out.println(e.toString());
			}
		}else {
			byte buffer[] = new byte[100];
			filename2=tfile2.getText();
			try {
				FileInputStream fin = new FileInputStream(filename);
				FileOutputStream fout = new FileOutputStream(filename2);
				copy(fin, fout);
				FileInputStream fin2 = new FileInputStream(filename2);
				fin2.read(buffer);
				String data = new String(buffer);
				tadata.setText(data+"\n");
				if(fin!=null) fin.close();
				if(fin2!=null) fin2.close();
				if(fout!=null) fout.close();
			}catch(IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	
	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
	
}

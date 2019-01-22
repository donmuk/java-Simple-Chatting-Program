
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUIAttachLineNumber extends Frame implements ActionListener{
	   private TextField filename;
	   private Button Bcopy, Bprint; 
	   private TextArea output;
	   public GUIAttachLineNumber() {
		      super( "번호추가");
		      setSize(800, 800);
		      setLayout( new BorderLayout());
		      Panel inputpanel = new Panel();
		      inputpanel.setLayout(new BorderLayout());
		      inputpanel.add("North",new Label("파일이름 입력 : "));
		      filename = new TextField( 20 );
		      inputpanel.add("Center",filename);
		      Bcopy = new Button("번호추가"); 
		      Bcopy.addActionListener(this);
		      inputpanel.add("South",Bcopy);
		      Panel panel = new Panel();
		      panel.setLayout(new BorderLayout());
		      Bprint = new Button("출력"); 
		      Bprint.addActionListener(this);
		      panel.add("North",Bprint);       
		      output = new TextArea("",24,40);
		      panel.add("Center",output);
		      add("North",inputpanel);
		      add("Center",panel);
		      addWindowListener(new WinListener());
		      setVisible(true);  
	   }
	   public static void main(String args[]){
		  new GUIAttachLineNumber();
	   }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="번호추가") {
			Addnumber();
		}else if(e.getActionCommand()=="출력"){
			Printnum();
		}
	}
	public void Addnumber() {
	      String buf;
	      FileReader fin=null;
	      FileWriter fout=null;
	      try{
	         fin = new FileReader(filename.getText()); // 소스 파일과 연결된 입력 파일 스트림
	         fout = new FileWriter("numbered_"+filename.getText()); // 대상 파일과 연결된 입력 파일 스트림
	      }catch(Exception e){
	         output.setText("정확한 파일이름을 입력해주세요..");
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
	      output.setText("numbered_"+filename.getText()+" 파일생성");
	}
	public void Printnum() {
		output.setText("");
		String buf;
		FileReader fin=null;
		
		try{
	         fin = new FileReader("numbered_"+filename.getText()); // 소스 파일과 연결된 입력 파일 스트림
		}catch(Exception e1){
			output.setText("numbered_"+filename.getText()+" 파일이 존재하지 않습니다.");
	    }
		LineNumberReader read = new LineNumberReader(fin);
		while(true){
	         try{
	            buf=read.readLine(); // 한 줄의 데이터를 읽는다.
	            if(buf==null) break;
	         }catch(IOException e2){
	            System.out.println(e2);
	            break;
	         }
	         output.append(buf);
	         output.append("\n");
		}
		try{
	         fin.close();
	      }catch(IOException e2){
	         System.out.println(e2);
	      }
	}
	class WinListener extends WindowAdapter{
		public void windowClosing(WindowEvent we){
			System.exit(0);
		}
	}
}

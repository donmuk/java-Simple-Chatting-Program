import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
public class GetHostInfor extends Frame implements ActionListener
{
   TextField hostname; // 호스트 이름을 입력받는 필드
   Button getinfor; // 입력된 호스트에 관한 IP 정보를 읽는 버튼
   TextArea display,display2; // 구해진 IP에 관한 정보를 출력하는 필드
   public static void main(String args[]) {
      GetHostInfor host = new GetHostInfor("InetAddress 클래스");
      host.setVisible(true);
   }
   public GetHostInfor(String str){
      super(str);
      addWindowListener(new WinListener());
      setLayout(new BorderLayout());
      Panel inputpanel = new Panel(); // 첫 번째 패널
      inputpanel.setLayout(new BorderLayout());
      inputpanel.add("North", new Label("호스트 이름:"));
      hostname = new TextField("", 30);
      getinfor = new Button("호스트 정보 얻기");
      inputpanel.add("Center", hostname);
      inputpanel.add("South", getinfor);
      getinfor.addActionListener(this); // 이벤트 등록
      add("North", inputpanel); // 패널을 프레임에 부착
      Panel outputpanel = new Panel(); // 두 번째 패널
      outputpanel.setLayout(new BorderLayout());
      display = new TextArea("", 24, 40);
      display.setEditable(false);
     
      outputpanel.add("North", new Label("인터넷 주소"));
      outputpanel.add("Center", display);
      add("Center", outputpanel);
      Panel outputpanel2 = new Panel(); // 세 번째 패널
      outputpanel2.setLayout(new BorderLayout());
      display2 = new TextArea("", 24, 40);
      display2.setEditable(false);
      outputpanel2.add("North", new Label("IP주소 클래스 유형"));
      outputpanel2.add("Center", display2);
      add("South", outputpanel2);
      setSize(400, 800);
   }
   public void actionPerformed(ActionEvent e ) {
	  display.setText("");
	  display2.setText("");
      String name = hostname.getText(); // 입력된 호스트 이름을 구한다.
      
      try{
         InetAddress inet[] = InetAddress.getAllByName(name); // InetAddress 객체생성
         String ip=null;
         for(int i=0;i<inet.length;i++) {
        	 ip = inet[i].getCanonicalHostName()+"\n"; // 호스트의 이름을 구한다.
        	 display.append(ip);
         }
         ip = ipClass(inet[0].getAddress())+"\n";
         display2.append(ip);
      }catch(UnknownHostException ue){
         String ip = name+": 해당 호스트가 없습니다.\n";
         display.append(ip);
         display2.append(ip);
      }
      
   }
   static char ipClass(byte[] ip){
	      int highByte = 0xff & ip[0];
	      return(highByte<128) ?  'A' :  (highByte<192) ?  'B' :  (highByte<224) ?   'C' : (highByte<240) ? 'D' : 'E';
   }
class WinListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent we){
         System.exit(0);
      }
   }
}
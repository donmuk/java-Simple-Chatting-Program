import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
public class GetHostInfor extends Frame implements ActionListener
{
   TextField hostname; // ȣ��Ʈ �̸��� �Է¹޴� �ʵ�
   Button getinfor; // �Էµ� ȣ��Ʈ�� ���� IP ������ �д� ��ư
   TextArea display,display2; // ������ IP�� ���� ������ ����ϴ� �ʵ�
   public static void main(String args[]) {
      GetHostInfor host = new GetHostInfor("InetAddress Ŭ����");
      host.setVisible(true);
   }
   public GetHostInfor(String str){
      super(str);
      addWindowListener(new WinListener());
      setLayout(new BorderLayout());
      Panel inputpanel = new Panel(); // ù ��° �г�
      inputpanel.setLayout(new BorderLayout());
      inputpanel.add("North", new Label("ȣ��Ʈ �̸�:"));
      hostname = new TextField("", 30);
      getinfor = new Button("ȣ��Ʈ ���� ���");
      inputpanel.add("Center", hostname);
      inputpanel.add("South", getinfor);
      getinfor.addActionListener(this); // �̺�Ʈ ���
      add("North", inputpanel); // �г��� �����ӿ� ����
      Panel outputpanel = new Panel(); // �� ��° �г�
      outputpanel.setLayout(new BorderLayout());
      display = new TextArea("", 24, 40);
      display.setEditable(false);
     
      outputpanel.add("North", new Label("���ͳ� �ּ�"));
      outputpanel.add("Center", display);
      add("Center", outputpanel);
      Panel outputpanel2 = new Panel(); // �� ��° �г�
      outputpanel2.setLayout(new BorderLayout());
      display2 = new TextArea("", 24, 40);
      display2.setEditable(false);
      outputpanel2.add("North", new Label("IP�ּ� Ŭ���� ����"));
      outputpanel2.add("Center", display2);
      add("South", outputpanel2);
      setSize(400, 800);
   }
   public void actionPerformed(ActionEvent e ) {
	  display.setText("");
	  display2.setText("");
      String name = hostname.getText(); // �Էµ� ȣ��Ʈ �̸��� ���Ѵ�.
      
      try{
         InetAddress inet[] = InetAddress.getAllByName(name); // InetAddress ��ü����
         String ip=null;
         for(int i=0;i<inet.length;i++) {
        	 ip = inet[i].getCanonicalHostName()+"\n"; // ȣ��Ʈ�� �̸��� ���Ѵ�.
        	 display.append(ip);
         }
         ip = ipClass(inet[0].getAddress())+"\n";
         display2.append(ip);
      }catch(UnknownHostException ue){
         String ip = name+": �ش� ȣ��Ʈ�� �����ϴ�.\n";
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
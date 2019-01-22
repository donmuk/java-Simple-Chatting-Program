import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UDPCalculatorClient extends Frame implements ActionListener{
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	private JFrame frame;
    private JPanel panel;
    private JTextArea text;
    private JButton B[], BAdd, BMinus, BMultiply, BDivide,BEqual, BCancel;
    private final String[] BValue = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	public  UDPCalculatorClient(){
		super("클라이언트");
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		 frame = new JFrame("Calculator");
		 frame.setResizable(true);
	     panel = new JPanel(new FlowLayout());

	     text = new JTextArea(2, 15);
	     B = new JButton[10];
	     for (int i = 0; i < 10; i++) {
	    	 B[i]= new JButton(String.valueOf(i));
	     }
	     BAdd = new JButton("+");
	     BMinus = new JButton("-");
	     BMultiply = new JButton("*");
	     BDivide = new JButton("/");
	     BEqual = new JButton("=");
	     BCancel = new JButton("C");
	     try {
	         socket = new DatagramSocket(4000); // 클라이언트가 사용하는 포트번호(4000)
	      }catch( SocketException se ) {
	         se.printStackTrace();
	         System.exit( 1 );
	      }
	}
	 public void waitForPackets(){
	      while ( true ) {
	    	  try { // 수신용 패킷을 만든다.
	              byte data[] = new byte[ 20 ];
	              receivePacket =  new DatagramPacket( data, data.length );
	              socket.receive( receivePacket ); // 패킷의 수신을 기다린다.
	              text.setText(new String( receivePacket.getData()));
	           }catch( IOException io ) {
	              io.printStackTrace();
	           }
	      }
	   }
	public void init() { //계산가 Gui 생성
		frame.setVisible(true);
        frame.setSize(200, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.add(text);
       
        for (int i = 1; i < 10; i++) {
            panel.add(B[i]);
            B[i].addActionListener(this);
        }
        panel.add(B[0]);
        B[0].addActionListener(this);
        
        panel.add(BAdd);
        panel.add(BMinus);
        panel.add(BMultiply);
        panel.add(BDivide);
        panel.add(BEqual);
        panel.add(BCancel);

        BAdd.addActionListener(this);
        BMinus.addActionListener(this);
        BMultiply.addActionListener(this);
        BDivide.addActionListener(this);
        BEqual.addActionListener(this);
        BCancel.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			 String s = e.getActionCommand();
			if(s.compareTo("+")==0||s.compareTo("-")==0||s.compareTo("*")==0||s.compareTo("/")==0) {
				text.setText("");
		        byte data1[] = s.getBytes();
		        sendPacket=new DatagramPacket(data1, data1.length, InetAddress.getLocalHost(), 5000);
		        socket.send( sendPacket );
			}else if(s.compareTo("=")==0){
				byte data[] = s.getBytes(); // 문자열을 바이트 배열로 변환한다.
		        sendPacket=new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000);
		        socket.send( sendPacket );
			}else if(s.compareTo("C")==0){
				byte data[] = s.getBytes();
				sendPacket=new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000);
		        socket.send( sendPacket );
		        text.setText("");
			}else {
				text.append(s);
				byte data1[] = s.getBytes();
		        sendPacket=new DatagramPacket(data1, data1.length, InetAddress.getLocalHost(), 5000);
		        socket.send( sendPacket );
			}
	      }catch ( IOException exception ) {
	         exception.printStackTrace();
	      }
	}
	public static void main(String[] args) {
		UDPCalculatorClient client = new UDPCalculatorClient();
		client.init();
		client.waitForPackets();
	}
}
/*
 *  메인이 되는 클레스 Calu 서브 클레스 Calculator
 *  간단한 + - / * 연산을 할수있다.
 *  작동법은 피연산자 연산자 피연산자 = 조합으로 가능
 *  피연산자 클릭후 연산자 클릭시 텍스트 Area가 초기화되고  Calculator클레스에있는 calculateBi메소드로 인자를 넘겨준다.
 *  그후 피연산자 클릭후 = 클릭시 계산하는 시스템
 *  reader 메소드를 통하여 실수형으로 출력해준다.
 */



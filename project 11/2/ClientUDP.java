import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
public class ClientUDP extends Frame implements ActionListener
{
   private TextField enter;
   private TextArea display;
   private DatagramPacket sendPacket, receivePacket;
   private DatagramSocket socket;
   public ClientUDP(){
      super( "클라이언트" );
      enter = new TextField( "메시지를 입력하세요" );
      enter.addActionListener( this ); // 입력된 데이터를 전송하기 위한 이벤트
      add( enter, BorderLayout.NORTH );
      display = new TextArea();
      add( display, BorderLayout.CENTER );
      addWindowListener(new WinListener());
      setSize( 400, 300 );
      setVisible( true );
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
              byte data[] = new byte[ 100 ];
              receivePacket =  new DatagramPacket( data, data.length );
              socket.receive( receivePacket ); // 패킷의 수신을 기다린다.
              display.append( "\n클라이언트의 메시지 : " + new String( receivePacket.getData() ) );
              // 수신받은 패킷을 다시 클라이언트에 전송한다.
           }catch( IOException io ) {
              display.append( io.toString() + "\n" );
              io.printStackTrace();
           }
      }
   }
   public void actionPerformed( ActionEvent e ){
      try {
         display.append( "\n송신 메시지: " + e.getActionCommand() + "\n" );
         String s = e.getActionCommand(); // 서버에 보낼 데이터를 구한다.(텍스트필드)
         byte data[] = s.getBytes(); // 문자열을 바이트 배열로 변환한다.
 sendPacket=new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000);
         socket.send( sendPacket );
         display.append( "패킷 전송 완료\n" );
         enter.setText("");
      }catch ( IOException exception ) {
         display.append( exception.toString() + "\n" );
         exception.printStackTrace();
      }
   }
   public static void main( String args[] )
   {
      ClientUDP c = new ClientUDP();
      c.waitForPackets();
   }
   class WinListener extends WindowAdapter{
      public void windowClosing(WindowEvent e){
         System.exit(0);
      }
   }
}
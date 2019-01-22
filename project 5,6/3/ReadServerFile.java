import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.net.*;

import javax.sound.midi.Track;

import org.w3c.dom.html.HTMLImageElement;

import java.io.*;
public class ReadServerFile extends Frame implements ActionListener
{
   private TextField enter;
   private TextArea contents,contents2;
   public ReadServerFile(){
      super("ȣ��Ʈ ���� �б�");
      setLayout( new BorderLayout() );
      enter = new TextField( "URL�� �Է��ϼ���!" );
      enter.addActionListener( this );
      add( enter, BorderLayout.NORTH );
      contents2=new TextArea("",0,0);
      add( contents2, BorderLayout.CENTER );
      contents=new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
      add( contents, BorderLayout.SOUTH );
      addWindowListener(new WinListener());
      setSize(500, 500);
      setVisible(true);
   }
   public void actionPerformed( ActionEvent e ) {
      URL url;
      InputStream is;
      BufferedReader input;
      String line;
      StringBuffer buffer = new StringBuffer();
      String location = e.getActionCommand(); // �ؽ�Ʈ �ʵ忡 �Էµ� URL�� ����
      try {
    	  contents2.setText("");
    	  contents.setText("");
         url = new URL( location );
         URLConnection connection = url.openConnection();
         connection.connect();
         contents2.append("url�� URL�� "+url+"\n");
         contents2.append(url+"�� ���������� "+url.getProtocol()+"\n");
         contents2.append(url+"�� ȣ��Ʈ�� "+url.getHost()+"\n");
         contents2.append(url+"�� ��Ʈ��ȣ�� "+url.getPort()+"\n");
         contents2.append(url+"�� �����̸��� "+url.getFile()+"\n");
         contents2.append(url+"�� ���� �� ������ġ�� "+url.getRef()+"\n");
         Object o = url.getContent();
         if(connection.getContentType().contains("video")) {
        	 contents.setText("���� url�Դϴ�.");
         }else if(connection.getContentType().contains("image")) {
        	 contents.setText("�̹��� url�Դϴ�.");       	 
         }else if (connection.getContentType().contains("audio")){
        	 contents.setText("����� url�Դϴ�."); 
         }else if(o instanceof InputStream){
        	 is = (InputStream) o;// location(ȣ��Ʈ)�� �����Ű�� InputStream ��ü����
        	 input = new BufferedReader(new InputStreamReader(is));
        	 contents.setText( "������ �д� ���Դϴ�...." );
        	 while ( ( line = input.readLine() ) != null ) // ����(��������)�� �д´�.
                 buffer.append( line ).append( '\n' );
              contents.setText( buffer.toString() ); // ���� ������ �ؽ�Ʈ ����� ���
              input.close();
         }
      }catch(MalformedURLException mal) {
         contents.setText("URL ������ �߸��Ǿ����ϴ�.");
      }catch ( IOException io ) {
         contents.setText( io.toString() );
      }catch ( Exception ex ) {
         contents.setText( "ȣ��Ʈ ��ǻ���� ���ϸ��� �� �� �ֽ��ϴ�." );
      }
   }
   public static void main(String args[]){
      ReadServerFile read = new ReadServerFile();
   }
   class WinListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent we){
         System.exit(0);
      }
   }
}
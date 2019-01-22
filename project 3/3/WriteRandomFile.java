import java.io.*;
import java.awt.*;
import java.awt.event.*;
public class WriteRandomFile extends Frame implements ActionListener
{
   private TextField accountField, nameField, balanceField;
   private Button enter, done, out;
   private RandomAccessFile output;
   private Record data;
   public WriteRandomFile(){
      super( "파일쓰기" );
      data = new Record();
      try {
         output = new RandomAccessFile( "customer.txt", "rw" );
      } catch ( IOException e ) {
         System.err.println( e.toString() );
         System.exit( 1 );
      }
      setSize( 300, 150 );
      setLayout( new GridLayout( 4, 3 ) );
      add( new Label( "구좌번호" ) );
      accountField = new TextField();
      add( accountField );
      add( new Label());
      add( new Label( "이름" ) );
      nameField = new TextField( 20 );
      add( nameField );
      add( new Label());
      add( new Label( "잔고" ) );
      balanceField = new TextField( 20 );
      add( balanceField );
      add( new Label());
      enter = new Button( "입력" );
      enter.addActionListener( this );
      add( enter );
      out = new Button( "출력" );
      out.addActionListener( this );
      add( out ); 
      done = new Button( "종료" );
      done.addActionListener( this );
      add( done );       
      setVisible( true );  
   }
   public void addRecord(){
      int accountNo = 0;
      Double d;
      if ( ! accountField.getText().equals( "" ) ) {
         try {
            accountNo = Integer.parseInt( accountField.getText() );
            if ( accountNo > 0 && accountNo <= 1000 ) {
               data.setAccount( accountNo );
               data.setName( nameField.getText() );
               d = new Double ( balanceField.getText() );
               data.setBalance( d.doubleValue() );
               output.seek((long) ( accountNo-1 ) * Record.size() );
               data.write( output );
            }
            // 텍스트 필드의 내용을 지운다.
            accountField.setText( "" );
            nameField.setText( "" );
            balanceField.setText( "" );
         } catch ( NumberFormatException nfe ) {
            System.err.println("숫자를 입력하세요" );
         } catch ( IOException io ) {
            System.err.println("파일쓰기 에러\n" + io.toString() );
            System.exit( 1 );
         }
      }
   }
   public void getRecord() {
	   int accountNo = 0;
	      if ( ! accountField.getText().equals( "" ) ) {
	            accountNo = Integer.parseInt( accountField.getText() );
	            try {
					output.seek(0);
					while(output.getFilePointer()<output.length()) {
		            	if(accountNo==output.readInt()) {
		            		output.seek(output.getFilePointer()-4);
		            		data.read(output);
		    	            nameField.setText( data.getName() );
		    	            balanceField.setText(Double.toString(data.getBalance()));
		    	            break;
		            	}else {
		            		output.seek(output.getFilePointer()-4);
		            		data.read(output);
		            	}
		            }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	      }
	 
   }
   public void actionPerformed( ActionEvent e ){
	   // 입력된 데이터를 저장한다.
	  if (e.getSource()== enter ) {
		  addRecord();
	  }else if ( e.getSource() == done ) {
         try {
            output.close();
         } catch ( IOException io ) {
            System.err.println( "파일 닫기 에러\n" + io.toString() );
         }
         System.exit( 0 );
      }else if( e.getSource() == out ) {
    			  getRecord();
      }
   }
   public static void main( String args[] ) {
      new WriteRandomFile();
   }
}

class Record
{
   private int account;
   private String name;
   private double balance;
   // RandomAccessFile로부터 한 레코드를 읽는다.
   public void read(RandomAccessFile file) throws IOException {
      account = file.readInt(); // file로부터 구좌번호를 읽는다.
      char namearray[] = new char[15];
      for(int i = 0; i < namearray.length; i++ )
         namearray[i] = file.readChar();
      name = new String(namearray);
      balance = file.readDouble();
   }
   // RandomAccessFile에 한 레코드를 저장한다.
   public void write(RandomAccessFile file) throws IOException {
      StringBuffer buf;
      file.writeInt( account ); // file에 구좌번호를 저장한다.
      if (name != null) 
         buf = new StringBuffer(name);
      else 
         buf = new StringBuffer(15);
      buf.setLength(15); // 이름을 저장하는 메모리 크기를 15로 설정
      file.writeChars(buf.toString());
      file.writeDouble( balance );
   }
   public void setAccount(int a) { account = a; } // 구좌번호를 설정한다.
   public int getAccount() { return account; } // 구좌번호를 반환한다.
   public void setName(String f) { name = f; } 
   public String getName() { return name; }
   public void setBalance(double b) { balance = b; }
   public double getBalance() { return balance; }
   public static int size() { return 42; } // 한 레코드의 길이
}

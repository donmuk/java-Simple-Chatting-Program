import java.io.*;
import java.net.*;
import java.util.Date;

public class DicServer {
	public static void main(String args[]) {
		ServerSocket theServer;
		Socket theSocket = null;
		try {
			theServer = new ServerSocket(7);
			// 7번 포트에서 클라이언트의 접속 요청을 기다리는 서버소켓 객체를 생성한다.
			while (true) {
				theSocket = theServer.accept(); // 클라이언트의 접속요청을
				ServerThread thread = new ServerThread(theSocket);
				thread.start();
			}
			// 기다리고 클라이언트의 소켓과 연결된 서버 측의 소켓(theSocket)을 생성한다.
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}

class ServerThread extends Thread {
	BufferedReader reader;
	BufferedWriter writer;
	Socket theSocket = null;

	public ServerThread(Socket theSocket1) {
		theSocket = theSocket1;
	}

	public void run() {
		InputStream is;
		BufferedReader reader;
		OutputStream os;
		BufferedWriter writer;
		String theLine;
		String rLine;
		try {
			is = theSocket.getInputStream();
			// 클라이언트가 전송한 데이터를 읽을 InputStream 객체를 생성한다.
			reader = new BufferedReader(new InputStreamReader(is));
			// 클라이언트에 전송한 데이터를 읽을 BufferedReader 객체를 생성한다.
			os = theSocket.getOutputStream();
			// 클라이언트에 데이터를 전송할 OutputStream 객체를 생성한다.
			writer = new BufferedWriter(new OutputStreamWriter(os));
			// 클라이언트에 데이터를 전송하는 BufferedWriter 객체를 생성한다.
			while ((theLine = reader.readLine()) != null) { // 클라이언트의 데이터를 수신
				FileReader fr = new FileReader("dictionary.txt");
				BufferedReader br = new BufferedReader(fr);
				boolean check = false;
				while ((rLine = br.readLine()) != null) {
					if (theLine.compareTo(rLine) == 0) {
						rLine = br.readLine();
						System.out.println(rLine); // 받은 데이터를 화면에 출력한다.
						writer.write(rLine + '\r' + '\n'); // 클라이언트에 데이터를 재전송
						writer.flush(); // 클라이언트에 데이터를 재전송
						check = true;
						break;
					}
				}
				br.close();
				if (check == false) {
					System.out.println("사전에 없는 단어입니다.");
					writer.write("사전에 없는 단어입니다." + '\r' + '\n'); // 클라이언트에 데이터를 재전송
					writer.flush(); // 클라이언트에 데이터를 재전송
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPCalculatorServer {
	public enum BiOperatorModes { // enum을 통해 연산자를 구별
		normal, add, minus, multiply, divide
	}

	private Double num1, num2;
	private BiOperatorModes mode = BiOperatorModes.normal;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	private String Operate="";
	public UDPCalculatorServer() {
		try {
			socket = new DatagramSocket(5000); // 서버에서 사용되는 포트번호(5000)
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void waitForPackets() {
		while (true) {
			try { // 수신용 패킷을 만든다.
				byte data[] = new byte[20];
				receivePacket = new DatagramPacket(data, data.length);
				socket.receive(receivePacket); // 패킷의 수신을 기다린다.
				String str = new String(receivePacket.getData());
				System.out.print(str.charAt(0));
				if(str.charAt(0)=='+') {
					calculateBi(mode,Double.parseDouble(Operate));
					Operate = "";
					mode = BiOperatorModes.add;
				}else if(str.charAt(0)=='-') {
					calculateBi(mode,Double.parseDouble(Operate));
					Operate = "";
					mode = BiOperatorModes.minus;
				}else if(str.charAt(0)=='*') {
					calculateBi(mode,Double.parseDouble(Operate));
					Operate = "";
					mode = BiOperatorModes.multiply;
				}
				else if(str.charAt(0)=='/') {
					calculateBi(mode,Double.parseDouble(Operate));
					Operate = "";
					mode = BiOperatorModes.divide;
				}else if(str.charAt(0)=='C') {
					reset();
					
				}
				else if(str.charAt(0)=='=') {
					 byte data1[] = calculateBi(mode,Double.parseDouble(Operate)).toString().getBytes(); // 문자열을 바이트 배열로 변환한다.
			         sendPacket=new DatagramPacket(data1, data1.length, InetAddress.getLocalHost(), 4000);
			         socket.send( sendPacket );
			         Operate = "";
			         System.out.println("결과값 전송");
				}else {
					Operate+= str.charAt(0);
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	private Double calculateBiImpl() {
		if (mode == BiOperatorModes.normal) {
			return num2;
		}
		if (mode == BiOperatorModes.add) {
			return num1 + num2;
		}
		if (mode == BiOperatorModes.minus) {
			return num1 - num2;
		}
		if (mode == BiOperatorModes.multiply) {
			return num1 * num2;
		}
		if (mode == BiOperatorModes.divide) {
			return num1 / num2;
		}
		throw new Error();
	}

	public Double calculateBi(BiOperatorModes newMode, Double num) {
		if (mode == BiOperatorModes.normal) {
			num2 = 0.0;
			num1 = num;
			mode = newMode;
			return Double.NaN;
		} else {
			num2 = num;
			num1 = calculateBiImpl();
			mode = newMode;
			return num1;
		}
	}

	public Double calculateEqual(Double num) {
		return calculateBi(BiOperatorModes.normal, num);
	}

	public Double reset() {
		num2 = 0.0;
		num1 = 0.0;
		mode = BiOperatorModes.normal;

		return Double.NaN;
	}

	public static void main(String args[]) {
		UDPCalculatorServer s = new UDPCalculatorServer();
		s.waitForPackets();
	}

	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

}
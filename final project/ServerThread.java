package ChatOldOne;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
	private Socket st_sock;
	private DataInputStream st_in;
	private DataOutputStream st_out;
	private StringBuffer st_buffer;
	/* 로그온된 사용자 저장 */
	private static Hashtable<String, ServerThread> logonHash;
	private static Vector<String> logonVector;
	/* 대화방 참여자 저장 */
	private static Hashtable<String, ServerThread> roomHash;
	private static Vector<String> roomVector;
	private static Hashtable<String, String> roomnameHash;
	private static Hashtable<String, String> enterroomHash;

	private static int isOpenRoom = 0; // 대화방이 개설안됨(초기값)

	private static final String SEPARATOR = "|"; // 메시지간 구분자
	private static final String DELIMETER = "`"; // 소메시지간 구분자
	private static Date starttime; // 로그온 시각

	public String st_ID; // ID 저장

	// 메시지 패킷 코드 및 데이터 정의
	private static final int WAITROOM = 100;
	private static final int CHATROOM = 101;
	private static final int MDY_WAITUSERS = 200;
	// 클라이언트로부터 전달되는 메시지 코드
	private static final int REQ_LOGON = 1001;
	private static final int REQ_ENTERROOM = 1011;
	private static final int REQ_SENDWORDS = 1021;
	private static final int REQ_WISPERSEND = 1022;
	private static final int REQ_LOGOUT = 1031;
	private static final int REQ_CREATEROOM = 1040;
	private static final int REQ_QUITROOM = 1041;

	// 클라이언트에 전송하는 메시지 코드
	private static final int YES_LOGON = 2001;
	private static final int NO_LOGON = 2002;
	private static final int YES_ENTERROOM = 2011;
	private static final int NO_ENTERROOM = 2012;
	private static final int YES_CREATEROOM = 2014;
	private static final int NO_CREATEROOM = 2015;
	private static final int MDY_USERIDS = 2013;
	private static final int MDY_ROOM = 2017;
	private static final int YES_SENDWORDS = 2021;
	private static final int YES_WISPERWORDS = 2023;
	private static final int NO_WISPERWORDS = 2024;
	private static final int YES_LOGOUT = 2031;
	private static final int YES_QUITROOM = 2041;
	private static final int C_LOGOUT = 2033;
	private static final int C_QUITROOM = 2042;
	// 에러 메시지 코드
	private static final int MSG_ALREADYUSER = 3001;
	private static final int MSG_SERVERFULL = 3002;
	private static final int MSG_CANNOTOPEN = 3011;

	static {
		logonHash = new Hashtable<String, ServerThread>(ChatServer.cs_maxclient);
		logonVector = new Vector<String>(ChatServer.cs_maxclient);
		roomHash = new Hashtable<String, ServerThread>(ChatServer.cs_maxclient);
		roomVector = new Vector<String>(ChatServer.cs_maxclient);
		roomnameHash = new Hashtable<String, String>(ChatServer.cs_maxclient);
		enterroomHash = new Hashtable<String, String>(ChatServer.cs_maxclient);
	}

	public ServerThread(Socket sock) {
		try {
			st_sock = sock;
			st_in = new DataInputStream(sock.getInputStream());
			st_out = new DataOutputStream(sock.getOutputStream());
			st_buffer = new StringBuffer(2048);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void run() {
		try {
			while (true) {
				String recvData = st_in.readUTF();
				StringTokenizer st = new StringTokenizer(recvData, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
				System.out.println(command);
				switch (command) {

				// 로그온 시도 메시지 PACKET : REQ_LOGON|ID
				case REQ_LOGON: {
					int result;
					System.out.println("result");
					String id = st.nextToken(); // 클라이언트의 ID를 얻는다.
					result = addUser(id, this);
					System.out.println(result);
					st_buffer.setLength(0);
					if (result == 0) { // 접속을 허용한 상태
						st_buffer.append(YES_LOGON);
						// YES_LOGON|개설시각|ID1`ID2`..
						st_buffer.append(SEPARATOR);
						starttime = new Date();
						st_buffer.append(starttime);
						st_buffer.append(SEPARATOR);
						String userIDs = getUsers(); // 대화방 참여 사용자ID를 구한다
						st_buffer.append(userIDs);
						st_buffer.append(SEPARATOR);
						String roomIDs = getRoom();
						if(roomIDs.compareTo("")==0) roomIDs="0";
						st_buffer.append(roomIDs);
						broadcast(st_buffer.toString(), WAITROOM,"0");
					} else { // 접속불가 상태
						st_buffer.append(NO_LOGON); // NO_LOGON|errCode
						st_buffer.append(SEPARATOR);
						st_buffer.append(result); // 접속불가 원인코드 전송
						send(st_buffer.toString());
					}
					break;
				}

				// 대화방 개설 시도 메시지 PACKET : REQ_ENTERROOM|ID|Room
				case REQ_ENTERROOM: {
					st_buffer.setLength(0);
					String id = st.nextToken(); // 클라이언트의 ID를 얻는다.
					String Room = st.nextToken();
					if (checkUserID(id) == null) {

						// NO_ENTERROOM PACKET : NO_ENTERROOM|errCode
						st_buffer.append(NO_ENTERROOM);
						st_buffer.append(SEPARATOR);
						st_buffer.append(MSG_CANNOTOPEN);
						send(st_buffer.toString()); // NO_ENTERROOM 패킷을 전송한다.
						break;
					}
					enterroomHash.replace(id, "0",Room);
					roomVector.add(id); // 사용자 ID 추가
					roomHash.put(id, this); // 사용자 ID 및 클라이언트와 통신할 스레드 저장

					if (isOpenRoom == 0) { // 대화방 개설시간 설정
						isOpenRoom = 1;
						starttime = new Date();
					}

					// YES_ENTERROOM PACKET : YES_ENTERROOM
					st_buffer.setLength(0);
					st_buffer.append(YES_ENTERROOM);
					send(st_buffer.toString()); // YES_ENTERROOM 패킷을 전송한다.
					// MDY_USERIDS PACKET : MDY_USERIDS|id1'id2' ....
					st_buffer.setLength(0);
					st_buffer.append(MDY_USERIDS);
					st_buffer.append(SEPARATOR);
					String userIDs = getRoomUsers(Room); // 대화방 참여 사용자 ID를 구한다
					st_buffer.append(userIDs);
					broadcast(st_buffer.toString(), CHATROOM,Room); // MDY_USERIDS 패킷을 전송한다.
					

					break;
				}
				case REQ_CREATEROOM: {
					st_buffer.setLength(0);
					String id = st.nextToken(); // 클라이언트의 ID를 얻는다.
					if (checkUserID(id) == null) {

						// NO_ENTERROOM PACKET : NO_ENTERROOM|errCode
						st_buffer.append(NO_CREATEROOM);
						st_buffer.append(SEPARATOR);
						st_buffer.append(MSG_CANNOTOPEN);
						send(st_buffer.toString()); // NO_ENTERROOM 패킷을 전송한다.
						break;
					}
					roomnameHash.replace(id, "0", id);

					if (isOpenRoom == 0) { // 대화방 개설시간 설정
						isOpenRoom = 1;
						starttime = new Date();
					}

					// YES_ENTERROOM PACKET : YES_CREATEROOM|ids
					st_buffer.append(YES_CREATEROOM);
					st_buffer.append(SEPARATOR);
					String ids = getRoom();
					st_buffer.append(ids);
					broadcast(st_buffer.toString(),WAITROOM,"0");
					break;
				}

				// 대화말 전송 시도 메시지 PACKET : REQ_SENDWORDS|ID|대화말
				case REQ_SENDWORDS: {
					st_buffer.setLength(0);
					st_buffer.append(YES_SENDWORDS);
					st_buffer.append(SEPARATOR);
					String id = st.nextToken(); // 전송한 사용자의 ID를 구한다.
					st_buffer.append(id);
					st_buffer.append(SEPARATOR);
					try {
						String data = st.nextToken(); // 대화말을 구한다.
						st_buffer.append(data);
					} catch (NoSuchElementException e) {
					}
					String Room = st.nextToken();
					broadcast(st_buffer.toString(), CHATROOM,Room); // YES_SENDWORDS 패킷 전송
					break;
				}
				case REQ_WISPERSEND: {
					String id = st.nextToken();
					String data = st.nextToken();
					String rid = st.nextToken();
					if (id.compareTo(rid) == 0) {
						st_buffer.setLength(0);
						st_buffer.append(NO_WISPERWORDS);
						send(st_buffer.toString());
					} else {
						st_buffer.setLength(0);
						st_buffer.append(YES_WISPERWORDS);
						st_buffer.append(SEPARATOR);
						// 전송한 사용자의 ID를 구한다.
						st_buffer.append(id);
						st_buffer.append(SEPARATOR);
						// 대화말을 구한다.

						st_buffer.append(data);
						st_buffer.append(SEPARATOR);
						st_buffer.append(rid);
						multicast(st_buffer.toString(), id, rid);
					}
					break;
				}
				// LOGOUT 전송 시도 메시지
				// PACKET : YES_LOGOUT|탈퇴자ID|탈퇴자 이외의 ids
				case REQ_LOGOUT: {
					String ids = "";
					String roommem=getRoomUsers(st_ID);
					if(roommem.compareTo("")==0) roommem=" ";
					String rooms = getRoom();
					if(rooms.compareTo("")==0) rooms=" ";
					st_buffer.setLength(0);
					st_buffer.append(YES_QUITROOM);
					st_buffer.append(SEPARATOR);
					st_buffer.append(roommem);
					st_buffer.append(SEPARATOR);
					st_buffer.append(rooms);
					broadcast(st_buffer.toString(),CHATROOM,st_ID);
					
					st_buffer.setLength(0);
					st_buffer.append(C_QUITROOM);
					broadcast(st_buffer.toString(),CHATROOM,st_ID);
					
					roomnameHash.replace(st_ID, st_ID, "0");
					rooms = getRoom();
					if(rooms.compareTo("")==0) rooms=" ";
					st_buffer.setLength(0);
					st_buffer.append(MDY_ROOM);
					st_buffer.append(SEPARATOR);
					st_buffer.append(rooms);
					broadcast(st_buffer.toString(), WAITROOM,"");
					st_buffer.setLength(0);
					st_buffer.append(YES_LOGOUT);
					st_buffer.append(SEPARATOR);
					st_buffer.append(st_ID);
					logonVector.remove(st_ID);
					logonHash.remove(st_ID);
					roomnameHash.remove(st_ID);
					enterroomHash.remove(st_ID);
					st_buffer.append(SEPARATOR);
					ids = getUsers();
					if (ids.compareTo("") == 0)
						ids = " ";
					st_buffer.append(ids);
					broadcast(st_buffer.toString(), WAITROOM,"0");
					st_buffer.setLength(0);
					st_buffer.append(C_LOGOUT);
					send(st_buffer.toString());
					
					String check;
					Enumeration<String> enu = logonVector.elements();
					while (enu.hasMoreElements()) {
						check = enu.nextElement();
						if (st_ID.compareTo(enterroomHash.get(check))==0) {
							roomVector.remove(check);
							roomHash.remove(check);
							enterroomHash.replace(check,st_ID,"0");
						}
					}
					break;
				}

				// 방 입장전의 LOGOUT 전송 시도 메시지 PACKET : YES_QUITROOM
				case REQ_QUITROOM: {
					String ids = "";
					String id;
					String rooms;
					st_buffer.setLength(0);
					st_buffer.append(YES_QUITROOM);
					st_buffer.append(SEPARATOR);
					id = enterroomHash.get(st_ID);
					roomVector.remove(st_ID);
					roomHash.remove(st_ID);
					enterroomHash.replace(st_ID,id,"0");
					ids = getRoomUsers(id);
					if (ids.compareTo("") == 0)
						ids = " ";
					rooms = getRoom();
					if(rooms.compareTo("")==0)
						rooms = " ";
					st_buffer.append(ids);
					st_buffer.append(SEPARATOR);
					st_buffer.append(rooms);
					broadcast(st_buffer.toString(), CHATROOM,id);
					st_buffer.setLength(0);
					st_buffer.append(MDY_ROOM);
					st_buffer.append(SEPARATOR);
					st_buffer.append(rooms);
					broadcast(st_buffer.toString(), WAITROOM,"");
					st_buffer.setLength(0);
					st_buffer.append(C_QUITROOM);
					send(st_buffer.toString());
					
					break;
				}

				} // switch 종료

				Thread.sleep(100);
			} // while 종료

		} catch (NullPointerException e) { // 로그아웃시 st_in이 이 예외를 발생하므로
		} catch (InterruptedException e) {
		} catch (IOException e) {
		}
	}

	// 자원을 해제한다.

	public void release() {
	}

	/*
	 * 해쉬 테이블에 접속을 요청한 클라이언트의 ID 및 전송을 담당하는 스레드를 등록. 즉, 해쉬 테이블은 대화를 하는 클라이언트의 리스트를
	 * 포함.
	 */
	private static synchronized int addUser(String id, ServerThread client) {
		if (checkUserID(id) != null) {
			return MSG_ALREADYUSER;
		}
		if (logonHash.size() >= ChatServer.cs_maxclient) {
			return MSG_SERVERFULL;
		}
		logonVector.addElement(id); // 사용자 ID 추가
		logonHash.put(id, client); // 사용자 ID 및 클라이언트와 통신할 스레드를 저장한다.
		roomnameHash.put(id, "0");
		enterroomHash.put(id, "0");
		client.st_ID = id;
		return 0; // 클라이언트와 성공적으로 접속하고, 대화방이 이미 개설된 상태.
	}

	/*
	 * 접속을 요청한 사용자의 ID와 일치하는 ID가 이미 사용되는 지를 조사한다. 반환값이 null이라면 요구한 ID로 대화방 입장이 가능함.
	 */
	private static ServerThread checkUserID(String id) {
		ServerThread alreadyClient = null;
		alreadyClient = (ServerThread) logonHash.get(id);
		return alreadyClient;
	}

	// 로그온에 참여한 사용자 ID를 구한다.
	private String getUsers() {
		StringBuffer id = new StringBuffer();
		String ids;
		Enumeration<String> enu = logonVector.elements();
		while (enu.hasMoreElements()) {
			id.append(enu.nextElement());
			id.append(DELIMETER);
		}
		try {
			ids = new String(id); // 문자열로 변환한다.
			ids = ids.substring(0, ids.length() - 1); // 마지막 "`"를 삭제한다.
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
		return ids;
	}
	private String getRoom() {
		StringBuffer id = new StringBuffer();
		String ids;
		String check;
		Enumeration<String> enu = logonVector.elements();
		while (enu.hasMoreElements()) {
			check=enu.nextElement();
			if(check.compareTo(roomnameHash.get(check))==0) {
				id.append(check);
				id.append(DELIMETER);
			}
		}
		try {
			ids = new String(id); // 문자열로 변환한다.
			ids = ids.substring(0, ids.length() - 1); // 마지막 "`"를 삭제한다.
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
		return ids;
	}
	// 대화방에 참여한 사용자 ID를 구한다.

	private String getRoomUsers(String Room) {
		StringBuffer id = new StringBuffer();
		String ids;
		String check;
		Enumeration<String> enu = roomVector.elements();
		while (enu.hasMoreElements()) {
			check = enu.nextElement();
			if (Room.compareTo(enterroomHash.get(check))==0) {
				id.append(check);
				id.append(DELIMETER);
			}
		}
		try {
			ids = new String(id);
			ids = ids.substring(0, ids.length() - 1); // 마지막 "`"를 삭제한다.
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
		return ids;
	}

	private void modifyWaitUsers() throws IOException {
		String ids = getUsers();
		st_buffer.setLength(0);
		st_buffer.append(MDY_WAITUSERS);
		st_buffer.append(SEPARATOR);
	}

	/*private void modifyRoomUsers() throws IOException {
		String ids = getRoomUsers();
		st_buffer.setLength(0);
		st_buffer.append(MDY_ROOMUSERS);
		st_buffer.append(SEPARATOR);
		st_buffer.append(ids);
		broadcast(st_buffer.toString(), CHATROOM);
	}*/

	// 대화방에 참여한 모든 사용자(브로드케스팅)에게 데이터를 전송한다.
	public synchronized void broadcast(String sendData, int room,String Room) throws IOException {
		ServerThread client;
		Enumeration<String> enu;
		if (room == WAITROOM) {
			enu = logonVector.elements();
			while (enu.hasMoreElements()) {
				client = (ServerThread) logonHash.get(enu.nextElement());
				client.send(sendData);
			}

		} else {
			enu = roomVector.elements();
			String check;
			while (enu.hasMoreElements()) {
				check = enu.nextElement();
				if(Room.compareTo(enterroomHash.get(check))==0) {
					client = (ServerThread) roomHash.get(check);
					client.send(sendData);
				}
			}
		}
	}

	public synchronized void multicast(String sendData, String id, String rid) throws IOException {
		ServerThread client;
		Enumeration<String> enu;
		enu = roomVector.elements();
		while (enu.hasMoreElements()) {
			String next = enu.nextElement();
			System.out.println(next);
			if (next.compareTo(id) == 0 || next.compareTo(rid) == 0) {
				client = (ServerThread) roomHash.get(next);
				client.send(sendData);
			}
		}
	}

	// 데이터를 전송한다.
	public void send(String sendData) throws IOException {
		synchronized (st_out) {
			st_out.writeUTF(sendData);
			st_out.flush();
		}
	}
}

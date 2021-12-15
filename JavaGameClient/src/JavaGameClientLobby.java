
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument.Iterator;
import javax.swing.JLayeredPane;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

public class JavaGameClientLobby extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane panel;
	private String UserName;
	private String Ip_Addr;
	private String Port_No;

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	public Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	private ListenNetwork net;

	private JLabel lblUserName;
	private JButton roomBtn;

	private String password;
	private String roomNameText;
	private String lookResult;
	private String secretResult;

	public JTextPane textArea;
	private int roomHeight = 5;
	public JavaGameClientLobby lobby;
	public JavaGameClientView view;
	public JavaGameClientRoom gameRoom;
	public JButton[] btnlist = new JButton[999];

	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public JavaGameClientLobby(String username, String ip_addr, String port_no) {
		lobby = this;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 435, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblUserName = new JLabel("Name");
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 23, 68, 40);
		contentPane.add(lblUserName);
		setVisible(true);

		Ip_Addr = ip_addr;
		Port_No = port_no;
		UserName = username;
		lblUserName.setText(username);

		JButton roomBtn = new JButton("방 만들기");
		roomBtn.setBounds(303, 23, 100, 40);
		contentPane.add(roomBtn);

		panel = new JScrollPane();
		panel.setBounds(12, 73, 391, 362);
		contentPane.add(panel);
		panel.setLayout(null);

		Myaction action = new Myaction();
		roomBtn.addActionListener(action);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			gameRoom = new JavaGameClientRoom(UserName, lobby);
			setVisible(false);
		}
	}

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		@SuppressWarnings("unused")
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
						// msg = String.format("\n%s", cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						String[] args = cm.data.split(" ");
						String roomID = args[0];
						roomNameText = args[1];
						lookResult = args[2];
						secretResult = args[3];
						if (secretResult == "Y")
							password = args[4];
						else
							password = null;

						GameRoom room = new GameRoom(roomID, cm.UserName, roomNameText, lookResult, secretResult,
								password);
						JPanel newPane = new JPanel();
						newPane = room.getPanel();
						newPane.setBounds(5, roomHeight, 380, 100);
						enterRoomAction action = new enterRoomAction();
						room.getEnterBtn().addActionListener(action);
						btnlist[Integer.parseInt(roomID)] = room.getEnterBtn();
						roomHeight += 120;
						panel.add(newPane);
						panel.repaint();
						break;

					case "201":
						String[] args201 = cm.data.split(" ");
						if (cm.UserName.equals(UserName)) {
							view = new JavaGameClientView(UserName, args201[1], lobby);
							setVisible(false);
							view.startTimer();
						}

						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}

						if (args201[0].equals(UserName)) {
							view.myTurn = 1;
							view.historyColor = 1;
							view.AppendText("[SERVER]");
							view.AppendText("상대방이 입장하였습니다.\n돌을 놓아주세요.");
							view.startTimer();
						}
						break;

					case "202":
						String[] args202 = cm.data.split(" ");
						String pw = args202[1];

						if (cm.UserName.equals(UserName)) {
							String checkpw = JOptionPane.showInputDialog("비밀번호를 입력하세요.");
							if (checkpw.equals(pw)) {
								JOptionPane.showInternalMessageDialog(null, "비밀번호가 맞습니다.\n게임을 시작합니다.");
								String msg202 = String.format("%s %s", args202[0], args202[2]);
								SendMessage("202", msg202);
								break;
							} else if (checkpw == null) {
								JOptionPane.showInternalMessageDialog(null, "비밀번호 입력을 취소하였습니다.");
								break;
							} else {
								JOptionPane.showInternalMessageDialog(null, "비밀번호가 아닙니다.");
								break;
							}
						}
						break;

					case "300": // chat message
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}
						if (cm.UserName.equals(UserName))
							view.AppendTextR(msg); // 내 메세지는 우측에
						else
							view.AppendText(msg);
						break;

					case "301":
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}
						if (cm.UserName.equals(UserName))
							view.AppendTextR("[" + cm.UserName + "]");
						// AppendTextR(" ");
						else
							view.AppendText("[" + cm.UserName + "]");
						// AppendText(" ");
						view.AppendImage(view.img);
						break;

					case "400":
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}
						String[] args400 = cm.data.split(" ");
						String opPlayer = args400[0];
						int x = Integer.parseInt(args400[1]);
						int y = Integer.parseInt(args400[2]);
						String record = String.format("%s %s", args400[1], args400[2]);

						if (opPlayer.matches(UserName)) {
							view.stopTimer();
							view.startTimer();
							view.map[x][y] = 2;
							view.myTurn = 1;
							for (int i = 0; i < view.recordStone.length; i++) {
								if (view.recordStone[i].equals("0")) {
									view.recordStone[i] = record;
									break;
								}
							}
						}
						break;

					case "401":
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}

						if (cm.data.matches(UserName)) {
							view.ShowBackRequest();
						}
						break;

					case "401Y":
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}
						String[] args401 = cm.data.split(" ");
						int num = Integer.parseInt(args401[1]);
						int stoneX = Integer.parseInt(args401[2]);
						int stoneY = Integer.parseInt(args401[3]);
						String answerY = "상대방이 무르기 요청을 수락하였습니다.";
						if (args401[0].matches(UserName)) {
							view.ShowMessage(answerY, "무르기 요청");
							view.map[stoneX][stoneY] = 0;
							view.recordStone[num] = "0";
						} else if (cm.UserName.matches(UserName)) {
							view.myTurn = 1;
						}
						break;

					case "401N":
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}
						String answerN = "상대방이 무르기 요청을 거부하였습니다.";
						if (cm.data.matches(UserName)) {
							view.ShowMessage(answerN, "무르기 요청");
						}
						break;
						
					case "405":
						if (view == null) {
							try {
								view = gameRoom.view;
							} catch (NullPointerException e) {
								break;
							}
						}
						String success = "You Win!";
						String lose = "You lose ..";
						String[] args405 = cm.data.split(" ");
						String opPlayer405 = args405[0];
						int x405 = Integer.parseInt(args405[1]);
						int y405 = Integer.parseInt(args405[2]);
						String record405 = String.format("%s %s", args405[1], args405[2]);

						if (opPlayer405.matches(UserName)) {
							view.map[x405][y405] = 2;
							for (int i = 0; i < view.recordStone.length; i++) {
								if (view.recordStone[i].equals("0")) {
									view.recordStone[i] = record405;
									break;
								}
							}
							view.ShowResult(lose);
						}
						if (cm.UserName.matches(UserName)) {
							view.ShowResult(success);
						}						
						break;
					}
				} catch (IOException e) {
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}

	class enterRoomAction implements ActionListener// 내부클래스로 액션 이벤트 처리 클래스
	{
		public void actionPerformed(ActionEvent e) {
			int roomID = -1;
			for (int i = 0; i < btnlist.length; i++) {
				if (e.getSource() == btnlist[i]) {
					roomID = i;
					break;
				}
			}
			System.out.println(roomID);

			// view = new JavaGameClientView(UserName, roomNameText, lobby);
			// setVisible(false);
			try {
				ChatMsg obcm = new ChatMsg(UserName, "201", Integer.toString(roomID));
				oos.writeObject(obcm);
			} catch (IOException e1) {
				// AppendText("dos.write() error");
				try {
					ois.close();
					oos.close();
					socket.close();
				} catch (IOException e11) {
					// TODO Auto-generated catch block
					e11.printStackTrace();
					System.exit(0);
				}
			}
		}
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String code, String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, code, msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "300", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			view.AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
}

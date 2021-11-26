
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

import javax.swing.JLayeredPane;
import javax.swing.BoxLayout;

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
	
	
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientLobby(String username, String ip_addr, String port_no)  {
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
			//JavaGameClientView view = new JavaGameClientView(username, ip_addr, port_no);
			//JavaGameClientRoom view = new JavaGameClientRoom(socket, oos, ois, net, UserName, Ip_Addr, Port_No);
			//setVisible(false);
			gameRoom = new JavaGameClientRoom(UserName, lobby);
			setVisible(false);
		}
	}
	
	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
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
						//msg = String.format("\n%s", cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						String[] args = cm.data.split(" ");
						roomNameText = args[0];
						lookResult = args[1];
						secretResult = args[2];
						if(secretResult == "Y")
							password = args[3];
						else
							password = null;

						GameRoom room = new GameRoom(UserName, roomNameText, lookResult, secretResult, password);
						JButton btnTest = new JButton();
						btnTest.setText("test");
						JPanel newPane = new JPanel();
						newPane = room.getPanel();
						newPane.setBounds(5, roomHeight, 380, 100);
						enterRoomAction action = new enterRoomAction();
						room.getEnterBtn().addActionListener(action);
						roomHeight += 120;
						panel.add(newPane);
						panel.repaint();

						break;
					
					case "201":
						break;
						
					case "300": // chat message
						if (view == null) {
							view = gameRoom.view;
						}
						if (cm.UserName.equals(UserName))
							view.AppendTextR(msg); // 내 메세지는 우측에
						else
							view.AppendText(msg);
						break;
						
					case "301":
						if (view == null) {
							view = gameRoom.view;
						}
						if (cm.UserName.equals(UserName))
							view.AppendTextR("[" + cm.UserName + "]");
							//AppendTextR(" ");
						else
							view.AppendText("[" + cm.UserName + "]");
							//AppendText(" ");
						view.AppendImage(view.img);
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
			try {
				// dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
				ChatMsg obcm = new ChatMsg(UserName, "201", "enterRoom");
				oos.writeObject(obcm);
			} catch (IOException e1) {
				// AppendText("dos.write() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					socket.close();
				} catch (IOException e11) {
					// TODO Auto-generated catch block
					e11.printStackTrace();
					System.exit(0);
				}
			}
			//setVisible(false);
			//JavaGameClientView view = new JavaGameClientView(UserName, socket, oos, ois, net, roomNameText);
			view = new JavaGameClientView(UserName, roomNameText, lobby);
			setVisible(false);
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

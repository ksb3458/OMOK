
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
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import java.awt.SystemColor;

public class JavaGameClientRoom extends JFrame {
	/**
	 * 
	 */
	private JPanel contentPane;
	private String UserName;
	private String Ip_Addr;
	private String Port_No;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	
	private JButton roomBtn;
	private JPanel panel;
	private JLabel roomLabel;
	private JButton cancelBtn;
	private JLabel nameLabel;
	private JTextField roomName;
	private JLabel secretLabel;
	private JPanel secretPanel;
	private JRadioButton secretYesBtn;
	private JRadioButton secretNoBtn;
	private JLabel lookLabel;
	private JPanel lookPanel;
	private JRadioButton lookNoBtn;
	private JRadioButton lookYesBtn;
	private JButton createRoom;
	private JTextField pwText;
	private JLabel pwLabel;
	
	private String password;
	private String roomNameText;
	private String lookResult;
	private String secretResult;
	public JTextPane textArea;
	
	public JavaGameClientLobby gameLobby;
	public JavaGameClientView view;
	
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientRoom(String username, JavaGameClientLobby lobby)  {
		gameLobby = lobby;
		UserName = username;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 375, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(12, 10, 336, 363);
		panel.setLayout(null);
		contentPane.add(panel);
		
		roomLabel = new JLabel("   방 만들기");
		roomLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		roomLabel.setBackground(Color.PINK);
		roomLabel.setOpaque(true);
		roomLabel.setBounds(0, 0, 336, 44);
		panel.add(roomLabel);
		
		cancelBtn = new JButton("취소");
		cancelBtn.setBackground(Color.LIGHT_GRAY);
		cancelBtn.setBounds(257, 4, 67, 37);
		panel.add(cancelBtn);
		
		nameLabel = new JLabel("방 제 목");
		nameLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		nameLabel.setBounds(30, 83, 67, 21);
		panel.add(nameLabel);
		
		roomName = new JTextField();
		roomName.setText(username);
		roomName.setBounds(111, 76, 174, 30);
		panel.add(roomName);
		roomName.setColumns(10);
		
		secretLabel = new JLabel("비 밀 방");
		secretLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		secretLabel.setBounds(30, 141, 67, 21);
		panel.add(secretLabel);
		
		secretPanel = new JPanel();
		secretPanel.setBounds(111, 118, 213, 49);
		panel.add(secretPanel);
		
		secretYesBtn = new JRadioButton("예");		
		secretNoBtn = new JRadioButton("아니오");
		secretNoBtn.setSelected(true);
		
		GroupLayout gl_secretPanel = new GroupLayout(secretPanel);
		gl_secretPanel.setHorizontalGroup(
			gl_secretPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_secretPanel.createSequentialGroup()
					.addComponent(secretYesBtn)
					.addGap(52)
					.addComponent(secretNoBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(100, Short.MAX_VALUE))
		);
		gl_secretPanel.setVerticalGroup(
			gl_secretPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_secretPanel.createSequentialGroup()
					.addContainerGap(20, Short.MAX_VALUE)
					.addGroup(gl_secretPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(secretYesBtn)
						.addComponent(secretNoBtn, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		secretPanel.setLayout(gl_secretPanel);
		
		lookLabel = new JLabel("관전가능");
		lookLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		lookLabel.setBounds(30, 241, 67, 21);
		panel.add(lookLabel);
		
		lookPanel = new JPanel();
		lookPanel.setBounds(111, 218, 215, 49);
		panel.add(lookPanel);
		
		lookNoBtn = new JRadioButton("아니오");
		lookYesBtn = new JRadioButton("예");
		lookYesBtn.setSelected(true);
		
		GroupLayout gl_lookPanel = new GroupLayout(lookPanel);
		gl_lookPanel.setHorizontalGroup(
			gl_lookPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 282, Short.MAX_VALUE)
				.addGroup(gl_lookPanel.createSequentialGroup()
					.addComponent(lookYesBtn)
					.addGap(52)
					.addComponent(lookNoBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(100, Short.MAX_VALUE))
		);
		gl_lookPanel.setVerticalGroup(
			gl_lookPanel.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 49, Short.MAX_VALUE)
				.addGroup(gl_lookPanel.createSequentialGroup()
					.addContainerGap(20, Short.MAX_VALUE)
					.addGroup(gl_lookPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lookYesBtn)
						.addComponent(lookNoBtn, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		lookPanel.setLayout(gl_lookPanel);
		
		createRoom = new JButton("만  들  기");
		createRoom.setFont(new Font("굴림", Font.PLAIN, 17));
		createRoom.setBounds(19, 305, 305, 37);
		panel.add(createRoom);
		setVisible(true);
		
		ButtonGroup  secretGroup = new ButtonGroup();
		secretGroup.add(secretYesBtn);
		secretGroup.add(secretNoBtn);
		
		ButtonGroup  lookGroup = new ButtonGroup();
		lookGroup.add(lookYesBtn);
		lookGroup.add(lookNoBtn);
		
		pwLabel = new JLabel("비밀번호");
		pwLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		pwLabel.setBounds(30, 177, 67, 21);	
		
		pwText = new JTextField();
		pwText.setColumns(10);
		pwText.setBounds(111, 172, 174, 30);
		
		goLobby action = new goLobby();
		cancelBtn.addActionListener(action);
		
		selectSecret action2 = new selectSecret();
		secretYesBtn.addActionListener(action2);
		secretNoBtn.addActionListener(action2);
		
		createRoomAction action3 = new createRoomAction();
		createRoom.addActionListener(action3);
	}

	class goLobby implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			//JavaGameClientView view = new JavaGameClientView(username, ip_addr, port_no);
			JavaGameClientLobby lobby = new JavaGameClientLobby(UserName, Ip_Addr, Port_No);
			setVisible(false);
		}
	}
	
	class selectSecret implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(secretYesBtn.isSelected()) {
				panel.add(pwLabel);
				panel.add(pwText);
				panel.repaint();
			}
			
			else {
				panel.remove(pwLabel);
				panel.remove(pwText);
				pwText.setText(null);
				panel.repaint();
			}
		}
	}
	
	class createRoomAction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			roomNameText = roomName.getText();
			if(secretYesBtn.isSelected()) {
				secretResult = "Y";
				password = pwText.getText();
			}
			else {
				secretResult = "N";
				password = null;
			}
			if(lookYesBtn.isSelected())
				lookResult = "Y";
			else
				lookResult = "N";
			
			String msg = new String();
			msg = String.format("%s %s %s %s", roomNameText, lookResult, secretResult, password);
			System.out.println("1. msg : " + msg);
			
			try {
				ChatMsg obcm = new ChatMsg(UserName, "200", msg);
				gameLobby.oos.writeObject(obcm);
			} catch (IOException e1) {
				try {
					gameLobby.ois.close();
					gameLobby.oos.close();
					gameLobby.socket.close();
				} catch (IOException e11) {
					// TODO Auto-generated catch block
					e11.printStackTrace();
					System.exit(0);
				}
			}
			view = new JavaGameClientView(UserName, roomNameText, gameLobby);
			setVisible(false);
		}
	}
}
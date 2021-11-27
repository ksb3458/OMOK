
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JOptionPane;

import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class JavaGameClientView extends JFrame {
	/**
	 * 
	 */
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	public int[][] map = new int[20][20];
	public int myTurn = 0;
	public String[] recordStone = new String[400];
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	private JLabel lblUserName;
	public JTextPane textArea;
	public JavaGameClientLobby gameLobby;

	JPanel panel;
	public ImageIcon img;
	
	ImageIcon faceIcon1 = new ImageIcon("icon/fun.png");
	ImageIcon faceIcon2 = new ImageIcon("icon/cry.png");
	ImageIcon faceIcon3 = new ImageIcon("icon/sp.png");
	ImageIcon faceIcon4 = new ImageIcon("icon/angry.png");
	ImageIcon board = new ImageIcon("src/board.png");
	
	ImageIcon black = new ImageIcon("icon/black-go-stone24.png");
	ImageIcon white = new ImageIcon("icon/white-go-stone24.png");
	
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientView(String username, String roomName, JavaGameClientLobby lobby)  {
		gameLobby = lobby;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(581, 162, 251, 316);
		contentPane.add(scrollPane);
		
		textArea = new JTextPane();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));

		txtInput = new JTextField();
		txtInput.setBounds(581, 485, 183, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
		btnSend.setBounds(765, 484, 68, 42);
		contentPane.add(btnSend);

		lblUserName = new JLabel("Name");
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(765, 10, 68, 40);
		contentPane.add(lblUserName);
		setVisible(true);

		UserName = username;
		lblUserName.setText(roomName);

		JButton btnNewButton = new JButton("종 료");
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "500", "Bye");
				gameLobby.SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(727, 530, 106, 40);
		contentPane.add(btnNewButton);
		
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(board.getImage(), 0, 0, panel.getWidth(), panel.getHeight(), panel);
				setOpaque(false);
				putStone(g);
			}
		};		
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 10, 560, 560);
		contentPane.add(panel);
		
		JButton btnBack = new JButton("무 르 기  요 청");
		btnBack.setFont(new Font("굴림", Font.PLAIN, 14));
		btnBack.setBounds(581, 530, 144, 40);
		contentPane.add(btnBack);
		
		faceBtn1 = new JButton(faceIcon1);
		faceBtn1.setFont(new Font("굴림", Font.PLAIN, 14));
		faceBtn1.setBounds(581, 96, 60, 60);
		contentPane.add(faceBtn1);
		
		faceBtn2 = new JButton(faceIcon2);
		faceBtn2.setFont(new Font("굴림", Font.PLAIN, 14));
		faceBtn2.setBounds(644, 96, 60, 60);
		contentPane.add(faceBtn2);
		
		faceBtn3 = new JButton(faceIcon3);
		faceBtn3.setFont(new Font("굴림", Font.PLAIN, 14));
		faceBtn3.setBounds(708, 96, 60, 60);
		contentPane.add(faceBtn3);
		
		faceBtn4 = new JButton(faceIcon4);
		faceBtn4.setFont(new Font("굴림", Font.PLAIN, 14));
		faceBtn4.setBounds(772, 96, 60, 60);
		contentPane.add(faceBtn4);
		
		JLabel timer = new JLabel("0  :  30");
		timer.setForeground(Color.BLUE);
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("돋움", Font.BOLD, 33));
		timer.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		timer.setBackground(Color.WHITE);
		timer.setBounds(581, 26, 144, 60);
		contentPane.add(timer);
		
		playGame();

		try {
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			faceBtn1.addActionListener(action2);
			faceBtn2.addActionListener(action2);
			faceBtn3.addActionListener(action2);
			faceBtn4.addActionListener(action2);
			BackStoneAction action3 = new BackStoneAction();
			btnBack.addActionListener(action3);
			panel.addMouseListener(new gameTurn());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				gameLobby.SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == faceBtn1) {
				gameLobby.SendMessage("301", "IMG");
				img = new ImageIcon("icon/fun.png");
				gameLobby.SendObject(img);
			}
			
			else if(e.getSource() == faceBtn2) {
				ChatMsg obcm = new ChatMsg(UserName, "301", "IMG");
				img = new ImageIcon("icon/cry.png");
				obcm.img = img;
				gameLobby.SendObject(obcm);
			}
			
			else if(e.getSource() == faceBtn3) {
				ChatMsg obcm = new ChatMsg(UserName, "301", "IMG");
				img = new ImageIcon("icon/sp.png");
				obcm.img = img;
				gameLobby.SendObject(obcm);
			}
			
			else if(e.getSource() == faceBtn4) {
				ChatMsg obcm = new ChatMsg(UserName, "301", "IMG");
				img = new ImageIcon("icon/angry.png");
				obcm.img = img;
				gameLobby.SendObject(obcm);
			}
		}
	}
	
	public void putStone(Graphics g) {
		for (int i=0; i<map.length; i++) {
			for(int j=0; j<map[i].length; j++) {
				if(map[i][j] == 1) {
					g.drawImage(white.getImage(), i*30, j*30, white.getIconWidth(), white.getIconHeight(), panel);
				}
				else if(map[i][j] == 2) {
					g.drawImage(black.getImage(), i*30, j*30, black.getIconWidth(), black.getIconHeight(), panel);
				}
			}
		}
		panel.repaint();
	}
	
	class gameTurn implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(myTurn == 1) {
				//System.out.println("x : "+e.getX()+", y : "+e.getY());
				int x = e.getX();
				int y = e.getY();
				if(map[x/30][y/30] != 0) {
					return;
				}
				map[x/30][y/30] = 1;
				myTurn = 0;
				String location = String.format("%d %d", x/30, y/30);
				for(int i=0; i<recordStone.length; i++) {
					if(recordStone[i].equals("0")) {
						recordStone[i] = location;
						break;
					}
				}
				ChatMsg msg = new ChatMsg(UserName, "400", location);
				gameLobby.SendObject(msg);
			}
		}

		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	class BackStoneAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(myTurn == 1) {
				ChatMsg msg = new ChatMsg(UserName, "401", "Request Back");
				gameLobby.SendObject(msg);
			}
		}
	}
	
	public void ShowBackRequest() {
		int result = JOptionPane.showConfirmDialog(contentPane, "상대방이 무르기를 요청하였습니다.\n 무르시겠습니까?", "무르기 요청", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(result == JOptionPane.CLOSED_OPTION || result == JOptionPane.NO_OPTION) {
			ChatMsg msg = new ChatMsg(UserName, "401N", "Request Back NO");
			gameLobby.SendObject(msg);
		}
		else if(result == JOptionPane.YES_OPTION) {
			int num = 0;
			for(int i=0; i<recordStone.length; i++) {
				if(recordStone[i].equals("0")) {
					num = i - 1;
					break;
				}
			}
			String[] stone = recordStone[num].split(" ");
			int stoneX = Integer.parseInt(stone[0]);
			int stoneY = Integer.parseInt(stone[1]);
			map[stoneX][stoneY] = 0;
			recordStone[num] = "0";
			String sendMsg = String.format("%d %d %d", num, stoneX, stoneY);
			ChatMsg msg = new ChatMsg(UserName, "401Y", sendMsg);
			gameLobby.SendObject(msg);
			myTurn = 0;
		}
	}
	
	public void ShowBackAnswer(String answer) {
		JOptionPane.showMessageDialog(contentPane, answer, "무르기 요청", JOptionPane.PLAIN_MESSAGE);
	}
	
	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	private JButton faceBtn1;
	private JButton faceBtn2;
	private JButton faceBtn3;
	private JButton faceBtn4;

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		//textArea.setCaretPosition(len);
		//textArea.replaceSelection(msg + "\n");
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");
	}
	
	// 화면 우측에 출력
	public void AppendTextR(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.	
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);	
	    doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(),msg+"\n", right );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");
	}
	
	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else {
			textArea.insertIcon(ori_icon);
			new_img = ori_img;
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
	}
	
	public void playGame() {
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[i].length; j++) {
				map[i][j] = 0;
			}
		}
		
		for(int i=0; i<recordStone.length; i++) {
			recordStone[i] = "0";
		}
	}
}

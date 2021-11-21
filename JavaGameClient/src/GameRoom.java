
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
import java.io.Serializable;
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

public class GameRoom extends JFrame {
	
	private String RoomName, Look, Secret, Password;
	
	public GameRoom(String roomname, String look, String secret, String password)  {
		getContentPane().setBackground(SystemColor.info);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 376, 156);
		getContentPane().setLayout(null);
		
		JLabel nameLabel = new JLabel("");
		nameLabel.setBounds(12, 31, 177, 43);
		getContentPane().add(nameLabel);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(201, 31, 57, 43);
		getContentPane().add(lblNewLabel);
		
		JButton enterBtn = new JButton("입장하기");
		enterBtn.setBounds(270, 31, 88, 43);
		getContentPane().add(enterBtn);
		
		RoomName = roomname;
		nameLabel.setText(RoomName);
	}
}
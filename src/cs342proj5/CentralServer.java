package cs342proj5;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.util.ArrayList;
import java.util.Random;

public class CentralServer {
	private final JFrame window;
	private final JMenuBar menuBar;

	private final JMenu about;
	private final JMenu help;

	private final JTabbedPane pgd;
	private final JTabbedPane suli;

	private final JButton bSignUp;
	
	private final DefaultListModel<String> lmPrivate;
	
	private final JList<String> lPrivate;

	private final JScrollPane spPrivate;

	private final JLabel lDisplay;
	private final JLabel lErr41;
	private final JLabel lErr42;
	private final JLabel lpvk4;
	private final JLabel lpbk4;

	private static JTextField tuser4;
	private final JTextField tpvk4;
	private final JTextField tpbk4;

	private final Checkbox cBox;
	
	private final double bSize;
	
	private static Socket socket;

	// false - disconnected
	// true - online
	private final TreeMap<String, Boolean> userTree;

	public CentralServer(String Title) {
		window = new JFrame(Title);
		menuBar = new JMenuBar();

		about = new JMenu("About");
		help = new JMenu("Help");

		about.addMouseListener(new MIAboutDev());
		help.addMouseListener(new MIHelpServer());

		pgd = new JTabbedPane();
		suli = new JTabbedPane();
		suli.setPreferredSize(new Dimension(450, 250));

		bSignUp = new JButton("Sign Up");
		bSignUp.addActionListener(new ButtonSignUp());

		lmPrivate = new DefaultListModel<String>();

		lPrivate = new JList<String>(lmPrivate);

		Dimension dTop = new Dimension(200, 40);
		Dimension dLeft = new Dimension(270, 340);

		spPrivate = new JScrollPane(lPrivate);
		spPrivate.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spPrivate.setPreferredSize(dLeft);

		lDisplay = new JLabel();
		lDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		lDisplay.setFont(new Font("Serif", Font.BOLD, 20));
		lDisplay.setPreferredSize(dTop);

		lpvk4 = new JLabel("   Private Key:    ");
		lpbk4 = new JLabel("   Public Key:    ");
		lpvk4.setEnabled(false);
		lpbk4.setEnabled(false);

		lErr41 = new JLabel(" ");
		lErr42 = new JLabel(" ");
		lErr41.setForeground(Color.RED);
		lErr42.setForeground(Color.RED);

		tuser4 = new JTextField(20);
		tpvk4 = new JTextField(20);
		tpbk4 = new JTextField(20);
		tpvk4.setEnabled(false);
		tpbk4.setEnabled(false);

		cBox = new Checkbox("Personally Assign Keys");
		cBox.addItemListener(new CBox());
		
		bSize = 4.0;
		
		socket = null;

		userTree = new TreeMap<String, Boolean>();
	}

	/****************************************************************
	 * |Public Methods |
	 ****************************************************************/
	public void setTop(String s) {
		lDisplay.setText(s);
		return;
	}

	public void createWindow() {
		Dimension winDim = new Dimension();
		int width, height;
		width = 800;
		height = 480;

		winDim.setSize(width, height);

		addGridToWindow();
		addMenuBarToWindow();

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(winDim);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	/****************************************************************
	 * |Private Methods |
	 ****************************************************************/
	private void addMenuBarToWindow() {
		menuBar.add(about);
		menuBar.add(help);
		window.setJMenuBar(menuBar);
	}

	private void addGridToWindow() {

		final JPanel pAll = new JPanel(new BorderLayout());
		final JPanel pTop = new JPanel(new BorderLayout());
		final JPanel pLeft = new JPanel(new BorderLayout());
		final JPanel pMid = new JPanel();

		final JPanel tab1 = new JPanel();
		final JPanel tab4 = new JPanel(new GridBagLayout());

		final JLabel lu4 = new JLabel("   Name:    ");

		tab1.add(spPrivate);

		pgd.addTab("Chat Room", tab1);


		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(10, 0, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 0;
		tab4.add(lErr41, c);

		c.insets = new Insets(10, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 1;
		tab4.add(lu4, c);

		c.gridx = 2;
		c.gridy = 1;
		tab4.add(tuser4, c);

		c.insets = new Insets(10, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 2;

		c.gridx = 2;
		c.gridy = 2;
		tab4.add(lErr42, c);

		c.gridx = 2;
		c.gridy = 3;
		tab4.add(cBox, c);

		c.gridx = 1;
		c.gridy = 4;
		tab4.add(lpvk4, c);

		c.gridx = 2;
		c.gridy = 4;
		tab4.add(tpvk4, c);

		c.gridx = 1;
		c.gridy = 5;
		tab4.add(lpbk4, c);

		c.gridx = 2;
		c.gridy = 5;
		tab4.add(tpbk4, c);

		c.anchor = GridBagConstraints.EAST;
		c.gridx = 2;
		c.gridy = 6;
		tab4.add(bSignUp, c);

		suli.addTab("Sign Up", tab4);

		pTop.add(lDisplay);

		pLeft.add(pgd);

		pMid.add(suli);

		pTop.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		pLeft.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		pMid.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		pAll.add(pTop, BorderLayout.PAGE_START);
		pAll.add(pLeft, BorderLayout.LINE_START);
		pAll.add(pMid, BorderLayout.CENTER);

		window.add(pAll);
	}

	/****************************************************************
	 * |Listeners |
	 ****************************************************************/
	private class ButtonSignUp implements ActionListener {

		private ArrayList<Double> primes;// = new ArrayList<Double>();
		// https://stackoverflow.com/questions/1801391/what-is-the-best-algorithm-for-checking-if-a-number-is-prime
		private boolean testPrime(double num) {
			if (num == 2)
				return true;
			if (num == 3)
				return true;
			if (num % 2 == 0)
				return false;
			if (num % 3 == 0)
				return false;

			int i = 5;
			int w = 2;

			while (i * i <= num) {
				if (num % i == 0)
					return false;

				i += w;
				w = 6 - w;
			}

			return true;
		}
		
		private boolean isValidKeys() {
			
			double p1, p2;
			
			if(!tpvk4.getText().matches("-?\\d+(\\.\\d+)?"))
				return false;
			
			if(!tpbk4.getText().matches("-?\\d+(\\.\\d+)?"))
				return false;
			
			p1 = Double.parseDouble(tpvk4.getText());
			p2 = Double.parseDouble(tpbk4.getText());
			
			if(!testPrime(p1) || !testPrime(p2))
				return false;
			
			if(p1*p2 > Math.pow(128.0, bSize))
				return true;
			return false;
			
		}
		
		private double randNum() {
			
			Random rand = new Random();
			
			Double num = primes.get(rand.nextInt(primes.size()));
			
			return num;
		}
		
		private void createUser() throws IOException {
			if (!userTree.containsKey(tuser4.getText()) && tuser4.getText().length()<=32) {

				if(cBox.getState()) {
					//System.out.println("true1");
					if(!isValidKeys()) {
						lErr42.setText("Invalid Primes");
						return;
					}
					//System.out.println("true2");
					Client a = new Client("CS 342 Project 5 Client");
					a.start();
					a.createWindow();
					a.setTop("Welcome to the Server");
					a.setMidTop(tuser4.getText());
					a.setPrivateKey(Double.parseDouble(tpvk4.getText()));
					a.setPublicKey(Double.parseDouble(tpbk4.getText()));
					lmPrivate.addElement(tuser4.getText());
					userTree.put(tuser4.getText(), true);					
				}
				
				else {
					//System.out.println("false");
					
					primes = new ArrayList<Double>();

					@SuppressWarnings("resource")
					BufferedReader reader = new BufferedReader(new FileReader("resources/primeNumbers.txt"));
					String line = reader.readLine();
					
					while ((line = reader.readLine()) != null) {
					    primes.add(Double.valueOf(line));
					}
										
					double prk, pbk;
					
					prk = randNum();
					pbk = randNum();
					
					while(prk == pbk) {
						pbk = randNum();
					}
					Client a = new Client("CS 342 Project 5 Client");
					a.start();
					a.createWindow();
					a.setTop("Welcome to the Server");
					a.setMidTop(tuser4.getText());
					a.setPrivateKey(prk);
					a.setPublicKey(pbk);
					lmPrivate.addElement(tuser4.getText());
					userTree.put(tuser4.getText(), true);
				}

			}
			
			else if(tuser4.getText().length()>32) {
				lErr41.setText("User Name is too Long");
				return;
			}

			else {
				lErr41.setText("User Already Exists");
				return;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			lErr41.setText(" ");
			lErr42.setText(" ");
			if (tuser4.getText().matches("[A-Za-z0-9]+")) {
				
				try {
					createUser();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			else if(tuser4.getText().matches("")) {
				lErr41.setText("Enter a Name");
			}
			
			else {
				lErr41.setText("AlphaNumeric Values Only");
			}
		}
	}

	private class CBox implements ItemListener {

		public void setButtons() {
			if (cBox.getState()) {
				lpvk4.setEnabled(true);
				lpbk4.setEnabled(true);
				tpvk4.setEnabled(true);
				tpbk4.setEnabled(true);
			}

			else {
				lpvk4.setEnabled(false);
				lpbk4.setEnabled(false);
				tpvk4.setEnabled(false);
				tpbk4.setEnabled(false);
			}
			return;
		}

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			setButtons();
			// System.out.println("Hello From CBox");
		}

	}

	/****************************************************************
	 * |MenuItems: Game |
	 ****************************************************************/
	private class MIAboutDev implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			JLabel label = new JLabel(
					"<html>CS 342 Program 5: Networked Chat Server<br>Michael Slomczynski (ms11)<br>Margi Gandhi (mgandh9)<br>Liam Edelman (ledelma2)</html>"); 
			label.setFont(new Font("Courier New", Font.PLAIN, 12));
			JOptionPane.showMessageDialog(null, label, "About", JOptionPane.PLAIN_MESSAGE);
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	/****************************************************************
	 * |MenuItems: Help |
	 ****************************************************************/
	private class MIHelpServer implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel label = new JLabel("<html>To get started put a name in the name textbox.<br>From here you have a choice of automatically being assigned a private and public key or picking one yourself.<br>To pick one yourself toggle the Personally Assign Key checkbox.<br>After this you are ready to Sign Up and start chatting.<br><br>In the chat room all you have to do is type your message and hit Send.<br></html>");
			label.setFont(new Font("Courier New", Font.PLAIN, 12));
			JOptionPane.showMessageDialog(null, label, "How to Use the Server", JOptionPane.PLAIN_MESSAGE);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		// run the program here
		// there can only be one instance of the program
		JLabel label = new JLabel(
				"<html>Code is not fully implemented.<br>Things That Work<br>-Can create clients.<br>-Name fill up the chat room.<br>-Cannot have multiple of the same people.<br>-Clients can be assigned private and public keys.<br>-Users can assign personal private and public key within parameters.<br>-The chat room almost works but all messages that any client creates can only be seen by the most recently created client.<br><br>Things That Do Not Work<br>-When a client is delete the chat room does not update.<br>-RSA was designed but not implemented.<br><br></html>"); 
		label.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		CentralServer g = new CentralServer("CS 342 Project 5 Server");
		g.createWindow();
		g.setTop("Networked Chat Server");
		
		JOptionPane.showMessageDialog(null, label, "Not Done", JOptionPane.PLAIN_MESSAGE);
		
		ServerSocket serverSocket = null;
		socket = null;

		try {
			serverSocket = new ServerSocket(1978);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//ArrayList<ServerThread> b = new ArrayList<ServerThread>();
		while (true) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
			}
			//System.out.println("helllo");
			ServerThread a = new ServerThread(socket);
			a.start();

		}
	}
}

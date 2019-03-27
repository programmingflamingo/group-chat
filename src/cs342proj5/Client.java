package cs342proj5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends Thread {
	    private final JFrame window;
	    private final JMenuBar menuBar;

	    private static Socket sock;
	    
	    private final JMenu about;
	    private final JMenu help;

	    private final JButton bSend;

	    private static JScrollPane spChat;
	    private static DefaultListModel<String> lmChat;
	    
	    private final JLabel tDisplay;
	    private final JLabel lMidTop;
	    
	    private double privateKey;
	    private double publicKey;
	    
	    private JTextField tWrite;
	    
	    public Client(String Title) throws UnknownHostException, IOException {
	        window = new JFrame(Title);
	        menuBar = new JMenuBar();
	        
	        sock = new Socket("127.0.0.1", 1978);

	        about = new JMenu("About");
	        help = new JMenu("Help");

	        about.addMouseListener(new MIGameAbout());
	        help.addMouseListener(new MIHelpServer());
	        
	        bSend = new JButton("Send");      
	        bSend.addActionListener(new ButtonSend());
	        
	        lmChat = new DefaultListModel<String>();
	        
		    JList<String> lChat = new JList<String>(lmChat);
	        lChat.setFont(new Font("Courier", Font.PLAIN, 12));
		    
	    	Dimension dTop = new Dimension(200, 40);
	    	Dimension dMid = new Dimension(425, 310);

	        spChat = new JScrollPane(lChat);
	        spChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        spChat.setPreferredSize(dMid);
	        	        
	        tDisplay = new JLabel();
	        tDisplay.setHorizontalAlignment(SwingConstants.CENTER);
	        tDisplay.setFont(new Font("Serif", Font.BOLD, 20));
		    tDisplay.setPreferredSize(dTop);
		    
		    lMidTop = new JLabel(" ");
		    lMidTop.setHorizontalAlignment(SwingConstants.CENTER);
		    lMidTop.setFont(new Font("Serif", Font.BOLD, 14));
	        
		    privateKey = -1.0;
		    publicKey = -1.0;
		    
	        tWrite = new JTextField(50);
	        tWrite.setFont(new Font("Courier", Font.PLAIN, 12));
	    }

	    /****************************************************************
	     |Public Methods                                                 |
	     ****************************************************************/
	    public void setTop(String s) {
	    	tDisplay.setText(s);
	        return;
	    }
	    
	    public void setMidTop(String s) {
	    	lMidTop.setText(s);
	        return;
	    }
	    
	    public void setPrivateKey(double d) {
	    	privateKey = d;
	        return;
	    }
	    
	    public void setPublicKey(double d) {
	    	publicKey = d;
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

	        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        window.setSize(winDim);
	        window.setResizable(false);
	        window.setLocationRelativeTo(null);
	        window.setVisible(true);
	    }

	    /****************************************************************
	     |Private Methods                                                |
	     ****************************************************************/
	    private void addMenuBarToWindow() {
	        menuBar.add(about);
	        menuBar.add(help);

	        window.setJMenuBar(menuBar);
	    }

	    private void addGridToWindow() {
	    	
	    	final JPanel pAll = new JPanel();
		    final JPanel pTop = new JPanel();
		    //final JPanel pLeft = new JPanel();
		    final JPanel pMid = new JPanel();
		    
		    final JPanel pMidTop = new JPanel();
		    final JPanel pMidMid = new JPanel();
		    final JPanel pMidBot = new JPanel();
		    
		    //final JPanel tab1 = new JPanel();
		    
		    pAll.setLayout(new BorderLayout());
		    pTop.setLayout(new BorderLayout());
		    //pLeft.setLayout(new BorderLayout());
		    pMid.setLayout(new BorderLayout());
		    
		    //tab1.add(spPrivate);
		    
		   // pgd.addTab("Chat Room", tab1);
		    
		    pTop.add(tDisplay);
		                
		    //pLeft.add(pgd);
		    
		    pMidTop.add(lMidTop);
		    pMidMid.add(spChat);
		    pMidBot.add(tWrite);
		    pMidBot.add(bSend);

		    pMid.add(pMidTop, BorderLayout.PAGE_START);
		    pMid.add(pMidMid, BorderLayout.CENTER);
		    pMid.add(pMidBot, BorderLayout.PAGE_END);
		    
		    pTop.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		    //pLeft.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		    pMid.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		    
		    pAll.add(pTop, BorderLayout.PAGE_START);
		    //pAll.add(pLeft, BorderLayout.LINE_START);
		    pAll.add(pMid, BorderLayout.CENTER);
		    	    
		    window.add(pAll);
	    }

	    /****************************************************************
	    |Listeners                                                      |
	    ****************************************************************/
	    private class ButtonSend implements ActionListener {

	    	private String checkString(String old) {
	    			    		
	    		String parsedStr = old.replaceAll("(.{57})", "$1<br/>");
	    			
	    		return parsedStr;
	    	}
	    	
	        private String getString() {
	        	
	        	String stepOne = "["+lMidTop.getText()+"]: "+tWrite.getText();
	        	
	        	//System.out.println(stepOne);
	        	
	        	String stepTwo = checkString(stepOne);
	        	
	        	String stepFinal = "<html>" + stepTwo + "</html>"; 

	        	//System.out.println(stepFinal);
	        	
	            return stepFinal;
	        }

	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	
	        	//tWrite.setText(getString());

	        	String a = getString();
	        	tWrite.setText("");
	        	
	        	
	        	Scanner serOutput;
	        	PrintStream p;
	        	
	        	try {
					p = new PrintStream(sock.getOutputStream());
					p.println(a);
					
	        		serOutput = new Scanner(sock.getInputStream());
					a = serOutput.nextLine();
					lmChat.addElement(a);
					
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	        	/*
	        	
	        	InputStream inp = null;
	            BufferedReader br=null;
	            DataOutputStream out = null;

	            
	            try {  	
	            	inp = sock.getInputStream();
	            	br = new BufferedReader(new InputStreamReader(inp));
	            	out = new DataOutputStream(sock.getOutputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            */
	        }
	    }
	    
	    /****************************************************************
	    |MenuItems: Game                                                |
	    ****************************************************************/
	    private class MIGameAbout implements MouseListener {

			@Override
			public void mouseClicked(MouseEvent e) {
	            JLabel label = new JLabel("<html>CS 342 Program 5: Networked Chat Server<br>Michael Slomczynski (ms11)<br>Margi Gandhi (mgandh9)<br>Liam Edelman (ledelma2)</html>"); 
	            label.setFont(new Font("Courier New", Font.PLAIN, 12));
	            JOptionPane.showMessageDialog(null, label, "About", JOptionPane.PLAIN_MESSAGE);
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

	    /****************************************************************
	    |MenuItems: Help                                                |
	    ****************************************************************/
	    private class MIHelpServer implements MouseListener {

			@Override
			public void mouseClicked(MouseEvent e) {
	            JLabel label = new JLabel("<html>To get started put a name in the name textbox.<br>From here you have a choice of automatically being assigned a private and public key or picking one yourself.<br>To pick one yourself toggle the Personally Assign Key checkbox.<br>After this you are ready to Sign Up and start chatting.<br><br>In the chat room all you have to do is type your message and hit Send.<br></html>");
	            label.setFont(new Font("Courier New", Font.PLAIN, 12));
	            JOptionPane.showMessageDialog(null, label, "How to Use the Client", JOptionPane.PLAIN_MESSAGE);
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

	    /****************************************************************
	    |MenuItems: Connection                                          |
	    ****************************************************************/
		/****************************************************************
		 * |RSA Generator |
		 ****************************************************************/
		private class RSA {
			private int p, q, m, n, d, e;

			// 131 is used for block size of 2, for 4 use 16411
			public RSA() {
				int x = 0;
				do {
					do {
						p = (int) (16411 + (Math.random() * ((Integer.MAX_VALUE) / 2 - 16411)));
					} while (!testPrime(p));
					do {
						q = (int) (16411 + (Math.random() * ((Integer.MAX_VALUE) / 2 - 16411)));
					} while (!testPrime(q));
					n = p * q;
				} while (n < 128);
				m = (p - 1) * (q - 1);
				do {
					e = (int) (Math.random() * (n - 2) + 1);
				} while (!testCoPrime(e, m));
				while ((1 + x * m) % e != 0) {
					x++;
				}
				d = (1 + x * m) / e;
			}

			// https://stackoverflow.com/questions/1801391/what-is-the-best-algorithm-for-checking-if-a-number-is-prime
			public boolean testPrime(int num) {
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

			public boolean testCoPrime(int x, int y) {
				int t;
				while (y != 0) {
					t = x;
					x = y;
					y = t % y;
				}
				if (x == 1)
					return true;
				else
					return false;
			}

			/*
			 * Encryption/blocking algorithm (might need to be separate from RSA class)
			 * Message should be 4 characters long. Use substring(int beginIndex, int
			 * endIndex) to split up full message. If message is less than 4, add null
			 * characters (\0) at the end.
			 */

			public int encrypt(String message) {
				int c1 = (int) message.charAt(0);
				int c2 = (int) message.charAt(1);
				int c3 = (int) message.charAt(2);
				int c4 = (int) message.charAt(3);
				c1 = c1;
				c2 = c2 * 128;
				c3 = c3 * (int) Math.pow(128, 2);
				c4 = c4 * (int) Math.pow(128, 3);
				int total = c1 + c2 + c3 + c4;
				int result = ((int) Math.pow(total, e)) % n;

				return result;
			}

			public String decrypt(int block) {
				char[] toString = new char[4];
				int dBlock = ((int) Math.pow(block, d)) % n;
				if ((dBlock / ((int) Math.pow(128, 3))) > 0) {
					double total = (double)dBlock / Math.pow(128, 3);
					total = Math.floor(total);
					toString[3] = (char)total;
					dBlock = dBlock - (int)(total * Math.pow(128, 3));
				}
				if ((dBlock / ((int) Math.pow(128, 2))) > 0) {
					double total = (double)dBlock / Math.pow(128, 2);
					total = Math.floor(total);
					toString[2] = (char)total;
					dBlock = dBlock - (int)(total * Math.pow(128, 2));
				}
				if ((dBlock / 128) > 0) {
					double total = (double)(dBlock / 128);
					total = Math.floor(total);
					toString[1] = (char)total;
					dBlock = dBlock - (int)(total * 128);
				}
				toString[0] = (char)dBlock;
				String message = new String(toString);
				return message;
			}
		}
    
		public static void main(String[] args) throws IOException {
			
			//while(true) {
    		//  serOutput = new Scanner(sock.getInputStream());
			//  a = serOutput.nextLine();
			//  lmChat.addElement(a);
			//}
			//run the program here
			//there can only be one instance of the program
			//Client g = new Client("CS 342 Project 5 Client", null);
			//g.createWindow();
			//g.setTop("Hello World");
			//g.setMidTop("Name");
		  }

	}


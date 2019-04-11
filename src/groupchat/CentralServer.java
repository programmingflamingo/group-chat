package groupchat;

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
import java.util.ArrayList;
import java.util.Random;
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

public class CentralServer {
  private final JFrame window;
  private final JMenuBar menuBar;
  private final JMenu mnuAbout;
  private final JMenu mnuHelp;
  private final JTabbedPane pgd;
  private final JTabbedPane suli;
  private final JButton btnSignUp;
  private final DefaultListModel<String> lmPrivate;
  private final JList<String> lstPrivate;
  private final JScrollPane spPrivate;
  private final JLabel lblDisplay;
  private final JLabel lblErr41;
  private final JLabel lblErr42;
  private final JLabel lblPrivateKey4;
  private final JLabel lblPublicKey4;
  private static JTextField txtUser4;
  private final JTextField txtPrivateKey4;
  private final JTextField txtPublicKey4;
  private final Checkbox chkBox;
  private final double size;
  private static Socket socket;
  // false - disconnected
  // true - online
  private final TreeMap<String, Boolean> userTree;
  
  /**
   * Hello World.
   * 
   */
  public CentralServer(String windowTitle) {
    window = new JFrame(windowTitle);
    menuBar = new JMenuBar();
    mnuAbout = new JMenu("About");
    mnuHelp = new JMenu("Help");
    mnuAbout.addMouseListener(new MenuAboutDev());
    mnuHelp.addMouseListener(new MenuHelpServer());
    pgd = new JTabbedPane();
    suli = new JTabbedPane();
    suli.setPreferredSize(new Dimension(450, 250));
    btnSignUp = new JButton("Sign Up");
    btnSignUp.addActionListener(new ButtonSignUp());
    lmPrivate = new DefaultListModel<String>();
    lstPrivate = new JList<String>(lmPrivate);
    spPrivate = new JScrollPane(lstPrivate);
    spPrivate.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    spPrivate.setPreferredSize(new Dimension(270, 340));
    lblDisplay = new JLabel();
    lblDisplay.setHorizontalAlignment(SwingConstants.CENTER);
    lblDisplay.setFont(new Font("Serif", Font.BOLD, 20));
    lblDisplay.setPreferredSize(new Dimension(200, 40));
    lblPrivateKey4 = new JLabel("   Private Key:    ");
    lblPublicKey4 = new JLabel("   Public Key:    ");
    lblPrivateKey4.setEnabled(false);
    lblPublicKey4.setEnabled(false);
    lblErr41 = new JLabel(" ");
    lblErr42 = new JLabel(" ");
    lblErr41.setForeground(Color.RED);
    lblErr42.setForeground(Color.RED);
    txtUser4 = new JTextField(20);
    txtPrivateKey4 = new JTextField(20);
    txtPublicKey4 = new JTextField(20);
    txtPrivateKey4.setEnabled(false);
    txtPublicKey4.setEnabled(false);
    chkBox = new Checkbox("Personally Assign Keys");
    chkBox.addItemListener(new CBox());
    size = 4.0;
    socket = null;
    userTree = new TreeMap<String, Boolean>();
  }

  public void setTop(String s) {
    lblDisplay.setText(s);
    return;
  }

  /**
   * Hello World.
   */
  public void createWindow() {
    Dimension winDim = new Dimension();
    int width = 800;
    int height = 480;    
    winDim.setSize(width, height);

    addGridToWindow();
    addMenuBarToWindow();

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setSize(winDim);
    window.setResizable(false);
    window.setLocationRelativeTo(null);
    window.setVisible(true);
  }

  private void addMenuBarToWindow() {
    menuBar.add(mnuAbout);
    menuBar.add(mnuHelp);
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
    tab4.add(lblErr41, c);
    c.insets = new Insets(10, 0, 0, 0);
    c.gridx = 1;
    c.gridy = 1;
    tab4.add(lu4, c);
    c.gridx = 2;
    c.gridy = 1;
    tab4.add(txtUser4, c);
    c.insets = new Insets(10, 0, 0, 0);
    c.gridx = 1;
    c.gridy = 2;
    c.gridx = 2;
    c.gridy = 2;
    tab4.add(lblErr42, c);
    c.gridx = 2;
    c.gridy = 3;
    tab4.add(chkBox, c);
    c.gridx = 1;
    c.gridy = 4;
    tab4.add(lblPrivateKey4, c);
    c.gridx = 2;
    c.gridy = 4;
    tab4.add(txtPrivateKey4, c);
    c.gridx = 1;
    c.gridy = 5;
    tab4.add(lblPublicKey4, c);
    c.gridx = 2;
    c.gridy = 5;
    tab4.add(txtPublicKey4, c);
    c.anchor = GridBagConstraints.EAST;
    c.gridx = 2;
    c.gridy = 6;
    tab4.add(btnSignUp, c);
    suli.addTab("Sign Up", tab4);
    pTop.add(lblDisplay);
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

  private class ButtonSignUp implements ActionListener {
    private ArrayList<Double> primes;// = new ArrayList<Double>();
    // https://stackoverflow.com/questions/1801391/what-is-the-best-algorithm-for-checking-if-a-number-is-prime
    
    private boolean testPrime(double num) {
      if (num == 2) {
        return true;
      }
      if (num == 3) {
        return true;
      }
      if (num % 2 == 0) {
        return false;
      }
      if (num % 3 == 0) {
        return false;
      }
      
      int i = 5;
      int w = 2;

      while (i * i <= num) {
        if (num % i == 0) {
          return false;
        }
        
        i += w;
        w = 6 - w;
      }
      
      return true;
    }
    
    private boolean isValidKeys() {    
      if (!txtPrivateKey4.getText().matches("-?\\d+(\\.\\d+)?")) {
        return false;
      }
      
      if (!txtPublicKey4.getText().matches("-?\\d+(\\.\\d+)?")) {
        return false;
      }
      
      double p1 = Double.parseDouble(txtPrivateKey4.getText());
      double p2 = Double.parseDouble(txtPublicKey4.getText());
      
      if (!testPrime(p1) || !testPrime(p2)) {
        return false;
      }
      
      if (p1 * p2 > Math.pow(128.0, size)) {
        return true;
      }
      
      return false;
    }
    
    private double randNum() {
      Random rand = new Random();
      Double num = primes.get(rand.nextInt(primes.size()));
      return num;
    }
    
    private void createUser() throws IOException {
      if (!userTree.containsKey(txtUser4.getText()) && txtUser4.getText().length() <= 32) {
        if (chkBox.getState()) {
          //System.out.println("true1");
          if (!isValidKeys()) {
            lblErr42.setText("Invalid Primes");
            return;
          }
          //System.out.println("true2");
          Client a = new Client("CS 342 Project 5 Client");
          a.start();
          a.createWindow();
          a.setTop("Welcome to the Server");
          a.setMidTop(txtUser4.getText());
          a.setPrivateKey(Double.parseDouble(txtPrivateKey4.getText()));
          a.setPublicKey(Double.parseDouble(txtPublicKey4.getText()));
          lmPrivate.addElement(txtUser4.getText());
          userTree.put(txtUser4.getText(), true);
        } else {
          //System.out.println("false");
          primes = new ArrayList<Double>();
          
          @SuppressWarnings("resource")
          BufferedReader reader = new BufferedReader(new FileReader("resources/primeNumbers.txt"));
          String line = reader.readLine();
          while ((line = reader.readLine()) != null) {
            primes.add(Double.valueOf(line));
          }
          
          double privateKey = randNum();
          double publicKey = randNum();
          
          while (privateKey == publicKey) {
            publicKey = randNum();
          }
          
          Client a = new Client("CS 342 Project 5 Client");
          a.start();
          a.createWindow();
          a.setTop("Welcome to the Server");
          a.setMidTop(txtUser4.getText());
          a.setPrivateKey(privateKey);
          a.setPublicKey(publicKey);
          lmPrivate.addElement(txtUser4.getText());
          userTree.put(txtUser4.getText(), true);
        }
      } else if (txtUser4.getText().length() > 32) {
        lblErr41.setText("User Name is too Long");
        return;
      } else {
        lblErr41.setText("User Already Exists");
        return;
      }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      lblErr41.setText(" ");
      lblErr42.setText(" ");
      if (txtUser4.getText().matches("[A-Za-z0-9]+")) {
        try {
          createUser();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else if (txtUser4.getText().matches("")) {
        lblErr41.setText("Enter a Name");
      } else {
        lblErr41.setText("AlphaNumeric Values Only");
      }
    }
  }

  private class CBox implements ItemListener {
    public void setButtons() {
      if (chkBox.getState()) {
        lblPrivateKey4.setEnabled(true);
        lblPublicKey4.setEnabled(true);
        txtPrivateKey4.setEnabled(true);
        txtPublicKey4.setEnabled(true);
      } else {
        lblPrivateKey4.setEnabled(false);
        lblPublicKey4.setEnabled(false);
        txtPrivateKey4.setEnabled(false);
        txtPublicKey4.setEnabled(false);
      }
      return;
    }
    
    @Override
    public void itemStateChanged(ItemEvent arg0) {
      setButtons();
      // System.out.println("Hello From CBox");
    }
  }

  private class MenuAboutDev implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent arg0) {
      JLabel label = new JLabel("<html>CS 342 Program 5: Networked Chat Server<br>Michael Slomczynski (ms11)<br>Margi Gandhi (mgandh9)<br>Liam Edelman (ledelma2)</html>"); 
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


  private class MenuHelpServer implements MouseListener {
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

  /**
   *  Hello World.
   */
  @SuppressWarnings("resource")
  public static void main(String[] args) throws IOException {
    // run the program here
    // there can only be one instance of the program
    JLabel label = new JLabel("<html>Code is not fully implemented.<br>Things That Work<br>-Can create clients.<br>-Name fill up the chat room.<br>-Cannot have multiple of the same people.<br>-Clients can be assigned private and public keys.<br>-Users can assign personal private and public key within parameters.<br>-The chat room almost works but all messages that any client creates can only be seen by the most recently created client.<br><br>Things That Do Not Work<br>-When a client is delete the chat room does not update.<br>-RSA was designed but not implemented.<br><br></html>"); 
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
      
      //System.out.println("hello");
      ServerThread a = new ServerThread(socket);
      a.start();
    }
  }
}

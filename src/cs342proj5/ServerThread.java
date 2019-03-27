package cs342proj5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {
	
    private Socket socket = null;

    public ServerThread(Socket s) {
        socket = s;
    }
    
    
	public void run() {
        InputStream cliIn = null;
        BufferedReader br = null;
        DataOutputStream serOut = null;
        /*	
		try {


			//serOutput = new Scanner(sock.getInputStream());
			//String temp = serOutput.nextLine();
			//lmChat.addElement(temp);
			
			//brin = new BufferedReader(new PrintStream(p));
		
			p = new PrintStream(sock.getOutputStream());
			p.println(tWrite.getText());
			
    		@SuppressWarnings("resource")
			Scanner serOutput = new Scanner(sock.getInputStream());
    		String temp = serOutput.nextLine();
    		
    		//System.out.println(temp);
            lmChat.addElement(temp);

            
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
	            */							
        try {
        	cliIn = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(cliIn));
			serOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

        String line;
		while(true) {
        	try {
        		line = br.readLine();
        		//System.out.println(line);
        		if (line == null) {
        			socket.close();
        			return;
        		} else {
        			serOut.writeBytes(line + "\n\r");
        			serOut.flush();
        		}
        	} catch (IOException e) {
        		e.printStackTrace();
        		return;
        	}
		}
		
	}
}

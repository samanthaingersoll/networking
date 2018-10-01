package proxy;

import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyThread extends Thread {
    private Socket socket = null;
    public BufferedWriter writer;
    public int byteSize;
    private static final int BUFFER_SIZE = 32768;
    
    public ProxyThread(Socket socket) {
        super("ProxyThread");
        this.socket = socket;
        try 
        {
        	String file = "proxy.log";
        	writer = new BufferedWriter(new FileWriter(file, true));
        
        }
        catch (IOException e) {
        	System.err.print(e);
        }
    }
    @SuppressWarnings("deprecation")
    public synchronized void fileWrite(String url, Long ID) {
    	try {
    		
    		writer.append("Thread ID: " + ID.toString() + "\n");
    		writer.append("Date and Time: " + new Date().toLocaleString() + "\n");
    		writer.append("IP Address: " + InetAddress.getLocalHost().toString() + "\n");
    		writer.append("Request for: " + url + "\n");
    		writer.append("Request size: " + byteSize + "\n");
    		writer.append("Port: " + 10000 + "\n");
    		
    		writer.close();
    	}
    	catch (IOException e) {
    		System.err.println(e);
    	}
    }

    public void run() {

        try {
            DataOutputStream out =
		new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(
		new InputStreamReader(socket.getInputStream()));

            String inputLine;
            String outputLine;
            int cnt = 0;
            String urlToCall = "";

            while ((inputLine = in.readLine()) != null) {
                try {
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                } catch (Exception e) {
                    break;
                }
                if (cnt == 0) {
                    String[] tokens = inputLine.split(" ");
                    urlToCall = tokens[1];
                    System.out.println("Request for : " + urlToCall);
                }

                cnt++;
            }

            BufferedReader rd = null;
            try {
                URL url = new URL(urlToCall);
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(false);

                InputStream is = null;
                HttpURLConnection huc = (HttpURLConnection)conn;
                if (conn.getContentLength() > 0) {
                    try {
                        is = conn.getInputStream();
                        rd = new BufferedReader(new InputStreamReader(is));
                    } catch (IOException ioe) {
                        System.out.println(
				"********* IO EXCEPTION **********: " + ioe);
                    }
                }

                byte by[] = new byte[ BUFFER_SIZE ];
                int index = is.read( by, 0, BUFFER_SIZE );
                
                byteSize = index;
                this.fileWrite(urlToCall, Thread.currentThread().getId());
                
                while ( index != -1 )
                {
                  out.write( by, 0, index );
                  index = is.read( by, 0, BUFFER_SIZE );
                }
                out.flush();

            } catch (Exception e) {
                System.err.println("Encountered exception: " + e);

                out.writeBytes("");
            }

            if (rd != null) {
                rd.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

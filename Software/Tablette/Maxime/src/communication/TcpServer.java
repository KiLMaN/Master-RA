package communication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import log.Log;
 
public class TcpServer extends Thread implements Runnable {
 
    public int _recievePort;
    private boolean running = false;
    private PrintWriter mOut;
 
    
    public TcpServer(int recievePort) {
        this._recievePort = recievePort;
    }

    
    public void sendMessage(String message){
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
        }
    }
 
    public void onMessageRecieve(Socket client, String content){
    	Log.i("Recieve message: " + content + " from " + client.getLocalAddress().toString());
    }
    
    @Override
    public void run() {
        running = true;
 
        try {
            Log.d("Connecting (Port = "+ _recievePort + ")...");
 
            ServerSocket serverSocket = new ServerSocket(_recievePort);

            Socket client = serverSocket.accept();
            Log.d("Receiving...");
 
            try {
 
                //sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
 
                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
 
                while (running) {
                    String message = in.readLine();
 
                    if (message != null) {
                    	onMessageRecieve( client, message);
                    }
                }
 
            } catch (Exception e) {
                Log.e("Error " + e.getMessage());
                e.printStackTrace();
            } finally {
                client.close();
            }
 
        } catch (Exception e) {
        	Log.e("Error " + e.getLocalizedMessage());
            e.printStackTrace();
        }
 
    }
}


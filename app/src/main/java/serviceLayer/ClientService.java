package serviceLayer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class ClientService {

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader inreader;
    private PrintWriter outwriter;
    private final String TAG = "Client";

    public ClientService() {
        Settings setting = new Settings();
        String remoteHost = setting.getRemoteHost();
        int remotePort = setting.getRemotePort();

        try {
            socket = new Socket();
            SocketAddress remoteAdd = new InetSocketAddress(remoteHost, remotePort);
            socket.connect(remoteAdd, 10000);  //set 10 seconds timeout for connection
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String str) {
        try {
            out=socket.getOutputStream();  //the output stream
            outwriter=new PrintWriter(out);
            outwriter.write(str);
            outwriter.flush();
            if (!socket.isClosed()){
                socket.shutdownOutput();  //close the output stream
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getData() {
        String data = "";
        try {
            in=socket.getInputStream();  //the input stream
            inreader = new BufferedReader(new InputStreamReader(in));
            String line="";
            Log.d(TAG, "Receiving data from server.");
            while((line=inreader.readLine())!=null){
                System.out.println("Server："+line);
                Log.d(TAG, "Receiving data: "+line);
                data = data + line +"/n";
            }
            in.close();
            inreader.close();
            if (!socket.isClosed()) {
                socket.shutdownInput();  //close the input stream
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void closeCon() {
        try {
            socket.close();  //close the socket
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

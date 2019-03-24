package serviceLayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientService {

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader inreader;
    private PrintWriter outwriter;

    public ClientService() {
        Settings setting = new Settings();
        String remoteHost = setting.getRemoteHost();
        int remotePort = setting.getRemotePort();
        try {
            socket = new Socket(remoteHost, remotePort);
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
            socket.shutdownOutput();  //close the output stream
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
            while((line=inreader.readLine())!=null){
                System.out.println("Server："+line);
                data = data + line +"/n";
            }
            in.close();
            inreader.close();
            socket.shutdownInput();  //close the input stream
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
        }
    }
}

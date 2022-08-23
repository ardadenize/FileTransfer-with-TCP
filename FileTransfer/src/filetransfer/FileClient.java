/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class FileClient {
    
    
private static Socket sock;
    private static String fileName;
    private static BufferedReader stdin;
    private static PrintStream os;
    
    
    

    public static void main(String[] args) throws IOException {
while(true) {
    
    
        try {
            sock = new Socket("localhost", 8080);
            stdin = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Cannot connect to the server, try again later.");
            System.exit(1);
        }
        os = new PrintStream(sock.getOutputStream());
                
        try {
              switch (Integer.parseInt(selectAction())) {
            case 1:
                os.println("1");
                sendFile();
                continue;
            case 2:
                os.println("2");
                System.out.print("Enter file name: ");
                fileName = stdin.readLine();
                os.println(fileName);
                receiveFile(fileName);
                continue;
	   case 3:
		 os.println("3");
                 sendFileUDP();
		continue;
           case 4:
		 sock.close();
		System.exit(1);
        }
        } catch (Exception e) {
            System.err.println("not valid input");
        }

}
       
    }

    public static String selectAction() throws IOException {
        System.out.println("1. Send file.");
        System.out.println("2. Recieve file.");
	System.out.println("3. Send File With UDP.");
        System.out.println("4. Exit.");
        System.out.print("\nMake selection: ");

        return stdin.readLine();
    }


    public static void sendFile() {
        try {
            System.out.print("Enter file name: ");
            fileName = stdin.readLine();

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
	    if(!myFile.exists()) {
		System.out.println("File does not exist..");
		return;
		}

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);
		
            OutputStream os = sock.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            long current = 0;
             int size = 10000;
            long fileLength = myFile.length();
            
            while(current!=fileLength){ 
                
            if(fileLength - current >= size)
 current += size;
 else{
 size = (int)(fileLength - current);
 current = fileLength;
 }
            
            System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
            }
            System.out.println("File "+fileName+" sent to Server.");
        } catch (Exception e) {
            System.err.println("Exceptionnnn: "+e);
        }
    }
    
    public static void sendFileUDP() {
        try {
            System.out.print("Enter file name: ");
             fileName = stdin.readLine();
             
             System.out.println("File "+fileName+" sent to Server.");
             
                    

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
             OutputStream os = sock.getOutputStream();
             DataOutputStream dos = new DataOutputStream(os);
             
             dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            
              
             if(!myFile.exists()) {
		System.out.println("File does not exist..");
		return;
		}
         byte b[]=new byte[3072];
                        DatagramSocket dsoc=new DatagramSocket(1000);
                        FileOutputStream f=new FileOutputStream(myFile);
                        while(true)
                        {
                                    DatagramPacket dp=new DatagramPacket(b,b.length);
                                    dsoc.receive(dp);
                                    System.out.println(new String(dp.getData(),0,dp.getLength()));                             

                        }
                        
                       
            
            
             
            
        } catch (Exception e) {
            System.err.println("Exceptionnnn: "+e);
        }
    }

    public static void receiveFile(String fileName) {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(fileName);
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            in.close();

            System.out.println("File "+fileName+" received from Server.");
        } catch (IOException ex) {
		System.out.println("Exception: "+ex);
         }
    
}
    private void simulateConcurrentClientRequest(LoadBalancer loadBalancer, int numOfCalls) {

        IntStream
                .range(0, numOfCalls)
                .parallel()
                .forEach(i ->
                        System.out.println(
                                "IP: " + loadBalancer.getIp()
                                + " --- Request from Client: " + i
                                + " --- [Thread: " + Thread.currentThread().getName() + "]")
                );
    }
    
}

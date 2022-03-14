package ServerSocket;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Helloworld");
//        String HTML = "./src/ServerSocket/";
        try {
            // Inet4Address inet4Address = (Inet4Address) Inet4Address.getLoopbackAddress();
            InputStream inS;
            OutputStream outS;
            String websiteroot = "F:\\Toogas\\Tahun 3\\Progjar\\server1\\";
            System.out.println(1 + ". Create server and client socket");
            // ServerSocket serverSocket = new ServerSocket(6666, 5, inet4Address);
            ServerSocket serverSocket = new ServerSocket(80);
            System.out.println(2);

            while(true){
                Socket client = serverSocket.accept();
                inS = client.getInputStream();
                outS = client.getOutputStream();
                System.out.println(3);

                System.out.println(4 + ". Obtain BufferedReader and BufferedWriter");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inS));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outS));

                String message = bufferedReader.readLine();
                System.out.println(message);
                String urn = new String();
                if(message != null){
                    urn = message.split(" ")[1].substring(1);
                }
//                System.out.println(urn);
                while (message != null && !message.isEmpty()) {
//                    System.out.println(message);
                    message = bufferedReader.readLine();
                }
                String fileContent;
                String statusCode;
                try {
                    String extension = new String();
                    if(urn != null && !urn.isEmpty()){
                        System.out.println(urn);
                        extension = urn.split("[.]")[1];
                    }
                    System.out.println(extension);
                    if (extension.equalsIgnoreCase("png")){
                        File file = new File(websiteroot + urn);
                        FileInputStream fis = new FileInputStream(file);
                        System.out.println("sampek sini");
                        byte[] data = new byte[(int) file.length()];
                        fis.read(data);
                        fis.close();

                        DataOutputStream binaryOut = new DataOutputStream(outS);
                        binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
                        binaryOut.writeBytes("Content-Type: image/png\r\n");
                        binaryOut.writeBytes("Content-Length: " + data.length + "\r\n");
                        binaryOut.writeBytes("Content-Disposition: attachment; filename=\"" + urn + "\"");
                        binaryOut.writeBytes("\r\n\r\n");
                        binaryOut.write(data);
                        binaryOut.flush();
                        binaryOut.close();
                    }else if(extension.equalsIgnoreCase("pdf")){
                        File file = new File(websiteroot + urn);
                        FileInputStream fis = new FileInputStream(file);
                        System.out.println("sampek sini");
                        byte[] data = new byte[(int) file.length()];
                        fis.read(data);
                        fis.close();

                        DataOutputStream binaryOut = new DataOutputStream(outS);
                        binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
                        binaryOut.writeBytes("Content-Type: application/pdf\r\n");
                        binaryOut.writeBytes("Content-Length: " + data.length + "\r\n");
                        binaryOut.writeBytes("Content-Disposition: attachment; filename=\"" + urn + "\"");
                        binaryOut.writeBytes("\r\n\r\n");
                        binaryOut.write(data);
                        binaryOut.flush();
                        binaryOut.close();
                    }else{
                        FileInputStream fileInputStream = new FileInputStream(websiteroot + urn);
                        fileContent = new String(fileInputStream.readAllBytes());
                        statusCode = "200 OK";
                        bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + fileContent.length() + "\r\n");
                        bufferedWriter.write("\r\n");
                        bufferedWriter.write(fileContent);
                        bufferedWriter.flush();
                    }
                } catch (FileNotFoundException e) {
                    FileInputStream fileInputStream = new FileInputStream(websiteroot + "NotFound.html");
                    fileContent = new String(fileInputStream.readAllBytes());
                    statusCode = "404 File Not Found";

                    bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: html\r\nContent-Length: " + fileContent.length() + "\r\n");
                    bufferedWriter.write("\r\n");
                    bufferedWriter.write(fileContent);
                    bufferedWriter.flush();
                }


                client.close();
            }

//            System.out.println("Message from client: " + message);
//
//            System.out.println(6 + ". Close the server");
//            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

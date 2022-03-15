package ServerSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream inS;
            OutputStream outS;
            String conf = "\\src\\ServerSocket\\file.conf";

            String confPath = new File("").getAbsolutePath().concat(conf);
            System.out.println(confPath);

            Config config = new Config(confPath);

            String websiteroot = config.getRoot1();

            System.out.println(1 + ". Create server and client socket");
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(config.getPort()),5, InetAddress.getByName(config.getIp()));
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

//                System.out.println(msg);
                String urn = "";
                String domain = "";

                if(message != null){
                    urn = message.split(" ")[1].substring(1);
                }
                System.out.println("domain: "+message);
                int i = 0;
                String connection="";
                while (message != null && !message.isEmpty()) {
//                    System.out.println("i = "+ i + " " + message);
                    if(i==1) {
                        System.out.println(message.substring(message.indexOf(" ") + 1));
                        domain = message.substring(message.indexOf(" ") + 1);
                    }
                    if(i==2){
                        int conIndex = ("Connection: ").length();
                        connection = message.substring(conIndex);
//                        System.out.println("CONNECTION = "+connection);
                    }
                    message = bufferedReader.readLine();
                    i++;

                }
                String fileContent;
                String statusCode;

                if(domain.equalsIgnoreCase("progjar.com")){
                    System.out.println("Masuk server 2");
                    websiteroot = config.getRoot2();
                }
                try {
                    String extension = "";
                    if(urn != null && !urn.isEmpty() && urn.contains(".")){
                        System.out.println("URN: "+urn);
                        extension = urn.split("[.]")[1];
                    }

                    if(urn.isEmpty()){
                        System.out.println("null URN");
                    }
//                    System.out.println("extension: "+urn);
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
                        if(urn.isEmpty()){
                            File files = new File(websiteroot + urn);
                            String[] fileList = files.list();
                            StringBuilder response = new StringBuilder("""
                                <!DOCTYPE html>
                                <html lang="en">
                                <head>
                                    <meta charset="UTF-8">
                                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                    <link rel="icon" type="image/x-icon" href="https://pngimg.com/uploads/letter_p/letter_p_PNG46.png">
                                    <title>FILE LIST</title>
                
                                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
                                </head>
                                <body>
                                <div class="container">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>File/Folder Name</th>
                                        <th>Type</th>
                                        <th>Last Modified</th>
                                        <th>Size (Byte)</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                            """);
                            assert fileList != null;
                            boolean flag = false;
                            for(String fileName : fileList) {
                                System.out.println("filename : "+fileName);
                                if(fileName.equalsIgnoreCase("index.html")){
                                    flag = true;
                                }
                                response.append("    <tr>\n");
                                File file = new File(files+"/"+fileName);
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                String lastModified = sdf.format(file.lastModified());
//                                if(urn.charAt(urn.length()-1)=='/')
//                                    urn = urn.substring(0, urn.length()-1);
                                response.append("        <td><a href=\"/").append(fileName).append(file.isFile() ? "" : "/").append("\">").append(fileName).append("</a></td>\n");
                                response.append(file.isFile() ? "        <td>File</td>\n" : "        <td>Folder</td>\n");
                                response.append("        <td>").append(lastModified).append("</td>\n");
                                response.append("        <td>").append(Files.size(file.toPath())).append("</td>\n");

                                response.append("    </tr>\n");
                            }

                            response.append("""
                                </tbody>
                                </table>
                                </div>
                                </body>
                            </html>""");

                            statusCode = "200 OK";
                            bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + response.toString().length() + "\r\n");
                            bufferedWriter.write("\r\n");
                            if(!flag){
                                bufferedWriter.write(response.toString());
                            }else{
                                FileInputStream fileInputStream = new FileInputStream(websiteroot + "index.html");
                                fileContent = new String(fileInputStream.readAllBytes());
                                bufferedWriter.write(fileContent);
                            }
                            bufferedWriter.flush();
                        }else if(urn.charAt(urn.length()-1)=='/'){
                            System.out.println("Masuk Folder");
                            File files = new File(websiteroot + urn);
                            String[] fileList = files.list();
                            StringBuilder response = new StringBuilder("""
                                <!DOCTYPE html>
                                <html lang="en">
                                <head>
                                    <meta charset="UTF-8">
                                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                    <link rel="icon" type="image/x-icon" href="https://pngimg.com/uploads/letter_p/letter_p_PNG46.png">
                                    <title>FILE LIST</title>
                
                                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
                                </head>
                                <body>
                                <div class="container">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>File/Folder Name</th>
                                        <th>Type</th>
                                        <th>Last Modified</th>
                                        <th>Size (Byte)</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                            """);
                            assert fileList != null;
                            for(String fileName : fileList) {
                                response.append("    <tr>\n");
                                File file = new File(files+"/"+fileName);
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                String lastModified = sdf.format(file.lastModified());
//                                if(urn.charAt(urn.length()-1)=='/')
//                                    urn = urn.substring(0, urn.length()-1);
                                response.append("        <td><a href=\"/").append(urn).append(fileName).append(file.isFile() ? "" : "/").append("\">").append(fileName).append("</a></td>\n");
                                response.append(file.isFile() ? "        <td>File</td>\n" : "        <td>Folder</td>\n");
                                response.append("        <td>").append(lastModified).append("</td>\n");
                                response.append("        <td>").append(Files.size(file.toPath())).append("</td>\n");

                                response.append("    </tr>\n");
                            }

                            response.append("""
                                </tbody>
                                </table>
                                </div>
                                </body>
                            </html>""");

                            statusCode = "200 OK";
                            bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + response.toString().length() + "\r\n");
                            bufferedWriter.write("\r\n");
                            bufferedWriter.write(response.toString());
                            bufferedWriter.flush();
                        }else{
                            FileInputStream fileInputStream = new FileInputStream(websiteroot + urn);
                            fileContent = new String(fileInputStream.readAllBytes());
                            statusCode = "200 OK";
                            bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + fileContent.length() + "\r\n");
                            bufferedWriter.write("\r\n");
                            bufferedWriter.write(fileContent);
                            bufferedWriter.flush();
                        }
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
                if(connection.equals("close"))
                    client.close();
            }
//            System.out.println("Message from client: " + message);
//            System.out.println(6 + ". Close the server");
//            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ServerSocket;

import java.io.*;

public class Config {
    private String port;
    private String ip;
    private String root1;
    private String root2;

    public Config(String configPath) throws FileNotFoundException {
        try {
            FileReader fr = new FileReader(configPath);
            BufferedReader br = new BufferedReader(fr);

            String st;
            st = br.readLine();

            while (st != null) {
                if(st.contains("Port")) {
                    int portIndex = ("Port ").length();
                    String port = st.substring(portIndex);
                    this.setPort(port);
                }

                if(st.contains("IP")) {
                    int ipIndex = ("IP ").length();
                    String ip = st.substring(ipIndex);
                    this.setIp(ip);
                }

                if(st.contains("Root1")) {
                    int rootIndex = ("Root1 ").length();
                    String root1 = st.substring(rootIndex);

                    root1 = root1.substring(root1.indexOf("\"")+1);
                    root1 = root1.substring(0, root1.indexOf("\""));

                    this.setRoot1(root1);
                }
                if(st.contains("Root2")) {
                    int rootIndex = ("Root2 ").length();
                    String root2 = st.substring(rootIndex);

                    root2 = root2.substring(root2.indexOf("\"")+1);
                    root2 = root2.substring(0, root2.indexOf("\""));

                    this.setRoot2(root2);
                }

                st = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRoot1() {
        return root1;
    }
    public String getRoot2() {
        return root2;
    }

    public void setRoot1(String root1) {
        this.root1 = root1;
    }
    public void setRoot2(String root2) {
        this.root2 = root2;
    }
}

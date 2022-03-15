package ServerSocket;

import java.io.*;

public class Config {
    private String port;
    private String ip;
    private String root;

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

                if(st.contains("Root")) {
                    int rootIndex = ("Root ").length();
                    String root = st.substring(rootIndex);

                    root = root.substring(root.indexOf("\"")+1);
                    root = root.substring(0, root.indexOf("\""));

                    this.setRoot(root);
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

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}

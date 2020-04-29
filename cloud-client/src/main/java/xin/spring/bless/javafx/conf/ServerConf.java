package xin.spring.bless.javafx.conf;

public class ServerConf {

    private String httpAddr;

    public String getHttpAddr() {
        return httpAddr;
    }

    public void setHttpAddr(String httpAddr) {
        this.httpAddr = httpAddr;
    }

    @Override
    public String toString() {
        return "ServerConf{" +
                "httpAddr='" + httpAddr + '\'' +
                '}';
    }
}

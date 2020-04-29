package xin.spring.bless.javafx.conf;

/**
 * 配置文件
 */
public class FastDfsServer {

    public FastDfsServer(){
    }

    private ServerConf server;

    public ServerConf getServer() {
        return server;
    }

    public void setServer(ServerConf server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "FastDfsServer{" +
                "server=" + server +
                '}';
    }
}

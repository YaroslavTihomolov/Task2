package nsu.ccfit.ru.tihomolov.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class ConnectService {
    private final ServerSocket serverSocket;

    public ConnectService(ServerMetaInf serverMetaInf) throws IOException {
        this.serverSocket = new ServerSocket(serverMetaInf.port());
    }

    public void execute() {
        try(ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool()) {
            log.info("Server start working");
            while (true) {
                    Socket socket = serverSocket.accept();
                    log.info("Get new client: " + socket.getInetAddress());
                    executor.submit(new Server(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

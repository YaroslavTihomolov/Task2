package nsu.ccfit.ru.tihomolov;

import nsu.ccfit.ru.tihomolov.server.ConnectService;
import nsu.ccfit.ru.tihomolov.server.ServerMetaInf;

import java.io.IOException;

public class ServerApplication {
    public static void main(String[] args) throws IOException {
        ConnectService connectService = new ConnectService(new ServerMetaInf(Integer.parseInt(args[0])));
        connectService.execute();
    }
}

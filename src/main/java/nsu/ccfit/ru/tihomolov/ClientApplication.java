package nsu.ccfit.ru.tihomolov;


import nsu.ccfit.ru.tihomolov.client.Client;
import nsu.ccfit.ru.tihomolov.client.ClientMetaInf;

public class ClientApplication {
    public static void main(String[] args) {
        try(Client client = new Client(new ClientMetaInf(args[0], args[1], Integer.parseInt(args[2])))) {
            client.sendFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package nsu.ccfit.ru.tihomolov.client;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.tihomolov.context.FileInfo;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import static nsu.ccfit.ru.tihomolov.context.Context.BUFFER_SIZE;
import static nsu.ccfit.ru.tihomolov.context.Context.TRANSFER_SUCCESS;

@Slf4j
public class Client implements AutoCloseable {
    private final Socket socket;
    private final FileInputStream fileInputStream;
    private final File file;
    private final ObjectOutputStream objectOutputStream;
    private final OutputStream outputStream;

    public Client(ClientMetaInf clientMetaInf) throws IOException {
        log.info("Init client");
        this.socket = new Socket(InetAddress.getByName(clientMetaInf.ip()), clientMetaInf.port());
        this.file = new File(clientMetaInf.pathToFile());
        this.fileInputStream =  new FileInputStream(file);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream = socket.getOutputStream();
    }

    public void sendFile() throws IOException {
        objectOutputStream.writeObject(new FileInfo(file.getName(), file.length()));
        objectOutputStream.flush();

        while (fileInputStream.available() != 0) {
            outputStream.write(fileInputStream.readNBytes(BUFFER_SIZE));
            outputStream.flush();
        }
    }

    @Override
    public void close() throws Exception {
        objectOutputStream.close();
        fileInputStream.close();
        socket.close();
    }
}

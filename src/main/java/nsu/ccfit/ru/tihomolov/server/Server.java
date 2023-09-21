package nsu.ccfit.ru.tihomolov.server;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.tihomolov.context.FileInfo;
import java.io.*;
import java.net.Socket;

import static nsu.ccfit.ru.tihomolov.context.Context.*;

@Slf4j
public class Server implements Runnable, AutoCloseable {
    private final Socket socket;
    private final InputStream inputStream;
    private final ObjectInputStream objectInputStream;

    public Server(Socket socket) {
        try {
            this.socket = socket;
            this.inputStream = socket.getInputStream();
            this.socket.setSoTimeout(TIMEOUT);
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFile(File file) {
        try {
            if (file.createNewFile()) {
                log.info("File was successfully created");
            } else {
                log.warn("File was not created");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeData(File file) {
        byte[] buffer = new byte[BUFFER_SIZE];
        SpeedScheduler speedScheduler = null;
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            int byteCounts;
            speedScheduler = new SpeedScheduler();
            speedScheduler.initSpeedTracker();

            while ((byteCounts = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteCounts);
                speedScheduler.updateData(byteCounts);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            if (speedScheduler != null) {
                speedScheduler.stopSpeedTracker();
            }
        }
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    @Override
    public void run() {
        try {
            FileInfo fileInfo = (FileInfo) objectInputStream.readObject();
            log.info("File name: " + fileInfo.name());
            log.info("File size: " + fileInfo.size());

            File file = new File(fileInfo.name());
            createFile(file);
            writeData(file);
            log.info("File successfully request");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

package nsu.ccfit.ru.tihomolov.context;

import java.io.Serializable;

public record FileInfo(String name, Long size) implements Serializable { }

package com.commerce.api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class GeradorPedidoCodigo {

    public static String novoCodigo() {
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String timestamp = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss").format(LocalDateTime.now());
        return uuid + timestamp;
    }
}

package com.github.carloscontrerasruiz.client.interceptors;

import io.grpc.Metadata;

public class ClientConstants {

    public static final Metadata.Key<String> USER_TOKEN_KEY = Metadata.Key.of("user-token",
            Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata METADATA = new Metadata();

    static {
        METADATA.put(
                Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER),
                "bank-client-secret"
        );
    }

    public static Metadata getClientToken() {
        return METADATA;
    }
}

package com.github.carloscontrerasruiz.ssl;

import com.github.carloscontrerasruiz.service.BankService;
import com.github.carloscontrerasruiz.service.PersonImpl;
import com.github.carloscontrerasruiz.service.TransferService;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GrpcServerSsl {
    public static void main(String[] args) throws IOException, InterruptedException {
        SslContext sslContext = null;
        try {
            URL resourceCrt = GrpcServerSsl.class.getClassLoader().getResource("ssl/localhost.crt");
            URL resourcePem = GrpcServerSsl.class.getClassLoader().getResource("ssl/localhost.pem");
            sslContext = GrpcSslContexts.configure(
                    SslContextBuilder.forServer(
                            new File(resourceCrt.toURI()),
                            new File(resourcePem.toURI())
                    )
            ).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Server server = NettyServerBuilder.forPort(6565)
                .sslContext(sslContext)
                .addService(new PersonImpl())
                .addService(new BankService())
                .addService(new TransferService())
                .build();
        server.start();
        server.awaitTermination();
    }
}

package com.github.carloscontrerasruiz.client.interceptors;

import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.concurrent.Executor;

public class UserSessionToken extends CallCredentials {

    private String jwt;

    public UserSessionToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(()->{
            Metadata metadata = new Metadata();
            metadata.put(ClientConstants.USER_TOKEN_KEY,this.jwt);
            metadataApplier.apply(metadata);
        });


    }

    @Override
    public void thisUsesUnstableApi() {

    }
}

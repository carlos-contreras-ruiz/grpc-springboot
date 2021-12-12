package com.github.carloscontrerasruiz.service;

import com.github.carloscontrerasruiz.proto.TransferRequest;
import com.github.carloscontrerasruiz.proto.TransferResponse;
import com.github.carloscontrerasruiz.proto.TransferServiceGrpc;
import com.github.carloscontrerasruiz.transferImpl.TransferStreamingRequest;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}

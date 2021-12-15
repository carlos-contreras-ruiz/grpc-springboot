package com.github.carloscontrerasruiz.client.bank;

import com.github.carloscontrerasruiz.client.interceptors.ClientConstants;
import com.github.carloscontrerasruiz.proto.Money;
import com.github.carloscontrerasruiz.proto.ResponseErrorHandling;
import com.github.carloscontrerasruiz.proto.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class ResponseHandlerOneOfStreamingResponse implements StreamObserver<ResponseErrorHandling> {

    private CountDownLatch countDownLatch;

    public ResponseHandlerOneOfStreamingResponse(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }


    @Override
    public void onNext(ResponseErrorHandling responseErrorHandling) {

        if (responseErrorHandling.hasSuccessResponse()){
            System.out.println("Respuest succesuful");
            System.out.println(responseErrorHandling.getSuccessResponse().getMoney().getValue());
        }else {
            System.out.println("Error response");
            System.out.println(responseErrorHandling.getErrorResponse().getErrorMessage());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        this.countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Completed");
        this.countDownLatch.countDown();
    }
}

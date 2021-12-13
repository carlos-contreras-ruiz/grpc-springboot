package com.github.carloscontrerasruiz.interceptor;

import io.grpc.*;

import java.util.Objects;

/*
* User-secret-3 and 2 are valid but only de 3 is prime
* the 2 is regular user
* */
public class AuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String clientToken1 = metadata.get(ServerConstants.TOKEN);
        System.out.println(clientToken1);
        String clientToken = metadata.get(ServerConstants.USER_TOKEN);
        System.out.println(clientToken);
        System.out.println("========================");
        if (this.validateToken(clientToken)) {
            ServerConstants.UserRole role= this.extractUserRole(clientToken);
            Context context = Context.current().withValue(
                    ServerConstants.CTX_USER_ROLE_KEY,
                    role
            );
            return Contexts.interceptCall(context, serverCall, metadata,serverCallHandler);
            //return serverCallHandler.startCall(serverCall,metadata);
        }else{
            Status status = Status.UNAUTHENTICATED.withDescription("Invalid token");
            serverCall.close(status,metadata);
        }
        return new ServerCall.Listener<ReqT>() {};
    }

    private boolean validateToken(String token){

        //return Objects.nonNull(token) && token.equals("bank-client-secret");
        return Objects.nonNull(token) &&
                (token.startsWith("user-secret-3") || token.startsWith("user-secret-2"));
    }

    private ServerConstants.UserRole extractUserRole(String jwt){
        return jwt.endsWith("prime") ? ServerConstants.UserRole.PRIME : ServerConstants.UserRole.STANDARD;
    }
}

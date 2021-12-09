package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.proto.common.Credentials;
import com.github.carloscontrerasruiz.proto.common.EmailCredentials;
import com.github.carloscontrerasruiz.proto.common.PhoneOtp;

public class OneOfDemo {

    public static void main(String[] args) {
        EmailCredentials email = EmailCredentials.newBuilder()
                .setEmail("email@email.com")
                .setPassword("1223").build();

        PhoneOtp phone = PhoneOtp.newBuilder().setNumber(1123).setCode(121212).build();

        Credentials credentials = Credentials.newBuilder()
                //.setEmailMode(email)
                .setPhoneMode(phone)
                .build();
        login(credentials);
    }

    private static void login(Credentials credentials){
        System.out.println(credentials.getEmailMode());
        System.out.println(credentials.getPhoneMode());
        System.out.println(credentials.getModeCase());

        switch (credentials.getModeCase()){
            case EMAILMODE:
                System.out.println("Email mode");
                break;
            case PHONEMODE:
                System.out.println("Phone mode");
                break;
            case MODE_NOT_SET:
                System.out.println("ningun metodo agregado ");
                break;
            default:
                System.out.println("algo apso");
        }
    }
}

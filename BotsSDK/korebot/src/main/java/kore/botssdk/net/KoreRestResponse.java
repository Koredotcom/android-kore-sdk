package kore.botssdk.net;


import kore.botssdk.models.KoreUser;

@SuppressWarnings("serial")
public class KoreRestResponse {

    public class KoreLoginResponse extends KoreUser {
        public String status;
    }

}

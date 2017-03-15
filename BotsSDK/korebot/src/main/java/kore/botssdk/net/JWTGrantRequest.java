package kore.botssdk.net;

import java.util.HashMap;

/**
 * Created by Ramachandra Pradeep on 15-Mar-17.
 */
public class JWTGrantRequest extends DemoRestRequest<RestResponse.JWTTokenResponse> {
    private String clientId;
    private String clientSecret;
    private String identity;
    private Boolean isAnonymous;
    public JWTGrantRequest(String clientId, String clientSecret, String identity, Boolean isAnonymous) {
        super(RestResponse.JWTTokenResponse.class);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.identity = identity;
        this.isAnonymous = isAnonymous;
    }

    @Override
    public RestResponse.JWTTokenResponse loadDataFromNetwork() throws Exception {
        HashMap<String, Object> hsh = new HashMap<>();
        hsh.put("clientId", clientId);
        hsh.put("clientSecret",clientSecret);
        hsh.put("identity", identity);
        hsh.put("aud","https://idproxy.kore.com/authorize");
        hsh.put("isAnonymous",isAnonymous);

        RestResponse.JWTTokenResponse jwt = getService().getJWTToken(hsh);
        return jwt;
    }
}

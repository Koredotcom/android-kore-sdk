/*package kore.botssdk.net;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.HashMap;

import kore.botssdk.models.JWTTokenResponse;

/**
 * Created by Shiva Krishna on 11/16/2017.
 */
/*public class KaJwtRequest extends DemoRestRequest<JWTTokenResponse> {
    private String clientId;
    private String clientSecret;
    private String identity;
    private Boolean isAnonymous;
    private boolean isUser;
    private String accessToken;
    public KaJwtRequest(String clientId, String clientSecret, String identity, Boolean isAnonymous,String accessToken) {
        super(JWTTokenResponse.class);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.identity = identity;
        this.isAnonymous = isAnonymous;
        this.accessToken = accessToken;
    }

    public KaJwtRequest(String accessToken,boolean isUser) {
        super(JWTTokenResponse.class);
        this.isUser = isUser;
        this.accessToken = accessToken;
        setPriority(SpiceRequest.PRIORITY_HIGH);
    }
    protected String accessTokenHeader(){
        return "bearer " + accessToken ;
    }
    @Override
    public JWTTokenResponse loadDataFromNetwork() throws Exception {
        if(!isUser) {
            HashMap<String, Object> hsh = new HashMap<>();
            hsh.put("clientId", clientId);
            hsh.put("clientSecret", clientSecret);
            hsh.put("identity", identity);
            hsh.put("aud", "https://idproxy.kore.com/authorize");
            hsh.put("isAnonymous", isAnonymous);

            return getService().getJWTToken(hsh*//*,accessTokenHeader()*//*);
        }else{
            return getService().getJWTToken(accessTokenHeader(), new HashMap<String, Object>());
        }
    }
}*/

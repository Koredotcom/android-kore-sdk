package com.kore.ai.widgetsdk.net;

import com.kore.ai.widgetsdk.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Shiva Krishna on 6/28/2018.
 */

public class GetContactsRequest {

    protected String userId;
    private String resourceId;
    private int limit,offSet;
    private String query;
    private boolean isEntities;
    private String accessToken;
    private Callback<KaRestResponse.ContactList> callback;

    public Call<KaRestResponse.ContactList> getContactsReq() {
        return contactsReq;
    }

    public void setContactsReq(Call<KaRestResponse.ContactList> contactsReq) {
        this.contactsReq = contactsReq;
    }

    private Call<KaRestResponse.ContactList> contactsReq;


    public GetContactsRequest(String accessToken, String userId, String resourceId, int limit, int offSet,
                              String query, boolean isEntities, Callback<KaRestResponse.ContactList> callback ) {
        this.userId = userId;
        this.resourceId = resourceId;
        this.accessToken = accessToken;
        this.limit = limit;
        this.offSet = offSet;
        this.query = query;
        this.isEntities = isEntities;
        this.callback = callback;
        if(!isEntities) {
            this.contactsReq = KaRestBuilder.getKaRestAPI().getContacts(userId, Utils.ah(accessToken), offSet, limit, resourceId, query);
        }else{
            this.contactsReq = KaRestBuilder.getKaRestAPI().getEntities(userId,Utils.ah(accessToken),offSet,limit,resourceId,query);
        }
    }

    public void loadDataFromNetwork(){

        KaRestAPIHelper.enqueueWithRetry(contactsReq,callback);
    }
    public void cancelRequest(){
        if(!this.contactsReq.isCanceled())
        this.contactsReq.cancel();
    }
}
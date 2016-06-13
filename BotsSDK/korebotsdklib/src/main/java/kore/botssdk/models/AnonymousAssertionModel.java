package kore.botssdk.models;

/**
 * Created by Pradeep Mahato on 07-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class AnonymousAssertionModel {
    private String issuer;
    private String subject;

    /**
     *
     * @param clientId : clientId generated from bot-admin console
     * @param secretKey :  client secret key generated from bot-admin console
     */
    public AnonymousAssertionModel(String clientId, String secretKey) {
        this.issuer = clientId;
        this.subject = secretKey;
    }
}

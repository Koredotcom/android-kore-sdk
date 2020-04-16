/*
package kore.botssdk.net;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.internal.operators.observable.ObservableRetryBiPredicate;
import kore.botssdk.utils.Utils;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Response;

*/
/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 *//*

public class GetBotMarketStreams {

    String userId, accestoken;
    public GetBotMarketStreams(String userId, String accessToken) {
        this.userId = userId;
        this.accestoken = accessToken;
    }

    public Observable<MarketStreamList> loadDataFromNetwork() {
        return Observable.create(new ObservableOnSubscribe<MarketStreamList>() {
            @Override
            public void subscribe(ObservableEmitter<MarketStreamList> emitter) throws Exception {
                try{
                    Call<MarketStreamList> _marketStreamList = RestBuilder.getRestAPI().getMarketStreams(userId, Utils.accessTokenHeader(accestoken));
                    Response<MarketStreamList> rBody = _marketStreamList.execute();
                    MarketStreamList list = rBody.body();

                    emitter.onNext(list);
                    emitter.onComplete();

                }catch(Exception e){
                    emitter.onError(e);
                }
            }
        });

        */
/*MarketStreamList marketStreamList = getService().getMarketStreams(userId, accessTokenHeader());
        return marketStreamList;*//*

    }
}
*/

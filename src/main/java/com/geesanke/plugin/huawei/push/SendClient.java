package com.geesanke.plugin.huawei.push;

import java.util.Map;

import com.geesanke.plugin.huawei.push.api.HuaweiOauth2ApiService;
import com.geesanke.plugin.huawei.push.api.HuaweiOauth2ApiService.AccessToken;
import com.geesanke.plugin.huawei.push.api.HuaweiPushApiService;
import com.geesanke.plugin.huawei.push.api.HuaweiPushApiService.NspCtx;
import com.geesanke.plugin.huawei.push.api.HuaweiPushApiService.PushSendRequest;
import com.geesanke.plugin.huawei.push.model.Payload;
import com.geesanke.plugin.huawei.push.model.PushPayload;
import com.geesanke.plugin.huawei.push.model.Result;
import com.geesanke.plugin.huawei.push.util.CommonUtils;
import com.geesanke.plugin.huawei.push.util.Object2Map;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class SendClient {
    
    private static final Log logger = LogFactory.get();
    
    private String clientSecret;

    private String clientId;

    private Token token;

    public SendClient(String clientSecret, String clientId) {
        this.clientSecret = clientSecret;
        this.clientId = clientId;
    }

    public Result push(String[] targets, Payload payload) {
        
        this.getAccessToken();
        PushPayload pushPayload = PushPayload.newInstance()
                                                .addAccessToken(this.token.getToken())
                                                .addDeviceTokenList(targets)
                                                .addPayload(payload)
                                            .build();
        
        
        Map<String, String> params = Object2Map.obj2StringMap(pushPayload);
        StringBuilder builder = new StringBuilder();
        for(String key:params.keySet()) {
            
            builder.append(key).append("=").append(params.get(key)).append("&");
            
        }
        
        logger.info("\r\n "+builder.toString());
        
        PushSendRequest request = PushSendRequest.newInstance()
                                                    .addNspCtx(NspCtx.newInstance()
                                                                            .addAppId(this.clientId)
                                                                        .build())
                                                    .addPushPayload(pushPayload)
                                                .build();
        
        // 推送
        HuaweiPushApiService.pushSend(request);
        
        return null;
    }

    
    public void getAccessToken() {
      
        if(CommonUtils.isEmpty(this.token)||isExpired()) {
            AccessToken  accessToken = HuaweiOauth2ApiService.getAccessToken(this.clientId, this.clientSecret);
            this.token = new Token(accessToken, System.currentTimeMillis()/100);
        }
        
    }

    public boolean isExpired() {
        long now = System.currentTimeMillis()/100;
        
        // 过期时间
        long expiredTime = this.token.getGetTime() + this.token.getAccessToken().getExpiresIn() - 600L;
        
        if(expiredTime >= now) {
            return true;
        }
        return false;
    }
    
    
    
    public static class Token {
        private AccessToken accessToken;
        // 获取时间
        private long getTime;
        
        public String getToken() {
            return this.accessToken.getAccessToken();
        }
        
        public Token(AccessToken accessToken, long getTime) {
            this.accessToken = accessToken;
            this.getTime = getTime;
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        public long getGetTime() {
            return getTime;
        }

        public void setGetTime(long getTime) {
            this.getTime = getTime;
        }
        
    }



    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  org.apache.http.conn.ssl.StrictHostnameVerifier
 */
package com.korevg.mv.webrtcsdk.sip.pjsip;

import android.content.Context;
import android.text.TextUtils;
import com.korevg.mv.webrtcsdk.im.InstanceMessageID;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.AudioCodesSession;
import com.korevg.mv.webrtcsdk.session.InfoAlert;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.sip.ACSipAccount;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;
import com.korevg.mv.webrtcsdk.sip.ACSipManager;
import com.korevg.mv.webrtcsdk.sip.SIPUriParser;
import com.korevg.mv.webrtcsdk.sip.enums.Transport;
import com.korevg.mv.webrtcsdk.sip.pjsip.ACPjSipCall;
import com.korevg.mv.webrtcsdk.sip.pjsip.MyLogWriter;
import com.korevg.mv.webrtcsdk.useragent.ACConfiguration;
import com.korevg.mv.webrtcsdk.useragent.AudioCodesUA;
import com.korevg.mv.webrtcsdk.useragent.Storage;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.IpChangeParam;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnIpChangeProgressParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.OnTransportStateParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.SipHeader;
import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.TlsConfig;
import org.pjsip.pjsua2.TlsInfo;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;

public class ACPjSipManager
extends ACSipManager {
    private static final String TAG = "ACPjSipMananger";
    private static ACPjSipManager instance = null;
    private static final MyLogWriter logWriter = new MyLogWriter();
    private ScheduledExecutorService executor;
    private static MyEndpoint endPoint;
    private AccountConfig accountConfig;
    private ACPjSipAccount pjsipAccount;
    private boolean verifyServer = false;
    private EpConfig endPointConfig = new EpConfig();
    private TransportConfig sipTransportConfig = new TransportConfig();
    private String userAgent = "";
    private String DEFAULT_USER_AGENT = "AudioCodes-WebRTC Android SDK V";
    private boolean useSessionTimer = true;
    private boolean shouldReregister = false;
    private int defaultRegExpires = 300;
    private boolean mUseRegistrar = false;
    private Map<String, Buddy> buddies = new HashMap<String, Buddy>();
    private String mPemFilePath = null;
    public String allowHeader = "talk,hold,conference";
    private int mSessionTImerExpires = -1;
    private static int DEFAULT_AUTOLOGIN_SESSION_EXPIRES;
    public static final long REINVITE_WAIT_TIMEOUT = 500L;
    private ArrayList<String> nameServers = this.getDefaultNameServers();

    private ACPjSipManager() {
    }

    private void initExecutor() {
        if (this.executor == null || this.executor.isShutdown()) {
            this.executor = Executors.newSingleThreadScheduledExecutor();
        }
    }

    public static synchronized ACPjSipManager getInstance() {
        if (instance == null) {
            instance = new ACPjSipManager();
        }
        instance.initExecutor();
        return instance;
    }

    public ScheduledExecutorService getExecutor() {
        return this.executor;
    }

    @Override
    public ACSipCall call(final RemoteContact callTo, final boolean withVideo, final ACSipCall.IRequestSDP requestSDPListener, final ACSipCall.ICallEventListener callEventListener, final Hashtable<String, String> inviteHeaders) throws WebRTCException {
        Log.d(TAG, "Begin of call: " + callTo.getUserName() + " video: " + withVideo);
        Future<ACSipCall> result = this.executor.submit(new Callable<ACSipCall>(){

            @Override
            public ACSipCall call() throws Exception {
                try {
                    return ACPjSipManager.this.callInternal(callTo, withVideo, requestSDPListener, callEventListener, inviteHeaders);
                }
                catch (WebRTCException e) {
                    Log.d(ACPjSipManager.TAG, "oops3: " + e.getMessage());
                    throw new ExecutionException(e);
                }
            }
        });
        ACSipCall call = null;
        try {
            call = result.get();
        }
        catch (ExecutionException ee) {
            Log.d(TAG, "oops4: " + ee.getMessage());
            Throwable t = ee.getCause();
            if (t instanceof WebRTCException) {
                throw (WebRTCException)t;
            }
        }
        catch (Exception e1) {
            Log.d(TAG, "oops5: " + e1.getMessage());
        }
        Log.d(TAG, "End of call: " + callTo + " video: " + withVideo);
        if (call == null) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        return call;
    }

    public ACSipCall callInternal(RemoteContact callTo, boolean withVideo, ACSipCall.IRequestSDP requestSDPListener, ACSipCall.ICallEventListener callEventListener, Hashtable<String, String> inviteHeaders) throws WebRTCException {
        CallOpParam prm = new CallOpParam(true);
        if (this.sipAccount == null) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.ACCOUNT_NOT_SET);
        }
        ACPjSipCall call = new ACPjSipCall(this.pjsipAccount, -1);
        call.setRequestSDPListener(requestSDPListener);
        call.setiCallEventListener(callEventListener);
        Log.d(TAG, "Calling to contact: " + callTo.getUserName());
        try {
            call.setInviteHeaders(inviteHeaders);
            call.makeCall(callTo);
        }
        catch (Exception e) {
            call.delete();
        }
        Log.d(TAG, "Call created: " + call.getActiveDialog().getId());
        return call;
    }

    @Override
    public void setServerConfig(String proxyAddress, int port, String domain, Transport transport) {
        if (this.sipAccount == null) {
            this.sipAccount = new ACSipAccount();
        }
        this.sipAccount.port = port;
        this.sipAccount.proxyAddress = proxyAddress;
        this.sipAccount.domain = domain;
        this.sipAccount.transport = transport;
    }

    @Override
    public void setAccount(String username, String password, String displayname, String authName) {
        this.sipAccount.username = username;
        this.sipAccount.displayname = displayname;
        this.sipAccount.password = password;
        this.sipAccount.authName = authName;
    }

    @Override
    public void init(boolean useRegistrar) {
        Log.d(TAG, "Begin Init pjsip");
        this.mUseRegistrar = useRegistrar;
        if (this.userAgent != null && this.userAgent.equals("")) {
            this.userAgent = this.DEFAULT_USER_AGENT + ACConfiguration.version();
        }
        this.executor.execute(new Runnable(){

            @Override
            public void run() {
                try {
                    ACPjSipManager.this.initInternal();
                    Log.d(ACPjSipManager.TAG, "End ofInit pjsip internal");
                }
                catch (WebRTCException e) {
                    Log.d(ACPjSipManager.TAG, "Oops22: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void setNameServers(ArrayList<String> nameServersList, boolean useDefaultServers) {
        this.nameServers.clear();
        for (String server : nameServersList) {
            this.nameServers.add(server);
        }
        if (useDefaultServers) {
            this.nameServers.addAll(this.getDefaultNameServers());
        }
    }

    private ArrayList<String> getDefaultNameServers() {
        ArrayList<String> servers = new ArrayList<String>();
        servers.add("8.8.8.8");
        servers.add("8.8.4.4");
        servers.add("2001:4860:4860::8888");
        return servers;
    }

    private StringVector getNameServers() {
        Log.d(TAG, "getNameServers");
        StringVector nameServersVector = new StringVector();
        for (String server : this.nameServers) {
            Log.d(TAG, "Settings nameserver: " + server);
            nameServersVector.add(server);
        }
        return nameServersVector;
    }

    public void initInternal() throws WebRTCException {
        if (this.sipAccount.username == null || this.sipAccount.password == null) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.USER_CREDENTIALS_NOT_SET);
        }
        if (this.sipAccount.domain == null || this.sipAccount.proxyAddress == null) {
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.SERVER_CONFIGURATION_NOT_SET);
        }
        if (this.verifyServer) {
            if (this.mPemFilePath == null) {
                this.loadPemfile();
            } else {
                Log.d(TAG, "Using custom CA file:" + this.mPemFilePath);
            }
            TlsConfig tls = this.sipTransportConfig.getTlsConfig();
            if (tls == null) {
                tls = new TlsConfig();
                this.sipTransportConfig.setTlsConfig(tls);
            }
            tls.setCaListFile(this.mPemFilePath);
            tls.setVerifyServer(false);
        }
        boolean initEndPoint = endPoint == null;
        Log.d(TAG, "initEndPoint2: " + initEndPoint);
        if (initEndPoint) {
            try {
                endPoint = new MyEndpoint();
                endPoint.libCreate();
            }
            catch (Exception e) {
                Log.d(TAG, "oops6: " + e.getMessage());
                throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
            }
        }
        this.sipTransportConfig.setPort(ACConfiguration.getConfiguration().getLocalServerPort());
        this.setLogLevelInternal();
        LogConfig log_cfg = this.endPointConfig.getLogConfig();
        log_cfg.setWriter(logWriter);
        log_cfg.setDecor(log_cfg.getDecor() & 0xFFFFFFFFFFFFFE7FL);
        this.endPointConfig.getUaConfig().setNameserver(this.getNameServers());
        UaConfig userAgentConfiguration = this.endPointConfig.getUaConfig();
        userAgentConfiguration.setUserAgent(this.userAgent);
        if (initEndPoint) {
            try {
                endPoint.libInit(this.endPointConfig);
            }
            catch (Exception e) {
                Log.d(TAG, "oops7: " + e.getMessage());
                throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
            }
            this.createTransports();
        }
        this.sipTransportConfig.setPort(ACConfiguration.getConfiguration().getLocalServerPort());
        this.register();
        if (initEndPoint) {
            try {
                endPoint.libStart();
            }
            catch (Exception e) {
                Log.d(TAG, "oops14: " + e.getMessage());
                throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
            }
        }
        this.setInitialized(true);
    }

    @Override
    public void setContactRewrite(boolean enable) {
        this.contactRewrite = enable ? 1 : 0;
    }

    @Override
    public void setSessionExpiresTimeout(int timeout) {
        this.mSessionTImerExpires = timeout;
    }

    @Override
    protected void setLogLevel() {
        Log.d(TAG, "Begin setLogLevel pjsip");
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipManager.this.setLogLevelInternal();
                Log.d(ACPjSipManager.TAG, "End of setLogLevel internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops15: " + e1.getMessage());
        }
        Log.d(TAG, "End setLogLevel pjsip");
    }

    private void setLogLevelInternal() {
        long logL = 4L;
        switch (this.logLevel) {
            case VERBOSE: {
                logL = 6L;
                break;
            }
            case DEBUG: {
                logL = 5L;
                break;
            }
            case INFO: {
                logL = 4L;
                break;
            }
            case WARN: {
                logL = 3L;
                break;
            }
            case ERROR: {
                logL = 2L;
                break;
            }
            case ASSERT: {
                logL = 1L;
                break;
            }
            case NONE: {
                logL = 0L;
            }
        }
        this.endPointConfig.getLogConfig().setLevel(logL);
        this.endPointConfig.getLogConfig().setConsoleLevel(logL);
    }

    @Override
    public void deinit() {
        Log.d(TAG, "Begin deinit pjsip");
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipManager.this.deinitInternal();
                Log.d(ACPjSipManager.TAG, "End of denit internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops16: " + e1.getMessage());
        }
        Log.d(TAG, "End denit pjsip");
    }

    public void deinitInternal() {
        Log.d(TAG, "deinitInternal");
        try {
            this.pjsipAccount.setRegistration(false);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.sipAccount = null;
        this.shouldReregister = true;
        Log.d(TAG, "deinitInternal: logout");
        this.sipEventListener.loginStateChanged(false, 603, "user logged out");
    }

    @Override
    public void handleNetworkChange() {
        Log.d(TAG, "Begin handleNetworkChange pjsip");
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipManager.this.handleNetworkChangeInternal();
                Log.d(ACPjSipManager.TAG, "End of handleNetworkChange internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops17: " + e1.getMessage());
        }
        Log.d(TAG, "End handleNetworkChange pjsip");
    }

    private void handleNetworkChangeInternal() {
        Log.d(TAG, "Network change detected");
        try {
            boolean contactRewriteEnabled = this.contactRewrite == 1;
            boolean inCall = !AudioCodesUA.getInstance().getSessionList().isEmpty();
            Log.d(TAG, "contact rewrite: " + contactRewriteEnabled);
            Log.d(TAG, "inCall: " + inCall);
            Log.d(TAG, "mUseRegistrar: " + this.mUseRegistrar);
            if (contactRewriteEnabled || inCall) {
                if (endPoint != null) {
                    IpChangeParam changeParam = new IpChangeParam();
                    endPoint.handleIpChange(changeParam);
                }
                if (!this.mUseRegistrar) {
                    Thread t = new Thread(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500L);
                            }
                            catch (Exception exception) {
                                // empty catch block
                            }
                            for (AudioCodesSession session : AudioCodesUA.getInstance().getSessionList()) {
                                session.hold(session.isLocalHold() || session.isRemoteHold());
                            }
                        }
                    });
                    t.start();
                }
            } else {
                Log.d(TAG, "do register");
                this.register();
            }
        }
        catch (Exception e) {
            Log.d(TAG, "Oops18: " + e.getMessage());
        }
    }

    @Override
    public boolean register() throws WebRTCException {
        RemoteContact contact = new RemoteContact();
        contact.setDomain(this.sipAccount.domain);
        contact.setUserName(this.sipAccount.username);
        contact.setDisplayName(this.sipAccount.displayname);
        contact.setScheme(this.sipAccount.SIP);
        String idUri = SIPUriParser.parseSipContact(contact);
        Log.d(TAG, "Setting idUri: " + idUri);
        String prvUriParams = null;
        if (this.accountConfig != null) {
            prvUriParams = this.accountConfig.getSipConfig().getContactUriParams();
        }
        this.accountConfig = new AccountConfig();
        this.addHeaders(this.accountConfig);
        this.accountConfig.getNatConfig().setContactRewriteUse(this.contactRewrite);
        this.accountConfig.setIdUri(idUri);
        this.accountConfig.getRegConfig().setTimeoutSec(this.defaultRegExpires);
        String pnRegToken = Storage.loadString(AudioCodesUA.getInstance().getContext(), "PN_REGISTRATION_TOKEN");
        String pnProjectID = Storage.loadString(AudioCodesUA.getInstance().getContext(), "PN_PROJECT_ID");
        String pnProvider = Storage.loadString(AudioCodesUA.getInstance().getContext(), "PN_PROVIDER");
        if (!(pnRegToken == null || pnRegToken.equals("") || pnProjectID == null || pnProjectID.equals("") || pnProvider == null || pnProvider.equals(""))) {
            Log.d(TAG, "Push token credentials filled, using push token");
            StringBuilder uriParams = new StringBuilder();
            uriParams.append(";");
            uriParams.append("pn-provider=" + pnProvider);
            uriParams.append(";");
            uriParams.append("pn-param=" + pnProjectID);
            uriParams.append(";");
            uriParams.append("pn-prid=" + pnRegToken);
            Log.d(TAG, "push token SIP params: " + uriParams.toString());
            if (!uriParams.toString().equals(prvUriParams)) {
                Log.d(TAG, "uriParams changed!");
                this.shouldReregister = true;
            }
            this.accountConfig.getSipConfig().setContactUriParams(uriParams.toString());
        }
        if (this.useSessionTimer) {
            this.accountConfig.getCallConfig().setTimerUse(1);
        } else {
            this.accountConfig.getCallConfig().setTimerUse(0);
        }
        if (this.mUseRegistrar) {
            String registrar = this.sipAccount.SIP + ":" + this.sipAccount.domain;
            Log.d(TAG, "Setting registrar internal: " + registrar);
            this.accountConfig.getRegConfig().setRegistrarUri(registrar);
        }
        Log.d(TAG, "Setting credentials2: " + this.sipAccount.authName);
        AuthCredInfoVector creds = this.accountConfig.getSipConfig().getAuthCreds();
        creds.clear();
        if (this.sipAccount.authName.length() != 0) {
            Log.d("BBB", "Adding credentials4");
            creds.add(new AuthCredInfo("Digest", "*", this.sipAccount.authName, 0, this.sipAccount.password));
        }
        StringVector proxies = this.accountConfig.getSipConfig().getProxies();
        proxies.clear();
        String proxy = this.sipAccount.SIP + ":" + this.sipAccount.proxyAddress + ":" + this.sipAccount.port + ";transport=" + this.sipAccount.transport.name().toLowerCase();
        Log.d(TAG, "Proxy address: " + proxy);
        if (proxy.length() != 0) {
            proxies.add(proxy);
        }
        this.accountConfig.getNatConfig().setIceEnabled(false);
        Log.d(TAG, "shouldReregister: " + this.shouldReregister);
        Log.d(TAG, "!isInitialized(): " + !this.isInitialized());
        Log.d(TAG, "pjsipAccount!=null: " + (this.pjsipAccount != null));
        try {
            if (!this.isInitialized() || this.shouldReregister) {
                if (this.shouldReregister && this.pjsipAccount != null) {
                    this.sipEventListener.loginStateChanged(false, 603, "restarting SIP account");
                    this.pjsipAccount.delete();
                }
                this.pjsipAccount = new ACPjSipAccount(this.accountConfig);
                this.pjsipAccount.create();
            } else {
                Log.d(TAG, "modify");
                this.pjsipAccount.modify(this.accountConfig);
                try {
                    this.pjsipAccount.setRegistration(this.mUseRegistrar);
                }
                catch (Exception e) {
                    Log.d(TAG, "oops24: " + e.getMessage());
                }
            }
        }
        catch (Exception e) {
            this.shouldReregister = false;
            this.sipAccount = null;
            Log.e(TAG, "oops19: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        this.shouldReregister = false;
        return true;
    }

    @Override
    public void setUseSessionTimer(boolean use) {
        if (use == this.useSessionTimer) {
            Log.d(TAG, "new value for session timer: " + use + " is the same as old value");
            return;
        }
        this.useSessionTimer = use;
        if (!this.isInitialized()) {
            Log.d(TAG, "new value for session timer: " + use + " will be applied when the stack will be initialized");
            return;
        }
        this.executor.execute(new Runnable(){

            @Override
            public void run() {
                try {
                    ACPjSipManager.this.register();
                    Log.d(ACPjSipManager.TAG, "End register pjsip internal");
                }
                catch (WebRTCException e) {
                    Log.d(ACPjSipManager.TAG, "Oops20: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void sipHeadersChanged() {
        Log.d(TAG, "Start of sipHeadersChanged");
        if (!this.isInitialized()) {
            Log.d(TAG, "updated headers will only be applied during init");
            return;
        }
        this.executor.execute(new Runnable(){

            @Override
            public void run() {
                try {
                    ACPjSipManager.this.register();
                    Log.d(ACPjSipManager.TAG, "End sipHeadersChanged internal");
                }
                catch (WebRTCException e) {
                    Log.d(ACPjSipManager.TAG, "Oops21: " + e.getMessage());
                }
            }
        });
    }

    private void addHeaders(AccountConfig accountConfig) {
        if (this.registerHeaders != null) {
            SipHeaderVector sipHeaderVector = new SipHeaderVector();
            Set<String> keys = this.registerHeaders.keySet();
            for (String key : keys) {
                SipHeader sipHeader = new SipHeader();
                Log.d(TAG, "Value of " + key + " is: " + (String)this.registerHeaders.get(key));
                sipHeader.setHName(key);
                sipHeader.setHValue((String)this.registerHeaders.get(key));
                sipHeaderVector.add(sipHeader);
            }
            accountConfig.getRegConfig().setHeaders(sipHeaderVector);
        }
    }

    @Override
    public void setTlsVerifyServer(boolean verifyServer) {
        this.verifyServer = verifyServer;
    }

    @Override
    public void setCaCertFilePath(String path) {
        this.mPemFilePath = path;
    }

    private void loadPemfile() {
        Log.d(TAG, "Using default certificate file");
        String filePath = "cacert.pem";
        Context context = AudioCodesUA.getInstance().getContext();
        File cacheDir = new File(context.getCacheDir(), filePath);
        try {
            int read;
            InputStream in = context.getAssets().open(filePath);
            FileOutputStream out = new FileOutputStream(cacheDir.getPath());
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
        this.mPemFilePath = cacheDir.getPath();
    }

    private String checkForReason(String reason, String wholeMsg) {
        String res = reason;
        Scanner scanner = new Scanner(wholeMsg);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == null || !line.startsWith("www-authenticate") || !line.contains("error=")) continue;
            String tmp = line.substring(line.indexOf("error=\""), line.length());
            tmp = tmp.replace("error=\"", "");
            res = tmp.substring(0, tmp.indexOf("\""));
        }
        scanner.close();
        return res;
    }

    public int getRegExpires() {
        return this.defaultRegExpires;
    }

    public void setRegExpires(int defaultRegExpires) {
        this.defaultRegExpires = defaultRegExpires;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public long sendInstantMessage(final RemoteContact remoteContact, final String message) {
        Log.d(TAG, "sendInstantMessage");
        final InstanceMessageID ID = new InstanceMessageID();
        Future<Void> result = this.executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipManager.this.sendInstantMessageInternal(remoteContact, message, ID);
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops127: " + e1.getMessage());
        }
        Log.d(TAG, "message ID " + ID.getID());
        return ID.getID();
    }

    public void sendInstantMessageInternal(RemoteContact remoteContact, String message, InstanceMessageID ID) {
        Log.d(TAG, "sendInstantMessageInternal");
        try {
            String user = "sip:" + remoteContact.getUserName() + "@";
            user = remoteContact.getDomain() != null && !remoteContact.getDomain().equals("") ? user + remoteContact.getDomain() : user + this.sipAccount.domain;
            Log.d(TAG, user + ", " + message);
            Buddy buddy = this.buddies.get(user);
            if (buddy == null) {
                Log.d(TAG, "create buddy");
                BuddyConfig buddyConfig = new BuddyConfig();
                buddyConfig.setUri(user);
                buddy = new Buddy();
                buddy.create(this.pjsipAccount, buddyConfig);
                this.buddies.put(user, buddy);
            }
            Log.d(TAG, "send IM");
            SendInstantMessageParam prm = new SendInstantMessageParam();
            prm.setContent(message);
            prm.setUserData(ID);
            buddy.sendInstantMessage(prm);
        }
        catch (Throwable th) {
            Log.e(TAG, "sendMessage error " + th.getMessage());
        }
    }

    private void createTransports() throws WebRTCException {
        try {
            endPoint.transportCreate(1, this.sipTransportConfig);
        }
        catch (Exception e) {
            Log.d(TAG, "oops8: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        try {
            endPoint.transportCreate(129, this.sipTransportConfig);
        }
        catch (Exception e) {
            Log.d(TAG, "oops9: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        try {
            endPoint.transportCreate(2, this.sipTransportConfig);
        }
        catch (Exception e) {
            Log.d(TAG, "oops10: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        try {
            endPoint.transportCreate(130, this.sipTransportConfig);
        }
        catch (Exception e) {
            Log.d(TAG, "oops11: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        try {
            endPoint.transportCreate(3, this.sipTransportConfig);
        }
        catch (Exception e) {
            Log.d(TAG, "oops12: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
        try {
            endPoint.transportCreate(131, this.sipTransportConfig);
        }
        catch (Exception e) {
            Log.d(TAG, "oops13: " + e.getMessage());
            throw new WebRTCException(WebRTCException.WebRTCExceptionCode.UNKNOWN);
        }
    }

    @Override
    public void setAllowHeader(String[] events) {
        if (events == null || events.length < 1) {
            this.allowHeader = null;
            return;
        }
        this.allowHeader = TextUtils.join((CharSequence)",", (Object[])events);
    }

    static {
        DEFAULT_AUTOLOGIN_SESSION_EXPIRES = 600;
    }

    class ACPjSipAccount
    extends Account {
        private static final String TAG = "ACSipAccount";
        public AccountConfig accountConfig;

        public ACPjSipAccount(AccountConfig config) {
            this.accountConfig = config;
            if (!ACPjSipManager.this.mUseRegistrar && ACPjSipManager.this.mSessionTImerExpires == -1) {
                ACPjSipManager.this.mSessionTImerExpires = DEFAULT_AUTOLOGIN_SESSION_EXPIRES;
            }
            Log.d(TAG, "Session expires timeout: " + ACPjSipManager.this.mSessionTImerExpires);
            if (ACPjSipManager.this.mSessionTImerExpires != -1) {
                this.accountConfig.getCallConfig().setTimerSessExpiresSec(ACPjSipManager.this.mSessionTImerExpires);
            }
        }

        @Override
        public void onRegState(OnRegStateParam prm) {
            boolean isLogin = false;
            if (prm.getCode() == 200) {
                try {
                    isLogin = ACPjSipManager.this.pjsipAccount.getInfo().getRegIsActive();
                }
                catch (Exception e) {
                    Log.d(TAG, "oops1: " + e.getMessage());
                }
            }
            Log.d(TAG, "onRegState: " + prm.getCode() + " logged in: " + isLogin);
            if (ACPjSipManager.this.sipEventListener != null) {
                String reason = null;
                if (!isLogin) {
                    reason = ACPjSipManager.this.checkForReason(prm.getReason(), prm.getRdata().getWholeMsg());
                }
                ACPjSipManager.this.sipEventListener.loginStateChanged(isLogin, prm.getCode(), reason);
            }
        }

        @Override
        public void onIncomingCall(OnIncomingCallParam prm) {
            Log.d(TAG, "onIncomingCall");
            String remoteSDP = prm.getRdata().getWholeMsg();
            int index = remoteSDP.indexOf("v=0");
            remoteSDP = index < 0 ? null : remoteSDP.substring(index, remoteSDP.length());
            ACPjSipCall call = new ACPjSipCall(this, prm.getCallId());
            call.setRemoteSDP(remoteSDP);
            if (ACPjSipManager.this.sipEventListener != null) {
                InfoAlert infoAlert = null;
                if (prm != null && prm.getRdata() != null) {
                    String wholeMsg = prm.getRdata().getWholeMsg();
                    infoAlert = call.getInfoAlert(wholeMsg);
                }
                ACPjSipManager.this.sipEventListener.incomingCall(remoteSDP, call, infoAlert);
            }
        }

        public void create() {
            try {
                super.create(this.accountConfig);
            }
            catch (Exception e) {
                Log.d(TAG, "oops2: " + e.getMessage());
            }
        }

        @Override
        public void onTypingIndication(OnTypingIndicationParam prm) {
            super.onTypingIndication(prm);
            Log.d(TAG, "onTypingIndication");
            Log.d(TAG, "from: " + prm.getFromUri());
        }

        @Override
        public void onInstantMessage(OnInstantMessageParam prm) {
            super.onInstantMessage(prm);
            Log.d(TAG, "onInstantMessage");
            Log.d(TAG, "from: " + prm.getFromUri());
            Log.d(TAG, "body: " + prm.getMsgBody());
            if (ACPjSipManager.this.instantMessageCallback != null) {
                RemoteContact contact = SIPUriParser.parseSipUri(prm.getFromUri());
                ACPjSipManager.this.instantMessageCallback.onInstantMessage(contact, prm.getMsgBody());
            }
        }

        @Override
        public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
            if (ACPjSipManager.this.instantMessageCallback != null) {
                long id2 = InstanceMessageID.getID(prm);
                Log.d(TAG, "status ID " + id2);
                ACPjSipManager.this.instantMessageCallback.onInstantMessageStatus(prm.getCode(), id2);
            }
            super.onInstantMessageStatus(prm);
        }
    }

    private class MyEndpoint
    extends Endpoint {
        public ArrayList<String> trustedAddresses = new ArrayList();

        @Override
        public void onTransportState(OnTransportStateParam prm) {
            super.onTransportState(prm);
            if (!ACPjSipManager.this.verifyServer) {
                Log.d(ACPjSipManager.TAG, "Not checking state, verifyServer = false");
                return;
            }
            if (ACPjSipManager.this.sipTransportConfig == null || ACPjSipManager.this.sipTransportConfig.getTlsConfig() == null) {
                Log.d(ACPjSipManager.TAG, "No TLS transport set, not verifying server");
                return;
            }
            if (ACPjSipManager.this.sipAccount == null || ACPjSipManager.this.sipAccount.transport != Transport.TLS) {
                Log.d(ACPjSipManager.TAG, "Not using TLS transport set, not verifying server");
                return;
            }
            TlsInfo info = prm.getTlsInfo();
            if (info == null) {
                return;
            }
            StringVector v = info.getVerifyMsgs();
            int i = 0;
            while ((long)i < v.size()) {
                Log.d(ACPjSipManager.TAG, "msg: " + v.get(i));
                ++i;
            }
            StrictHostnameVerifier verfier = new StrictHostnameVerifier();
            StringVector alts = info.getRemoteCertInfo().getSubjectAltName();
            ArrayList<String> list = new ArrayList<String>();
            int i2 = 0;
            while ((long)i2 < alts.size()) {
                Log.d(ACPjSipManager.TAG, "alts: " + alts.get(i2));
                list.add(alts.get(i2));
                ++i2;
            }
            String[] stringArray = list.toArray(new String[0]);
            boolean verified = false;
            try {
                verfier.verify(ACPjSipManager.this.sipAccount.proxyAddress, new String[]{info.getRemoteCertInfo().getSubjectCn()}, stringArray);
                verified = true;
            }
            catch (Exception e) {
                Log.d(ACPjSipManager.TAG, "oops" + e.getMessage());
            }
            Log.d(ACPjSipManager.TAG, "verfied: " + verified);
            Long t = new Long(info.getVerifyStatus());
            if (t != null) {
                Log.d(ACPjSipManager.TAG, "onTransportState getVerifyStatus: " + Long.toBinaryString(t));
            }
            if (verified || info.getVerifyStatus() == 0L) {
                this.trustedAddresses.add(info.getRemoteAddr());
            } else {
                Log.d(ACPjSipManager.TAG, "Checking trusted addresses");
                boolean trusted = false;
                for (String str : this.trustedAddresses) {
                    if (str == null || !str.equals(info.getRemoteAddr())) continue;
                    trusted = true;
                }
                Log.d(ACPjSipManager.TAG, "Checking addresses, trusted = " + trusted);
                if (!trusted) {
                    try {
                        this.transportShutdown(prm.getHnd());
                    }
                    catch (Exception e) {
                        Log.d(ACPjSipManager.TAG, "oops64: " + e.getMessage());
                    }
                }
            }
        }

        @Override
        public void onIpChangeProgress(OnIpChangeProgressParam prm) {
            super.onIpChangeProgress(prm);
            Log.d(ACPjSipManager.TAG, "onIpChangeProgress getStatus: " + prm.getStatus());
            Log.d(ACPjSipManager.TAG, "onIpChangeProgress getOp: " + prm.getOp());
            Log.d(ACPjSipManager.TAG, "onIpChangeProgress getOp: " + prm.getOp());
        }
    }
}


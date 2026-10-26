/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.text.TextUtils
 */
package com.korevg.mv.webrtcsdk.sip;

import android.net.Uri;
import android.text.TextUtils;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.sip.pjsip.ACPjSipManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SIPUriParser {
    public static final String TAG = "SIPUriParser";
    private static final String SIP_SCHEME_RULE = "sip(?:s)?|tel";
    private static final Pattern SIP_CONTACT_ADDRESS_PATTERN = Pattern.compile("^([^@:]+)@([^@]+)$");
    private static final Pattern SIP_CONTACT_PATTERN = Pattern.compile("^(?:\")?([^<\"]*)(?:\")?[ ]*(?:<)?(sip(?:s)?|tel):([^@]+)@([^>]+)(?:>)?$");
    private static final Pattern SIP_HOST_PATTERN = Pattern.compile("^(?:\")?([^<\"]*)(?:\")?[ ]*(?:<)?(sip(?:s)?|tel):([^@>]+)(?:>)?$");

    private SIPUriParser() {
    }

    public static String parseSipContact(RemoteContact contact) {
        StringBuilder uri = new StringBuilder();
        if (contact.getDisplayName() != null && !contact.getDisplayName().equals("")) {
            uri.append("\"");
            uri.append(contact.getDisplayName());
            uri.append("\"");
        }
        uri.append(" <");
        if ((contact.getScheme() == null || contact.getScheme().equals("")) && ACPjSipManager.getInstance().sipAccount != null) {
            uri.append(ACPjSipManager.getInstance().sipAccount.SIP);
        } else {
            uri.append(contact.getScheme());
        }
        uri.append(":");
        uri.append(contact.getUserName());
        uri.append("@");
        if ((contact.getDomain() == null || contact.getDomain().equals("")) && ACPjSipManager.getInstance().sipAccount != null) {
            uri.append(ACPjSipManager.getInstance().sipAccount.domain);
        } else {
            uri.append(contact.getDomain());
        }
        uri.append(">");
        Log.d(TAG, "uri: " + uri.toString());
        return uri.toString();
    }

    public static RemoteContact parseSipUri(String sipUri) {
        RemoteContact remoteContact = new RemoteContact();
        if (!TextUtils.isEmpty((CharSequence)sipUri)) {
            Matcher m = SIP_CONTACT_PATTERN.matcher(sipUri);
            if (m.matches()) {
                remoteContact.setDisplayName(Uri.decode((String)m.group(1).trim()));
                remoteContact.setDomain(m.group(4));
                remoteContact.setUserName(Uri.decode((String)m.group(3)));
                remoteContact.setScheme(m.group(2));
            } else {
                m = SIP_HOST_PATTERN.matcher(sipUri);
                if (m.matches()) {
                    remoteContact.setDisplayName(Uri.decode((String)m.group(1).trim()));
                    remoteContact.setDomain(m.group(3));
                    remoteContact.setScheme(m.group(2));
                } else {
                    m = SIP_CONTACT_ADDRESS_PATTERN.matcher(sipUri);
                    if (m.matches()) {
                        remoteContact.setUserName(Uri.decode((String)m.group(1)));
                        remoteContact.setDomain(m.group(2));
                    } else {
                        remoteContact.setUserName(sipUri);
                    }
                }
            }
        }
        return remoteContact;
    }
}


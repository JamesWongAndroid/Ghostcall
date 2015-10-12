package com.kickbackapps.ghostcall.PJSIP;

import android.util.Log;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;

/**
 * Created by Ynott on 10/6/15.
 */
public class SipAccount extends Account {

    public SipAccount() {
        super();
    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        super.onIncomingCall(prm);
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        super.onRegState(prm);
        String msg_str = "";
        if (prm.getExpiration() == 0)
            msg_str += "Unregistration";
        else
            msg_str += "Registration";
        if (prm.getCode().swigValue()/100 == 2)
            msg_str += " successful";
        else
            msg_str += " failed: " + prm.getReason();
        Log.i("SipAccount", "reg state = " + msg_str);
    }
}

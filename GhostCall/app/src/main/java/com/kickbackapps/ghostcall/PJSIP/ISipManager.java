package com.kickbackapps.ghostcall.PJSIP;

/**
 * Created by Ynott on 10/6/15.
 */
public interface ISipManager {
    void notifyIncomingCall(SipCall call);
    void notifyCallState(SipCall call);
}

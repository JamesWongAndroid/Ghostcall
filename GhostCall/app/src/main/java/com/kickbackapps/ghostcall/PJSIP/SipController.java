package com.kickbackapps.ghostcall.PJSIP;

import android.util.Log;

import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.EnumSet;

/**
 * Created by Ynott on 10/6/15.
 */
public class SipController implements ISipManager {
    private final SipAccount localAccount;
    private SipCall sipAudioCall;

    @Override
    public void notifyIncomingCall(SipCall call) {
        CallOpParam callOpParam = new CallOpParam();
        callOpParam.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        try {
            sipAudioCall.answer(callOpParam);
        } catch (Exception e) {
            Log.d("notifyincomingcall", e.getMessage());
        }
    }

    private enum State {
        IN_SIP_CALL,
        CALL_IN_PROGRESS
    }

    private EnumSet<State> sipState = EnumSet.noneOf(State.class);

    private SipController(String host, String user, String password) {
        localAccount = GhostcallSipProfileFactory.createPjSip(host, user, password);
    }

    public void setIsCallInProgress(boolean isInProgress) {
        if (!isInProgress) {
                if (localAccount == null) {
                    return;
                }
                try {
                    sipAudioCall = new SipCall(localAccount, -1, this);
                    sipAudioCall.makeCall("sip:622591@sip.ghostcall.in");
                } catch (Exception e) {
                    e.printStackTrace();
                    sipAudioCall.delete();
                }
        }
    }

    public void destroy() {
        Log.i("SipController", "destroy");
        cleanUpSipClient();
    }

    private void cleanUpAudioCall() {
        if (sipAudioCall != null) {
            if (sipAudioCall.isActive()) {
                sipAudioCall.delete();
            }
        }

        sipState.remove(State.CALL_IN_PROGRESS);
        sipState.remove(State.IN_SIP_CALL);
    }

    private void cleanUpSipClient() {
        if (localAccount!=null) {
            localAccount.delete();
        }
    }

    public static SipController create(String host, String user, String password) {
        return new SipController(host, user, password);
    }

    @Override
    public void notifyCallState(SipCall call) {
        if (sipAudioCall == null || call.getId() != sipAudioCall.getId()) {
            return;
        }
        CallInfo callInfo;
        try {
            callInfo = call.getInfo();
            if (callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                sipAudioCall = null;
            }
        } catch (Exception e) {
            Log.d("notifycallstate", e.getMessage());
        }
    }
}

package com.kickbackapps.ghostcall;

import android.util.Log;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.MediaConfig;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsip_transport_type_e;
import org.pjsip.pjsua2.pjsua_call_media_status;

/**
 * Created by Ynott on 11/4/15.
 */
public class MyPJSIP {

    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }

    public static Endpoint ep = new Endpoint();
    TransportConfig sipTpConfig = new TransportConfig();
    public static String host = "sip:sip.ghostcall.in";
    public static MyCall call;
    public static MyAccount acc;
    public static AccountInfo info;
    public static OnIncomingCallParam incomingCallParam;

    public void init(String userName, String password) {
        try {
            if (ep == null) {
                ep = new Endpoint();
            }
            ep.libCreate();
            EpConfig epConfig = new EpConfig();
            MediaConfig med_cfg = epConfig.getMedConfig();
            med_cfg.setNoVad(true);
            ep.libInit(epConfig);
            sipTpConfig.setPort(5060);
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
            ep.libStart();

            AccountConfig acfg = new AccountConfig();
            acfg.setIdUri("sip:" + userName);
            acfg.getRegConfig().setRegistrarUri(host);
            AuthCredInfo cred = new AuthCredInfo("plain", "*", userName, 0, password);
            acfg.getSipConfig().getAuthCreds().add(cred);
            acc = new MyAccount();
            acc.create(acfg);
            Thread.sleep(1000);

            try {
                info = acc.getInfo();
                Log.d("is reg active: ", Boolean.toString(info.getRegIsActive()));
                Log.d("reg status: ", info.getRegStatusText());

            } catch (Exception e) {
                System.out.print(e.getMessage());
            }


        } catch (Exception e) {
            return;
        }

    }

    public static void endPJSIP() {
        if (ep != null) {
            try {
                if (acc != null) {
                    acc.delete();
                }
                ep.libDestroy();
                ep.delete();
                ep = null;
            } catch (Exception e) {

            }

        }
    }

    public static class MyAccount extends Account {

        @Override
        public void onIncomingCall(OnIncomingCallParam prm) {
            super.onIncomingCall(prm);
            Log.d("THERE'S A INCOMING CALL", "ANSWER ME!");
            call = new MyCall(acc, prm.getCallId());

            incomingCallParam = prm;
        }
    }

    public static class MyCall extends Call {

        public MyCall(Account acc, int call_id) {
            super(acc, call_id);
        }

        @Override
        public void onCallState(OnCallStateParam prm) {
            try {
                CallInfo ci = getInfo();
                if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                    Log.d("testing call state", Boolean.toString(call.isActive()));
                    this.delete();
                    call = null;
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                return;
            }
        }

        @Override
        public void onCallMediaState(OnCallMediaStateParam prm) {
            CallInfo ci;
            try {
                ci = getInfo();
            } catch (Exception e) {
                System.out.print(e.getMessage());
                return;
            }

            CallMediaInfoVector cmiv = ci.getMedia();
            for (int i = 0; i < cmiv.size(); i++) {
                CallMediaInfo cmi = cmiv.get(i);
                if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO && (cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                        cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                    Media m = getMedia(i);
                    AudioMedia am = AudioMedia.typecastFromMedia(m);
                    try {
                        ep.audDevManager().getCaptureDevMedia().startTransmit(am);
                        am.startTransmit(ep.audDevManager().getPlaybackDevMedia());
                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                        continue;
                    }

                }
            }
        }
    }

    public static void makeCall(String toSipAccount) {
        call = new MyCall(acc, -1);
        CallOpParam prm = new CallOpParam(true);
        try {
            call.makeCall(toSipAccount, prm);
        } catch (Exception e) {
            Log.d("TEST PJSIP ERROR", e.getMessage());
        }
    }

    public static void answerCall() {
            Log.d("answering", Integer.toString(incomingCallParam.getCallId()));
            CallOpParam prmi = new CallOpParam(true);
            try {
                prmi.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
                call.answer(prmi);
            } catch (Exception e) {
                Log.d("TEST PJSIP ERROR", e.getMessage());
            }
    }
}

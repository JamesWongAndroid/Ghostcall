package com.kickbackapps.ghostcall.PJSIP;

import android.util.Log;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsua_call_media_status;

/**
 * Created by Ynott on 10/6/15.
 */
public class SipCall extends Call {
    ISipManager manager;
//    public static Endpoint endpoint = new Endpoint();
//    private EpConfig epConfig = new EpConfig();
//    private TransportConfig sipTpConfig = new TransportConfig();
//    private MyLogWriter logWriter;

    public SipCall(Account acc, int call_id, ISipManager manager) {
        super(acc, call_id);
        this.manager = manager;
//        try {
//            endpoint.libCreate();
//        } catch (Exception e) {
//            Log.d("endpoint exception", e.getMessage());
//        }
//
//        int SIP_PORT = 5000;
//        int LOG_LEVEL = 4;
//        sipTpConfig.setPort(SIP_PORT);
//
//        LogConfig log_cfg = epConfig.getLogConfig();
//        logWriter = new MyLogWriter();
//        log_cfg.setWriter(logWriter);
//        log_cfg.setDecor(log_cfg.getDecor() &
//                ~(pj_log_decoration.PJ_LOG_HAS_CR.swigValue() |
//                        pj_log_decoration.PJ_LOG_HAS_NEWLINE.swigValue()));
//
//        log_cfg.setLevel(LOG_LEVEL);
//        log_cfg.setConsoleLevel(LOG_LEVEL);
//
//        try {
//            endpoint.libInit(epConfig);
//            endpoint.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
//            endpoint.libStart();
//        } catch (Exception e) {
//            Log.d("endpoint libinit", e.getMessage());
//        }

    }



    @Override
    public void onCallState(OnCallStateParam prm) {
        super.onCallState(prm);
        manager.notifyCallState(this);
        try {
            CallInfo ci = getInfo();
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                this.delete();
            }
        } catch (Exception e) {
            Log.d("SIPCALL ERROR - ", e.getMessage());
        }
    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        super.onCallMediaState(prm);
        CallInfo ci;
        try {
            ci = getInfo();
        } catch (Exception e) {
            return;
        }

        CallMediaInfoVector cmiv = ci.getMedia();

        for (int i = 0; i < cmiv.size(); i++) {
            CallMediaInfo cmi = cmiv.get(i);
            if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO && (cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                Media media = getMedia(i);
                AudioMedia audioMedia = AudioMedia.typecastFromMedia(media);

//                try {
//                    endpoint.audDevManager().getCaptureDevMedia().startTransmit(audioMedia);
//                    audioMedia.startTransmit(endpoint.audDevManager().getPlaybackDevMedia());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        }
    }

    public void makeCall(String dest_uri) throws Exception {
        CallOpParam callOpParam = new CallOpParam();
        CallSetting callSetting = callOpParam.getOpt();
        callSetting.setVideoCount(0);
        callSetting.setAudioCount(1);
        super.makeCall(dest_uri, callOpParam);
    }

    @Override
    public void processMediaUpdate(OnCallMediaStateParam prm) {
        super.processMediaUpdate(prm);
    }

//    public void flushNativeDeinit() {
//        Runtime.getRuntime().gc();
//
//        try {
//            endpoint.libDestroy();
//        } catch (Exception e) {
//        }
//
//        endpoint.delete();
//        endpoint = null;
//    }

    class MyLogWriter extends LogWriter {
        @Override
        public void write(LogEntry entry) {
            System.out.println(entry.getMsg());
        }
    }


}

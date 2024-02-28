package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.os.Bundle;
import android.widget.TextView;

import com.audiocodes.mv.webrtcsdk.session.ACCallStatistics;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;


public class CallStatsActivity extends BaseAppCompatActivity {

    private static final String TAG = "CallStatsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_stats_activity);
        initGui();
    }

    private void initGui() {
        TextView packetsReceivedTextView =(TextView) findViewById(R.id.call_stats_textview_packets_received_text);
        TextView bytesReceivedTextView =(TextView) findViewById(R.id.call_stats_textview_bytes_received_text);
        TextView packetsLostTextView =(TextView) findViewById(R.id.call_stats_textview_packets_lost_text);
        TextView jitterTextView =(TextView) findViewById(R.id.call_stats_textview_jitter_text);
        TextView fractionLostTextView =(TextView) findViewById(R.id.call_stats_textview_fraction_lost_text);
        TextView packetsSentTextView =(TextView) findViewById(R.id.call_stats_textview_packets_sent_text);
        TextView bytesSentTextView =(TextView) findViewById(R.id.call_stats_textview_bytes_sent_text);

        ACCallStatistics acCallStatistics = Prefs.getCallStats();
        Log.d(TAG, "ACCallStatistics: " + acCallStatistics);
        try {
            ACCallStatistics.RTPInboundStatistics rtpInboundStatistics = acCallStatistics.rtpInboundStats;
            ACCallStatistics.RTPOutboundStatistics rtpOutboundStatistics = acCallStatistics.rtpOutboundStats;


            packetsReceivedTextView.setText(String.valueOf(rtpInboundStatistics.packetsReceived));
            bytesReceivedTextView.setText(String.valueOf(rtpInboundStatistics.bytesReceived));
            packetsLostTextView.setText(String.valueOf(rtpInboundStatistics.packetsLost));
            jitterTextView.setText(String.valueOf(rtpInboundStatistics.jitter));
            fractionLostTextView.setText(String.valueOf(rtpInboundStatistics.fractionLost));
            packetsSentTextView.setText(String.valueOf(rtpOutboundStatistics.packetsSent));
            bytesSentTextView.setText(String.valueOf(rtpOutboundStatistics.bytesSent));
        } catch (Exception e) {
            Log.e(TAG,"error: "+e);
        }

    }
}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//
//        View view = initGui(R.layout.call_stats_activity);
//        setContentView(view);
//    }

//    private View initGui(int viewID) {
//
//        View mainView = View.inflate(this, viewID, null);
////        LayoutInflater inflater = CallStatsActivity.get.getLayoutInflater();
////        View view = inflater.inflate(viewID, null, false);
//        LinearLayout callStatsDataView = (LinearLayout) mainView.findViewById(R.id.call_stats_layout_stats);
//
//        ACCallStatistics acCallStatistics = Prefs.getCallStats();
//        Log.d(TAG, "ACCallStatistics: " + acCallStatistics);
//
//        if (acCallStatistics != null) {
//
//
//            ACCallStatistics.RTPInboundStatistics rtpInboundStatistics = acCallStatistics.rtpInboundStats;
//            ACCallStatistics.RTPOutboundStatistics rtpOutboundStatistics = acCallStatistics.rtpOutboundStats;
//            callStatsDataView = addViewFromClass (rtpInboundStatistics, callStatsDataView);
//            callStatsDataView = addViewFromClass (rtpOutboundStatistics, callStatsDataView);
//        }
//        return mainView;
//
//    }
//
//    private LinearLayout addViewFromClass(Object cls, LinearLayout view)
//    {
//        Field[] fields = cls.getClass().getDeclaredFields();
//
//
//        //print field names paired with their values
//        for (Field field : fields) {
//            TextView textViewHeader = new TextView(this);
//            TextView textViewValue = new TextView(this);
//            //result.append("  ");
//            try {
//
//
//                if (!field.isSynthetic() && field.getModifiers() <= 1) {
//                    String header = field.getName();
//
//                    field.setAccessible(true);
//                    Object dataObj = field.get(cls);
//                    String data = String.valueOf(dataObj);
//
//                    textViewHeader.setText(header);
//                    textViewValue.setText(data);
//
//                    view.addView(textViewHeader);
//                    view.addView(textViewValue);
//                }
//            } catch (Exception ex) {
//                System.out.println(ex);
//                break;
//            }
//
//        }
//        return view;
//    }
//}
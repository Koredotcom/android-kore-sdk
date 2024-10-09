package com.kore.ui.audiocodes.webrtcclient.activities

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.databinding.CallStatsActivityBinding

class CallStatsActivity : BaseAppCompatActivity() {
    private lateinit var binding: CallStatsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.call_stats_activity, null, false)
        setContentView(binding.root)
        initGui()
    }

    private fun initGui() {
        val acCallStatistics = Prefs.getCallStats(this)
        Log.d(TAG, "ACCallStatistics: $acCallStatistics")
        try {
            val rtpInboundStatistics = acCallStatistics?.rtpInboundStats
            val rtpOutboundStatistics = acCallStatistics?.rtpOutboundStats
            if (rtpInboundStatistics != null) {
                binding.callStatsTextviewPacketsReceivedText.text = rtpInboundStatistics.packetsReceived.toString()
                binding.callStatsTextviewBytesReceivedText.text = rtpInboundStatistics.bytesReceived.toString()
                binding.callStatsTextviewPacketsLostText.text = rtpInboundStatistics.packetsLost.toString()
                binding.callStatsTextviewJitterText.text = rtpInboundStatistics.jitter.toString()
                binding.callStatsTextviewFractionLostText.text = rtpInboundStatistics.fractionLost.toString()
                if (rtpOutboundStatistics != null) {
                    binding.callStatsTextviewPacketsSentText.text = rtpOutboundStatistics.packetsSent.toString()
                    binding.callStatsTextviewBytesSentText.text = rtpOutboundStatistics.bytesSent.toString()
                }
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "error: $e")
        }
    }

    companion object {
        private const val TAG = "CallStatsActivity"
    }
}
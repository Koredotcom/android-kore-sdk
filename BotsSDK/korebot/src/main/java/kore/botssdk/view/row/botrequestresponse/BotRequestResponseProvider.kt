package kore.botssdk.view.row.botrequestresponse

import android.view.LayoutInflater
import android.view.ViewGroup
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.databinding.RowBotRequestResponseMsgBinding as Binding

class BotRequestResponseProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}
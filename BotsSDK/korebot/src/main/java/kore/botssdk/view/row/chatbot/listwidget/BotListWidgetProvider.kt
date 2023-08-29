package kore.botssdk.view.row.chatbot.listwidget

import android.view.LayoutInflater
import android.view.ViewGroup
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.databinding.ListwidgetViewBinding as Binding

class BotListWidgetProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}
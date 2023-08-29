package kore.botssdk.view.row.chatbot.listwidget.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.databinding.ListwidgetDetailsItemBinding as Binding

class BotListWidgetDetailProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}
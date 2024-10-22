package com.kore.ui.audiocodes.webrtcclient.fragments.recents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.BaseAppCompatActivity
import com.kore.ui.audiocodes.webrtcclient.fragments.BaseFragment
import com.kore.ui.audiocodes.webrtcclient.fragments.FragmentLifecycle
import com.kore.ui.audiocodes.webrtcclient.fragments.recents.row.RecentListItemRow
import com.kore.ui.audiocodes.webrtcclient.structure.CallEntry
import com.kore.ui.databinding.MainFragmentRecentListBinding

class RecentListFragment : BaseFragment(), FragmentLifecycle {
    private var binding: MainFragmentRecentListBinding? = null

    companion object {
        private const val TAG = "RecentListFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment_recent_list, container, false)
        initGui()
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initGui() {
        binding?.apply {
            //Erase all table
            list.layoutManager = LinearLayoutManager(root.context)
            list.adapter = SimpleListAdapter(RecentListItemRowType.values().asList())
            list.addItemDecoration(SimpleListRow.VerticalSpaceItemDecoration(2))
            recentButtonErase.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirm!")
                builder.setMessage("Are you sure want to Erase all recent call data?")
                builder.setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                    (requireActivity() as BaseAppCompatActivity).getDataBase().deleteTable()
                    refreshData()
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
            refreshData()
        }
    }

    private fun refreshData() {
        binding?.apply {
            val callEntryList = (requireActivity() as BaseAppCompatActivity).getDataBase().allEntries
            val rows = callEntryList.map {
                RecentListItemRow(it, this@RecentListFragment::onItemClick, this@RecentListFragment::onCallButtonClick)
            }
            (list.adapter as SimpleListAdapter).submitList(rows)
        }
    }

    private fun onItemClick(callEntry: CallEntry) {
        LogUtils.d(TAG, "list view row selected")
        Toast.makeText(context, "audio call to: ${callEntry.contactName} number: ${callEntry.contactNumber}", Toast.LENGTH_SHORT).show()
        acManager.callNumber(requireContext(), callEntry.contactNumber)
    }

    private fun onCallButtonClick(callEntry: CallEntry) {
        LogUtils.d(TAG, "list view button selected")
        Toast.makeText(requireContext(), "Video call to: ${callEntry.contactName} number: ${callEntry.contactNumber}", Toast.LENGTH_SHORT)
            .show()
        acManager.callNumber(requireContext(), callEntry.contactNumber, true)
    }

    override fun onPauseFragment() {}
    override fun onResumeFragment() {
        try {
            LogUtils.d(TAG, "refreshData")
            if (binding?.list?.adapter != null) refreshData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
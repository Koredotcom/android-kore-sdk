package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.constants.MediaConstants.Companion.EXTRA_LOCAL_FILE_PATH
import com.kore.common.constants.MediaConstants.Companion.EXTRA_MEDIA_TYPE
import com.kore.common.utils.FileUtils
import com.kore.listeners.AttachmentListener
import com.kore.ui.R

class ComposeBarAttachmentAdapter(
    val context: Context,
    private val attachmentListener: AttachmentListener,
    private val dataList: List<Map<String, *>>
) : RecyclerView.Adapter<ComposeBarAttachmentAdapter.ImageAttachView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAttachView {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.attachment_view_composebar, parent, false)
        return ImageAttachView(view)
    }

    override fun onBindViewHolder(holder: ImageAttachView, position: Int) {

        when(val fileExtension: String = dataList[position][EXTRA_MEDIA_TYPE] as String)
        {
            "jpg","png","jpeg" -> {
                Glide.with(context).load(dataList[position][EXTRA_LOCAL_FILE_PATH])
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.attachView))
            }
            else -> {
                Glide.with(context).load(getDrawableByExt(fileExtension))
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(holder.attachView))
            }
        }

        holder.closeIcon.setOnClickListener {
            notifyItemRangeInserted(0, dataList.size - 1)
            attachmentListener.onRemoveAttachment()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getData(): List<Map<String, *>> {
        return dataList
    }


    class ImageAttachView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val closeIcon: View
        val attachView: ImageView

        init {
            closeIcon = itemView.findViewById(R.id.close_icon)
            attachView = itemView.findViewById(R.id.attach_view)
        }
    }

    private fun getDrawableByExt(ext: String): Int {
        return if (FileUtils.videoTypes().contains(ext)) {
            com.kore.botclient.R.drawable.ic_video
        }  else if (FileUtils.spreadTypes().contains(ext)) {
            com.kore.botclient.R.drawable.ic_sheet_old
        } else if (FileUtils.docType().contains(ext)) {
            com.kore.botclient.R.drawable.ic_document_old
        } else if (FileUtils.pdf == ext) {
            com.kore.botclient.R.drawable.ic_pdf
        } else {
            com.kore.botclient.R.drawable.ic_file_general
        }
    }

}
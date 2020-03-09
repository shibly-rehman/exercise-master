package com.example.hsexercise.feature.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hsexercise.R
import com.example.hsexercise.feature.database.FeatureModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_feature.view.*

class FeatureAdapter(ctx: Context) : RecyclerView.Adapter<FeatureAdapter.Renderer>() {

    private var features = mutableListOf<FeatureModel>()

    val glide by lazy { Glide.with(ctx) }

    /** diff util to merge new feature data. */
    private val diffUtil = object: DiffUtil.ItemCallback<FeatureModel>() {
        override fun areItemsTheSame (oldItem: FeatureModel, newItem: FeatureModel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FeatureModel, newItem: FeatureModel) = oldItem == newItem
    }

    /** diff util to merge new feature data. */
    private var differ = AsyncListDiffer (this, diffUtil)


    /** Inflate a item row layout from XML and return the holder */
    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): Renderer {
        val inflater = LayoutInflater.from(parent.context)
        return Renderer(inflater.inflate(R.layout.item_feature, parent, false))
    }

    /** Bind the data with the holder. */
    override fun onBindViewHolder (renderer: Renderer, index: Int) = renderer.bind( differ.currentList[index] )

    /** Returns the total count of items in the list */
    override fun getItemCount () = differ.currentList.size

    /** Update features */
    fun updateData(features: MutableList<FeatureModel>) {
        this.features = features
        differ.submitList(features)
        notifyDataSetChanged()
    }

    /**
     * View holder class that renders features.
     */
    inner class Renderer (override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

        /** Data binder */
        fun bind (feature: FeatureModel) {

            // load image
            glide
                .load(feature.url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_not_found)
                .addListener( object : RequestListener<Drawable> {
                    override fun onLoadFailed( e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Log.d(TAG, "${feature.url} failed to load")
                        return false
                    }

                    override fun onResourceReady( resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                  dataSource: DataSource?, isFirstResource: Boolean ): Boolean {
                        return false
                    }

                })
                .into(containerView.image)
            containerView.author.text = feature.author
            containerView.size.text = String.format("%s x %s", feature.width, feature.height)
        }
    }
}

private val TAG = FeatureAdapter::class.java.simpleName
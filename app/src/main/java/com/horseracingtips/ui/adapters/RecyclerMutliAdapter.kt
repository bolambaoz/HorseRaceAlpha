package com.horseracingtips.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horseracingtips.R
import com.horseracingtips.data.db.entity.HorseNews
import com.horseracingtips.data.db.entity.HorseVideo
import kotlinx.android.synthetic.main.item_horse_news.view.*

class RecyclerMutliAdapter(
    private val context: Context,
    private val listNews: List<HorseNews>? = null,
    private val listVideo: List<HorseVideo>? = null,
    val type: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isVisible = View.GONE
    var toggleTextView = false

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title = view.item_horse_news_title
        var imageUrl = view.item_horse_news_image
        var description = view.item_hors_news_description
        var expandbleLayout = view.expandableLayout
        var cardView = view.home_cardview

    }

    private inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title = view.item_horse_news_title
        var imageUrl = view.item_horse_news_image
        var description = view.item_hors_news_description
        var expandbleLayout = view.expandableLayout
        var cardView = view.home_cardview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_horse_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listNews?.get(position)

        if(holder is HomeViewHolder){
            holder.title.text = item?.title
            Glide.with(context).load(item?.imageUrl).into(holder.imageUrl)
            holder.description.text = item?.description
            holder.expandbleLayout.visibility = isVisible

            holder.cardView.setOnClickListener {
                setIsVisible(!toggleTextView)
                notifyItemChanged(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return listNews?.size!!
    }

   fun getIsVisibleItem(): Int {
        return isVisible
    }

    fun setIsVisible(toggle: Boolean){
        when(toggle){
            true -> {
                isVisible = View.VISIBLE
                toggleTextView = true
            }
            false -> {
                isVisible = View.GONE
                toggleTextView = false
            }
        }
    }

}





//    (context: Context, list: List<HorseNews>,
//                  val typeView: Int) :
//                    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//}
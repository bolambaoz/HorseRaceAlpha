package com.horserace.ui.dashboard

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.horserace.R
import com.horserace.data.db.entity.HorseNews
import com.horserace.databinding.ItemHorseNewsBinding
import com.xwray.groupie.viewbinding.BindableItem

class HorseNewsItem(
    private val horseNews: HorseNews,
    private val context: Context
): BindableItem<ItemHorseNewsBinding>() {

    private var isVisible = View.GONE
    var toggleTextView = false

    fun getIsVisible(): Int {
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

    override fun bind(viewBinding: ItemHorseNewsBinding, position: Int) {
        viewBinding.itemHorseNewsTitle.text = horseNews.title
        Glide.with(context).load(horseNews.imageUrl).into(viewBinding.itemHorseNewsImage)
        viewBinding.itemHorsNewsDescription.text = horseNews.description
        viewBinding.expandableLayout.visibility = getIsVisible()
    }

    override fun getLayout(): Int = R.layout.item_horse_news
    override fun initializeViewBinding(view: View): ItemHorseNewsBinding = ItemHorseNewsBinding.bind(view)
}
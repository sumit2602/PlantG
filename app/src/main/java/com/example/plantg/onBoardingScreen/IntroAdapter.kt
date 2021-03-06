package com.example.plantg.onBoardingScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantg.R
import kotlinx.android.synthetic.main.slider_container.view.*

class IntroAdapter(private val introSlider: List<IntroSlider>) :
    RecyclerView.Adapter<IntroAdapter.IntroViewHolder>() {
    inner class IntroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textTittle = view.Heading1
        private val textDesc = view.Heading2
        private val image=view.gifImageView_Swap
        fun bind(introSlider: IntroSlider) {
            textTittle.setImageResource(introSlider.HeadingOne)
            textDesc.text = introSlider.HeadingTwo
            image.setImageResource(introSlider.Image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        return IntroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.slider_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        holder.bind(introSlider[position])
    }

    override fun getItemCount(): Int {
        return introSlider.size
    }
}
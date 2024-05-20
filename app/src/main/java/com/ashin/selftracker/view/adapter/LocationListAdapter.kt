package com.ashin.selftracker.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashin.selftracker.databinding.LocationItemBinding
import com.ashin.selftracker.model.pojo.LocationInfo
import com.ashin.selftracker.view.activity.RouteMapActivity
import io.realm.kotlin.types.RealmList
import java.util.Date

class LocationListAdapter(val list: RealmList<LocationInfo>): RecyclerView.Adapter<LocationListAdapter.ItemViewHolder>() {
    private lateinit var binding: LocationItemBinding
    class ItemViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
          binding=LocationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
         return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item=list[position]
        binding.tvLatLong.text= "Lat: ${item.latitude}, Lng: ${item.longitude}"
        binding.tvTime.text="Timestamp: ${Date(item.timestamp)}"
        binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, RouteMapActivity::class.java)
            intent.putExtra("latitude", item.latitude)
            intent.putExtra("longitude", item.longitude)
            holder.itemView.context.startActivity(intent)
        }
    }
}
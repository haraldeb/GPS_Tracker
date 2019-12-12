package com.gpstrack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gpstrack.R
import com.gpstrack.model.Track

class TrackAdapter(context: Context, var tracks: List<Track>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        //Prüfen ob die View schon existiert?
        if(convertView == null) {

            //wenn sie nicht existiert, custom row aus xml inflaten
            view = inflater.inflate(R.layout.list_item_view, parent,false)

            //neuer ViewHolder
            holder = ViewHolder()
            holder.title = view.findViewById(R.id.tvTrackTitle) as TextView
            holder.start = view.findViewById(R.id.tvTrackStart) as TextView
            holder.stop = view.findViewById(R.id.tvTrackStop) as TextView


            view.tag = holder
        } else {
            //nicht mehr inflaten, den bereits erstellten holder holen
            view = convertView
            holder = convertView.tag as ViewHolder

        }

        val tvTrackTitle = holder.title
        val tvTrackStart = holder.start
        val tvTrackStop = holder.stop

        val track = getItem(position) as Track

        tvTrackTitle.text = track.title
        tvTrackStart.text = track.starttimestamp.toString()
        tvTrackStop.text = track.stoptimestamp.toString()


        return view
    }

    override fun getItem(position: Int): Any {
        return tracks[position]
    }

    override fun getItemId(position: Int): Long {
        return tracks[position].id
    }

    override fun getCount(): Int {
        return tracks.size
    }

    private class ViewHolder {
        lateinit var title: TextView
        lateinit var start: TextView
        lateinit var stop: TextView
}
}



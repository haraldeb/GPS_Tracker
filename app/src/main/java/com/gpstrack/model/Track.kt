package com.gpstrack.model

data class  Track(val id: Long, var title: String, var starttimestamp: Long, var stoptimestamp: Long,  var GPSPoints: MutableList<Trackpoint>)

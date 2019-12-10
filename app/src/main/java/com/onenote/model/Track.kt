package com.onenote.model

data class Track(val id: Long, var title: String, val starttimestamp: Long, val stoptimestamp: Long,  var GPSPoints: List<Trackpoint>)

package com.gpstrack.model

data class Note(val id: Long, val timestamp: Long, var title: String, var text: String, val latitude: Double, val longitude: Double)

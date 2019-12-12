package com.gpstrack.prefereces

import android.content.Context

class Preferences {

    companion object {
        private const val PREFERENCES_NAME = "preferences_note"
        private const val PREFERENCE_AGREED = "agreed"
    }



    fun getAgreed(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PREFERENCE_AGREED, null)
    }

    fun setAgreed(context: Context, loggedin: String?) {
        val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(PREFERENCE_AGREED, loggedin).apply()
    }

}
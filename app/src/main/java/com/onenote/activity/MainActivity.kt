package com.onenote.activity

import com.onenote.prefereces.Preferences
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.onenote.R
import com.onenote.adapter.TrackAdapter
import com.onenote.constants.Constants
import com.onenote.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener {


    private var db = DatabaseHelper(this)
    private var adapter : TrackAdapter? = null

    val preferences = Preferences()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar initialisieren
        setSupportActionBar(tbMain)


        //Alle Notes aus Datenbank holen.
        val tracks = db.getAllTracks()

        //Note Adapter initialisieren
        adapter = TrackAdapter(this, tracks)
        lvTracks.adapter = adapter
        lvTracks.onItemClickListener = this
    }

    override fun onResume() {
        super.onResume()

        //Track aktualisieren
        updateTracks()


    }


    override fun onClick(v: View?) {
      Toast.makeText(this, R.string.logged_in,Toast.LENGTH_LONG).show()
        finish()
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(this, TrackShowActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_ID,id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //TrackShowActivity öffnen
        val intent = Intent(this, TrackRecordActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun updateTracks()
    {
        //Alle Tracks aus Datenbank holen.
        val tracks = db.getAllTracks()

        adapter!!.tracks = tracks
        adapter!!.notifyDataSetChanged()
    }

}





package com.onenote.activity


import com.onenote.prefereces.Preferences
import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.onenote.R
import com.onenote.constants.Constants
import com.onenote.database.DatabaseHelper
import com.onenote.model.Track
import kotlinx.android.synthetic.main.activity_main.tbMain
import kotlinx.android.synthetic.main.activity_track_show.*


class TrackShowActivity : AppCompatActivity(), View.OnClickListener, DialogInterface.OnClickListener {


    private val preferences = Preferences()
    private var db = DatabaseHelper(this)
    private var track: Track? = null



    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_show)

        //Toolbar initialisieren
        setSupportActionBar(tbMain)

        //BackButton
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Button
        btnSave.setOnClickListener(this)

        //Get Note ID from input
        val id = intent.getLongExtra(Constants.INTENT_EXTRA_ID, Constants.INVALID_VALUE)
        if (id != Constants.INVALID_VALUE) {
            track = db.getTrack(id)

            //set Track title value
            etTitel.setText(track?.title)

            //todo: Track anzeigen aus track.GPSPoints


        }





    }

    override fun onResume() {
        super.onResume()


    }






    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track_edit, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, this)
                .show()

        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun deleteTack() {
        track?.let { db.deleteTrack(it) }

    }

    override fun onClick(v: View?) {
        if (v == btnSave) {
            val title = etTitel.text.toString()



            track?.let {
                it.title = title
                db.updateTrack(it)
            }

            }

            finish()


    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        deleteTack()
        finish()
    }







}







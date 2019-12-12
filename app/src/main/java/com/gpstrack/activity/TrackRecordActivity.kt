package com.gpstrack.activity


import android.Manifest
import com.gpstrack.prefereces.Preferences
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gpstrack.R
import com.gpstrack.constants.Constants
import com.gpstrack.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_track_show.*
import kotlinx.android.synthetic.main.activity_track_record.*
import com.gpstrack.model.Track
import com.gpstrack.model.Trackpoint
import java.time.format.DateTimeFormatter
import java.util.*


class TrackRecordActivity : AppCompatActivity(), View.OnClickListener, DialogInterface.OnClickListener {


    private val preferences = Preferences()
    private var db = DatabaseHelper(this)
    private var track: Track? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_record)

        //Toolbar initialisieren
        //setSupportActionBar(tbMain)

        //BackButton
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Button
        btnStartRecord.setOnClickListener(this)
        btnStopRecord.setOnClickListener(this)


        // INit Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



    }

    override fun onResume() {
        super.onResume()

        checkLastLocation()
    }


    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun checkLastLocation() {
        if(checkPermissions()) {
            fusedLocationClient.lastLocation.addOnCompleteListener(this) {task ->
                lastLocation = task.result

                lastLocation?.let {
                    var trackpoint: Trackpoint? = null
                    trackpoint?.latitude = it.latitude
                    trackpoint?.longitude = it.longitude
                    trackpoint?.timestamp = System.currentTimeMillis()
                    track?.GPSPoints?.add(trackpoint!!)

                }
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            Constants.REQUEST_CODE_LOCATION
        )
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
        if (v == btnStartRecord) {

            val toast = Toast.makeText(applicationContext, "Starte Recording",Toast.LENGTH_SHORT)
            toast.show()

            //Create new Track
            track?.let {
                it.starttimestamp = System.currentTimeMillis()
                it.title = "Test"
                //db.updateNote(it)
            }


            //todo: mit checkLastLocation wird nur ein punkt aufgenommen!
            checkLastLocation()
            //Toast.makeText(applicationContext, track!!.GPSPoints[0].longitude.toString(),Toast.LENGTH_SHORT)
            //track?.let {
            //    it.title = title
            //    db.updateTrack(it)
            //}

        }
        if (v == btnStopRecord) {
            track?.let {
                it.stoptimestamp = System.currentTimeMillis()
                val toast = Toast.makeText(applicationContext, "Stoppe REcording",Toast.LENGTH_SHORT)


                toast.show()
                db.insertTrack(it)




            }
            //finish()
        }



    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        deleteTack()
        finish()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == Constants.REQUEST_CODE_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                checkLastLocation()
            }

        }
    }




}







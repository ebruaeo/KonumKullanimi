package com.example.konumkullanimi

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.konumkullanimi.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var izinKontrol = 0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        flpc = LocationServices.getFusedLocationProviderClient(this)

        binding.buttonKonum.setOnClickListener {
            izinKontrol =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (izinKontrol == PackageManager.PERMISSION_GRANTED) {   // onay verildi
                locationTask = flpc.lastLocation

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            }

        }
    }

    fun  konumBilgisiAl(){
        locationTask.addOnSuccessListener {
            if (it != null) {
                binding.textViewSonuc.text ="${it.latitude} - ${it.longitude}"
            }else {
                binding.textViewSonuc.text = "Enlem boylam bilgisi yok"
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "İzin onaylandı", Toast.LENGTH_SHORT).show()
                locationTask =flpc.lastLocation
                konumBilgisiAl()

            } else {
                Toast.makeText(applicationContext, "İzin onaylanmadı", Toast.LENGTH_SHORT).show()


            }
        }
    }
}
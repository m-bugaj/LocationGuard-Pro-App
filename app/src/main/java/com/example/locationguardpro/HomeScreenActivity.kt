package com.example.locationguardpro

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.app.ActivityOptionsCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.R.anim

class HomeScreenActivity : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home) // Ustaw widok dla tej aktywności

        // Sprawdź uprawnienia lokalizacyjne
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Masz uprawnienia do lokalizacji
            // Kontynuuj z uzyskiwaniem lokalizacji
            initializeMap()
        } else {
            // Poproś użytkownika o uprawnienia lokalizacyjne
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        val startTrackingButton = findViewById<Button>(R.id.start_button)
        val settingsButton = findViewById<ImageButton>(R.id.settings_button)
        val reportsButton = findViewById<Button>(R.id.reports_button)
        val helpButton = findViewById<ImageButton>(R.id.help_button)


        startTrackingButton.setOnClickListener {

            // Tworzymy Intencję, aby przenieść się na ekran TrackingScreenActivity
            val intent = Intent(this, TrackingScreenActivity::class.java)

            // Uruchamiamy aktywność
            startActivity(intent)

            // Ustawiamy animację wejścia i wyjścia
            overridePendingTransition(anim.fade_in, anim.fade_out)
        }

        helpButton.setOnClickListener {
            val intent = Intent(this, HelpScreenActivity::class.java)
            intent.putExtra("isFromHomeScreen", 1)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, anim.fade_in)
        }

        settingsButton.setOnClickListener{
            val intent = Intent(this, LoginScreenActivity::class.java)

            // Uruchamiamy aktywność
            startActivity(intent)

            // Ustawiamy animację wejścia i wyjścia
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        reportsButton.setOnClickListener{
            val intent = Intent(this, StatisticsScreenActivity::class.java)

            // Uruchamiamy aktywność
            startActivity(intent)

            // Ustawiamy animację wejścia i wyjścia
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }



        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienia lokalizacyjne zostały udzielone
                // Kontynuuj z uzyskiwaniem lokalizacji
                initializeMap()
            } else {
                // Uprawnienia lokalizacyjne nie zostały udzielone
                // Obsłuż ten przypadek (np. wyświetl komunikat o braku dostępu)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Mapa jest gotowa do użycia
        this.googleMap = googleMap

        // Tutaj możesz dodać kod dotyczący obsługi mapy, tak jak dodawanie markera, itp.
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

            // Uzyskaj ostatnią lokalizację
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location: Location? ->
                    if (location != null) {
                        // Masz dostęp do obiektu Location, który zawiera aktualne współrzędne
                        val latitude = location.latitude
                        val longitude = location.longitude

                        // Stwórz obiekt LatLng z aktualnych współrzędnych
                        val currentLocation = LatLng(latitude, longitude)

                        // Dodaj marker na mapie
                        googleMap.addMarker(
                            MarkerOptions().position(currentLocation).title("Current Location")
                        )

                        // Przesuń kamerę na aktualną lokalizację
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLocation,
                                15.0f
                            )
                        )
                    }
                }
                .addOnFailureListener(this) { e: Exception? -> }
        } else {
            // Poproś użytkownika o uprawnienia lokalizacyjne
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun initializeMap() {
        // Inicjalizuj mapę tutaj, jeśli to konieczne
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

}
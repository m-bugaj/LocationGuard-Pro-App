package com.example.locationguardpro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.ImageButton
import android.widget.Button
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
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

import com.example.locationguardpro.R

import androidx.fragment.app.FragmentContainerView
import android.view.View



class TrackingScreenActivity : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_screen)

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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        val relativeLayout = findViewById<RelativeLayout>(R.id.relativeLayout)
        val relativeLayoutLocation = findViewById<RelativeLayout>(R.id.relativeLayout_location)
        val locationButton = findViewById<ImageButton>(R.id.location_button)
        val qrBackButton = findViewById<ImageButton>(R.id.qr_back_button)
        val map_view = findViewById<CardView>(R.id.maps_card_view)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        val stopTrackingButton = findViewById<Button>(R.id.stop_button)


//        // Dodaj obsługę kliknięcia dla RelativeLayout_location
//        relativeLayoutLocation.setOnClickListener {
//            // Ukryj RelativeLayout
//            relativeLayout.visibility = View.GONE
//            map_view.visibility = View.VISIBLE
//        }

        stopTrackingButton.setOnClickListener {
            // Tworzymy Intencję, aby przenieść się na ekran TrackingScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)

            // Uruchamiamy aktywność
            startActivity(intent)

            // Ustawiamy animację wejścia i wyjścia
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Dodaj obsługę kliknięcia dla przycisku wewnątrz RelativeLayout_location
        locationButton.setOnClickListener {
            // Ukryj RelativeLayout
            relativeLayout.startAnimation(fadeOut)
            relativeLayout.visibility = View.GONE
            map_view.startAnimation(fadeIn)
            map_view.visibility = View.VISIBLE
            locationButton.startAnimation(fadeOut)
            locationButton.visibility = View.GONE
            qrBackButton.startAnimation(fadeIn)
            qrBackButton.visibility = View.VISIBLE
        }

        qrBackButton.setOnClickListener {
            // Ukryj RelativeLayout
            relativeLayout.startAnimation(fadeIn)
            relativeLayout.visibility = View.VISIBLE
            map_view.startAnimation(fadeOut)
            map_view.visibility = View.GONE
            qrBackButton.startAnimation(fadeOut)
            qrBackButton.visibility = View.GONE
            locationButton.startAnimation(fadeIn)
            locationButton.visibility = View.VISIBLE
        }

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

//    fun start_tracking_button_onClick(view: View) {
//        view.setFocusable(false);
//        // Tworzymy Intencję, aby przenieść się na ekran TrackingScreenActivity
//        val intent = Intent(this, HomeScreenActivity::class.java)
//
//        // Uruchamiamy aktywność
//        startActivity(intent)
//    }

}
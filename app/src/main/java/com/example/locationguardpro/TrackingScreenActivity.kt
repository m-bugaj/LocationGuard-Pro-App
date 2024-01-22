package com.example.locationguardpro

//import me.dm7.barcodescanner.zxing.ZXingScannerView


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.locationguardpro.model.WorkHours
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//import android.R.anim



class TrackingScreenActivity : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private val PERMISSION_REQUEST_CAMERA = 1
    private val qrCodes = listOf(
        QrCode("12345"),
        QrCode("67890"),
        // Dodaj inne kody QR, które są ważne dla pracownika
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_screen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        }

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
        val noActionAnim = AnimationUtils.loadAnimation(this, R.anim.noaction)

        val stopTrackingButton = findViewById<Button>(R.id.stop_button)
        val helpButton = findViewById<ImageButton>(R.id.help_button)
        val qrScannerButton = findViewById<RelativeLayout>(R.id.relativeLayout)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val startTimeSeconds = intent.getLongExtra("START_TIME_SECONDS", -1)

        val myApplication = application as MyApplication
        val appDatabase = myApplication.appDatabase


//        // Dodaj obsługę kliknięcia dla RelativeLayout_location
//        relativeLayoutLocation.setOnClickListener {
//            // Ukryj RelativeLayout
//            relativeLayout.visibility = View.GONE
//            map_view.visibility = View.VISIBLE
//        }

        stopTrackingButton.setOnClickListener {
            val userId = sharedPreferences.getLong("USER_ID", -1)
            if(!userId.equals(-1)){
                val endTimeSeconds = SystemClock.elapsedRealtime() / 1000
                val elapsedTimeSeconds = endTimeSeconds - startTimeSeconds
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val currentDate = Date(System.currentTimeMillis())
                val date = dateFormat.format(currentDate)
                val elapsedTimeHours = elapsedTimeSeconds.toDouble()/3600.0

                val workHours = WorkHours(userId = userId, date = date, hoursWorked = elapsedTimeHours)
                val workHoursDao = appDatabase.workHoursDao()
                runBlocking { workHoursDao.insertWorkHours(workHours) }
                sharedPreferences.edit().remove("START_TIME_SECONDS").apply()




                // Tworzymy Intencję, aby przenieść się na ekran TrackingScreenActivity
                val intent = Intent(this, HomeScreenActivity::class.java)
                intent.putExtra("STOP", true)

                // Uruchamiamy aktywność
                startActivity(intent)

                // Ustawiamy animację wejścia i wyjścia
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            else
                Toast.makeText(this, "Log in to measure time", Toast.LENGTH_SHORT).show()

        }

        helpButton.setOnClickListener {
            val intent = Intent(this, HelpScreenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom, android.R.anim.fade_out)
        }

        qrScannerButton.setOnClickListener {
            initQRCodeScanner()
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

    private fun initQRCodeScanner() {
        // Initialize QR code scanner here
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setOrientationLocked(true)
        integrator.setPrompt("Scan a QR code")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Skanowanie anulowane", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(this, "Zeskanowano: ${result.contents}", Toast.LENGTH_LONG).show()
                checkScannedQRCode(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkScannedQRCode(scannedCode: String) {
        val found = qrCodes.any { it.content == scannedCode }
        if (found) {
            Toast.makeText(this, "Zeskanowano poprawny kod", Toast.LENGTH_LONG).show()
            // Pracownik jest na właściwym miejscu
        } else {
            Toast.makeText(this, "Nieprawidłowy kod", Toast.LENGTH_LONG).show()
            // Pracownik nie jest na miejscu, podejmij odpowiednie działania
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
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
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

data class QrCode(val content: String)



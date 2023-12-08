package com.example.locationguardpro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeScreenActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home); // Ustaw widok dla tej aktywności

        // Sprawdź uprawnienia lokalizacyjne
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Masz uprawnienia do lokalizacji
            // Kontynuuj z uzyskiwaniem lokalizacji
            initializeMap();
        } else {
            // Poproś użytkownika o uprawnienia lokalizacyjne
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienia lokalizacyjne zostały udzielone
                // Kontynuuj z uzyskiwaniem lokalizacji
                initializeMap();
            } else {
                // Uprawnienia lokalizacyjne nie zostały udzielone
                // Obsłuż ten przypadek (np. wyświetl komunikat o braku dostępu)
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Mapa jest gotowa do użycia
        this.googleMap = googleMap;

        // Tutaj możesz dodać kod dotyczący obsługi mapy, tak jak dodawanie markera, itp.

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

            // Uzyskaj ostatnią lokalizację
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // Masz dostęp do obiektu Location, który zawiera aktualne współrzędne
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Stwórz obiekt LatLng z aktualnych współrzędnych
                            LatLng currentLocation = new LatLng(latitude, longitude);

                            // Dodaj marker na mapie
                            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));

                            // Przesuń kamerę na aktualną lokalizację
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        // Obsłuż błąd uzyskiwania lokalizacji
                    });
        } else {
            // Poproś użytkownika o uprawnienia lokalizacyjne
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void initializeMap() {
        // Inicjalizuj mapę tutaj, jeśli to konieczne
    }
}

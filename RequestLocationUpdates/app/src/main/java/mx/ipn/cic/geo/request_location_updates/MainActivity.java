package mx.ipn.cic.geo.request_location_updates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION = 0x100;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean requestingLocationUpdates = false;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Revisar si se cuentan con los permisos necesarios para la app.
        requestUserPermissions();
        getLastLocation();
        createLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                String locationMessage;

                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...

                    mMap.clear();
                    
                    LatLng ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(ubicacion).title("I'm Here."));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));

                    locationMessage = "Last location onLocationResult:(" + location.getLatitude() + "," +
                                         location.getLongitude() + ")";
                    // Enviar el mensaje al log.
                    Log.i("onLocationResult", locationMessage);
                    // Logic to handle location object
                    Toast.makeText(MainActivity.this, locationMessage, Toast.LENGTH_LONG).show();
                }
            };
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    // Request user permission section.
    private void requestUserPermissions()
    {
        // Here, thisActivity is the current activity
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED))
            {
            // Permission is not granted
            // Should we show an explanation?
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) ||
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) ||
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET))) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d("requestPermissions", "Enviar un toast con información más detallada.");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION);
                // MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d("requestPermissions", "Se obtuvieron los permisos correspondientes.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission was granted, yay! Do the location task you need to do.
                Toast.makeText(this, "Se obtuvo el permiso requerido", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Se obtuvo el permiso requerido", Toast.LENGTH_LONG).show();
                // Permission denied, boo! Disable the functionality that depends on this permission.
            }
        }
        // Other 'case' lines to check for other permissions this app might request.
    }

    // Retrieve the last know user location.
    @SuppressLint("MissingPermission")
    private void getLastLocation()
    {
        final String[] locationMessage = new String[1];

        fusedLocationClient.getLastLocation().addOnSuccessListener(
            this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        userLocation = location;
                        locationMessage[0] = "Last location using getLastLocation:(" + location.getLatitude() + "," +
                            location.getLongitude() + ")";
                        // Logic to handle location object
                        Toast.makeText(MainActivity.this, locationMessage[0], Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    protected void createLocationRequest() {
        final int requestLocationInterval = 10000;
        final int requestLocationFastestInterval = 5000;

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(requestLocationInterval);
        locationRequest.setFastestInterval(requestLocationFastestInterval);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                                  .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                requestingLocationUpdates = true;
                startLocationUpdates();
                Toast.makeText(MainActivity.this, "Criterios de localización correctos", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        resolvable.startResolutionForResult(MainActivity.this,
                        REQUEST_CHECK_SETTINGS);
                        // Falta agregar el onActivityResult.
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
        Looper.getMainLooper());
    }
}

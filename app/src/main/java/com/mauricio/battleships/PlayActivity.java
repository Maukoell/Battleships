package com.mauricio.battleships;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class PlayActivity extends AppCompatActivity {
    private TextView pt;
    private Context ct;
    private String myIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ct = this;
        pt = findViewById(R.id.ipField);
        setIPOfTextView();
        Button b3 = findViewById(R.id.connectButton);
        Thread listeningThread = new Thread(new ListenForConnection(this));
        listeningThread.start();
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.opponentIpField);
                Thread sendingThread = new Thread(new SendForConnection(myIp, tv.getText().toString()));
                sendingThread.start();
            }
        });
    }

    public void launchWaitingActivity() {
        Intent intent = new Intent(ct, WaitingActivity.class);
        intent.putExtra("ipAddress", myIp);
        startActivity(intent);
    }

    protected void setIPOfTextView() {
        myIp = wifiIpAddress(ct);
        pt.setText(myIp);
    }

    protected String wifiIpAddress(Context context) {
        int permissionCallback = 0;
        Log.i("T", this.toString());
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ct,
                Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_WIFI_STATE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                        permissionCallback);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //Toast.makeText(this, "IP-Address not found!", Toast.LENGTH_SHORT).show();
        }



        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }
}

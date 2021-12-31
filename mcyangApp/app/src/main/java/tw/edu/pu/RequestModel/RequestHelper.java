package tw.edu.pu.RequestModel;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.permissionx.guolindev.PermissionX;

import org.altbeacon.bluetooth.BluetoothMedic;

import tw.edu.pu.R;

public class RequestHelper {

    final String TAG = "RequestHelper: ";

    private final Activity activity;

    public void requestBasicPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            PermissionX.init((FragmentActivity) activity)
                    .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

                    .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(
                            deniedList, "Grant Permission!", "Sure", "Cancel"))

                    .request((allGranted, grantedList, deniedList) -> {
                        if (!allGranted) {
                            Toast.makeText(activity, "Grant Permission failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(activity , "您的手機無法使用該應用...", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    public RequestHelper(Activity activity) {
        this.activity = activity;
    }

    public void requestBluetooth() {
        BluetoothAdapter mBluetoothAdapter;

        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(activity, "您的手機無法使用該應用...", Toast.LENGTH_SHORT).show();

        }

        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "您的手機無法使用該應用...", Toast.LENGTH_SHORT).show();

        }

        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enableBluetooth);
        }
    }

    public void flushBluetooth() {
        BluetoothMedic medic = BluetoothMedic.getInstance();
        medic.enablePowerCycleOnFailures(activity);
        medic.enablePeriodicTests(activity, BluetoothMedic.SCAN_TEST |
                BluetoothMedic.TRANSMIT_TEST);
    }

    public void checkGPS_Enabled() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (!gps_enabled || !network_enabled) {
            new MaterialAlertDialogBuilder(activity)
                    .setMessage(R.string.request_Gps_Not_Enabled)
                    .setPositiveButton("確認", (dialogInterface, i) -> activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    public void checkInternet_Enabled() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null)
            if (!networkInfo.isConnected()) {
                Toast.makeText(activity, "偵測到沒網絡，請麻煩打開網絡。", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(activity::finish, 3000);
            }
    }
}

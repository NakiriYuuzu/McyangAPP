package tw.edu.pu.BeaconModel;

import android.app.Activity;
import android.bluetooth.le.AdvertiseSettings;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Collections;

import tw.edu.pu.DefaultSetting;
import tw.edu.pu.StoredData.ShareData;

public class BeaconController {

    private static final String TAG = "RangingActivity";

    private static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 1000L;

    //Settings
    private final Activity activity;
    private Beacon beacon;
    private BeaconManager beaconManager;
    private BeaconTransmitter beaconTransmitter;
    private final ShareData shareData;

    //Scan_Beacon
    private Region region;

    //Broadcast_Beacon
    private final BeaconParser beaconParser = new BeaconParser()
            .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

    public BeaconController(Activity activity) {
        this.activity = activity;
        shareData = new ShareData(activity);
    }

    public void beaconInit(String url) {
        beaconManager = BeaconManager.getInstanceForApplication(activity);
        this.region = new Region("UniqueID", Identifier.parse(url), null, null);
        //beacon AddStone m:0-3=4c000215 or alt beacon = m:2-3=0215
        beaconManager.getBeaconParsers().add(beaconParser);
        beaconManager.setForegroundScanPeriod(DEFAULT_FOREGROUND_SCAN_PERIOD);
    }

    public void startScanning(BeaconModify beaconModify) {
        beaconManager.addRangeNotifier(beaconModify::modifyData);

        beaconManager.startRangingBeacons(region);
    }

    public void stopScanning() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.stopRangingBeacons(region);
        beaconManager.removeAllRangeNotifiers();
    }

    public void init_Sign_BroadcastBeacon() {
        try {
            if (shareData.getMajor() != null && shareData.getMinor() != null) {
                Log.e(TAG, "Major: " + shareData.getMajor() + " Minor: " + shareData.getMinor());
                beacon = new Beacon.Builder()
                        .setId1(DefaultSetting.BEACON_UUID_SIGN)
                        .setId2(shareData.getMajor())
                        .setId3(shareData.getMinor())
                        .setManufacturer(0x0118)
                        .setTxPower(-79)
                        .setDataFields(Collections.singletonList(0L))
                        .build();

                beaconTransmitter = new BeaconTransmitter(activity, beaconParser);
                beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
                beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            }
            else {
                if (shareData.getMajor() == null)
                    Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
                if (shareData.getQuestion_ID() == null)
                    Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            if (shareData.getMajor() == null)
                Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
            if (shareData.getQuestion_ID() == null)
                Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
        }
    }

    public void init_Race_BroadcastBeacon() {
        try {
            if (shareData.getMajor() != null && shareData.getRaceID() != null) {
                Log.e(TAG, "Major: " + shareData.getMajor() + " Minor: " + shareData.getRaceID());
                beacon = new Beacon.Builder()
                        .setId1(DefaultSetting.BEACON_UUID_RACE)
                        .setId2(shareData.getMajor())
                        .setId3(shareData.getRaceID())
                        .setManufacturer(0x0118)
                        .setTxPower(-79)
                        .setDataFields(Collections.singletonList(0L))
                        .build();

                beaconTransmitter = new BeaconTransmitter(activity, beaconParser);
                beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
                beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            } else {
                if (shareData.getMajor() == null)
                    Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
                if (shareData.getQuestion_ID() == null)
                    Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            if (shareData.getMajor() == null)
                Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
            if (shareData.getQuestion_ID() == null)
                Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
        }
    }

    public void init_Answer_BroadcastBeacon() {
        try {
            if (shareData.getMajor() != null && shareData.getQuestion_ID() != null) {
                Log.e(TAG, "Major: " + shareData.getMajor() + " Minor: " + shareData.getRaceID());
                beacon = new Beacon.Builder()
                        .setId1(DefaultSetting.BEACON_UUID_ANSWER)
                        .setId2(shareData.getMajor())
                        .setId3(shareData.getQuestion_ID())
                        .setManufacturer(0x0118)
                        .setTxPower(-69)
                        .setDataFields(Collections.singletonList(0L))
                        .build();

                beaconTransmitter = new BeaconTransmitter(activity, beaconParser);
                beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
                beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            }
            else {
                if (shareData.getMajor() == null)
                    Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
                if (shareData.getQuestion_ID() == null)
                    Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, "無法打開beacon，請先點名后在操作！", Toast.LENGTH_SHORT).show();
        }
    }

    public void init_GroupSecond_BroadcastBeacon() {
        try {
            if (shareData.getMajor() != null && shareData.getDesc_ID() != null) {
                Log.e(TAG, "Major: " + shareData.getMajor() + " Minor: " + shareData.getDesc_ID());
                beacon = new Beacon.Builder()
                        .setId1(DefaultSetting.BEACON_UUID_GROUP)
                        .setId2(shareData.getMajor())
                        .setId3(shareData.getDesc_ID())
                        .setManufacturer(0x0118)
                        .setTxPower(-79)
                        .setDataFields(Collections.singletonList(0L))
                        .build();

                beaconTransmitter = new BeaconTransmitter(activity, beaconParser);
                beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
                beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            }
            else {
                if (shareData.getMajor() == null)
                    Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
                if (shareData.getQuestion_ID() == null)
                    Toast.makeText(activity, "無法取得群組ID！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, "無法打開beacon，請先點名后在操作！", Toast.LENGTH_SHORT).show();
        }
    }

    public void init_GroupThird_BroadcastBeacon() {
        try {
            if (shareData.getMajor() != null && shareData.getDesc_ID() != null) {
                Log.e(TAG, "Major: " + shareData.getMajor() + " Minor: " + shareData.getDesc_ID());
                beacon = new Beacon.Builder()
                        .setId1(DefaultSetting.BEACON_UUID_TEAM)
                        .setId2(shareData.getMajor())
                        .setId3(shareData.getDesc_ID())
                        .setManufacturer(0x0118)
                        .setTxPower(-79)
                        .setDataFields(Collections.singletonList(0L))
                        .build();

                beaconTransmitter = new BeaconTransmitter(activity, beaconParser);
                beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
                beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            }
            else {
                if (shareData.getMajor() == null)
                    Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
                if (shareData.getQuestion_ID() == null)
                    Toast.makeText(activity, "無法取得群組ID！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, "無法打開beacon，請先點名后在操作！", Toast.LENGTH_SHORT).show();
        }
    }

    public void start_BroadcastBeacon() {
        try {
            beaconTransmitter.startAdvertising(beacon);
        } catch (Exception e) {
            if (shareData.getMajor() == null)
                Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
            if (shareData.getQuestion_ID() == null)
                Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop_BroadcastBeacon() {
        try {
            beaconTransmitter.stopAdvertising();
        } catch (Exception e) {
            if (shareData.getMajor() == null)
                Toast.makeText(activity, "請先點名后在開始廣播！", Toast.LENGTH_SHORT).show();
            if (shareData.getQuestion_ID() == null)
                Toast.makeText(activity, "無法取得題目ID！", Toast.LENGTH_SHORT).show();
        }
    }

    public interface BeaconModify {
        void modifyData(Collection<Beacon> beacons, Region region);
    }
}

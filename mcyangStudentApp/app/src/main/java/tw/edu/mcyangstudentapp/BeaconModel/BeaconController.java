package tw.edu.mcyangstudentapp.BeaconModel;

import android.app.Activity;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Collections;

import tw.edu.mcyangstudentapp.DefaultSetting;
import tw.edu.mcyangstudentapp.StoredData.ShareData;

public class BeaconController {

    private static final String TAG = "RangingActivity";

    private static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 1000L;

    private final Activity activity;
    private Beacon beacon;
    private BeaconManager beaconManager;
    private BeaconTransmitter beaconTransmitter;

    ShareData shareData;

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
        this.region = new Region("UniqueID", Identifier.parse(url), null, null);

        beaconManager = BeaconManager.getInstanceForApplication(activity);

        //beacon AddStone m:0-3=4c000215 or alt beacon = m:2-3=0215
        beaconManager.getBeaconParsers().add(beaconParser);
        beaconManager.setForegroundScanPeriod(DEFAULT_FOREGROUND_SCAN_PERIOD);
    }

    public void startScanning() {
        beaconManager.addRangeNotifier((beacons, region) -> {
            if (beacons.size() > 0) {
                Log.e(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away, id = " + beacons.iterator().next().getId1());
            } else {
                Log.e(TAG, "Nothing Here!");
            }
        });

        beaconManager.startRangingBeacons(region);
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

    public void init_BroadcastBeacon() {
        beacon = new Beacon.Builder()
                .setId1(DefaultSetting.BEACON_UUID_MAIN)
                .setId2(shareData.getStudentID())
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-79)
                .setDataFields(Collections.singletonList(0L))
                .build();

        beaconTransmitter = new BeaconTransmitter(activity, beaconParser);
    }

    public void start_BroadcastBeacon() {
        beaconTransmitter.startAdvertising(beacon);
    }

    public void stop_BroadcastBeacon() {
        beaconTransmitter.stopAdvertising();
    }

    public interface BeaconModify {
        void modifyData(Collection<Beacon> beacons, Region region);
    }

    public interface BeaconSimulator {
        void beaconSimulator();
    }
}

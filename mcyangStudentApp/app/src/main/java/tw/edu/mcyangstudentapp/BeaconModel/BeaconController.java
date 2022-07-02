package tw.edu.mcyangstudentapp.BeaconModel

import android.app.Activity
import org.altbeacon.beacon.*
import tw.edu.mcyangstudentapp.StoredData.ShareData
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController
import tw.edu.mcyangstudentapp.BeaconModel.BeaconController.BeaconModify
import tw.edu.mcyangstudentapp.DefaultSetting

class BeaconController(private val activity: Activity) {
    private var beacon: Beacon? = null
    private var beaconManager: BeaconManager? = null
    private var beaconTransmitter: BeaconTransmitter? = null
    var shareData: ShareData

    //Scan_Beacon
    private var region: Region? = null

    //Broadcast_Beacon
    private val beaconParser = BeaconParser()
        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")

    fun beaconInit(url: String?) {
        region = Region("UniqueID", Identifier.parse(url), null, null)
        beaconManager = BeaconManager.getInstanceForApplication(activity)

        //beacon AddStone m:0-3=4c000215 or alt beacon = m:2-3=0215
        beaconManager!!.beaconParsers.add(beaconParser)
        beaconManager!!.foregroundScanPeriod = DEFAULT_FOREGROUND_SCAN_PERIOD
    }

    fun startScanning(beaconModify: BeaconModify) {
        beaconManager!!.addRangeNotifier { beacons: Collection<Beacon?>?, region: Region? ->
            beaconModify.modifyData(
                beacons,
                region
            )
        }
        beaconManager!!.startRangingBeacons(region!!)
    }

    fun stopScanning() {
        beaconManager!!.removeAllMonitorNotifiers()
        beaconManager!!.stopRangingBeacons(region!!)
        beaconManager!!.removeAllRangeNotifiers()
    }

    fun init_BroadcastBeacon() {
        beacon = Beacon.Builder()
            .setId1(DefaultSetting.BEACON_UUID_MAIN)
            .setId2(shareData.studentID)
            .setId3("2")
            .setManufacturer(0x0118)
            .setTxPower(-79)
            .setDataFields(listOf(0L))
            .build()
        beaconTransmitter = BeaconTransmitter(activity, beaconParser)
    }

    fun start_BroadcastBeacon() {
        beaconTransmitter!!.startAdvertising(beacon)
    }

    fun stop_BroadcastBeacon() {
        beaconTransmitter!!.stopAdvertising()
    }

    interface BeaconModify {
        fun modifyData(beacons: Collection<Beacon?>?, region: Region?)
    }

    companion object {
        private const val DEFAULT_FOREGROUND_SCAN_PERIOD = 1000L
    }

    init {
        shareData = ShareData(activity)
    }
}
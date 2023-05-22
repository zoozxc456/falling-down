package com.example.fallingdown

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class AddNewFriendFragment(private val fragment: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            LayoutInflater.from(context).inflate(R.layout.fragment_scan_qr_code, container, false)
        val surfaceView = view.findViewById<SurfaceView>(R.id.qrcode_scanner)
        scanQrCode(surfaceView)
        return view
    }

    private fun scanQrCode(surfaceView: SurfaceView) {
        val barcodeDetector =
            BarcodeDetector.Builder(this.context).setBarcodeFormats(Barcode.QR_CODE).build()
        val cameraSource =
            CameraSource.Builder(this.context, barcodeDetector).setRequestedPreviewSize(300, 300)
                .build()
        surfaceView.holder.addCallback(
            MySurfaceCallback(
                requireContext(),
                barcodeDetector,
                cameraSource
            )
        )

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                TODO("Not yet implemented")
            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>) {
                val qrCodes: SparseArray<Barcode> = p0.detectedItems
                if (qrCodes.size() != 0) {
                    Runnable {
                        Log.d("qrcode", qrCodes.valueAt(0).displayValue)
                    }
                }
            }
        })
    }

    class MySurfaceCallback(
        context: Context,
        barcodeDetector: BarcodeDetector,
        cameraSource: CameraSource
    ) : SurfaceHolder.Callback {
        private var _context: Context
        private var _barcodeDetector: BarcodeDetector
        private var _cameraSource: CameraSource

        init {
            _context = context
            _barcodeDetector = barcodeDetector
            _cameraSource = cameraSource
        }

        override fun surfaceCreated(p0: SurfaceHolder) {
            if (ActivityCompat.checkSelfPermission(
                    _context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            _cameraSource.start(p0)
        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            _cameraSource.stop()
        }
    }
}
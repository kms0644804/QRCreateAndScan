package com.boring.qrcreateandscan

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scan_qr.*

class ScanQR : AppCompatActivity() {

    private var qrScan: IntentIntegrator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        scanning()

        rescan.setOnClickListener {
            scanning()
        }
        back_button.setOnClickListener {
            finish()
        }
    }

    private fun scanning() {
        qrScan = IntentIntegrator(this)
        qrScan!!.setOrientationLocked(false)
        qrScan!!.setPrompt("QR 코드 스캔")
        qrScan!!.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "인식못함", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result.contents)))
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "인터넷 연결 실패!\n클립보드에 읽어온 데이터를 저장합니다.", Toast.LENGTH_SHORT).show()
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipdata = ClipData.newPlainText("qr_code",result.contents)
                    clipboard.setPrimaryClip(clipdata)
                    Toast.makeText(this,"저장된 데이터: $clipdata", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import com.example.scan2Connect.R
import com.example.scan2Connect.domain.qr.ContactData
import com.example.scan2Connect.domain.qr.exception.NoUserDataException
import org.json.JSONObject
import qrcode.render.QRCodeGraphics

class YourBusinessCardActivity : ComponentActivity() {

    private var isIndividual = false
    private lateinit var tvYourName : TextView
    private lateinit var tvYourStreet : TextView
    private lateinit var tvYourCity : TextView
    private lateinit var tvYourPhoneNumber : TextView
    private lateinit var tvYourEmailAddress : TextView
    private lateinit var mBtnEditBusinessCardGray : CardView
    private lateinit var mBtnEditBusinessCardBlue : CardView
    private lateinit var mBtnShowQRCard : CardView
    private lateinit var mBtnEditShareBusinessCard : CardView
    private var mUserDataJSONFromPrefString = ""

    private var editBusinessCardListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            setViewLayout()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewLayout()
    }

    private fun setViewLayout() {
        val sharedPref = this.getSharedPreferences("com.example.scan2Connect.shared_prefs", Context.MODE_PRIVATE)
        mUserDataJSONFromPrefString = sharedPref.getString("userDataJSON", "").toString()

        if (mUserDataJSONFromPrefString != "")  {
            isIndividual = true
            setContentView(R.layout.activity_your_business_card_indivdiual)
            initIndividualView()
        }
        else {
            isIndividual = false
            setContentView(R.layout.activity_your_business_card_general)
            initGeneralView()
        }
    }

    private fun initIndividualView() {
        tvYourName = findViewById(R.id.tv_your_name)
        tvYourStreet = findViewById(R.id.tv_your_street)
        tvYourCity = findViewById(R.id.tv_your_city)
        tvYourPhoneNumber = findViewById(R.id.tv_your_phone_number)
        tvYourEmailAddress = findViewById(R.id.tv_your_email_address)


        mBtnEditBusinessCardGray = findViewById(R.id.btn_edit_business_card_gray)
        mBtnEditBusinessCardGray.setOnClickListener {
            val i = Intent(this, EditBusinessCardActivity::class.java)
            editBusinessCardListener.launch(i)
        }

        mBtnShowQRCard = findViewById(R.id.btn_show_qr_code)
        mBtnShowQRCard.setOnClickListener {
            val intent = Intent(this, YourQRCodeActivity::class.java)
            startActivity(intent)
        }

        mBtnEditShareBusinessCard = findViewById(R.id.btn_share_business_card)
        mBtnEditShareBusinessCard.setOnClickListener {
            // TODO not implemented yet
        }

        getUserData()
        renderQrCode()
    }

    private fun initGeneralView() {
        mBtnEditBusinessCardBlue = findViewById(R.id.btn_edit_business_card_blue)
        mBtnEditBusinessCardBlue.setOnClickListener {
            val i = Intent(this, EditBusinessCardActivity::class.java)
            editBusinessCardListener.launch(i)
        }
    }

    /**
     * Render contact data as QR code and display it
     */
    private fun renderQrCode() {
        val qrImgView: ImageView = findViewById(R.id.qr_code)

        val sharedPref = this.getSharedPreferences("com.example.scan2Connect.shared_prefs", Context.MODE_PRIVATE)
        var renderedQr: QRCodeGraphics? = null
        try {
            renderedQr = ContactData.generateContactQr(sharedPref)
        } catch (e: NoUserDataException) {
            Log.d("qr", "Not generating QR on business card activity because no user data exists")
        }

        if (renderedQr === null) {
            return
        }

        qrImgView.setImageBitmap(renderedQr.nativeImage() as Bitmap?)
    }

    private fun getUserData() {
        val userDataJSONFromPref = JSONObject(mUserDataJSONFromPrefString)
        val lastname = userDataJSONFromPref["lastname"].toString()
        val firstname = userDataJSONFromPref["firstname"].toString()
        val street = userDataJSONFromPref["street"].toString()
        val houseNumber = userDataJSONFromPref["house_number"].toString()
        val plz = userDataJSONFromPref["plz"].toString()
        val city = userDataJSONFromPref["city"].toString()
        val telNumber = userDataJSONFromPref["tel_number"].toString()
        val emailAddress = userDataJSONFromPref["email_address"].toString()

        tvYourName.text = "$lastname $firstname"
        tvYourStreet.text = "$street $houseNumber"
        tvYourCity.text = "$plz $city"
        tvYourPhoneNumber.text = telNumber
        tvYourEmailAddress.text = emailAddress
    }
}

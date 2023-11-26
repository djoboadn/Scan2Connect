package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import com.example.scan2Connect.R
import org.json.JSONObject

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
    private lateinit var mBtnShareBusinessCard : CardView
    private lateinit var mBusinessCard : CardView
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
        mBusinessCard = findViewById(R.id.business_card)


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

        mBtnShareBusinessCard = findViewById(R.id.btn_share_business_card)
        mBtnShareBusinessCard.setOnClickListener {
            convertBusinessCardToBitmap()
        }
        getUserData()
    }

    private fun initGeneralView() {
        mBtnEditBusinessCardBlue = findViewById(R.id.btn_edit_business_card_blue)
        mBtnEditBusinessCardBlue.setOnClickListener {
            val i = Intent(this, EditBusinessCardActivity::class.java)
            editBusinessCardListener.launch(i)
        }
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

    private fun convertBusinessCardToBitmap() {
        val businessCardBitmap = mBusinessCard.drawToBitmap()
        println("businessCardBitmap : $businessCardBitmap")
        saveBitmapToGallery(businessCardBitmap)
    }

    private fun saveBitmapToGallery(businessCardBitmap: Bitmap) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "businessCardBitmap.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        val outputStream = contentResolver.openOutputStream(uri)
        outputStream.use {
            if (outputStream != null) {
                businessCardBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }

        outputStream!!.close()

        shareBusinessCard(uri)
    }

    private fun shareBusinessCard(uri : Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(shareIntent, "Visitenkarte teilen"))
    }
}

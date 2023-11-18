package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.scan2Connect.R
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class EditBusinessCardActivity : ComponentActivity() {

    private lateinit var mProgressLayout : RelativeLayout
    private lateinit var mEtLastname : EditText
    private lateinit var mEtFirstname : EditText
    private lateinit var mEtStreet : EditText
    private lateinit var mEtHouseNumber : EditText
    private lateinit var mEtPLZ : EditText
    private lateinit var mEtCity : EditText
    private lateinit var mEtTelNumber : EditText
    private lateinit var mEtEmailAddress : EditText
    private lateinit var mSaveButton : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_business_card)

        val btnBack : ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener{
            finish()
        }

        initView()
    }

    private fun initView() {
        mProgressLayout = findViewById(R.id.layout_progress)
        mEtLastname = findViewById(R.id.et_lastname)
        mEtFirstname = findViewById(R.id.et_firstname)
        mEtStreet = findViewById(R.id.et_street)
        mEtHouseNumber = findViewById(R.id.et_house_number)
        mEtPLZ = findViewById(R.id.et_plz)
        mEtCity = findViewById(R.id.et_city)
        mEtTelNumber = findViewById(R.id.et_tel_number)
        mEtEmailAddress = findViewById(R.id.et_email_address)
        mSaveButton = findViewById(R.id.btn_save)
        mProgressLayout.visibility = VISIBLE
        checkIfDataAvailable()
    }

    private fun checkIfDataAvailable() {
        val sharedPref = this.getSharedPreferences("com.example.scan2Connect.shared_prefs", Context.MODE_PRIVATE)
        val userDataJSONFromPrefString = sharedPref.getString("userDataJSON", "").toString()

        if (userDataJSONFromPrefString != "")  {
            val userDataJSONFromPref = JSONObject(userDataJSONFromPrefString)
            mEtLastname.setText(userDataJSONFromPref["lastname"].toString())
            mEtFirstname.setText(userDataJSONFromPref["firstname"].toString())
            mEtStreet.setText(userDataJSONFromPref["street"].toString())
            mEtHouseNumber.setText(userDataJSONFromPref["house_number"].toString())
            mEtPLZ.setText(userDataJSONFromPref["plz"].toString())
            mEtCity.setText(userDataJSONFromPref["city"].toString())
            mEtTelNumber.setText(userDataJSONFromPref["tel_number"].toString())
            mEtEmailAddress.setText(userDataJSONFromPref["email_address"].toString())
        }
        mProgressLayout.visibility = INVISIBLE

        mSaveButton.setOnClickListener {
            mProgressLayout.visibility = VISIBLE
            checkUserInput()
        }
    }

    @SuppressLint("ResourceType")
    private fun checkUserInput() {
        if (
            mEtLastname.text.toString()== "" ||
            mEtFirstname.text.toString()== "" ||
            mEtStreet.text.toString()== "" ||
            mEtHouseNumber.text.toString()== "" ||
            mEtPLZ.text.toString()== "" ||
            mEtCity.text.toString()== "" ||
            mEtTelNumber.text.toString()== "" ||
            mEtEmailAddress.text.toString()== ""
        ) {
            Toast.makeText(this, resources.getString(R.string.check_input), Toast.LENGTH_SHORT).show()
            mProgressLayout.visibility = INVISIBLE
        }
        else {
            getUserInputs()
        }
    }

    private fun getUserInputs() {
        val lastname = mEtLastname.text
        val firstname = mEtFirstname.text
        val street = mEtStreet.text
        val houseNumber = mEtHouseNumber.text
        val plz = mEtPLZ.text
        val city = mEtCity.text
        val telNumber = mEtTelNumber.text
        val emailAddress = mEtEmailAddress.text

        val userDateJSON = JSONObject()
        userDateJSON.put("lastname", lastname)
        userDateJSON.put("firstname", firstname)
        userDateJSON.put("street", street)
        userDateJSON.put("house_number", houseNumber)
        userDateJSON.put("plz", plz)
        userDateJSON.put("city", city)
        userDateJSON.put("tel_number", telNumber)
        userDateJSON.put("email_address", emailAddress)

        saveToSharedPrefAndFinish(userDateJSON)
    }

    private fun saveToSharedPrefAndFinish(userDataJSON : JSONObject) {
        val sharedPref = this.getSharedPreferences("com.example.scan2Connect.shared_prefs", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("userDataJSON", userDataJSON.toString())
            apply()
        }
        mProgressLayout.visibility = INVISIBLE
        Toast.makeText(this, resources.getString(R.string.data_saved_successfull), Toast.LENGTH_SHORT).show()

        setResult(RESULT_OK)
        finish()
    }

}

package com.example.scan2Connect.domain.qr

import android.content.SharedPreferences
import android.util.Log
import com.example.scan2Connect.domain.qr.exception.NoUserDataException
import org.json.JSONObject
import qrcode.QRCode
import qrcode.render.QRCodeGraphics

/**
 * Functions for generating vCard data to put into the QR code
 *
 * specification: https://www.rfc-editor.org/rfc/rfc6350; especially p. 56
 */
class ContactData {
    companion object {
        /**
         * Generate QR contact code from stored contact data
         */
        fun generateContactQr(sharedPref: SharedPreferences): QRCodeGraphics {
            val userDataJSONFromPref = this.getFromStorage(sharedPref)
            val vCardEncoded = this.constructVCard(userDataJSONFromPref)
            Log.d("vCardEncoded", vCardEncoded)
            return this.generateQr(vCardEncoded)
        }

        /**
         * Get contact data object from storage
         */
        private fun getFromStorage(sharedPref: SharedPreferences): JSONObject {
            val userDataJSONFromPrefString = sharedPref.getString("userDataJSON", "").toString()

            if (userDataJSONFromPrefString === "") {
                throw NoUserDataException()
            }

            return JSONObject(userDataJSONFromPrefString)
        }

        /**
         * Generate vCard data format for a given contact
         */
        private fun constructVCard(userDataJSONFromPref: JSONObject): String {
            var vCardStr: String = ""

            vCardStr += "BEGIN:VCARD\n"
            vCardStr += "VERSION:4.0\n"
            vCardStr += "FN:%s %s\n".format(
                userDataJSONFromPref["firstname"].toString(),
                userDataJSONFromPref["lastname"].toString()
            )
            vCardStr += "N:%s;%s;;;\n".format(
                userDataJSONFromPref["lastname"].toString(),
                userDataJSONFromPref["firstname"].toString()
            )
            vCardStr += "EMAIL;PREF=1:%s\n".format(userDataJSONFromPref["email_address"].toString())
            vCardStr += "TEL;PREF=1:%s\n".format(
                userDataJSONFromPref["tel_number"].toString()
            )
            vCardStr += "ADR;PREF=1:;;%s %s;%s;;%s;DE\n".format(
                userDataJSONFromPref["street"].toString(),
                userDataJSONFromPref["house_number"].toString(),
                userDataJSONFromPref["city"].toString(),
                userDataJSONFromPref["plz"].toString()
            )
            vCardStr += "END:VCARD"

            return vCardStr
        }

        /**
         * Generate QR code graphic based on given vCard data
         */
        private fun generateQr(vCardEncoded: String): QRCodeGraphics {
            val contactQr = QRCode.ofSquares()
                .build(vCardEncoded)
            return contactQr.render()
        }
    }
}

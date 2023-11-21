package com.example.scan2Connect.domain.qr

/**
 * Functions for generating vCard data to put into the QR code
 */
class ContactData {
    companion object {
        /**
         * Generate vCard data format for a given contact
         */
        fun constructVCard(): String {
            var vCardStr: String = ""

            vCardStr += "BEGIN:VCARD"
            vCardStr += "VERSION:4.0"
            vCardStr += "FN:%s".format("Jarne Jost")
            vCardStr += "N:%s;%s;;;".format("Jost", "Jarne")
            // TODO: add other fields
            vCardStr += "END:VCARD"

            return vCardStr
        }
    }
}

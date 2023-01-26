package com.example.mycontactskotlinapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.ui.graphics.Color
import java.util.*

data class ContactObject(
    var id: String,
    val firstName: String,
    val lastName: String,
    val photoUri: Bitmap?,
    val phoneList: MutableList<PhoneNumber>,
    val emailsList: MutableList<Email>,
    val backgroundColor: Color
)

data class PhoneNumber(var number: String, var type: Int) {
    fun getTypeName(): String {
        return when (type) {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "Home"
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "Mobile"
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "Work"
            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> "Fax (Work)"
            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> "Fax (Home)"
            ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> "Pager"
            ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "Other"
            ContactsContract.CommonDataKinds.Phone.TYPE_CAR -> "Car"
            ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM -> "Custom"
            else -> "Unknown"
        }
    }
}

data class Email(val address: String, val type: Int) {
    fun getTypeName(): String {
        return when (type) {
            ContactsContract.CommonDataKinds.Email.TYPE_HOME -> "Home"
            ContactsContract.CommonDataKinds.Email.TYPE_WORK -> "Work"
            ContactsContract.CommonDataKinds.Email.TYPE_OTHER -> "Other"
            ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> "Mobile"
            ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> "Custom"
            else -> "Unknown"
        }
    }


}
fun getPhoneTypeNumber(number: String): Int {
    return when (number) {
        "Home" -> ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        "Mobile" -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        "Work" -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        "Fax (Work)" -> ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK
        "Fax (Home)" -> ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME
        "Pager" -> ContactsContract.CommonDataKinds.Phone.TYPE_PAGER
        "Other" -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
        "Car" -> ContactsContract.CommonDataKinds.Phone.TYPE_CAR
        "Custom" -> ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM
        else -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
    }
}
fun getEmailTypeNumber(address: String): Int {
    return when (address) {
        "Home" -> ContactsContract.CommonDataKinds.Email.TYPE_HOME
        "Work" -> ContactsContract.CommonDataKinds.Email.TYPE_WORK
        "Other" -> ContactsContract.CommonDataKinds.Email.TYPE_OTHER
        "Mobile" -> ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
        "Custom" -> ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM
        else -> ContactsContract.CommonDataKinds.Email.TYPE_OTHER
    }
}

class ContactRepository(private val context: Context) {

    @SuppressLint("Range")
    fun fetchContacts(): ArrayList<ContactObject> {
        val contacts: ArrayList<ContactObject> = ArrayList<ContactObject>()
        val cursor: Cursor? = context.contentResolver
            .query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null
            )

        if (cursor != null && cursor.count > 0) {
            Log.i(TAG, "fetchContacts: cursor.getCount() is " + cursor.count)
            while (cursor.moveToNext()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                // Photo
                val contactImage: Bitmap?
                val hasImage =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)) > 0
                if (hasImage) {
                    //retrieve contact's image
                    val uri: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
                    val inputStream =
                        context.contentResolver.openInputStream(android.net.Uri.parse(uri))
                    contactImage = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                } else {
                    contactImage = null
                }


                // Phone numbers list check
                val phoneCursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                val phoneNumbers = mutableListOf<PhoneNumber>()
                while (phoneCursor?.moveToNext() == true) {
                    val phoneNumber =
                        phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val phoneType =
                        phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                    phoneNumbers.add(PhoneNumber(phoneNumber, phoneType))
                }
                phoneCursor?.close()

                // Email list check
                val emailCursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                val emails = mutableListOf<Email>()
                while (emailCursor?.moveToNext() == true) {
                    val email =
                        emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    val emailType =
                        emailCursor.getInt(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
                    emails.add(Email(email, emailType))
                }
                emailCursor?.close()
                val fullName = name.split("\\s".toRegex()).toTypedArray()
                val rnd = Random()
                val background = Color(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                val contact = ContactObject(
                    id,
                    fullName[0],
                    fullName[1],
                    contactImage,
                    phoneNumbers,
                    emails,
                    background
                )

                contacts.add(contact)
            }
        }
        cursor?.close()
        return contacts
    }


    companion object {
        private const val TAG = "deb-inf ContRepo"
    }

}
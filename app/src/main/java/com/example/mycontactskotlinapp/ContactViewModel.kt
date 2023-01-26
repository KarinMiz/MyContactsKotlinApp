package com.example.mycontactskotlinapp

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
class ContactViewModel : ViewModel() {

    private var repository: ContactRepository? = null
    private var _contacts: MutableLiveData<ArrayList<ContactObject>>? = null

    fun contactViewModel(context: Context?) {
        repository = context?.let { ContactRepository(it) }
        _contacts = MutableLiveData<ArrayList<ContactObject>>()
    }

    fun checkPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            // Permission has already been granted
            // Access contacts here
            _contacts?.value = repository?.fetchContacts()
        }
    }

    fun getContacts(): MutableLiveData<ArrayList<ContactObject>>? {
        return _contacts
    }
}
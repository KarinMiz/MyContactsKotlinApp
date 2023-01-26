package com.example.mycontactskotlinapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mycontactskotlinapp.ui.theme.LighterGrey

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactInfoScreen(contactId: String?, navController: NavHostController?) {
    val contactInfo = contactsInfoList.first { contact -> contactId == contact.id }
    val fullName = "${contactInfo.firstName} ${contactInfo.lastName}"
    Scaffold(topBar =
    {
        AppBar(
            title = "$fullName Info",
            icon = Icons.Default.ArrowBack,
        ) {
            // Navigate Back
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(LighterGrey),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,

                ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                ) {
                    IconButton(onClick =
                    { navController?.navigate("contact_edit_info/${contactId}") }) {
                        Image(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "EDIT",
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 0.dp),
                        )
                    }
                }
                val name = contactInfo.firstName[0] + "" + contactInfo.lastName[0]
                ContactPicture(contactInfo.photoUri, 150.dp, name, contactInfo.backgroundColor)
                ContactInfo(fullName)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp, start = 30.dp)
                        .background(Color.White),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Phone Number:",
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    if (contactInfo.phoneList.size == 0) {
                        Text(
                            text = "This contact does not have a phone number",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                    for (phone in contactInfo.phoneList)
                        Text(
                            text = "${phone.getTypeName()} : ${phone.number}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    Text(
                        text = "Email:",
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    if (contactInfo.emailsList.size == 0) {
                        Text(
                            text = "This contact does not have an email address",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                    for (email in contactInfo.emailsList)
                        Text(
                            text = "${email.getTypeName()} : ${email.address}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 10.dp)
                        )

                }
            }


        }
    }
}

package com.example.mycontactskotlinapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycontactskotlinapp.ui.theme.LighterGrey

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactEditInfoScreen(contactId: String?, navController: NavHostController?) {
    val contactInfo = contactsInfoList.first { contact -> contactId == contact.id }
    val fullName = "${contactInfo.firstName} ${contactInfo.lastName}"

    var phoneTypeState = remember { mutableStateOf(" ") }
    var phoneNumberState = remember { mutableStateOf(" ") }
    var emailTypeState = remember { mutableStateOf(" ") }
    var emailNumberState = remember { mutableStateOf(" ") }


    Scaffold(topBar =
    {
        AppBar(
            title = "$fullName Info",
            icon = Icons.Default.ArrowBack
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
                    .padding(top = 20.dp, start = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                ) {
                    IconButton(onClick =
                    {

                    }) {
                        Image(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Save_Changes",
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
                        .padding(top = 20.dp, end = 20.dp)
                        .background(Color.White),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Phone Number:",
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline
                    )
                    for (phone in contactInfo.phoneList) {
                        phoneTypeState = remember { mutableStateOf("${phone.getTypeName()}") }
                        phoneNumberState = remember { mutableStateOf("${phone.number}") }


                        TextField(
                            value = phoneTypeState.value,
                            onValueChange = { phoneTypeState.value = it },
                            modifier = Modifier
                                .background(Color.White)
                                .width(100.dp),
                            textStyle = TextStyle(color = Color.Black, fontSize = 22.sp),
                        )

                        TextField(
                            value = phoneNumberState.value,
                            onValueChange = { phoneNumberState.value = it },
                            modifier = Modifier
                                .background(Color.White),
                            textStyle = TextStyle(color = Color.Black, fontSize = 22.sp),
                        )

                    }

                    Text(
                        text = "Email:",
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                    for (email in contactInfo.emailsList) {

                        emailTypeState =
                            remember { mutableStateOf("${email.getTypeName()}") }
                        emailNumberState = remember { mutableStateOf("${email.address}") }

                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            TextField(
                                value = emailTypeState.value,
                                onValueChange = { emailTypeState.value = it },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(100.dp),
                                textStyle = TextStyle(color = Color.Black, fontSize = 22.sp),
                            )

                            TextField(
                                value = emailNumberState.value,
                                onValueChange = { emailNumberState.value = it },
                                modifier = Modifier
                                    .background(Color.White),
                                textStyle = TextStyle(color = Color.Black, fontSize = 22.sp),
                            )

                        }

                    }


                }
            }


        }
    }
}

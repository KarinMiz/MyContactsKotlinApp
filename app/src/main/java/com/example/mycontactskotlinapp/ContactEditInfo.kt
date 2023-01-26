package com.example.mycontactskotlinapp

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
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
import androidx.compose.ui.text.style.TextAlign
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

    val editedFirstName = remember { mutableStateOf(contactInfo.firstName) }
    val editedLastName = remember { mutableStateOf(contactInfo.lastName) }

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
        val state = ScrollState(0)
        Surface(
            modifier = Modifier.fillMaxSize()
//                .background(LighterGrey),
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
                    )
                }
            }
            Box(
                modifier = Modifier.scrollable(
                    state = state,
                    orientation = Orientation.Vertical,

                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    val name = contactInfo.firstName[0] + "" + contactInfo.lastName[0]
                    ContactPicture(contactInfo.photoUri, 170.dp, name, contactInfo.backgroundColor)
                    Column(
                        Modifier
                            .background(Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "First Name: ",
                                modifier = Modifier
                                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    .wrapContentHeight()
                                    .background(Color.White),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                            TextField(
                                value = editedFirstName.value,
                                onValueChange = { editedFirstName.value = it },
                                modifier = Modifier
                                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    .wrapContentHeight()
                                    .background(Color.White),
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 5.dp)
                        ) {
                            Text(
                                text = "Last Name: ",
                                modifier = Modifier
                                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    .wrapContentHeight()
                                    .background(Color.White),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                            TextField(
                                value = editedLastName.value,
                                onValueChange = { editedLastName.value = it },
                                modifier = Modifier
                                    .background(Color.White)
                                    .wrapContentWidth(align = Alignment.End)
                                    .wrapContentHeight(),
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }

                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = "Phone Number:",
                            style = MaterialTheme.typography.h5,
                            textDecoration = TextDecoration.Underline
                        )
                        for (phone in contactInfo.phoneList) {
                            val phoneNumberState = remember { mutableStateOf(phone.number) }
                            val phoneTypeState =
                                remember { mutableStateOf(phone.getTypeName()) }
                            Row(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .defaultMinSize(minHeight = 10.dp, minWidth = 30.dp)
                            ) {
                                Column(
                                    modifier = Modifier.defaultMinSize(
                                        minHeight = 20.dp,
                                        minWidth = 30.dp
                                    )
                                ) {
                                    TextField(
                                        value = if (contactInfo.phoneList.size != 0)
                                            phoneTypeState.value else "Phone Type",
                                        onValueChange = {
                                            phoneTypeState.value = it
                                        },
                                        modifier = Modifier
                                            .background(Color.LightGray)
                                            .width(90.dp)
                                            .wrapContentHeight()
                                            .defaultMinSize(minHeight = 10.dp),
                                        textStyle = TextStyle(
                                            color = Color.Black,
                                            fontSize = 19.sp
                                        ),
                                    )
                                }

                                Column {
                                    TextField(
                                        value = phoneNumberState.value,
                                        onValueChange = { phoneNumberState.value = it },
                                        modifier = Modifier
                                            .background(Color.White)
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        textStyle = TextStyle(
                                            color = Color.Black,
                                            fontSize = 19.sp
                                        ),
                                    )
                                }

                            }


                        }

                        Text(
                            text = "Emaili:",
                            style = MaterialTheme.typography.h5,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .padding(top = 15.dp)
                        )
                        for (email in contactInfo.emailsList) {
                            var emailTypeState =
                                remember { mutableStateOf("") }
                            var emailNumberState = remember { mutableStateOf("") }
                            if (contactInfo.phoneList.size > 0) {
                                emailTypeState =
                                    remember { mutableStateOf(email.getTypeName()) }
                                emailNumberState = remember { mutableStateOf(email.address) }
                            } else {
                                emailTypeState.value = remember { " Email Type " }
                                emailNumberState.value = remember { "Email Address" }
                            }
                            Row(
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                Column {
                                    TextField(
                                        value = emailTypeState.value,
                                        onValueChange = { emailTypeState.value = it },
                                        modifier = Modifier
                                            .background(Color.LightGray)
                                            .width(86.dp)
                                            .wrapContentHeight()
                                            .background(LighterGrey),
                                        textStyle = TextStyle(
                                            color = Color.Black,
                                            fontSize = 19.sp
                                        ),
                                    )
                                }
                                Column {
                                    TextField(
                                        value = emailNumberState.value,
                                        onValueChange = { emailNumberState.value = it },
                                        modifier = Modifier
                                            .background(Color.White)
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        textStyle = TextStyle(
                                            color = Color.Black,
                                            fontSize = 19.sp
                                        ),
                                    )
                                }


                            }

                        }


                    }
                }
            } // Box

        } // Surface
    } // Scaffold
} // Main

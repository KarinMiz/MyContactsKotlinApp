package com.example.mycontactskotlinapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycontactskotlinapp.ui.theme.MyApplicationTheme
import com.example.mycontactskotlinapp.ui.theme.lightGreen
import coil.compose.rememberImagePainter
import com.example.mycontactskotlinapp.ui.theme.LighterGrey
import java.util.*

var contactsInfoList = ArrayList<ContactObject>()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val viewModel = ContactViewModel()

        setContent {
            viewModel.contactViewModel(this)
            viewModel.checkPermission(this)

            if (viewModel._contacts != null) {
                for (contact in viewModel._contacts!!) {
                    contactsInfoList.add(contact)
                }
            }

            MyApplicationTheme {
                ContactsApplication(contactsInfoList)
            }
        }
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in this case if you want to access contacts
                    // Access contacts here

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system settings in an effort
                    // to convince the user to change their decision.
                    Log.d(ContentValues.TAG, "NO PERMISSION")
                }
                return
            }
        }
    }


}

@Composable
fun ContactsApplication(contactsInfo: List<ContactObject>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "contacts_list") {
        composable("contacts_list") {
            MainScreen(contactsInfo, navController)
        }
        composable(
            route = "contact_info/{contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            ContactInfoScreen(
                navBackStackEntry.arguments!!.getString("contactId")!!,
                navController
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    contactsInfo: List<ContactObject>,
    navController: NavHostController?
) {
    var filter = { "" }
    Scaffold(topBar = {
        AppBar(
            title = "Contacts List",
            icon = Icons.Default.Search
        ) {
            // Filter
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn {
                items(contactsInfo) { contactInfo ->
                    ContactCard(contactInfo = contactInfo) {
                        navController?.navigate("contact_info/${contactInfo.id}")
                    }
                }
            }

        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactInfoScreen(contactId: String?, navController: NavHostController?) {
    val contactInfo = contactsInfoList.first { contact -> contactId == contact.id }
    val fullName = "${contactInfo.firstName} ${contactInfo.lastName}"
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
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val name = contactInfo.firstName[0] + "" + contactInfo.lastName[0]
                ContactPicture(contactInfo.photoUri, 150.dp, name, contactInfo.backgroundColor)
                ContactInfo(fullName)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp, start = 30.dp)
                        .background(Color.White),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Phone Number:",
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline
                    )
                    for (phone in contactInfo.phoneList)
                        Text(text="${phone.getTypeName()} : ${phone.number}",style = MaterialTheme.typography.h6)
                    Text(
                        text = "Email:",
                        style = MaterialTheme.typography.h5,
                        textDecoration = TextDecoration.Underline
                    )

                    for (email in contactInfo.emailsList)
                        Text(text = "${email.getTypeName()} : ${email.address}",style = MaterialTheme.typography.h6)

                }
            }


        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "description",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable(onClick = { iconClickAction.invoke() })
            )
        },
        title = { Text(title) },
    )
}

@Composable
fun ContactCard(contactInfo: ContactObject, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() }),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            val name = contactInfo.firstName[0] + "" + contactInfo.lastName[0]
            ContactPicture(contactInfo.photoUri, 72.dp, name, contactInfo.backgroundColor)
            ContactInfo(contactInfo.firstName + " " + contactInfo.lastName)

        }
    }
}

@Composable
fun ContactPicture(imageBitmap: Bitmap?, imageSize: Dp, name: String, color: Color) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.lightGreen
        ),
        modifier = Modifier
            .padding(16.dp)
            .size(imageSize),
        elevation = 4.dp
    ) {

        if (imageBitmap != null) {

            Image(
                rememberImagePainter(data = imageBitmap),
                contentDescription = "drawableId",
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White)
            )
        } else {
//            val rnd = Random()
//            val color = Color(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            Text(
                text = name,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(20.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

    }
}

@Composable
fun ContactInfo(contactName: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = contactName,
                style = MaterialTheme.typography.h5
            )
            // Phones,Emails
        }

    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen(contactsInfo = contactsInfoList, null)
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    MyApplicationTheme {
        ContactInfoScreen(contactId = contactsInfoList[0].id, null)
    }
}

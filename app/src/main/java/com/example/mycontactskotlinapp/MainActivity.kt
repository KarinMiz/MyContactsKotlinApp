package com.example.mycontactskotlinapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.mycontactskotlinapp.ui.theme.MyApplicationTheme
import java.util.*

var contactsInfoList = ArrayList<ContactObject>()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val viewModel = ContactViewModel()

        setContent {
            viewModel.contactViewModel(this)
            viewModel.checkPermission(this)
            val myLiveData = viewModel.getContacts()
            if (myLiveData != null) {
                contactsInfoList = myLiveData.observeAsState().value!!
            }
            MyApplicationTheme {
                ContactsApplication(contactsInfoList)
            }
        }
    }

}

@Composable
fun ContactsApplication(contactsInfo: List<ContactObject>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "contacts_list") {
        composable("contacts_list") {
            val textState = remember { mutableStateOf(TextFieldValue("")) }
            MainScreen(contactsInfo, navController, textState)
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
        composable(
            route = "contact_edit_info/{contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            ContactEditInfoScreen(
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
    navController: NavHostController?,
    state: MutableState<TextFieldValue>
) {
    var filteredContacts: ArrayList<ContactObject>
    Scaffold(topBar = {
        SearchView(state = state)
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
        ) {
            LazyColumn {
                val searchText = state.value.text
                if (searchText.isEmpty()) {
                    filteredContacts = contactsInfo as ArrayList<ContactObject>
                } else {
                    val resultList = ArrayList<ContactObject>()
                    for (contact in contactsInfo) {
                        if (contact.firstName.lowercase
                                (Locale.getDefault())
                                .contains(searchText.lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(contact)
                        }
                    }
                    filteredContacts = resultList
                }
                items(filteredContacts) { filteredContact ->
                    ContactCard(contactInfo = filteredContact) {
                        navController?.navigate("contact_info/${filteredContact.id}")
                    }
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
            ContactInfo(contactInfo.firstName + " " + contactInfo.lastName, MaterialTheme.typography.h5)

        }
    }
}

@Composable
fun ContactPicture(imageBitmap: Bitmap?, imageSize: Dp, name: String, color: Color) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 3.dp,
            color = Color.Magenta
        ),
        modifier = Modifier
            .padding(14.dp)
            .size(imageSize),
        elevation = 4.dp
    ) {

        if (imageBitmap != null) {

            Image(
                rememberAsyncImagePainter(model = imageBitmap),
                contentDescription = "drawableId",
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White)
            )
        } else {
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
fun ContactInfo(contactName: String,style: TextStyle) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = contactName,
                style = style
            )
        }

    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        MainScreen(contactsInfo = contactsInfoList, null, textState)
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    MyApplicationTheme {
        ContactInfoScreen(contactId = contactsInfoList[0].id, null)
    }
}

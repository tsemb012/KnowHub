package com.example.droidsoftthird.composable.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.droidsoftthird.ProfileViewModel
import com.example.droidsoftthird.R

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    toProfileEdit: () -> Unit
) {
    val userDetail = viewModel.userDetail
    val residentialArea = userDetail.value.area.prefecture?.name + ", " + userDetail.value.area.city?.name
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Edit Profile") },
                icon = { Icon(Icons.Filled.AddCircle, contentDescription = null) },
                onClick = { toProfileEdit() }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            Log.d("ProfileScreen", "imagePainter.state = ${innerPadding}")
            Column(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(viewModel.downloadUrl1.value)
                        .transformations(CircleCropTransformation())
                        .build(),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,

                    /*placeholder = { CircularProgressIndicator() },
                    error = { Text("Image not found") }*/
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userDetail.value.comment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.h6
                )
                ProfileInfoItem(
                    title = stringResource(id = R.string.gender),
                    value = userDetail.value.gender ?:"",
                    icon = Icons.Filled.Group
                )
                ProfileInfoItem(
                    title = stringResource(id = R.string.age),
                    value = userDetail.value.age.toString(),
                    icon = Icons.Filled.LocationOn
                )
                ProfileInfoItem(
                    title = stringResource(id = R.string.residential_area),
                    value = residentialArea,
                    icon = Icons.Filled.LocationCity
                )
            }
        }
    )
}

@Composable
fun ProfileInfoItem(title: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = value)
        }
    }
}

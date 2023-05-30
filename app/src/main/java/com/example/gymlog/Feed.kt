package com.example.gymlog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
@Composable
fun FeedScreen(navController: NavController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.grey))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(colorResource(id = R.color.gold))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.outline_home_24),
                    contentDescription = "Home",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 30.dp)
                )
                Image(
                    painter = painterResource(R.drawable.outline_person_24),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 30.dp)
                )
                Image(
                    painter = painterResource(R.drawable.outline_groups_24),
                    contentDescription = "Feed",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 30.dp)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_bar_chart_24),
                    contentDescription = "Chart",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 30.dp)
                )
                Image(
                    painter = painterResource(R.drawable.outline_notifications_active_24),
                    contentDescription = "Bell",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
}

@Preview
@Composable
fun FeedScreenPreview() {
    FeedScreen(navController = rememberNavController())
}
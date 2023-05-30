package com.example.gymlog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AppScreen(
    navController: NavController
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.gold)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.navigate(Screen.Home.route) },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_home_24), // Replace with your drawable resource
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
//                    .clickable {
//                        navController.navigate(Screen.Home.route)
//                    }
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.Profile.route) },
            modifier = Modifier.padding(start = 30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_person_24), // Replace with your drawable resource
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
//                    .clickable {
//                        navController.navigate(Screen.Profile.route)
//                    }
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.Feed.route) },
            modifier = Modifier.padding(start = 30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_groups_24), // Replace with your drawable resource
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
//                    .clickable {
//                        navController.navigate(Screen.Feed.route)
//                    }
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.Chart.route) },
            modifier = Modifier.padding(start = 30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_bar_chart_24), // Replace with your drawable resource
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
//                    .clickable {
//                        navController.navigate(Screen.Chart.route)
//                    }
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.Bell.route) },
            modifier = Modifier.padding(start = 60.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_notifications_active_24), // Replace with your drawable resource
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
//                    .clickable {
//                        navController.navigate(Screen.Bell.route)
//                    }
            )
        }
    }

}

@Preview
@Composable
fun AppScreenPreview() {
    AppScreen(navController = rememberNavController())
}
package com.example.myaura.ui.profile.component

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.myaura.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myaura.domain.model.UserProfile
import androidx.core.net.toUri

@Composable
fun ProfileHeader(
    navController: NavController,
    userProfile: UserProfile
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = userProfile.profilePictureUrl.ifEmpty { R.drawable.ic_launcher_background },
                contentDescription = stringResource(id = R.string.profile_picture_cd),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                error = painterResource(id = R.drawable.ic_launcher_background)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("edit_profile")
                },
                modifier = Modifier
                    .height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.edit_profile_button), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                userProfile.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                userProfile.job,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                userProfile.tagline,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            Text(
                text = userProfile.bio,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val instagramUrl = userProfile.socialMediaLinks["instagram"]
                if (instagramUrl?.isNotBlank() == true) {
                    Image(
                        painter = painterResource(R.drawable.instagram__1_),
                        contentDescription = stringResource(id = R.string.instagram_cd),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                openUrl(context, instagramUrl)
                            }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                val githubUrl = userProfile.socialMediaLinks["github"]
                if (githubUrl?.isNotBlank() == true) {
                    Image(
                        painter = painterResource(R.drawable.github),
                        contentDescription = stringResource(id = R.string.github_cd),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                openUrl(context, githubUrl)
                            },
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                val linkedinUrl = userProfile.socialMediaLinks["linkedin"]
                if (linkedinUrl?.isNotBlank() == true) {
                    Image(
                        painter = painterResource(R.drawable.linkedin),
                        contentDescription = stringResource(id = R.string.linkedin_cd),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                openUrl(context, linkedinUrl)
                            }
                    )
                }
            }
        }
    }
}

private fun openUrl(context: android.content.Context, url: String) {
    var finalUrl = url

    if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
        finalUrl = "https://$finalUrl"
    }

    try {
        val intent = Intent(Intent.ACTION_VIEW, finalUrl.toUri())
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, R.string.cant_open_link, Toast.LENGTH_LONG).show()
    }
}
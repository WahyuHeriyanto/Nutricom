package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.wahyuheriyanto.nutricom.data.model.UserProfile
import org.wahyuheriyanto.nutricom.viewmodel.fetchUserProfile
import org.wahyuheriyanto.nutricom.viewmodel.updateUserProfile

@Composable
fun DataDiriScreen(navController: NavController) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // Load data on start
    LaunchedEffect(Unit) {
        fetchUserProfile { profile -> userProfile = profile }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah data yang dimasukan sudah benar?") },
            confirmButton = {
                TextButton(onClick = {
                    userProfile?.let {
                        updateUserProfile(it) {
                            isEditing = false
                            showDialog = false
                        }
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Urungkan")
                }
            }
        )
    }

    userProfile?.let { profile ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = profile.imageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            @Composable
            fun inputField(label: String, value: String, onChange: (String) -> Unit) {
                Text(label, fontWeight = FontWeight.Bold)
                if (isEditing) {
                    OutlinedTextField(value, onChange, modifier = Modifier.fillMaxWidth())
                } else {
                    Text(value, modifier = Modifier.padding(bottom = 8.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            inputField("Nama Lengkap", profile.fullName) {
                userProfile = profile.copy(fullName = it)
            }
            inputField("Email", profile.email) {
                userProfile = profile.copy(email = it)
            }
            inputField("Jenis Kelamin", profile.gender) {
                userProfile = profile.copy(gender = it)
            }
            inputField("Username", profile.userName) {
                userProfile = profile.copy(userName = it)
            }
            inputField("Tanggal Lahir", profile.dateOfBirth) {
                userProfile = profile.copy(dateOfBirth = it)
            }
            inputField("No Telepon", profile.phoneNumber) {
                userProfile = profile.copy(phoneNumber = it)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                Button(
                    onClick = {
                        if (isEditing) showDialog = true else isEditing = true
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
                ) {
                    Text(if (isEditing) "Simpan" else "Edit", color = Color.White)
                }
            }

        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

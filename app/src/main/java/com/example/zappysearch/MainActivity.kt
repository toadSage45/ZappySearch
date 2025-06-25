package com.example.zappysearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.zappysearch.presentation.navigation.AppNavigation
import com.example.zappysearch.ui.theme.ZappySearchTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var auth : FirebaseAuth
    @Inject lateinit var credentialManager : CredentialManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZappySearchTheme {
                Surface(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    AppNavigation()
                }
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser : FirebaseUser? = auth.currentUser
//
//    }
}




package com.example.zappysearch.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.zappysearch.R
@Composable
fun StartChatIconButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(48.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.chat_add_on_24px),
            contentDescription = "Start Chat",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Start Chat")
    }
}

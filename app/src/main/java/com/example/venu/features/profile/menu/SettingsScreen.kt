package com.example.venu.features.profile.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.venu.core.core_common.core_ui.theme.VenuColors

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    val context = LocalContext.current

    var notificationsEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    var showSignOutDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationsEnabled = isGranted
    }

    fun onPushNotificationsChanged(checked: Boolean) {
        if (!checked) {
            notificationsEnabled = false
            return
        }

        val permissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (permissionGranted) {
            notificationsEnabled = true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(26.dp),
                    tint = VenuColors.TextPrimary
                )
            }

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = VenuColors.TextPrimary
            )
        }

        Text(
            text = "Manage your account and app preferences",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = VenuColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(26.dp))

        SectionTitle("Notifications")

        Spacer(modifier = Modifier.height(12.dp))

        SettingsCard {
            SettingsSwitchRow(
                title = "Push Notifications",
                leadingIcon = Icons.Filled.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = ::onPushNotificationsChanged
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        SectionTitle("Account")

        Spacer(modifier = Modifier.height(12.dp))

        SettingsCard {
            SettingsActionRow(
                title = "Sign Out",
                leadingIcon = Icons.Filled.Logout,
                isDestructive = true,
                onClick = { showSignOutDialog = true }
            )
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = {
                Text("Sign out?")
            },
            text = {
                Text("You’ll need to sign in again to access your profile.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        onSignOutClick()
                    }
                ) {
                    Text(
                        text = "Sign Out",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        content()
    }
}

@Composable
private fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = VenuColors.TextPrimary
    )
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    leadingIcon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = VenuColors.TextSecondary
        )

        Spacer(modifier = Modifier.size(18.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = VenuColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = null
        )
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    leadingIcon: ImageVector,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (isDestructive) {
        MaterialTheme.colorScheme.error
    } else {
        VenuColors.TextPrimary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = contentColor
        )

        Spacer(modifier = Modifier.size(18.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}
package tr.com.gndg.self.ui.backup

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jakewharton.processphoenix.ProcessPhoenix
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tr.com.gndg.self.MainActivity
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.dataSource.roomDatabase.SelfDatabase
import tr.com.gndg.self.core.pdfService.FileHandler
import tr.com.gndg.self.core.preferences.sharedPreferencesString
import tr.com.gndg.self.core.util.Constants
import tr.com.gndg.self.core.util.Constants.BACKUP_FILE_NAME
import tr.com.gndg.self.core.util.dateTagForBackup
import tr.com.gndg.self.core.util.findAndroidActivity
import tr.com.gndg.self.core.util.toastMessage
import tr.com.gndg.self.ui.dialogs.SelfAlertDialogScreen
import tr.com.gndg.self.ui.navigation.NavigationDestination
import java.io.File

@Preview(showBackground = true)
@Composable
fun BackupBodyPreview() {
    BackupBody(
        Modifier.fillMaxSize(),
        backup = {},
        restore = {}
    )
}

object BackupDestination : NavigationDestination {
    override val route = "backup"
    override val titleRes = R.string.backupScreen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
) {
    val context = LocalContext.current
    val activity = context.findAndroidActivity()
    val mainActivity = (activity as MainActivity)
    val roomBackup = mainActivity.roomBackup

    var odtPref by context.sharedPreferencesString("openDocumentTree")

    var openDocumentTreeDialog by remember { mutableStateOf(false) }
    var openSelectRestoreDialog by remember { mutableStateOf(false) }
    var openRestoreRestartDialog by remember { mutableStateOf(false) }
    var openBackupRestartDialog by remember { mutableStateOf(false) }

    val restorePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            try {
                uri?.let {
                    restoreByUri(
                        uri = it,
                        context = context,
                        backup = roomBackup,
                        restoreSuccess = {
                        openRestoreRestartDialog = true
                    }
                    )
                }
            } catch (e: Exception) {
                toastMessage(context, e.message.toString())
            }

        }
    )

    val openDocumentTree = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = {uri->
            uri?.let {
                odtPref = it.toString()
                openSelectRestoreDialog = true
            }

        }
    )
    when {
        openDocumentTreeDialog -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    openDocumentTreeDialog = false
                    openDocumentTree.launch(null)
                },
                dialogTitle = stringResource(id = R.string.folderSelectTitle),
                dialogText = stringResource(id = R.string.folderSelectAlert),
                icon = Icons.Filled.Info,
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDocumentTreeDialog = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )
        }

        openSelectRestoreDialog -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    openSelectRestoreDialog = false
                    restorePicker.launch("*/*")
                },
                dialogTitle = stringResource(id = R.string.fileSelectTitle),
                dialogText = stringResource(id = R.string.fileSelectAlert),
                icon = Icons.Filled.Info,
                dismissButton = {
                    TextButton(
                        onClick = {
                            openSelectRestoreDialog = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )
        }

        openRestoreRestartDialog -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    openRestoreRestartDialog = false
                    ProcessPhoenix.triggerRebirth(context)
                },
                dialogTitle = stringResource(id = R.string.restoreOkTitle),
                dialogText = stringResource(id = R.string.restoreOkText),
                icon = Icons.Filled.Warning
            ) {

            }
        }

        openBackupRestartDialog -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    openBackupRestartDialog = false
                    ProcessPhoenix.triggerRebirth(context)
                },
                dialogTitle = stringResource(id = R.string.backupOkTitle),
                dialogText = stringResource(id = R.string.backupOkText),
                icon = Icons.Filled.Warning
            ) {

            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = stringResource(BackupDestination.titleRes),
                canNavigateBack = canNavigateBack,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp,
                navigateBack = navigateBack,
                action = {

                }
            )
        }

    )
    {innerPadding->

        BackupBody(modifier = Modifier.padding(innerPadding),
            backup = {
                backupRequest(
                    backup = roomBackup,
                    context = context,
                    backupSuccess = {
                        openBackupRestartDialog = true
                    }
                )
            },
            restore = {
                if (odtPref.isBlank()) {
                    openDocumentTreeDialog = true
                } else {
                    openSelectRestoreDialog = true
                }

            })

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BackupBody(
    modifier : Modifier,
    backup: () -> Unit,
    restore: () -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(permissions = Constants.BACKUP_PERMISSIONS)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_large)))

        Text(
            text = stringResource(id = R.string.backupInfoText),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.padding_medium))
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(
                onClick = {
                    if (permissionState.allPermissionsGranted) {
                        backup()
                    } else {
                        permissionState.launchMultiplePermissionRequest()
                    }
                     },
            ) {

                Icon(painter = painterResource(id = R.drawable.baseline_backup_24)  , contentDescription = "backup")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = stringResource(id = R.string.backupButtonText))

            }

            ElevatedButton(onClick = {
                if (permissionState.allPermissionsGranted) {
                    restore()
                } else {
                    permissionState.launchMultiplePermissionRequest()
                }
            }) {

                Icon(painter = painterResource(id = R.drawable.baseline_cloud_download_24)  , contentDescription = "restore")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = stringResource(id = R.string.restoreButtonText))

            }

        }

    }

}

fun restoreByUri(uri: Uri, context: Context, backup: RoomBackup, restoreSuccess: () -> Unit) {
    try {
        val path = FileHandler().getRealPathFromURI(context, uri)
        if (path != null) {
            val backupLocation = File(path)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                FileHandler().createFileFromContentUri("geriyukleme", uri, BACKUP_FILE_NAME, context)
            } else {
                File(backupLocation, "")
                    .copyTo(File("${context.filesDir}", BACKUP_FILE_NAME), true)
            }
            backup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_FILE)
                .backupLocationCustomFile(File("${context.filesDir}", BACKUP_FILE_NAME))
                .database(SelfDatabase.invoke(context))
                .enableLogDebug(true)
                .backupIsEncrypted(true)
                .customEncryptPassword("pK6!j8X9H5b6").apply {
                    onCompleteListener { success, message, exitCode ->
                        Log.d("TAG", "success: $success, message: $message, exitCode: $exitCode")
                        if (!success) Toast.makeText(context, "${context.getString(R.string.restoreFailed)} : $message, code: $exitCode", Toast.LENGTH_SHORT).show()
                        if (success)  {
                            restoreSuccess()
                        }
                    }

                }.restore()
        } else {
            Toast.makeText(context, context.getString(R.string.fileLocationError), Toast.LENGTH_SHORT).show()
        }

    } catch (e: Exception) {
        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        Log.d("TAG", "Hata: uri catch ${e.message}")
    }
}

private fun backupRequest(backup: RoomBackup, context: Context, backupSuccess: () -> Unit) {
    val fileLocation = "${context.filesDir}/$BACKUP_FILE_NAME"
    val documentFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    backup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_FILE)
        .backupLocationCustomFile(File(fileLocation))
        .database(SelfDatabase.invoke(context))
        .enableLogDebug(true)
        .backupIsEncrypted(true)
        .customEncryptPassword("pK6!j8X9H5b6")
        //maxFileCount: else 1000 because i cannot surround it with if condition
        .apply {
            onCompleteListener { success, message, exitCode ->
                try {
                    Log.d("TAG", "success: $success, message: $message, exitCode: $exitCode")

                    if (!success) Toast.makeText( context, "${context.getString(R.string.restoreFailed)} : $message, kod: $exitCode", Toast.LENGTH_SHORT).show()

                    CoroutineScope(Dispatchers.Main).launch {
                        if (success) {
                            val job = CoroutineScope(Dispatchers.Default).launch {
                                try {
                                    val dateTag = dateTagForBackup()
                                    val appName = context.getString(R.string.app_name)
                                    val backupTag = context.getString(R.string.backupTag)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        FileHandler().createFileFromContentUri("yedekleme", File(fileLocation).toUri(), "${appName}_${backupTag}_${dateTag}.sqlite3", context)
                                    } else {
                                        File("${context.filesDir}", BACKUP_FILE_NAME)
                                            .copyTo(File(documentFolder, "${appName}_${backupTag}_${dateTag}.sqlite3"), true)
                                    }


                                } catch (e: Exception) {
                                    Log.e("dosyaOlusturma", "Dosya kopyalama hatası")
                                    println(e.message)
                                }

                            }
                            job.join()
                            backupSuccess()
                        }
                    }

                }catch (e: Exception) {
                    println(e.message)
                }
            }
        }.backup()
}
package com.example.composeloginui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.composeloginui.ui.theme.FancyLoginScreenTheme
import com.example.composeloginui.ui.theme.Shapes
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FancyLoginScreenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUp(getVideoUri())
                }
            }
        }
    }

    private fun getVideoUri(): Uri {
        val rawId = resources.getIdentifier("clouds", "raw", packageName)
        val videoUri = "android.resource://$packageName/$rawId"
        return Uri.parse(videoUri)
    }
}

private fun Context.doSignUp() {
    Toast.makeText(
        this,
        "Coming Soon ... ",
        Toast.LENGTH_SHORT
    ).show()
}

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }


private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        useController = false
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

@Composable
fun SignUp(videoUri: Uri) {
    val context = LocalContext.current
    val passwordFocusRequester = FocusRequester()
    val emailFocusRequester = FocusRequester()
    val phoneFocusRequester = FocusRequester()
    val confirmPasswordFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val exoPlayer = remember { context.buildExoPlayer(videoUri) }

    DisposableEffect(
        AndroidView(
            factory = { it.buildPlayerView(exoPlayer) },
            modifier = Modifier.fillMaxSize()
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }

    ProvideWindowInsets {

        Column(
            Modifier
                .navigationBarsWithImePadding()
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.run {
                spacedBy(16.dp, alignment = Alignment.Top)
                    .apply { Center }
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                Modifier.padding(16.dp, 16.dp, 16.dp, 160.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Welcome Back", color = Color.White, fontSize = 36.sp)
            }

            SignUpTextInput(SignUpInputType.FullName, keyboardActions = KeyboardActions(onDone = {
                //emailFocusRequester.requestFocus()
                focusManager.clearFocus()
            }))

            SignUpTextInput(SignUpInputType.Email, keyboardActions = KeyboardActions(onDone = {
                //phoneFocusRequester.requestFocus()
                focusManager.clearFocus()
            })
            )


            SignUpTextInput(
                SignUpInputType.PhoneNumber,
                keyboardActions = KeyboardActions(onDone = {
                    //passwordFocusRequester.requestFocus()
                    focusManager.clearFocus()
                }),
                focusRequester = phoneFocusRequester
            )


            SignUpPasswordTextInput(
                SignUpInputType.Password,
                focusRequester = passwordFocusRequester,
                keyboardActions = KeyboardActions(onDone = {
                    //confirmPasswordFocusRequester.requestFocus()
                    focusManager.clearFocus()
                })
            )

            SignUpPasswordTextInput(
                SignUpInputType.ConfirmPassword,
                focusRequester = confirmPasswordFocusRequester,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )

            Button(onClick = {
                context.doSignUp()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("SIGN UP", Modifier.padding(vertical = 8.dp))
            }


            Divider(
                color = Color.White.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 48.dp)
            )


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Have an account?", color = Color.White)
                TextButton(onClick = {
                    context.startActivity(Intent(context, MainActivity::class.java))

                }) {
                    Text("SING IN")
                }
            }
        }
    }
}

sealed class SignUpInputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object FullName : SignUpInputType(
        label = "Full Name",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )

    object PhoneNumber : SignUpInputType(
        label = "Phone Number",
        icon = Icons.Default.Phone,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Phone
        ),
        visualTransformation = VisualTransformation.None
    )

    object Email : SignUpInputType(
        label = "Email",
        icon = Icons.Default.Email,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        visualTransformation = VisualTransformation.None
    )


    object Password : SignUpInputType(
        label = "Password",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        visualTransformation = PasswordVisualTransformation()
    )

    object ConfirmPassword : SignUpInputType(
        label = "Confirm Password",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        visualTransformation = PasswordVisualTransformation()
    )

}

@Composable
fun SignUpTextInput(
    inputType: SignUpInputType,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions
) {

    var value by remember { mutableStateOf("") }

    TextField(
        value = value,
        onValueChange = { value = it },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, null) },
        label = { Text(text = inputType.label) },
        shape = Shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun SignUpPasswordTextInput(
    inputType: SignUpInputType,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions
) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { password = it },
        leadingIcon = { Icon(imageVector = inputType.icon, null) },
        label = { Text(text = inputType.label) },
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = Shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusOrder(focusRequester ?: FocusRequester()),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        keyboardActions = keyboardActions,
    )
}

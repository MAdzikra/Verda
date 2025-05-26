package com.example.verdaapp.ui.view.forgot

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.verdaapp.R
import com.example.verdaapp.navigation.Screen
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.theme.poppinsFontFamily

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val viewModel: ResetPasswordViewModel = viewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.contains("terkirim", ignoreCase = true)) {
                navController.popBackStack() // atau arahkan ke login
            }
            viewModel.clearMessage()
        }
    }

    BackHandler {
        activity?.finish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_verda),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Lupa Password",
            fontFamily = poppinsFontFamily,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Masukkan email untuk reset password",
            fontFamily = poppinsFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Email",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            shape = RoundedCornerShape(25.dp),
            textStyle = TextStyle(color = Color.Black),
            placeholder = { Text("Enter your Email", fontFamily = poppinsFontFamily) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email Icon",
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = emailError != null,
            singleLine = true
        )
        emailError?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = null
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
                if (email.isEmpty()) {
                    emailError = "Email wajib diisi"
                } else if (!email.matches(emailPattern)) {
                    emailError = "Format email tidak valid"
                } else {
                    viewModel.requestPasswordReset(email)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1D8348), Color(0xFF117A65))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Kirim Link Reset",
                    fontFamily = poppinsFontFamily,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Kembali ke Login",
            fontFamily = poppinsFontFamily,
            color = Color(0xFF117A65),
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    token: String
) {
    Log.d("ResetPasswordScreen", "Received token: $token")

    val viewModel: ResetPasswordViewModel = viewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newpasswordVisible by remember { mutableStateOf(false) }
    var confirmpasswordVisible by remember { mutableStateOf(false) }

    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.contains("berhasil", ignoreCase = true)) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.ResetPassword.route) { inclusive = true }
                }
            }
            viewModel.clearMessage()
        }
    }

    BackHandler {
        activity?.finish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_verda),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Reset Password",
            fontFamily = poppinsFontFamily,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Masukkan password baru Anda",
            fontFamily = poppinsFontFamily,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Password Baru", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            shape = RoundedCornerShape(25.dp),
            visualTransformation = if (newpasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("Masukkan password baru", fontFamily = poppinsFontFamily) },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_lock), contentDescription = "Password Icon")
            },
            trailingIcon = {
                val image = if (newpasswordVisible)
                    painterResource(id = R.drawable.ic_visibility)
                else
                    painterResource(id = R.drawable.ic_visibility_off)

                IconButton(onClick = { newpasswordVisible = !newpasswordVisible }) {
                    Icon(painter = image, contentDescription = null)
                }
            },
            isError = newPasswordError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        newPasswordError?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            )
        }

        Text("Konfirmasi Password", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            shape = RoundedCornerShape(25.dp),
            visualTransformation = if (confirmpasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("Ulangi password", fontFamily = poppinsFontFamily) },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_lock), contentDescription = "Lock Icon")
            },
            isError = confirmPasswordError != null,
            trailingIcon = {
                val image = if (confirmpasswordVisible)
                    painterResource(id = R.drawable.ic_visibility)
                else
                    painterResource(id = R.drawable.ic_visibility_off)

                IconButton(onClick = { confirmpasswordVisible = !confirmpasswordVisible }) {
                    Icon(painter = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        confirmPasswordError?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                newPasswordError = null
                confirmPasswordError = null

                if (newPassword.length < 6) {
                    newPasswordError = "Minimal 6 karakter"
                } else if (newPassword != confirmPassword) {
                    confirmPasswordError = "Password tidak cocok"
                } else {
                    viewModel.resetPassword(token, newPassword)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1D8348), Color(0xFF117A65))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Password",
                    fontFamily = poppinsFontFamily,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    VerdaAppTheme {
        val navController: NavHostController = rememberNavController()
        ForgotPasswordScreen(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResetPasswordScreen() {
    VerdaAppTheme {
        val navController: NavHostController = rememberNavController()
        ResetPasswordScreen(navController, "")
    }
}

package com.example.verdaapp.ui.view.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.compose.rememberNavController
import com.example.verdaapp.R
import com.example.verdaapp.navigation.Screen
import com.example.verdaapp.supabase.SupabaseClient
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.theme.poppinsFontFamily
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = (context as? Activity)
    val loginViewModel: LoginViewModel = viewModel()
    val loginState by loginViewModel.loginState.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val googleLoginViewModel: GoogleLoginViewModel = viewModel()
    val syncSuccess by googleLoginViewModel.syncSuccess.collectAsState()
    val syncError by googleLoginViewModel.errorMessage.collectAsState()
    val loginError by loginViewModel.loginError.collectAsState()


    val action = SupabaseClient.instance.composeAuth.rememberSignInWithGoogle(
        onResult = { result ->
            when (result) {
                is NativeSignInResult.Success -> {
                    val user = SupabaseClient.instance.auth.currentUserOrNull()
                    val session = SupabaseClient.instance.auth.currentSessionOrNull()
                    if (user != null) {
                        val namaUser = user.userMetadata?.get("full_name")?.toString()?.trim('"') ?: ""
                        val emailUser = user.email ?: ""
                        val accessToken = session?.accessToken ?: ""
                        val userId = user.id
                        googleLoginViewModel.syncGoogleUser(
                            context = context,
                            userId = userId,
                            email = emailUser,
                            nama = namaUser,
                            token = accessToken
                        )
                        googleLoginViewModel.saveUserData(context, namaUser, accessToken, userId)
                    }

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                    Log.d("Login", "Login Google berhasil, menuju Home")
                }

                is NativeSignInResult.Error -> {
                    Toast.makeText(
                        context,
                        "Login gagal: ${result.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Login", "Login Google gagal: ${result.exception?.message}")
                }

                is NativeSignInResult.NetworkError -> {
                    Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
                    Log.e("Login", "Login gagal, masalah jaringan")
                }

                else -> {
                    Log.e("Login", "Login Google: Hasil tidak diketahui")
                }
            }
        }
    )

    LaunchedEffect(loginError) {
        loginError?.let {
            emailError = it
            passwordError = it
            loginViewModel.resetLoginError()
        }
    }


    LaunchedEffect(loginState) {
        loginState?.let { message ->
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (message.contains("sukses", true) || message.contains("berhasil", true)) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            loginViewModel.resetLoginState()
        }
    }

    LaunchedEffect(syncSuccess) {
        if (syncSuccess) {
            Toast.makeText(context, "Profil berhasil disinkronkan", Toast.LENGTH_SHORT).show()
            googleLoginViewModel.resetState()
        }
    }

    LaunchedEffect(syncError) {
        syncError?.let {
            Toast.makeText(context, "Gagal sinkronisasi: $it", Toast.LENGTH_SHORT).show()
            googleLoginViewModel.resetState()
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ellipse),
                contentDescription = "Background Gradient",
                modifier = Modifier.size(350.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_verda),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Login", fontFamily = poppinsFontFamily, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "Welcome to Verda!!", fontFamily = poppinsFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Normal)
            }
        }

        Text(text = "Email", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
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
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
        )
        emailError?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp).align(Alignment.Start))
        }


        Text(text = "Password", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(25.dp),
            textStyle = TextStyle(color = Color.Black),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("Enter your Password", fontFamily = poppinsFontFamily) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = "Lock Icon",
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.ic_visibility)
                else
                    painterResource(id = R.drawable.ic_visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = "Toggle Password Visibility")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = passwordError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                autoCorrect = false
            )
        )
        passwordError?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp).align(Alignment.Start))
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text(text = "Remember me", fontFamily = poppinsFontFamily)
            }
            Text(
                text = "Forgot Password?",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                emailError = null
                passwordError = null

                var hasError = false

                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

                if (email.isEmpty()) {
                    emailError = "Email is required"
                    hasError = true
                } else if (!email.matches(emailPattern)) {
                    emailError = "Invalid email format"
                    hasError = true
                }
                if (password.isEmpty()) {
                    passwordError = "Password is required"
                    hasError = true
                }

                if (!hasError) {
                    loginViewModel.login(context, email.trim(), password)
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
                Text(text = "Login", fontFamily = poppinsFontFamily, color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f).height(1.dp).background(Color.Gray)
            )
            Text(text = "  Or login with  ", fontFamily = poppinsFontFamily, fontSize = 14.sp, color = Color.Gray)
            Box(
                modifier = Modifier.weight(1f).height(1.dp).background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                modifier = Modifier.size(25.dp).clickable {
                /* TODO: Login Google */
                    action.startFlow()
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text(text = "Don't have an account?", fontFamily = poppinsFontFamily, color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign Up",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF117A65),
                modifier = Modifier.clickable { navController.navigate(Screen.Register.route) }
            )
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

@Preview
@Composable
fun LoginPreview(){
    VerdaAppTheme {
        val navController = rememberNavController()
        LoginScreen(navController)
    }
}
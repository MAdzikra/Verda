package com.example.verdaapp.ui.view.login

import android.app.Activity
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
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.theme.poppinsFontFamily

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

    LaunchedEffect(loginState) {
        loginState?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (message.contains("sukses", true) || message.contains("berhasil", true)) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            loginViewModel.resetLoginState()
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
            isError = emailError != null
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
            isError = passwordError != null
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
                modifier = Modifier.clickable { /* TODO: Tambahkan aksi */ }
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
                    loginViewModel.login(email.trim(), password)
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
                modifier = Modifier.size(25.dp).clickable { /* TODO: Login Google */ }
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
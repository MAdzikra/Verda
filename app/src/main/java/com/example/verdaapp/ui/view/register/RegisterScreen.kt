package com.example.verdaapp.ui.view.register

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val viewModel: RegisterViewModel = viewModel()
    val registerState by viewModel.registerState.collectAsState()
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(registerState) {
        registerState?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.contains("Registrasi Berhasil", ignoreCase = true)) {
                navController.navigate(Screen.Login.route)
            } else {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            kotlinx.coroutines.delay(300)
            viewModel.resetRegisterState()
        }
    }

    BackHandler {
        navController.popBackStack()
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
                modifier = Modifier.size(300.dp),
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
                Text(text = "Sign Up", fontFamily = poppinsFontFamily, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "Welcome to Verda!!", fontFamily = poppinsFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Normal)
            }
        }

        Text(text = "Full Name", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            shape = RoundedCornerShape(25.dp),
            textStyle = TextStyle(color = Color.Black),
            placeholder = { Text("Enter your Name", fontFamily = poppinsFontFamily) },
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
            isError = nameError != null
        )
        nameError?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp).align(Alignment.Start))
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

        Text(text = "Confirm Password", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            shape = RoundedCornerShape(25.dp),
            textStyle = TextStyle(color = Color.Black),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("Enter your Password", fontFamily = poppinsFontFamily) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_locked),
                    contentDescription = "Lock Icon",
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    painterResource(id = R.drawable.ic_visibility)
                else
                    painterResource(id = R.drawable.ic_visibility_off)

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(painter = image, contentDescription = "Toggle Password Visibility")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = confirmPasswordError != null
        )
        confirmPasswordError?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp).align(Alignment.Start))
        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                emailError = null
                passwordError = null
                confirmPasswordError = null
                nameError = null

                var hasError = false
                if (name.isEmpty()) {
                    nameError = "Name is required"
                    hasError = true
                }
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
                else if (!password.matches(".*[A-Z].*".toRegex())) {
                    passwordError = "Password must contain at least one uppercase letter"
                    hasError = true
                } else if (!password.matches(".*\\d.*".toRegex())) {
                    passwordError = "Password must contain at least one number"
                    hasError = true
                }
                if (confirmPassword.isEmpty()) {
                    confirmPasswordError = "Confirm password is required"
                    hasError = true
                }
                if (password != confirmPassword) {
                    confirmPasswordError = "Passwords do not match"
                    hasError = true
                }

                if (!hasError) {
                    viewModel.register(name, email, password, "Guru")
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
                Text(text = "Sign Up", fontFamily = poppinsFontFamily, color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text(text = "Already have an account?", fontFamily = poppinsFontFamily, color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Login",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF117A65),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
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
private fun RegisterPreview() {
    VerdaAppTheme {
        val navController = rememberNavController()
        RegisterScreen(navController)
    }
}
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
import androidx.compose.runtime.rememberCoroutineScope
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
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val viewModel: RegisterViewModel = viewModel()
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        registerState?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.equals("Registrasi Berhasil", ignoreCase = true)) {
//                Toast.makeText(context, "Register success!", Toast.LENGTH_SHORT).show()
                println("Navigating to login...")
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
                .padding(vertical = 8.dp)
        )

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
                .padding(vertical = 8.dp)
        )

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
                .padding(vertical = 8.dp)
        )

        Text(text = "Confirm Password", fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
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
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    println("DEBUG: Passwords do not match")
                } else if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    println("DEBUG: Fields missing")
                } else {
                    println("DEBUG: Call viewModel.register()")
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
                modifier = Modifier.clickable { navController.navigate(Screen.Login.route) }
            )
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
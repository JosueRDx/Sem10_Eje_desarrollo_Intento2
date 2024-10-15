package com.josuerdx.sem10_e_desarrollo_intento2.view

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductoApp() {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.50.39:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ProductoApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar { Text("Productos") } },
        content = {
            NavHost(navController, startDestination = "productos") {
                composable("productos") { ProductoListScreen(navController, apiService) }
                composable("productoNuevo") { /* Pantalla de creación */ }
                composable("productoEditar/{id}") { /* Pantalla de edición */ }
                composable("productoEliminar/{id}") { /* Pantalla de eliminación */ }
            }
        }
    )
}
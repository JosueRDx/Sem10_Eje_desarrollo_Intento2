package com.josuerdx.sem10_e_desarrollo_intento2.view

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Productos") }
            )
        },
        content = {
            NavHost(navController, startDestination = "productos") {
                composable("productos") { ProductoListScreen(navController, apiService) }
                composable("productoNuevo") { ProductoCrearScreen(navController, apiService) }
                composable("productoEditar/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    ProductoEditarScreen(navController, apiService, id)
                }
                composable("productoEliminar/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    ProductoEliminarScreen(navController, apiService, id)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("productoNuevo") }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Producto")
            }
        }
    )
}
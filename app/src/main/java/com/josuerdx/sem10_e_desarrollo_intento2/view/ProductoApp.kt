package com.josuerdx.sem10_e_desarrollo_intento2.view

import ProductoListScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
                title = { Text("Productos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.LightGray, // Color claro para el TopAppBar
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            NavigationBar(navController)
        },
        content = {
            NavHost(navController, startDestination = "inicio") {
                composable("inicio") { InicioScreen() }
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

@Composable
fun NavigationBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.LightGray,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentDestination == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Productos") },
            label = { Text("Productos") },
            selected = currentDestination == "productos",
            onClick = {
                navController.navigate("productos") {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun InicioScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido a la aplicación de Productos")
    }
}

package com.josuerdx.sem10_e_desarrollo_intento2.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.Producto
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import kotlinx.coroutines.launch
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
                    containerColor = Color.LightGray,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            NavigationBar(navController)
        },
        content = { paddingValues ->
            // Ajustar el contenido para respetar la TopAppBar y NavigationBar
            NavHost(
                navController = navController,
                startDestination = "inicio",
                modifier = Modifier.padding(paddingValues)
            ) {
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
fun ProductoListScreen(navController: NavController, apiService: ProductoApiService) {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            productos = apiService.getProductos()
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(productos) { producto ->
            ProductoItem(producto, navController)
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: ${producto.precio}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }
            Row {
                IconButton(
                    onClick = {
                        navController.navigate("productoEditar/${producto.id}")
                    }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF6200EE))
                }
                IconButton(
                    onClick = {
                        navController.navigate("productoEliminar/${producto.id}")
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFF44336))
                }
            }
        }
    }
}

@Composable
fun InicioScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Shopping Cart Icon",
            tint = Color(0xFF6200EE),
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Bienvenido a la aplicación de Productos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Aquí podrás gestionar todos los productos de tu tienda fácilmente. ¡Explora, agrega, edita y elimina productos de forma rápida y sencilla!",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

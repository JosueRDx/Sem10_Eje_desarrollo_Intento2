package com.josuerdx.sem10_e_desarrollo_intento2.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.Producto
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import kotlinx.coroutines.launch

@Composable
fun ProductoListScreen(navController: NavController, apiService: ProductoApiService) {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            productos = apiService.getProductos()
        }
    }

    LazyColumn {
        items(productos) { producto ->
            ProductoItem(producto, navController)
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, navController: NavController) {
    Row {
        Text(text = producto.nombre)
        IconButton(
            onClick = {
                // Navegar a la pantalla de edición con el ID del producto
                navController.navigate("productoEditar/${producto.id}")
            }
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(
            onClick = {
                // Navegar a la pantalla de eliminación con el ID del producto
                navController.navigate("productoEliminar/${producto.id}")
            }
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

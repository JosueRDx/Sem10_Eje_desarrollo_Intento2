package com.josuerdx.sem10_e_desarrollo_intento2.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.Producto
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import kotlinx.coroutines.launch

@Composable
fun ProductoCrearScreen(navController: NavController, apiService: ProductoApiService) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })
        TextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") })
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
        Button(onClick = {
            coroutineScope.launch {
                val producto = Producto(0, nombre, precio, stock.toInt(), "", categoria.toInt(), null, null)
                apiService.createProducto(producto)
                navController.navigate("productos")
            }
        }) {
            Text("Crear Producto")
        }
    }
}
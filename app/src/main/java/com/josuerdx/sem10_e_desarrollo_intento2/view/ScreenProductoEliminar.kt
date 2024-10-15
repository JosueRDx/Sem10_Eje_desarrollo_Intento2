package com.josuerdx.sem10_e_desarrollo_intento2.view


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import kotlinx.coroutines.launch

@Composable
fun ProductoEliminarScreen(navController: NavController, apiService: ProductoApiService, id: Int) {
    var showDialog by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de que deseas eliminar este producto?") },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        apiService.deleteProducto(id)
                        navController.navigate("productos")
                    }
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    navController.navigate("productos")
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
package com.josuerdx.sem10_e_desarrollo_intento2.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.josuerdx.sem10_e_desarrollo_intento2.data.Categoria
import com.josuerdx.sem10_e_desarrollo_intento2.data.Producto
import com.josuerdx.sem10_e_desarrollo_intento2.data.ProductoApiService
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoCrearScreen(navController: NavController, apiService: ProductoApiService) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
    var selectedCategoria by remember { mutableStateOf<Categoria?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            categorias = apiService.getCategorias()
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Crear Nuevo Producto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
            }

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedCategoria?.nombre ?: "Selecciona una categoría",
                    onValueChange = {},
                    label = { Text("Categoría") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                selectedCategoria = categoria
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Button(
                onClick = {
                    val stockValue = stock.toIntOrNull()
                    val categoriaValue = selectedCategoria?.id

                    if (stockValue != null && categoriaValue != null) {
                        coroutineScope.launch {
                            val pubDate = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                            val producto = Producto(
                                id = 0,
                                nombre = nombre,
                                precio = precio,
                                stock = stockValue,
                                pubDate = pubDate,
                                categoria = categoriaValue,
                                imagen = null,
                                imagenUrl = null
                            )
                            val response = apiService.createProducto(producto)
                            if (response.isSuccessful) {
                                navController.navigate("productos")
                            } else {
                                errorMessage = "Error al crear el producto. Inténtalo de nuevo."
                            }
                        }
                    } else {
                        errorMessage = "Por favor, introduce un valor numérico válido para el stock y selecciona una categoría."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Crear Producto", color = Color.White)
            }
        }
    }
}


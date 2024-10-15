package com.josuerdx.sem10_e_desarrollo_intento2.data


import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {
    @GET("productos/")
    suspend fun getProductos(): List<Producto>

    @GET("productos/{id}/")
    suspend fun getProducto(@Path("id") id: Int): Response<Producto>

    @POST("productos/")
    suspend fun createProducto(@Body producto: Producto): Response<Producto>

    @PUT("productos/{id}/")
    suspend fun updateProducto(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    @DELETE("productos/{id}/")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Producto>
}
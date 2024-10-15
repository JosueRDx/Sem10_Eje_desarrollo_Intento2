package com.josuerdx.sem10_e_desarrollo_intento2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.josuerdx.sem10_e_desarrollo_intento2.ui.theme.Sem10_E_desarrollo_intento2Theme
import com.josuerdx.sem10_e_desarrollo_intento2.view.ProductoApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sem10_E_desarrollo_intento2Theme {
                ProductoApp()
            }
        }
    }
}

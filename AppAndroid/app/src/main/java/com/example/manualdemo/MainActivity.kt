package com.example.manualdemo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navBar()

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) !is Home) {
            val mainFragment = Home()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, mainFragment)
                .commit()
        }
    }
    private fun navBar() {
        val navigationBarView = findViewById<NavigationBarView>(R.id.bnBar)
        navigationBarView.selectedItemId = R.id.inicio
        navigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.traducir -> {
                    mostrarCambioIdioma()
                    true
                }

                R.id.inicio -> {
                    if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) !is Home) {
                        val mainFragment = Home()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, mainFragment)
                            .commit()
                    }
                    true
                }

                R.id.demo -> {
                    val detalleFragment = Demo.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, detalleFragment)
                        .addToBackStack(null)
                        .commit()
                    true
                }

                else -> false
            }
        }
    }

    private fun mostrarCambioIdioma() {
        val idiomas = arrayOf("Español", "Inglés")

        MaterialAlertDialogBuilder(this)
            .setTitle("Selecciona un idioma")
            .setItems(idiomas) { _, which ->
                // which se refiere al índice del elemento seleccionado en el array
                when (which) {
                    0 -> {
                        // Implementa la lógica para el idioma Español
                    }
                    1 -> {
                        // Implementa la lógica para el idioma Inglés
                    }
                }
            }
            .show()
    }
}

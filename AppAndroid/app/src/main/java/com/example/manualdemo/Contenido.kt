package com.example.manualdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private var ARG_PARAM2: Int = 0
private lateinit var temarioList: ArrayList<Temario>
private const val MAX_SECTION: Int = 7
private var posicion: Int = 0

/**
 * A simple [Fragment] subclass.
 * Use the [Contenido.newInstance] factory method to
 * create an instance of this fragment.
 */
class Contenido : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: Int? = null
    private var titulo: String? = null
    private var homeInstance: Home? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contenido, container, false)
        val temarioName = arguments?.getString("temarioName")

        // Encuentra el TextView por su ID en la vista inflada y establece el texto
        view.findViewById<TextView>(R.id.titulo).text = temarioName
        //val arrayManualUsuario = resources.getStringArray(R.array.manual_usuario)
        //view.findViewById<TextView>(R.id.texto).text = arrayManualUsuario[posicion]

        // Encuentra el botón por su ID en la vista inflada
        val btnAnterior = view.findViewById<Button>(R.id.btnAnterior)

        // Agrega un OnClickListener al botón Anterior
        btnAnterior.setOnClickListener {
            // Aquí colocas el código que deseas ejecutar cuando se hace clic en el botón Anterior
            refreshContent(-1, view)
        }

        // Encuentra el botón por su ID en la vista inflada
        val btnSiguiente = view.findViewById<Button>(R.id.btnSiguiente) // Cambiado a btnSiguiente

        // Agrega un OnClickListener al botón Siguiente
        btnSiguiente.setOnClickListener {
            // Aquí colocas el código que deseas ejecutar cuando se hace clic en el botón Siguiente
            refreshContent(1, view)
        }

        return view
    }

    private fun refreshContent(opcion: Int, view: View) {
        val nuevoParam2 = posicion + opcion
        if (nuevoParam2 >= 0 && nuevoParam2 <= MAX_SECTION) {
            posicion = nuevoParam2
            val temarioName = temarioList[posicion].name
            homeInstance?.changeContent(temarioName, posicion, homeInstance!!)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            homeInstance: Home,
            param1: ArrayList<Temario>,
            position: Int,
            temarioName: String
        ) =
            Contenido().apply {
                this.homeInstance = homeInstance
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, position)
                    putString("temarioName", temarioName)
                }
            }

        private fun putInt(argParam2: Int, position: Int) {
            posicion = position
        }

        private fun putParcelableArrayList(argParam1: String, param1: ArrayList<Temario>) {
            temarioList = param1
        }
    }

}
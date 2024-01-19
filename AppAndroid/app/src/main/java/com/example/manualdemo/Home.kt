package com.example.manualdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment(), TemarioAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var temarioList: ArrayList<Temario>
    private lateinit var temarioAdapter: TemarioAdapter
    private var param1: String? = null
    private var param2: String? = null
    private var temarioName: String? = null
    private var temarioImagen: Int? = null
    private var temarioDesc: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mostrarReciclerView(view)
        return view
    }
    private fun mostrarReciclerView(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.rvItems)
        recyclerView.layoutManager = LinearLayoutManager(context)
        temarioList = ArrayList()

        temarioList.add(Temario(R.drawable.pantalla_de_inicio, "1. " + getString(R.string.iniciar_sesion),
            getString(R.string.desc_iniciar_sesion)))
        temarioList.add(Temario(R.drawable.registro, "2. " + getString(R.string.registrarse),
            getString(
                R.string.desc_registrarse
            )))
        temarioList.add(Temario(R.drawable.subida_archivos, "3. " + getString(R.string.subir_archivos),
            getString(
                R.string.desc_subir_archivo
            )))
        temarioList.add(Temario(R.drawable.eliminacion_descarga, "4. " + getString(R.string.borrar_archivos),
            getString(
                R.string.desc_eliminar_archivo
            )))
        temarioList.add(Temario(R.drawable.subida_archivos, "5. " + getString(R.string.descargar_archivos),
            getString(
                R.string.desc_descargar_archivo
            )))
        temarioList.add(Temario(R.drawable.subida_archivos, "6. " + getString(R.string.crear_carpeta),
            getString(
                R.string.desc_crear_carpeta
            )))
        temarioList.add(Temario(R.drawable.subida_archivos, "7. " + getString(R.string.borrar_carpeta),
            getString(
                R.string.desc_eliminar_carpeta
            )))
        temarioList.add(Temario(R.drawable.sesion_iniciada_correo, "8. " + getString(R.string.buscar_archivo),
            getString(
                R.string.desc_email
            )))
        temarioAdapter = TemarioAdapter(temarioList, this)
        recyclerView.adapter = temarioAdapter
    }

    override val supportFragmentManager: Any
        get() = TODO("Not yet implemented")

    override fun onItemClick(position: Int) {
        temarioName = temarioList[position].name
        temarioImagen = temarioList[position].Image
        temarioDesc = temarioList[position].descripcion
        changeContent(temarioName!!, temarioImagen!!, temarioDesc!!, position, this)
    }

    fun changeContent(temarioName: String, temarioImagen: Int, temarioDesc: String,position: Int, homeInstance: Home) {
        val detalleFragment = Contenido.newInstance(homeInstance, temarioList, position, temarioName, temarioImagen, temarioDesc)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, detalleFragment)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString("temarioName", param3)
                }
            }
    }

}

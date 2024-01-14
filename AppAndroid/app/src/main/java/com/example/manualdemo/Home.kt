package com.example.manualdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "1. " + getString(R.string.iniciar_sesion)))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "2. " + getString(R.string.registrarse)))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "3. " + getString(R.string.subir_archivos)))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "4. " + getString(R.string.borrar_archivos)))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "5. " + getString(R.string.cambiar_idioma)))
        temarioAdapter = TemarioAdapter(temarioList, this)
        recyclerView.adapter = temarioAdapter
    }

    override val supportFragmentManager: Any
        get() = TODO("Not yet implemented")

    override fun onItemClick(position: Int) {
        temarioName = temarioList[position].name
        changeContent(temarioName!!, position, this)
    }

    fun changeContent(temarioName: String, position: Int, homeInstance: Home) {
        val detalleFragment = Contenido.newInstance(homeInstance, temarioList, position, temarioName)
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

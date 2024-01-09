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
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "1. Inicio Sesión"))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "1. Inicio Sesión"))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "1. Inicio Sesión"))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "1. Inicio Sesión"))
        temarioList.add(Temario(R.drawable.ic_launcher_foreground, "1. Inicio Sesión"))
        temarioAdapter = TemarioAdapter(temarioList, this)
        recyclerView.adapter = temarioAdapter
    }

    override fun onItemClick(position: Int) {

        // Aquí obtienes la información del temario según la posición
        val temarioName = temarioList[position].name

        // Reemplaza el contenido del contenedor con el nuevo fragmento
        val detalleFragment = Contenido.newInstance(temarioName)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, detalleFragment)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

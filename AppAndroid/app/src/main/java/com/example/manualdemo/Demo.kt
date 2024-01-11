package com.example.manualdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Demo : Fragment() {
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
        // Inflar el diseño del fragmento que contiene el WebView
        val view = inflater.inflate(R.layout.fragment_demo, container, false)
        val webView: WebView = view.findViewById(R.id.demoApp)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl("http://192.168.0.19:4200")
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            Demo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

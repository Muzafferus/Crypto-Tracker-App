package com.muzafferus.cryptotrackerapp.ui.alerthistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.data.entities.CryptoModel
import com.muzafferus.cryptotrackerapp.databinding.FragmentAlertHistoryBinding
import com.muzafferus.cryptotrackerapp.ui.BaseFragment
import com.muzafferus.cryptotrackerapp.util.OnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertHistoryFragment :
    BaseFragment<FragmentAlertHistoryBinding>(FragmentAlertHistoryBinding::inflate) {

    private var cryptoId: String? = null

    private val viewModel: AlertHistoryViewModel by viewModels()
    private lateinit var adapter: AlertHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cryptoId = arguments?.getString("id")
        initAdapter()
        initData()
    }

    private fun initAdapter() {
        adapter = AlertHistoryAdapter()

        adapter.setOnClick(object : OnClickListener<AlertModel> {
            override fun clicked(clickObject: AlertModel) {
                viewModel.delete(alert = clickObject)
                initData()
            }
        })

        binding.rvAlert.layoutManager = LinearLayoutManager(context)
        binding.rvAlert.adapter = adapter
    }

    private fun initData() {
        if (cryptoId != null) {
            viewModel.getCryptoList(cryptoId!!).observe(viewLifecycleOwner) { response ->
                if (response.isNullOrEmpty()) {
                    Toast.makeText(context, "Null or Empty", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.setList(response)
                }
            }
        }
    }
}
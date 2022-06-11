package com.muzafferus.cryptotrackerapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import coil.load
import com.muzafferus.cryptotrackerapp.R
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.data.entities.DetailModel
import com.muzafferus.cryptotrackerapp.databinding.FragmentDetailBinding
import com.muzafferus.cryptotrackerapp.ui.BaseFragment
import com.muzafferus.cryptotrackerapp.util.ViewState
import com.muzafferus.cryptotrackerapp.util.WorkManagerScheduler.refreshPeriodicWork
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel: DetailViewModel by viewModels()
    private var cryptoId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initOnClick()
        initWorkManager()
    }

    private fun initData() {
        cryptoId = arguments?.getString("id")
        if (cryptoId != null) {
            viewModel.cryptoPrice.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ViewState.Success -> {
                        binding.tvPrice.text = response.value?.data?.get(cryptoId)?.usd.toString()
                    }
                    else -> {
                        binding.tvPrice.text = ""
                    }
                }
            }

            viewModel.cryptoDetail.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ViewState.Loading -> {
                        binding.cryptoFetchProgress.visibility = View.VISIBLE
                        binding.cryptoNotFound.visibility = View.GONE
                        binding.etMax.visibility = View.GONE
                        binding.etMin.visibility = View.GONE
                        binding.btnMaxSet.visibility = View.GONE
                        binding.btnMinSet.visibility = View.GONE
                        binding.btnAlertHistory.visibility = View.GONE
                    }
                    is ViewState.Success -> {
                        showData(response.value?.data)
                        binding.cryptoFetchProgress.visibility = View.GONE
                        binding.cryptoNotFound.visibility = View.GONE
                        binding.etMax.visibility = View.VISIBLE
                        binding.etMin.visibility = View.VISIBLE
                        binding.btnMaxSet.visibility = View.VISIBLE
                        binding.btnMinSet.visibility = View.VISIBLE
                        binding.btnAlertHistory.visibility = View.VISIBLE
                    }
                    is ViewState.Error -> {
                        binding.cryptoFetchProgress.visibility = View.GONE
                        binding.cryptoNotFound.visibility = View.VISIBLE
                        binding.cryptoNotFound.text = response.message
                    }
                }
            }

            viewModel.getCryptoPrice(cryptoId!!)
            viewModel.getCryptoDetails(cryptoId!!)
        }
    }

    private fun showData(data: DetailModel?) {
        if (data != null) {
            binding.tvName.text = data.name
            binding.tvId.text = data.id
            binding.tvSymbol.text = data.symbol
            binding.tvRank.text = data.coingecko_rank.toString()
            binding.imgCrypto.load(data.image.small)
        }

    }

    private fun initOnClick() {
        binding.btnMaxSet.setOnClickListener {
            if (binding.etMax.length() > 0 && cryptoId != null) {
                viewModel.insertAlert(
                    AlertModel(
                        type = 2,
                        cryptoId = cryptoId!!,
                        price = binding.etMax.text.toString(),
                        id = null
                    )
                )
                Toast.makeText(context, "Your maximum alert created!", Toast.LENGTH_SHORT).show()
                binding.etMax.setText("")
            }
        }

        binding.btnMinSet.setOnClickListener {
            if (binding.etMin.length() > 0 && cryptoId != null) {
                viewModel.insertAlert(
                    AlertModel(
                        type = 1,
                        cryptoId = cryptoId!!,
                        price = binding.etMin.text.toString(),
                        id = null
                    )
                )
                Toast.makeText(context, "Your minimum alert created!", Toast.LENGTH_SHORT).show()
                binding.etMin.setText("")
            }
        }

        binding.btnAlertHistory.setOnClickListener {
            navController.navigate(R.id.action_detailFragment_to_alertHistoryFragment,
                Bundle().apply {
                    putString("id", cryptoId)
                })
        }
    }

    private fun initWorkManager() {
        if (context == null) return
        refreshPeriodicWork(requireContext())
    }
}
package com.muzafferus.cryptotrackerapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.muzafferus.cryptotrackerapp.R
import com.muzafferus.cryptotrackerapp.data.entities.CryptoModel
import com.muzafferus.cryptotrackerapp.databinding.FragmentHomeBinding
import com.muzafferus.cryptotrackerapp.ui.BaseFragment
import com.muzafferus.cryptotrackerapp.util.OnClickListener
import com.muzafferus.cryptotrackerapp.util.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: CryptoAdapter
    private var list: List<CryptoModel> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initData()
        initSearchView()
    }

    private fun initData() {
        viewModel.cryptoList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    binding.cryptoFetchProgress.visibility = View.VISIBLE
                    binding.cryptoNotFound.visibility = View.GONE
                    binding.rvCrypto.visibility = View.GONE
                    binding.svCrypto.visibility = View.GONE
                }
                is ViewState.Success -> {
                    val value = response.value?.data
                    if (!value.isNullOrEmpty()) {
                        list = value
                    }

                    if (list.isNullOrEmpty()) {
                        binding.cryptoFetchProgress.visibility = View.GONE
                        binding.cryptoNotFound.visibility = View.VISIBLE
                        binding.rvCrypto.visibility = View.GONE
                        binding.svCrypto.visibility = View.GONE
                    } else {
                        adapter.setList(list)
                        binding.rvCrypto.visibility = View.VISIBLE
                        binding.cryptoFetchProgress.visibility = View.GONE
                        binding.cryptoNotFound.visibility = View.GONE
                        binding.svCrypto.visibility = View.VISIBLE
                    }
                }
                is ViewState.Error -> {
                    binding.cryptoFetchProgress.visibility = View.GONE
                    binding.cryptoNotFound.visibility = View.VISIBLE
                    binding.rvCrypto.visibility = View.GONE
                    binding.svCrypto.visibility = View.GONE
                    binding.cryptoNotFound.text = response.message
                }
            }
        }

        if (list.isNullOrEmpty()) {
            viewModel.getCryptoList()
        }
    }

    private fun initAdapter() {
        adapter = CryptoAdapter()

        adapter.setOnClick(object : OnClickListener<CryptoModel> {
            override fun clicked(clickObject: CryptoModel) {
                binding.svCrypto.setQuery("", false)
                navController.navigate(R.id.action_homeFragment_to_detailFragment,
                    Bundle().apply {
                        putString("id", clickObject.id)
                    })
            }
        })

        binding.rvCrypto.layoutManager = LinearLayoutManager(context)
        binding.rvCrypto.adapter = adapter
    }

    private fun initSearchView() {
        binding.svCrypto.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapterFilter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterFilter(newText)
                return false
            }
        })
    }

    private fun adapterFilter(query: String?) {
        if (!query.isNullOrEmpty()) {
            adapter.setList(list.filter { it.name.lowercase().contains(query.lowercase()) })
        } else {
            adapter.setList(list)
        }
    }

}
package com.muzafferus.cryptotrackerapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.muzafferus.cryptotrackerapp.databinding.ListItemCryptoBinding
import com.muzafferus.cryptotrackerapp.util.OnClickListener
import com.muzafferus.cryptotrackerapp.data.entities.CryptoModel

class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    private val cryptoList: ArrayList<CryptoModel> = arrayListOf()
    private lateinit var onClick: OnClickListener<CryptoModel>

     fun setList(cryptos: List<CryptoModel>) {
        cryptoList.clear()
        cryptoList.addAll(cryptos)
        notifyDataSetChanged()
    }

     fun setOnClick(onClick: OnClickListener<CryptoModel>) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ListItemCryptoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(cryptoList[position])
        holder.getMain().setOnClickListener {
            onClick.clicked(cryptoList[position])
        }
    }

    override fun getItemCount(): Int = cryptoList.size

    class CryptoViewHolder(private val binding: ListItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun getMain(): LinearLayout {
            return binding.main
        }

        fun bind(crypto: CryptoModel) {
            binding.tvName.text = crypto.name
            binding.tvCode.text = String.format("(%s)", crypto.symbol)
        }

    }
}
package com.muzafferus.cryptotrackerapp.ui.alerthistory

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.muzafferus.cryptotrackerapp.R
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.databinding.ListItemAlertBinding
import com.muzafferus.cryptotrackerapp.util.OnClickListener

class AlertHistoryAdapter : RecyclerView.Adapter<AlertHistoryAdapter.AlertHistoryViewHolder>() {

    private val alertList: ArrayList<AlertModel> = arrayListOf()
    private lateinit var onClick: OnClickListener<AlertModel>


    fun setList(alerts: List<AlertModel>) {
        alertList.clear()
        alertList.addAll(alerts)
        notifyDataSetChanged()
    }

    fun setOnClick(onClick: OnClickListener<AlertModel>) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHistoryViewHolder {
        val binding =
            ListItemAlertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return AlertHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertHistoryViewHolder, position: Int) {
        holder.bind(alertList[position])
        holder.getButton().setOnClickListener {
            onClick.clicked(alertList[position])
        }
    }

    override fun getItemCount(): Int = alertList.size

    class AlertHistoryViewHolder(private val binding: ListItemAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun getButton(): Button {
            return binding.btnDelete
        }

        fun bind(alert: AlertModel) {
            if (alert.type == 1) {
                binding.tvMaxMin.text =
                    binding.tvMaxMin.context.resources.getString(R.string.minimum_alert)
            } else {
                binding.tvMaxMin.text =
                    binding.tvMaxMin.context.resources.getString(R.string.maximum_alert)
            }

            binding.tvPrice.text = alert.price
        }


    }
}
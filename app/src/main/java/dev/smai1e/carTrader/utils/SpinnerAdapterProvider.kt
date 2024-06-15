package dev.smai1e.carTrader.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.SpinnerItemBinding
import dev.smai1e.carTrader.ui.models.SpinnerItem

interface SpinnerAdapterProvider {

    fun <T> Spinner.addSpinnerAdapter(
        dataList: List<SpinnerItem<out T?>>,
        block: (Int) -> Unit
    ) {

        val adapter = SpinnerAdapter(context, R.layout.spinner_item, dataList)
        this.adapter = adapter
        this.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {
                    block(index)
                }
            }
    }

    private class SpinnerAdapter<T>(
        context: Context,
        textViewResourceId: Int,
        dataList: List<SpinnerItem<out T?>>
    ) : ArrayAdapter<SpinnerItem<out T?>>(context, textViewResourceId, dataList) {

        private val _dataList = dataList

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, parent, _dataList)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, parent, _dataList)
        }

        private fun getCustomView(
            position: Int,
            parent: ViewGroup,
            dataList: List<SpinnerItem<out T?>>
        ): View {

            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SpinnerItemBinding.inflate(layoutInflater, parent, false)

            val item = dataList[position]
            item.iconId?.let {
                binding.brandImageView.setImageResource(it)
            }
            binding.brandTextView.text = parent.resources.getString(item.nameId)

            return binding.root
        }
    }
}
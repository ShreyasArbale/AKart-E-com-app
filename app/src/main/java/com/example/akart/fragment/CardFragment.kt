package com.example.akart.fragment

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.akart.R
import com.example.akart.activity.AddressActivity
import com.example.akart.activity.CategoryActivity
import com.example.akart.adapter.CardAdapter
import com.example.akart.databinding.FragmentCardBinding
import com.example.akart.roomdb.AppDatabase
import com.example.akart.roomdb.ProductModel

/**
 * A simple [Fragment] subclass.
 * Use the [CardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding
    private lateinit var list: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardBinding.inflate(layoutInflater)

        val preference  = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()

        list = ArrayList()

        dao.getAllProducts().observe(requireActivity()){
            binding.cardRecycler.adapter = CardAdapter(requireContext(), it)

            list.clear()
            for (data in it){
                list.add(data.productId)
            }
            totalCost(it)

        }


        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!){
            total += item.productSp!!.toInt()
        }
        binding.textView12.text = "${data.size}"
        binding.textView13.text = "$total"

        binding.checkout.setOnClickListener{
            val intent = Intent(context, AddressActivity::class.java)
            val b = Bundle()
            b.putStringArrayList("productIds", list)
            b.putString("totalCost", total.toString())
            intent.putExtras(b)
            startActivity(intent)
        }
    }

}
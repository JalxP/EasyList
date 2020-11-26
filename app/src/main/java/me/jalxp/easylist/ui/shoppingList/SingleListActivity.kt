package me.jalxp.easylist.ui.shoppingList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.ActivitySingleListBinding
import me.jalxp.easylist.ui.products.ProductsFragment

class SingleListActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var sortsArray: Array<String>
    private lateinit var binding: ActivitySingleListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleListBinding.inflate(layoutInflater)
        val view = binding.root

        val selectedListId = intent.getLongExtra(EXTRA_LIST_ID, 0L)
        val selectedListTitle = intent.getStringExtra(EXTRA_LIST_TITLE)

        val bundle = Bundle()
        val productsFragment = ProductsFragment()
        productsFragment.arguments = bundle

        supportFragmentManager.beginTransaction().add(
            R.id.products_frame, productsFragment).commit()

        setContentView(view)

        title = selectedListTitle

        setSupportActionBar(binding.singleListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sortsArray = resources.getStringArray(R.array.sort_by_array)
        setupSpinner()

    }

    private fun setupSpinner() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, sortsArray
        )

        with(binding) {
            sortBySpinner.adapter = adapter
            sortBySpinner.onItemSelectedListener = this@SingleListActivity
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this@SingleListActivity, sortsArray[position], Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
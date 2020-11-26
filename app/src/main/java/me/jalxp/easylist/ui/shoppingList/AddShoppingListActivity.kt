package me.jalxp.easylist.ui.shoppingList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.ActivityAddShoppingListBinding

const val LIST_TITLE = "title"

class AddShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddShoppingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShoppingListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = getString(R.string.activity_create_list)
        setSupportActionBar(binding.createShoppingListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.createListButton.setOnClickListener {
            createShoppingList()
        }
    }

    private fun createShoppingList() {
        val resultIntent = Intent()

        val listTitle = binding.createShoppingListTitle.text.toString()

        if (listTitle.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            resultIntent.putExtra(LIST_TITLE, listTitle)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }
}
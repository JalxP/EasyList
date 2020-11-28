package me.jalxp.easylist.ui.categories

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.ActivityAddCategoryBinding
import me.jalxp.easylist.ui.shoppingList.LIST_TITLE

const val CATEGORY_DESCRIPTION = "category description"

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = getString(R.string.activity_add_category)
        setSupportActionBar(binding.addCategoryToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addCategoryButton.setOnClickListener {
            addCategory()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun addCategory() {
        val resultIntent = Intent()

        val categoryTitle = binding.addCategoryTitle.text.toString()

        if (categoryTitle.isNullOrEmpty()) {
            binding.categoryTextLayout.error = getString(R.string.need_description_error)
            return
        } else {
            resultIntent.putExtra(CATEGORY_DESCRIPTION, categoryTitle)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }
}
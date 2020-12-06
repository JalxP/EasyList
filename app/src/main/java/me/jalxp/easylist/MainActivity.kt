package me.jalxp.easylist

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import me.jalxp.easylist.databinding.ActivityMainBinding
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_TITLE
import me.jalxp.easylist.ui.shoppingList.EXTRA_PRODUCT_NAME


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val productsViewModel: ProductsViewModel by viewModels {
        ProductsViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.include.toolbar)

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_products,
                R.id.nav_categories,
                R.id.nav_measurementUnits,
                R.id.nav_shoppinglists
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.nav_shoppinglists -> getString(R.string.menu_shoppinglists)
                R.id.nav_categories -> getString(R.string.menu_categories)
                R.id.nav_products -> getString(R.string.menu_products)
                R.id.nav_measurementUnits -> getString(R.string.menu_measurementUnits)
                R.id.nav_addShoppingListFragment -> getString(R.string.fragment_add_shoppingList)
                R.id.nav_addCategoryFragment -> getString(R.string.fragment_add_category)
                R.id.nav_addProductFragment -> getString(R.string.fragment_add_product)
                R.id.nav_addMeasurementUnitFragment -> getString(R.string.fragment_add_measurementUnits)
                R.id.nav_basketFragment -> getString(R.string.fragment_basket)
                R.id.nav_singleListFragment -> {
                    arguments?.getString(EXTRA_LIST_TITLE, "")
                }
                R.id.nav_productDetailFragment -> {
                    arguments?.getString(EXTRA_PRODUCT_NAME, "")
                }
                else -> "Default title"
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val menuItem = menu.findItem(R.id.nav_basketFragment)
        menuItem.setActionView(R.layout.menu_item_shopping_basket)

        productsViewModel.getProductsOnCart().observe(this, Observer {
            val n = it.size
            when {
                n in 1..99 -> {
                    menuItem.actionView.findViewById<FrameLayout>(R.id.view_alert_red_circle).visibility =
                        View.VISIBLE
                    menuItem.actionView.findViewById<TextView>(R.id.view_alert_count_textview).text =
                        it.size.toString()
                }
                n > 99 -> {
                    menuItem.actionView.findViewById<TextView>(R.id.view_alert_count_textview).text =
                        getString(
                            R.string.max_value_cart
                        )
                }
                else -> menuItem.actionView.findViewById<FrameLayout>(R.id.view_alert_red_circle).visibility =
                    View.GONE
            }

        })


        menuItem.actionView.setOnClickListener {
            menuItem.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
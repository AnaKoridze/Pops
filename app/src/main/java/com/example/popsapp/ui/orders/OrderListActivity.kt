package com.example.popsapp.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.popsapp.R
import com.example.popsapp.config.PopsApplication
import com.example.popsapp.data.Result
import com.example.popsapp.data.model.OrderStatus
import com.example.popsapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.order_list.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [OrderDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class OrderListActivity : AppCompatActivity(), OnOrderClickListener {
    override fun onOrderClicked(id: String) {
        if (twoPane) {
            val fragment = OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(OrderDetailFragment.ARG_ITEM_ID, id)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.order_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, OrderDetailActivity::class.java).apply {
                putExtra(OrderDetailFragment.ARG_ITEM_ID, id)
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                ordersViewModel.logout()
                ordersViewModel.logoutResponse.observe(this, Observer {
                    if (it is Result.Success) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var adapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        ordersViewModel = ViewModelProviders.of(this, OrdersViewModelFactory(application as PopsApplication))
            .get(OrdersViewModel::class.java)

        adapter = OrdersAdapter(this)
        order_list.layoutManager = LinearLayoutManager(this)
        order_list.adapter = adapter

        /**
         * Sorry, did not have time to implement picker, so setting order status by default
         */
        ordersViewModel.getOrders(OrderStatus.PRINTING)
        ordersViewModel.loadState()?.observe(this, Observer {
            when (it) {
                is Result.Progress -> {
                    loading.playAnimation()
                    loading.visibility = View.VISIBLE
                }
                is Result.Error -> Toast.makeText(this, R.string.error_fetching_data, Toast.LENGTH_SHORT).show()
                is Result.Success -> {
                    loading.cancelAnimation()
                    loading.visibility = View.GONE

                    adapter.submitList(it.data)
                }
            }
        })
        ordersViewModel.orderList?.observe(this, Observer {
            adapter.submitList(it)
        })
        loading.playAnimation()

        if (order_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

    }

}

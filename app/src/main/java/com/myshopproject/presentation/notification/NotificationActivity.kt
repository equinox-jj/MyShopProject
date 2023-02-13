package com.myshopproject.presentation.notification

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myshopproject.R
import com.myshopproject.databinding.ActivityNotificationBinding
import com.myshopproject.domain.entities.FcmDataDomain
import com.myshopproject.presentation.notification.adapter.NotificationAdapter
import com.myshopproject.presentation.viewmodel.LocalViewModel
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    private lateinit var menuNotification: Menu

    private val viewModel by viewModels<LocalViewModel>()
    private var adapter: NotificationAdapter? = null
    private var isMultipleSelect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        initObserver()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarNotification)
        supportActionBar?.title = getString(R.string.notification)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_notification_toolbar, menu)
                menuNotification = menu
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        onBackPressed()
                    }
                    R.id.menu_check_notification -> {
                        showMultipleSelect()
                    }
                    R.id.menu_read_notification -> {
                        readNotification()
                    }
                    R.id.menu_delete_notification -> {
                        deleteNotification()
                    }
                }
                return true
            }
        })
    }

    private fun showMultipleSelect() {
        isMultipleSelect = !isMultipleSelect
        initObserver()

        if (isMultipleSelect) {
            menuNotification.findItem(R.id.menu_read_notification)?.isVisible = true
            menuNotification.findItem(R.id.menu_delete_notification)?.isVisible = true
            menuNotification.findItem(R.id.menu_check_notification)?.isVisible = false

            binding.toolbarNotification.title = getString(R.string.multiple_select)
        } else {
            menuNotification.findItem(R.id.menu_read_notification)?.isVisible = false
            menuNotification.findItem(R.id.menu_delete_notification)?.isVisible = false
            menuNotification.findItem(R.id.menu_check_notification)?.isVisible = true

            binding.toolbarNotification.title = getString(R.string.notification)
        }
    }

    private fun readNotification() {
        viewModel.setAllReadNotification(true)
        onBackPressed()
    }

    private fun deleteNotification() {
        viewModel.deleteNotification(true)
        onBackPressed()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllNotification().collectLatest { result ->
                    if (result.isNotEmpty()) {
                        adapter = NotificationAdapter(
                            context = this@NotificationActivity,
                            isMultipleSelect = isMultipleSelect,
                            onItemClicked = { onNotificationItemClicked(it) },
                            onCheckboxChecked = { onCheckboxChecked(it) }
                        )
                        binding.rvNotification.adapter = adapter
                        binding.rvNotification.setHasFixedSize(true)

                        adapter?.submitData(result)

                        binding.rvNotification.show()
                        binding.tvNoNotification.hide()
                    } else {
                        binding.rvNotification.hide()
                        binding.tvNoNotification.show()
                    }
                }
            }
        }
    }

    private fun onNotificationItemClicked(data: FcmDataDomain) {
        viewModel.updateReadNotification(true, data.id)
        AlertDialog.Builder(this@NotificationActivity)
            .setTitle(data.notificationTitle)
            .setMessage(data.notificationBody)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    private fun onCheckboxChecked(data: FcmDataDomain) {
        val productId = data.id
        val isChecked = !data.isChecked
        viewModel.updateCheckedNotification(isChecked, productId)
    }

    override fun onBackPressed() {
        if (isMultipleSelect) {
            showMultipleSelect()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.setAllUncheckedNotification()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isMultipleSelect) {
            showMultipleSelect()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.setAllUncheckedNotification()
        return true
    }

    /** NEW BACK PRESSED */
    private fun backPressed() {
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        onBackPressedDispatcher.addCallback(this@NotificationActivity, onBackPressedCallback)
    }
}
package com.freight.shipper.ui.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.freight.shipper.FreightApplication
import com.freight.shipper.R
import com.freight.shipper.extensions.setupToolbar
import com.freight.shipper.repository.LoadRepository
import com.freight.shipper.ui.addload.AddLoadFragment
import com.freight.shipper.ui.bookings.assigned.LoadPagerFragment
import com.freight.shipper.ui.home.HomeFragment
import com.freight.shipper.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.toolbar.*

class DashboardActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    // region - Companion object
    companion object {
        const val EXTRA_START_SCREEN = "start_screen"
        const val START_SCREEN_PROFILE = 0
        const val START_SCREEN_HOME = 1
    }
    // endregion

    // region - Private properties
    private var loginManager = FreightApplication.instance.loginManager
    private var lastItemSelected = R.id.navigation_home

    private val loadFragment by lazy { LoadPagerFragment.newInstance() }
    private val homeFragment by lazy { HomeFragment.newInstance() }
    private val addLoadFragment by lazy { AddLoadFragment.newInstance() }
    private val profileFragment by lazy { ProfileFragment.newInstance() }
    // endregion

    // region - Overridden functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        setupToolbar(toolbar, enableUpButton = false)
        setStartScreen()
        LoadRepository(
            FreightApplication.instance.api,
            FreightApplication.instance.loginManager
        ).getMasterConfigData()
    }

    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them
     * appear non-interactive.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> onHomeSelected()
            R.id.navigation_add -> onSecondSelected()
            R.id.navigation_favorite -> onFavoritesSelected()
            R.id.navigation_profile -> onProfileSelected()
            else -> return false
        }
        return true
    }
    // endregion

    // region - Public functions
    fun setToolbarTitle(@StringRes titleRes: Int = R.string.new_load) {
        tvToolbarTitle?.text = getString(titleRes)
    }
    // endregion

    // region - Private functions
    private fun onHomeSelected() {
        lastItemSelected = R.id.navigation_home
        setToolbarTitle()
        replaceFragmentOrAction(loadFragment)
    }

    private fun onSecondSelected() {
        lastItemSelected = R.id.navigation_add
        setToolbarTitle(R.string.add_new_load)
        replaceFragmentOrAction(addLoadFragment)
    }

    private fun onFavoritesSelected() {
        lastItemSelected = R.id.navigation_favorite
        setToolbarTitle(R.string.favorites)
        message.setText(R.string.favorites)
    }

    private fun onProfileSelected() {
        lastItemSelected = R.id.navigation_profile
        setToolbarTitle(R.string.profile)
        replaceFragmentOrAction(profileFragment)
    }

    private fun setStartScreen() {
        val startScreen = intent.getIntExtra(EXTRA_START_SCREEN, START_SCREEN_HOME)
        val initialFragment = when (startScreen) {
            START_SCREEN_HOME -> {
                bottomNavigationView.selectedItemId = R.id.navigation_home
                setToolbarTitle()
                loadFragment
            }
            START_SCREEN_PROFILE -> {
                bottomNavigationView.selectedItemId = R.id.navigation_profile
                setToolbarTitle(R.string.profile)
                profileFragment
            }
            else -> {
                bottomNavigationView.selectedItemId = R.id.navigation_home
                setToolbarTitle()
                loadFragment
            }
        }

        changeTabFragment(initialFragment, false)
    }

    private fun <T : Fragment> replaceFragmentOrAction(fragment: T, action: ((T) -> Unit)? = null) {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) != fragment) {
            changeTabFragment(fragment)
        } else {
            action?.invoke(fragment)
        }
    }

    private fun changeTabFragment(fragment: Fragment, withLoss: Boolean = true) {
        val toReplace = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.fragment_container, fragment)
        if (withLoss) {
            toReplace.commitAllowingStateLoss()
        } else {
            toReplace.commit()
        }
    }
    // endregion
}

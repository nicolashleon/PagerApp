package com.pager.teamapp.ui.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.pager.teamapp.R
import com.pager.teamapp.ui.TeamItemDecoration
import com.pager.teamapp.ui.adapters.TeamMembersAdapter
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.presenters.TeamMembersPresenter
import com.pager.teamapp.ui.views.TeamMembersView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class TeamActivity : AppCompatActivity(), TeamMembersView {


    private lateinit var presenter: TeamMembersPresenter
    private lateinit var adapter: TeamMembersAdapter


    //region Android Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_team_toolbar_title)
        presenter = TeamMembersPresenter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(TeamItemDecoration())
        adapter = TeamMembersAdapter()
        recyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
        presenter.getTeamMembers()
    }

    override fun onPause() {
        presenter.dettach()
        super.onPause()
    }

    //endregion

    //region TeamMembersView
    override fun showLoader(isVisible: Boolean) {
        if (isVisible) {
            adapter.clearItemsAndNotify()
        }
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showError() {
        val snackbar = Snackbar.make(coordinatorLayout,
                getString(R.string.txt_error_msg),
                Snackbar.LENGTH_LONG)

        snackbar.setAction(getString(R.string.action_reload)) { presenter.getTeamMembers() }
        snackbar.show()

    }

    override fun showTeamMembers(members: List<TeamMember>) {
        adapter.addAllItemsAndNotify(members)
    }

    //endRegion
}

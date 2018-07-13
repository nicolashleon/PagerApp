package com.pager.teamapp.ui.activities

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.pager.teamapp.R
import com.pager.teamapp.ui.TeamItemDecoration
import com.pager.teamapp.ui.adapters.TeamMembersAdapter
import com.pager.teamapp.ui.models.TeamMember
import com.pager.teamapp.ui.presenters.TeamMembersPresenter
import com.pager.teamapp.ui.views.TeamMembersView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class TeamActivity : AppCompatActivity(), TeamMembersView, TeamMembersAdapter.OnStatusUpdateListener {

    private lateinit var presenter: TeamMembersPresenter
    private lateinit var adapter: TeamMembersAdapter


    //region Android Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_team_toolbar_title)
        presenter = TeamMembersPresenter()
        adapter = TeamMembersAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this@TeamActivity)
        recyclerView.addItemDecoration(TeamItemDecoration())
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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
        presenter.getStatusUpdates()
    }

    override fun showStatusUpdateDialog(teamMember: TeamMember) {
        val layout = LayoutInflater.from(this).inflate(R.layout.layout_send_status_update, null) as ConstraintLayout
        val statusEditText = layout.findViewById<EditText>(R.id.statusEditText)
        AlertDialog.Builder(this)
                .setView(layout)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.txt_send)) { dialog, whichButton ->
                    Toast.makeText(this@TeamActivity, getString(R.string.txt_toast_send_status_update), Toast.LENGTH_SHORT).show()
                    presenter.sendStatusUpdate(statusEditText.text.toString(), teamMember)
                    dialog.dismiss()
                }
                .show()
    }

    //endRegion

    override fun onStatusUpdated(teamMember: TeamMember) {
        presenter.showUpdateStatusDialog(teamMember)
    }

}

package com.pager.teamapp.ui.delegates

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pager.teamapp.R
import com.pager.teamapp.ui.CircleTransform
import com.pager.teamapp.ui.models.TeamMember
import com.squareup.picasso.Picasso

class TeamMembersDelegateAdapter : DelegateAdapter<TeamMembersDelegateAdapter.TeamMemberViewHolder, TeamMember> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        return TeamMemberViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: TeamMemberViewHolder, uiModel: TeamMember) {
        uiModel.apply {
            viewHolder.nameTextView.text = name
            viewHolder.roleTextView.text = role
            if (!picture.isEmpty()) {
                Picasso.get().load(picture)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.ic_avatar_outline_48dp)
                        .error(R.drawable.ic_avatar_outline_48dp)
                        .into(viewHolder.picture)
            }
        }
    }

    class TeamMemberViewHolder(parent: ViewGroup) : ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_team_member, parent, false)) {
        val nameTextView: TextView by lazy { itemView.findViewById(R.id.nameTextView) as TextView }
        val roleTextView: TextView by lazy { itemView.findViewById(R.id.roleTextView) as TextView }
        val picture: ImageView by lazy { itemView.findViewById(R.id.imageView) as ImageView }

    }
}
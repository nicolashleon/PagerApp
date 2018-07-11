package com.pager.teamapp.ui.delegates

import android.support.v7.widget.CardView
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

            if (status.isNotEmpty()) {
                viewHolder.statusTextView.text = status
            }
            if (picture.isNotEmpty()) {
                Picasso.get().load(picture)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.ic_avatar_outline_48dp)
                        .error(R.drawable.ic_avatar_outline_48dp)
                        .into(viewHolder.picture)
            }

            viewHolder.apply {
                githubHandleTextView.text = itemView.context.getString(R.string.txt_github_handle, github)
                skillsTextView.text = skills.joinToString()
                locationTextView.text = location
                languagesTextView.text = languages.joinToString()
            }

        }
    }

    class TeamMemberViewHolder(parent: ViewGroup) : ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_team_member, parent, false)) {
        val nameTextView: TextView by lazy { itemView.findViewById(R.id.nameTextView) as TextView }
        val roleTextView: TextView by lazy { itemView.findViewById(R.id.roleTextView) as TextView }
        val statusTextView: TextView by lazy { itemView.findViewById(R.id.statusTextView) as TextView }
        val githubHandleTextView: TextView by lazy { itemView.findViewById(R.id.githubHandleTextView) as TextView }
        val skillsTextView: TextView by lazy { itemView.findViewById(R.id.skillsTextView) as TextView }
        val locationTextView: TextView by lazy { itemView.findViewById(R.id.locationTextView) as TextView }
        val languagesTextView: TextView by lazy { itemView.findViewById(R.id.languagesTextView) as TextView }
        val picture: ImageView by lazy { itemView.findViewById(R.id.imageView) as ImageView }
        val additionalInformationCardView: CardView by lazy { itemView.findViewById(R.id.additionalInformationCardView) as CardView }

    }
}
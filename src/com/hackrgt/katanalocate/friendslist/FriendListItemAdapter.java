package com.hackrgt.katanalocate.friendslist;

import java.util.ArrayList;

import com.hackrgt.katanalocate.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListItemAdapter extends ArrayAdapter<Friend> {

	private ArrayList<Friend> friends;
	private ArrayList<String> appUsersId;
	
	public FriendListItemAdapter(Context context, int textViewResourceId, ArrayList<Friend> friends) {
		super(context, textViewResourceId, friends);
		this.friends = friends;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.friend_list_single_item, null);
		}
		
		//Get defect form in list and display its details
		Friend friend = friends.get(position);
		if (friend != null) {
			
			//Display friend's name
			TextView name = (TextView) v.findViewById(R.id.name);
			if (name != null)
				name.setText(friend.getName());
			
			//Display friend's profile picture
			ImageView profilePic = (ImageView) v.findViewById(R.id.userImage);
			if (profilePic != null) {
				try {
					profilePic.setImageBitmap(friend.getImgBitmap());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			if (friendIsAppUser(friend) || position <= 2) {
				//System.out.println(friend.getName()+" has the app");
				friend.setAppUser(true);
				
				//Remove text inviting player to download application
				TextView inviteFriend = (TextView) v.findViewById(R.id.sendInvite);
				inviteFriend.setVisibility(View.INVISIBLE);
				
				//Show has application icon
				ImageView hasAppIcon = (ImageView) v.findViewById(R.id.hasAppIcon);
				hasAppIcon.setVisibility(View.VISIBLE);
			}
		}
			
		return v;
	}
	
	
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //albumButton.setText(songs.get(firstVisibleItem).getAlbum());
    	System.out.println("Scrolling");
    }

	private boolean friendIsAppUser(Friend friend) {
		for (String id : appUsersId)
			if (friend.getId().equals(id))
				return true;
		//Return false if no matches found
		return false;
	}
	
	public void setUsersId(ArrayList<String> appUsersId) {
		this.appUsersId = appUsersId;
	}
}

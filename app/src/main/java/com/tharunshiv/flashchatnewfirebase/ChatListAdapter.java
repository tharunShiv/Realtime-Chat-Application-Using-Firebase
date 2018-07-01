package com.tharunshiv.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.graphics.Color;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.time.Instant;
import java.util.ArrayList;

import static com.tharunshiv.flashchatnewfirebase.R.color.colorAccent;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            // when a new message is added to our database
            // the dataSnapshot comes in the form of JSON

            // add method appends
            mSnapshotList.add(dataSnapshot);

            // after each addition we must notify the listview
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    // creates and configures our adapter
    public ChatListAdapter(Activity activity, DatabaseReference ref, String name) {

        mActivity = activity;
        mDisplayName = name;
        mDatabaseReference = ref.child("messages"); // from where we will get teh chat messages

        // added later
        // database reference - during constructor
        mDatabaseReference.addChildEventListener(mListener);

        // added beffore
        mSnapshotList = new ArrayList<>();
    }

    // will hold all the view from a single chat list
    // inner class
    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        // getCount is called by the listview to the adapter
        // when it wants to know how many items are there to display
        // adapter will respond with the number


        return mSnapshotList.size();
    }

    // Return type changed from Object to InstantMessage
    @Override
    public InstantMessage getItem(int position) {
        // List calls this when it wants to get the item
        // i.e,. the entire row
        // position 0, position 1.....

        DataSnapshot snapshot = mSnapshotList.get(position);
        // extracting the InstantMessage object
        // this class converts the json from snapshot into a Instant message object
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {


        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // first check if there is an existing row that can be reused

        if (convertView == null){
            // if it doesnt
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // androids speak for parse the xml
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);

            // inner helper class that will hold on to all the things that make up a individual chat msg
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();

            // now we will temporarily hold our View to re use it later
            convertView.setTag(holder);
        }

        // now we have to make sure that we are showing the corect msg
        final InstantMessage message = getItem(position);

        // retrieve the Viewholder that was temporarily saved
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, holder);
        // but this ViewHolder now will have the old data
        // so set the new texts
        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        // though there are 100s of mesage , only few msgs are shown a a time
        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder) {

        if (isItMe) {
            holder.params.gravity = Gravity.END;
            // import this
            // import android.graphics.Color;
            holder.authorName.setTextColor(Color.RED);
            holder.body.setBackgroundResource(R.drawable.bubble2);

        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    // a method to stop checking for new messages

    public void cleanup() {

        mDatabaseReference.removeEventListener(mListener);
    }
}

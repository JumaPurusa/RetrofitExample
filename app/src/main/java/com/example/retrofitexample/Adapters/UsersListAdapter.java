package com.example.retrofitexample.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofitexample.Models.User;
import com.example.retrofitexample.R;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<User> users;
    private final static int ITEM = 0;
    private final static int LOADING = 1;
    private boolean isLoadingAdded = false;

    public UsersListAdapter(Context context){
        this.mContext = context;
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType){

            case ITEM:
                View view1 = inflater.inflate(R.layout.single_item, parent, false);
                viewHolder = new UserViewHolder(view1);
                break;

            case LOADING:
                View view2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(view2);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        User user = users.get(position);

        switch (getItemViewType(position)){

            case ITEM:
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                Glide.with(mContext)
                        .load(user.getAvatar_url())
                        .into(userViewHolder.imageView);

                userViewHolder.textName.setText(user.getName());
                userViewHolder.textUsername.setText(user.getLogin());
                userViewHolder.textCompany.setText(user.getCompany());
                userViewHolder.textBlog.setText(user.getBlog());
                userViewHolder.textLocation.setText(user.getLocation());
                break;

            case LOADING:
                break;

        }


    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == users.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textName, textUsername, textCompany, textBlog, textLocation;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImage);
            textName = itemView.findViewById(R.id.textName);
            textUsername = itemView.findViewById(R.id.textUsername);
            textCompany = itemView.findViewById(R.id.textCompany);
            textBlog = itemView.findViewById(R.id.textBlog);
            textLocation = itemView.findViewById(R.id.textLocation);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{


        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /*
    Helper Methods
     */

    public void addUser(User user){

        users.add(user);
        notifyItemInserted(users.size() - 1);
    }

    public void addAll(List<User> userList){
        for(User user : userList){

            addUser(user);
        }
    }

    public void remove(User user){

        int position = users.indexOf(user);

        if(position > -1){

            users.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){

        isLoadingAdded = false;

        while(getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }

    public void addLoadingFooter(){

        isLoadingAdded = true;
        addUser(new User());
    }

    public void removeLoadingFooter(){

        isLoadingAdded = false;
        int position = users.size() - 1;
        User user = getItem(position);

        if(user != null){
            users.remove(position);
            notifyItemRemoved(position);
        }
    }

    public User getItem(int position){

        return users.get(position);

    }
}

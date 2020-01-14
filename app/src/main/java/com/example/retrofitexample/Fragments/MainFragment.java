package com.example.retrofitexample.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofitexample.Adapters.UsersListAdapter;
import com.example.retrofitexample.Interfaces.PaginationOnScrollListener;
import com.example.retrofitexample.Interfaces.UserService;
import com.example.retrofitexample.MainActivity;
import com.example.retrofitexample.Models.User;
import com.example.retrofitexample.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private RecyclerView recyclerView;
    private UsersListAdapter adapter;
    private List<User> users;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;

    //index from which pagination should start (0 is 1st page in our case)
    private final static int PAGE_START = 1;

    //indicates if footer ProgressBar is shown (i.e next page is loading)
    private boolean isLoading = false;

    //if current page is the last page (Pagination will stop after this page load)
    private boolean isLastpage = false;

    //total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 3;

    //indicates the current page which Pagination is fetching
    private int currentPage = PAGE_START;

    private UserService userService;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        users = new ArrayList<>();

        adapter = new UsersListAdapter(getContext());

        if(recyclerView != null)
            recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(
                new PaginationOnScrollListener(layoutManager) {
                    @Override
                    protected void loadMoreItems() {

                        isLoading = true;

                        //Increment page index to load the next one
                        currentPage++;

                        loadNextpage();
                    }

                    @Override
                    public int getTotalPageCount() {

                        return TOTAL_PAGES;

                    }

                    @Override
                    public boolean isLastPage() {

                        return isLastpage;

                    }

                    @Override
                    public boolean isLoading() {

                        return isLoading;

                    }
                }
        );

        return view;
    }

    public void showProgressBar(){

        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){

        progressBar.setVisibility(View.GONE);

    }

       /*
        Performs a Retrofit call to the users API
     */

    private Call<List<User>> callUsersApi(){

        return userService.getUsers(10, currentPage);
    }

    public void loadFirstpage(List<User> users){

        hideProgressBar();

        adapter.addAll(users);

        if(currentPage <= TOTAL_PAGES)
            adapter.addLoadingFooter();
        else
            isLastpage = true;

    }

    private void loadNextpage(){

        callUsersApi().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                adapter.removeLoadingFooter();

                users = response.body();

                adapter.addAll(users);

                if(currentPage != TOTAL_PAGES)
                    adapter.addLoadingFooter();
                else
                    isLastpage = true;
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.example.retrofitexample;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofitexample.Activities.PalendromeActivity;
import com.example.retrofitexample.Adapters.UsersListAdapter;
import com.example.retrofitexample.Fragments.ErrorsFragment;
import com.example.retrofitexample.Fragments.MainFragment;
import com.example.retrofitexample.Interfaces.InternetConnectionListener;
import com.example.retrofitexample.Interfaces.PaginationOnScrollListener;
import com.example.retrofitexample.Interfaces.UserService;
import com.example.retrofitexample.Models.User;
import com.example.retrofitexample.Utils.App;
import com.example.retrofitexample.Utils.GithubServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements InternetConnectionListener {

    private final static String TAG = MainActivity.class.getSimpleName();

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

    private MainFragment mainFragment;

    private ErrorsFragment errorsFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Github Users");
        actionBar.setHomeButtonEnabled(true);

        ((App)getApplication()).setmInternetConnectionListener(this);

        fragmentManager = getSupportFragmentManager();

        mainFragment = new MainFragment();

        errorsFragment = new ErrorsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.mainFrame, mainFragment)
                .commitNow();

        //mocking 0.5 seconds network delay
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

//                        userService =
//                                GithubServiceGenerator.createService(UserService.class);
                        onStartLoading();
                    }
                }
        , 500);
    }

    public void onStartLoading(){

        mainFragment.showProgressBar();

        ((App)getApplication()).getUserService().getUsers(10, currentPage)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {


                        if(response.isSuccessful()){

                            mainFragment.loadFirstpage(response.body());

                        }else{

                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, errorsFragment)
                                    .commit();
                        }


                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {

                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, errorsFragment)
                                .commit();

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((App)getApplication()).removeInternetConnectionListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         menu.add(1000, 0, 0, "Palindrome");
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;

            case 0:
                startActivity(new Intent(MainActivity.this, PalendromeActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInternetUnavailable() {


//        fragmentManager.beginTransaction()
//                .replace(R.id.mainFrame, errorsFragment)
//                .commit();
//
//        errorsFragment.onDeviceOffline();

    }

    @Override
    public void onCacheUnavailable() {


//        fragmentManager.beginTransaction()
//                .replace(R.id.mainFrame, errorsFragment)
//                .commit();
//
//        errorsFragment.onError("No Cached data available");
    }
}

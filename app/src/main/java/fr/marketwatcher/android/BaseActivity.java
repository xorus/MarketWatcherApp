package fr.marketwatcher.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Intent MainActivity;
    public Intent LoginActivity;
    public Intent CatalogActivity;
    public Intent BarcodeResultActivity;
    public Intent DealsActivity;

    SharedPreferences preferences;

    public static final String API_URL = "https://api.marketwatcher.fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {
        LinearLayout fullView = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(fullView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectActualItemOnTheNavigationDrawer(navigationView);

    }

    public void selectActualItemOnTheNavigationDrawer(NavigationView navigationView) {
        if (getTitle().equals(getString(R.string.home)))
            navigationView.setCheckedItem(R.id.nav_home);
        else if (getTitle().equals(getString(R.string.catalog)))
            navigationView.setCheckedItem(R.id.nav_catalog);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home && !getTitle().equals(getString(R.string.home))) {

            MainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MainActivity);

        } else if (id == R.id.nav_catalog && !getTitle().equals(getString(R.string.catalog))) {

            CatalogActivity = new Intent(getApplicationContext(), CatalogActivity.class);
            startActivity(CatalogActivity);

        } else if (id == R.id.nav_deals && !getTitle().equals(getString(R.string.deals))) {

            DealsActivity = new Intent(getApplicationContext(), DealsActivity.class);
            startActivity(DealsActivity);

        } else if (id == R.id.nav_add && !getTitle().equals(getString(R.string.navAdd))) {

            BarcodeResultActivity = new Intent(getApplicationContext(), BarcodeResultActivity.class);
            startActivity(BarcodeResultActivity);

        } else if (id == R.id.nav_disconnect) {
            deleteToken();

            LoginActivity = new Intent(this, LoginActivity.class);
            startActivity(LoginActivity);

            Toast.makeText(getApplicationContext(), getString(R.string.toastLogout), Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return preferences;
    }

    public String getToken() {
        String token = null;
        if (getPreferences().contains("token")) {
            token = getPreferences().getString("token", "");
        }
        return token;
    }

    @SuppressLint("ApplySharedPref")
    public void deleteToken() {
        SharedPreferences.Editor editor = getPreferences().edit();

        // using commit because we need the change *now*
        editor.clear().commit();
    }

    public void redirectionToLogin() {
        Intent HomeIntent;
        HomeIntent = new Intent(this, MainActivity.class);
        startActivity(HomeIntent);
    }
}

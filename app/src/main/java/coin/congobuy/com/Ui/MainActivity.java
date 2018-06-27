package coin.congobuy.com.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import coin.congobuy.com.R;
import coin.congobuy.com.Ui.Settings.Settings;
import coin.congobuy.com.Ui.SigneLogin.LoginActivity;
import coin.congobuy.com.adapter.MyAdapter;
import coin.congobuy.com.object.MyItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listOfSeller;
    // creating arraylist of MyItem type to set to adapter
    ArrayList<MyItem> myitems=new ArrayList<>();
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        listOfSeller=(ListView)findViewById(R.id.mainactivitylistview);
        //Adding data i.e images and title to be set to adapter to populate list view
        //here i am passing image id from drawable and string as to be set as title as
        // a parameter to MyItem Constructor as our ArrayList is type of  MyItem

        myitems.add(new MyItem(R.drawable.images,"Christ Redeemer"));
                myitems.add(new MyItem(R.drawable.images,"Oliver Wall "));
        myitems.add(new MyItem(R.drawable.images,"Papy Picchu"));
        myitems.add(new MyItem(R.drawable.images,"Bwana Jordan"));
        myitems.add(new MyItem(R.drawable.images,"Kabumba Mexico"));
        myitems.add(new MyItem(R.drawable.images,"Louis Mihigio"));
        myitems.add(new MyItem(R.drawable.images,"Tresors"));
        //Creating Adapter object for setting to listview
        MyAdapter adapter=new MyAdapter(MainActivity.this,myitems);
        listOfSeller.setAdapter(adapter);
// Handle Listview click
        listOfSeller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Perform click events

                //get firebase auth instance
                auth = FirebaseAuth.getInstance();

                //get current user
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
                MyItem myitem=(MyItem)myitems.get(position);
                TextView nom = view.findViewById(R.id.txt_title);

                String title=myitem.getImageheading();
                Toast.makeText(getApplicationContext(),"seller"+nom.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_commandes){
            // Handle the camera action
        } else if (id == R.id.nav_vendeur) {

        } else if (id == R.id.nav_equipes) {
            Intent intent_myTeam= new Intent(this,EquipeActivity.class);
            startActivity(intent_myTeam);

        } else if (id == R.id.nav_mail) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}

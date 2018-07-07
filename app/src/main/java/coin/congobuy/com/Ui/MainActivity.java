package coin.congobuy.com.Ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import coin.congobuy.com.R;
import coin.congobuy.com.Ui.Fragment.FragmentAchats;
import coin.congobuy.com.Ui.MailUs.MailUsActivity;
import coin.congobuy.com.Ui.Settings.Settings;
import coin.congobuy.com.Ui.SigneLogin.LoginActivity;
import coin.congobuy.com.adapter.MyPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    public TextView txtTeamName, txtStock, txtPrice, txtMinToBuy, txtHappyClient,txtUser,txtUserPhone;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;


    FragmentPagerAdapter adapterViewPager;
    private Spinner spinner1;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "CBCpref";
    public static final String Number = "numKey";
    public static final String Email = "emailKey";
    public Button btnAchetr, btnVendre;
    public LinearLayout Lvente;

    //dialogue button



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        Lvente=findViewById(R.id.lvente);
        btnVendre=(Button) findViewById(R.id.btnvendre);
        txtUser= findViewById(R.id.txt_user);
        txtUserPhone= findViewById(R.id.txt_user_phone);
        txtTeamName = findViewById(R.id.txt_title);
        txtStock = findViewById(R.id.txtstock);
        txtPrice = findViewById(R.id.txtpricecoin);
        txtMinToBuy = findViewById(R.id.txtmin_to_buy);
        db = FirebaseFirestore.getInstance();

        //onclick achat et vente

        txtUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddNumber();
            }
        });

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        txtUser.setText("");
        if( getIntent().getExtras() != null)
        {
            //do here
            Intent i = getIntent();
            String txtData = i.getExtras().getString("email","");
            txtUser.setText(txtData);
            fab.setImageResource(R.drawable.ic_lock_open);
            ReadSingleContact();
            SaveSharePref();

        }
        else
        {
            if (sharedpreferences.contains(Number)) {
            txtUserPhone.setText(sharedpreferences.getString(Number, "000"));
                fab.setImageResource(R.drawable.ic_lock_open);
        }
            if (sharedpreferences.contains(Email)) {
                txtUser.setText(sharedpreferences.getString(Email, "MyEmail"));

            }

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUser.getText()==""){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }
                else{
                    fab.setImageResource(R.drawable.ic_lock);
                    DeleteSharePref();
                    txtUser.setText("");
                    Snackbar.make(view, "Déconnecter", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    finish();
                    startActivity(getIntent());
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //set teamInfo
        //todo etoile
        txtTeamName.setText("CongoBuyCoin");
        txtMinToBuy.setText("10$");
        setTeam("0.01574 Bitcoin", "0.001584btcoin/ 1$", "25");


    }


    private void setTeam(String Stock, String price, String txtHappyClients) {
        txtStock.setText(Stock);
        txtPrice.setText(price);
//        txtHappyClient.setText(txtHappyClients);

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
            startActivity(new Intent(MainActivity.this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_commandes_plus) {
            // Handle the camera action
        } else if (id == R.id.nav_confiances) {

        } else if (id == R.id.nav_equipes) {
            Intent intent_myTeam = new Intent(this, EquipeActivity.class);
            startActivity(intent_myTeam);

        } else if (id == R.id.nav_mail) {
            Intent intent_myTeam = new Intent(this, MailUsActivity.class);
            startActivity(intent_myTeam);

        }
        else if (id == R.id.nav_share) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void SaveSharePref() {
        String email = txtUser.getText().toString();
        String phoneNumber = txtUserPhone.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Number, phoneNumber);
        editor.putString(Email, email);
        editor.commit();
    }
    public void SaveSharePrefAdmin(String price) {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Price", price);
        editor.commit();
    }

    public void DeleteSharePref(){
        txtUser.setText("");
        txtUserPhone.setText("");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("name"); // will delete key name
        editor.remove("email"); // will delete key email
        editor.clear();
        editor.commit();
    }
    private void ReadSingleContact() {

        DocumentReference user = db.collection("User").document(txtUser.getText().toString());
        DocumentReference price = db.collection("Admin").document("andy");
        price.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    StringBuilder fieldsprice = new StringBuilder("");

                    //fields.append("Name: ").append(doc.get("Name"));

                    //fields.append("\nEmail: ").append(doc.get("Email"));

                    fieldsprice.append("\nPhone: ").append(doc.get("Prix"));

                    if (doc.get("Prix")!=null) {
                        String num = doc.get("Prix").toString();
                        SaveSharePrefAdmin(num);
                        //whiteUserNumer(txtUser.getText().toString(),txtUserPhone.getText().toString());
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        user.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    StringBuilder fields = new StringBuilder("");

                    //fields.append("Name: ").append(doc.get("Name"));

                    //fields.append("\nEmail: ").append(doc.get("Email"));

                    fields.append("\nPhone: ").append(doc.get("number"));

                    if (doc.get("number")!=null) {
                        String num = doc.get("number").toString();
                        txtUserPhone.setText(num);
                        SaveSharePref();
                        //whiteUserNumer(txtUser.getText().toString(),txtUserPhone.getText().toString());
                    }
                    else {
                        showDialogAddNumber ();
                    }

                }

            }

        })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"NOTICES Numer",Toast.LENGTH_SHORT).show();
                    }

                });

    }
    private void whiteUserNumer(String email,String userNumber) {
        String number = "number";

        Map<String, Object> newUserNumer = new HashMap<>();

        newUserNumer.put(number, userNumber);
        newUserNumer.put("achat20", "");
        newUserNumer.put("achat40", "");
        newUserNumer.put("achat50", "");
        newUserNumer.put("achat100", "");
        newUserNumer.put("achat200", "");
        newUserNumer.put("achat300", "");
        newUserNumer.put("achat400", "");
        newUserNumer.put("achat500", "");

        db.collection("User").document(email).set(newUserNumer)

                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override

                    public void onSuccess(Void aVoid) {

                        Toast.makeText(MainActivity.this, "Numéro enregistré...",

                                Toast.LENGTH_SHORT).show();

                    }

                })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "ERROR" + e.toString(),

                                Toast.LENGTH_SHORT).show();

                        Log.d("TAG", e.toString());

                    }

                });
    }


    public void showDialogAddNumber (){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
        //AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getApplicationContext(),);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        whiteUserNumer(txtUser.getText().toString(),userInputDialogEditText.getText().toString());
                        ReadSingleContact();
                       // Toast.makeText(MainActivity.this,"hey",Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnvendre: {
                if (ifNumberExist()){

                    //todo your sell
                }
                else {
                    showDialogAddNumber ();
                }
            } break;
            case R.id.btnachat:{
                if (ifNumberExist()){

                    //todo purchase
                }
                else {
                    showDialogAddNumber ();
                }
            }


        }

    }
    public boolean ifNumberExist(){
        Boolean ret=false;
        if ((txtUserPhone.getText().toString()!="")||(txtUserPhone.getText().toString()==null)){
            ret= false;
        }else {
            ret =true;
        }
        return ret;
    }
}

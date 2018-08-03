package coin.congobuy.com.Ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
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

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
    SharedPreferences sharedpreferencesFotTabView;
    public static final String mypreference = "CBCpref";
    public static final String Number = "numKey";
    public static final String Email = "emailKey";
    public Button btnAchetr, btnVendre;
    public LinearLayout Lvente;
    private Boolean firstTime = null;
    private Boolean dialogueForFistTime=false;
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
        spinner1 =findViewById(R.id.spinner);
        db = FirebaseFirestore.getInstance();

        //onclick achat et vente

        txtUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUserPhone.getText().length()<10){
                   // Toast.makeText(getApplicationContext(),"yuhuu 2",Toast.LENGTH_LONG).show();
                showDialogAddNumber();}
                else {
                    Toast.makeText(getApplicationContext(),"Vous avez déjà un numéro",Toast.LENGTH_SHORT).show();
                }
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
        getPrice ();//GetPrice
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUser.getText()==""){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }
                else{
                    fab.setImageResource(R.drawable.ic_lock);

                    //get firebase auth instance
                    auth = FirebaseAuth.getInstance();

                    //get current user
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    auth.signOut();
                    txtUser.setText("");
                    Snackbar.make(view, "Déconnecter", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //finish();
                    //startActivity(getIntent());
                    DeleteSharePref();


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
        txtMinToBuy.setText("20$");
        setTeam("X.XXX5...X Bitcoin", "0.001584btc/ 1$", "215");

        if (isFirstTime()){
            show_tapFab();

        }
    }
    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }
public void show_tapFab(){
    showTapTarget("Idenfication du Numéro","Avant tout achat, Vérifier votre numéro est enregisté. Si non Cliquez ici .....",R.id.fab);
}

public void showTapTarget(String title, String Descriptif,int ID){

    TapTargetView.showFor(this,                 // `this` is an Activity
//            TapTarget.forView(findViewById(R.id.fab), "Idenfication du Numéro", "Avant tout achat \\n Vérifier votre numéro est enregisté.\\n Si non Cliquez ici .....")
            TapTarget.forView(findViewById(ID), title, Descriptif)
                    // All options below are optional
                    .outerCircleColor(R.color.white)      // Specify a color for the outer circle
                    .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                    .targetCircleColor(R.color.yellow)   // Specify a color for the target circle
                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.orange)      // Specify the color of the title text
                    .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                    .descriptionTextColor(R.color.whitedivider)  // Specify the color of the description text
                    .textColor(R.color.blue)            // Specify a color for both the title and description text
                    .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                    .dimColor(R.color.colorPrimary)            // If set, will dim behind the view with 30% opacity of the given color
                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                    .tintTarget(true)                   // Whether to tint the target view's color
                    .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                    .icon(getResources().getDrawable(R.drawable.ic_lock))                     // Specify a custom drawable to draw as the target
                    .targetRadius(60),                  // Specify the target radius (in dp)
            new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                @Override
                public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);      // This call is optional
                    //doSomething();
                    show_four();

                }
            });
}

    public void show_four(){
       // showTapTarget("Idenfication du Numéro","Avant tout achat \n Vérifier votre numéro est enregisté.\n Si non Cliquez ici .....",R.id.txt_user_phone);
        TapTargetView.showFor(this,                 // `this` is an Activity
//            TapTarget.forView(findViewById(R.id.fab), "Idenfication du Numéro", "Avant tout achat \\n Vérifier votre numéro est enregisté.\\n Si non Cliquez ici .....")
                TapTarget.forView(findViewById(R.id.txt_user_phone), "Idenfication du Numéro", "Avant tout achat, Vérifier votre numéro est enregisté. Si non Cliquez ici .....")
                        // All options below are optional
                        .outerCircleColor(R.color.white)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.yellow)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.orange)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.whitedivider)  // Specify the color of the description text
                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPrimary)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(getResources().getDrawable(R.drawable.ic_add_number_missed))                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        //doSomething();
                    }
                });

        //        new GuideView.Builder(this)
//                .setTitle("Idenfication du Numéro")
//                .setContentText("Avant tout achat \n Vérifier votre numéro est enregisté.\n Si non Cliquez ici .....")
//                .setGravity(GuideView.Gravity.auto) //optional
//                .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
//                .setTargetView(spinner1)
//                .setContentTextSize(12)//optional
//                .setTitleTextSize(14)//optional
//                .setGuideListener(new GuideView.GuideListener() {
//                    @Override
//                    public void onDismiss(View view) {
//                        //TODO ...
//                        // show_tree();
//                    }
//                })
//                .build()
//                .show();
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
            Intent intent_CommandePls = new Intent(this, MailUsActivity.class);
            startActivity(intent_CommandePls);
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
//        String urlGoolgePLayCBC ="http://www.google.com";
//            Intent urlGPlay = new Intent(Intent.ACTION_VIEW);
//            urlGPlay.setData(Uri.parse(urlGoolgePLayCBC));
//            startActivity(urlGPlay);
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "CONGO BUY COIN");
            share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

            startActivity(Intent.createChooser(share, "Share link!"));

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
    private  void getPrice (){
        DocumentReference price = db.collection("Admin").document("Sddqqzwyqg6SB5E57XIf");
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
                        //whiteUserNumer(txtUser.getText().toString(),txtUserPhone.getText().toString());
                        txtPrice.setText(num+" BTC/20$");
                        SaveSharePrefAdmin(num);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void ReadSingleContact() {

        DocumentReference user = db.collection("User").document(txtUser.getText().toString());
        DocumentReference price = db.collection("Admin").document("Sddqqzwyqg6SB5E57XIf");
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
                        txtPrice.setText(num+" BTC/20$");
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
                        //Toast.makeText(getApplicationContext(),"yuhuu 111",Toast.LENGTH_LONG).show();
                        SaveSharePref();
                        //whiteUserNumer(txtUser.getText().toString(),txtUserPhone.getText().toString());
                        if ((txtUserPhone.getText().toString().isEmpty()) && !(dialogueForFistTime)){

                            dialogueForFistTime=true;
                           // Toast.makeText(getApplicationContext(),"yuhuu 1",Toast.LENGTH_LONG).show();
                            showDialogAddNumber (); //todo---
                        }

                    }
                    else if (!(dialogueForFistTime)) {
                        showDialogAddNumber ();
                      //  Toast.makeText(getApplicationContext(),"yuhuu",Toast.LENGTH_LONG).show();
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
        Toast.makeText(getApplicationContext(),"Votre Numéro de téléphone SVP...",Toast.LENGTH_LONG).show();
        userInputDialogEditText.setHint("Entre votre Numéro");
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if(!(txtUserPhone.getText().toString().isEmpty())){
                            dialogBox.cancel();
                        }
                        if (userInputDialogEditText.getText().toString().trim().isEmpty()){
                            Toast.makeText(getApplicationContext(),"Numéro incorrecte, Cliquez sur \'My Phone number'",Toast.LENGTH_LONG).show();
                            showDialogAddNumber ();
                           // Toast.makeText(getApplicationContext(),"yuhuu 6",Toast.LENGTH_LONG).show();
                        }else if(userInputDialogEditText.getText().toString().trim().length()<10){
                            Toast.makeText(getApplicationContext(),"Numéro incorrecte, Cliquez sur \'My Phone number'",Toast.LENGTH_LONG).show();
                            showDialogAddNumber ();
                           // Toast.makeText(getApplicationContext(),"yuhuu 5",Toast.LENGTH_LONG).show();
                        }
                        else {
                            // ToDo get user input here
                            whiteUserNumer(txtUser.getText().toString(), userInputDialogEditText.getText().toString());
                            ReadSingleContact();
                            // Toast.makeText(MainActivity.this,"hey",Toast.LENGTH_SHORT).show();
                        }
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
                   // Toast.makeText(getApplicationContext(),"yuhuu 4",Toast.LENGTH_LONG).show();
                    showDialogAddNumber ();
                }
            } break;
            case R.id.btnachat:{
                if (ifNumberExist()){

                    //todo purchase
                }
                else {
                    //Toast.makeText(getApplicationContext(),"yuhuu 3",Toast.LENGTH_LONG).show();
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

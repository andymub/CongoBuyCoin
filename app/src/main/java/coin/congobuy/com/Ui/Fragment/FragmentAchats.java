package coin.congobuy.com.Ui.Fragment;

/**
 * Created by Andymub on 04/07/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import coin.congobuy.com.R;
import coin.congobuy.com.Ui.MainActivity;

public class FragmentAchats extends Fragment implements View.OnClickListener {
    private String title;
    private int image;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "CBCpref";
    public static final String Number = "numKey";
    public static final String Email = "emailKey";
    public Button btnAchetr, btnVendre;
    public EditText editText_price;
    public Spinner Achatspinner;
    public FirebaseFirestore db;
    public String pricebit,bitcoinvalue;
    int existe = 0;


    public static FragmentAchats newInstance(String title, int resImage) {
        FragmentAchats fragment = new FragmentAchats();
        Bundle args = new Bundle();
        args.putInt("image", resImage);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getArguments().getInt("image", 0);
        title = getArguments().getString("title");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        sharedpreferences = this.getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_achat, container, false);
         final TextView tvLabel = (TextView) view.findViewById(R.id.txtMain);
        tvLabel.setText(title);
        db = FirebaseFirestore.getInstance();
        Button btnAchat= view.findViewById(R.id.btnachat);
        Achatspinner=view.findViewById(R.id.spinner);
        editText_price=view.findViewById(R.id.editText_price);
        editText_price.setText(sharedpreferences.getString("Prix", "0.0025"));
        Achatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String v=sharedpreferences.getString("Prix", "0.0025");
                Double value = Double.parseDouble(v);

                if (position==1){//40

                    value=(value*2);
                    editText_price.setText(value.toString());
                    pricebit="achat40";
                    bitcoinvalue=value.toString();

                }
               else if (position==0){//20
                   value=(value*1);
                    editText_price.setText(value.toString());
                    pricebit="achat20";
                    bitcoinvalue=value.toString();

                }
                else if (position==2){//50
                    value=(value*2.5);
                    editText_price.setText(value.toString());
                    pricebit="achat50";
                    bitcoinvalue=value.toString();

                }
                else if (position==3){//100
                    value=(value*5);
                    editText_price.setText(value.toString());
                    pricebit="achat100";
                    bitcoinvalue=value.toString();

                }else if (position==4){//200
                    value=(value*10);
                    editText_price.setText(value.toString());
                    pricebit="achat200";
                    bitcoinvalue=value.toString();

                }else if (position==5){//300
                    value=(value*15);
                    editText_price.setText(value.toString());
                    pricebit="achat300";
                    bitcoinvalue=value.toString();

                }else if (position==6){//400
                    value=(value*20);
                    editText_price.setText(value.toString());
                    pricebit="achat400";
                    bitcoinvalue=value.toString();

                }
                else if (position==7){//500
                    value=(value*25);
                    editText_price.setText(value.toString());
                    pricebit="achat500";
                    bitcoinvalue=value.toString();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // this = your fragment

        btnAchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user="",phone="";
                String [] valeurAchat ={pricebit,bitcoinvalue};

                if (sharedpreferences.contains(Email)) {
                    user=(sharedpreferences.getString(Email, "MyEmail"));

                } if (sharedpreferences.contains(Number)) {
                    phone=(sharedpreferences.getString(Number, "000"));
                }

                if ((sharedpreferences.getString(Number, ""))!=null ||(sharedpreferences.getString(Number, "")!=""))
                {
                    addNewCommande(user,phone,valeurAchat);
                }

               // Toast.makeText(getContext(),"" +simpleV,Toast.LENGTH_SHORT).show();


            }
        });


        ImageView imageView = (ImageView) view.findViewById(R.id.imgMain);
        imageView.setImageResource(image);


        return view;

    }


    @Override
    public void onClick(View v) {

    }
    private void addNewCommande(String user,String number,String[] valeur) {
        if ((ReadSingleUserPricePackage(user,valeur[0]))==1){
            Toast.makeText(getContext(), "Veuillez valider conclure la commande précédente",

                    Toast.LENGTH_SHORT).show();


        }else {
            UpdateData(user,valeur);
        }

            Map<String, Object> newPurshase = new HashMap<>();

            newPurshase.put("Etat", "En cours de validation");


            db.collection("Achats").document(user+"--"+number+"--"+valeur[0]).set(newPurshase)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override

                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getContext(), "Votre commande est en Cours de validation...",

                                    Toast.LENGTH_SHORT).show();

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getContext(), "ERROR" + e.toString(),

                                    Toast.LENGTH_SHORT).show();

                            Log.d("TAG", e.toString());

                        }

                    });

    }

    private int ReadSingleUserPricePackage(String userEmail, final String valeur) {
        final String[] data = {null};

        DocumentReference user = db.collection("User").document(userEmail);

        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {

            @Override

            public void onComplete(@NonNull Task < DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");
                    //fields.append("\nPhone: ").append(doc.get(valeur));
                    //data[0] =doc.get(valeur).toString();
                    if (doc.get(valeur).toString()==null){
                        existe=1;
                    }

                }

            }

        }).addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        Log.d("Hey",e.getMessage());

                    }

                });
        return existe;

    }
    private void UpdateData(String user,String[] valeur) {

        DocumentReference contact = db.collection("User").document(user);
        contact.update(valeur[0], valeur[1])

                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override

                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getContext(), "Updated Successfully",

                                Toast.LENGTH_SHORT).show();

                    }

                });
    }
}
package coin.congobuy.com.Ui.Fragment;

import coin.congobuy.com.R;

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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentListCommandes extends Fragment {
    private String title;
    private int imageMain;
    public FirebaseFirestore db;
    private int imageSecondary;
    public ListView list = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "CBCpref";
    public static final String Number = "numKey";
    public static final String Email = "emailKey";
    private String[] mNames = { "Fabian", "Carlos", "Alex", "Andrea", "Karla",
            "Freddy", "Lazaro", "Hector", "Carolina", "Edwin", "Jhon",
            "Edelmira", "Andres" };
    public final List<String> titre = new ArrayList<String>();

    private String[] mAnimals = { "Perro", "Gato", "Oveja", "Elefante", "Pez",
            "Nicuro", "Bocachico", "Chucha", "Curie", "Raton", "Aguila",
            "Leon", "Jirafa" };
    public int iFor=0;

    int[] flags = new int[]{
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping,
            R.drawable.ic_shopping
    };
    public ImageButton btnSync;


    public static FragmentListCommandes newInstance(String title, int resMainImage, int resSecondaryImage) {
        FragmentListCommandes fragment = new FragmentListCommandes();
        Bundle args = new Bundle();
        args.putInt("imageMain", resMainImage);
        args.putInt("imageSecondary", resSecondaryImage);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageMain = getArguments().getInt("imageMain", 0);
        imageSecondary = getArguments().getInt("imageSecondary", 0);
        title = getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        sharedpreferences = this.getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_liste_commande, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.txtMain);
        tvLabel.setText(title);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgMain);
        imageView.setImageResource(imageMain);

        ///ImageView imageViewSecondary = (ImageView) view.findViewById(R.id.imgSecondary);
       // imageViewSecondary.setImageResource(imageSecondary);
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

//        for(int i=0;i<10;i++){
//            HashMap<String, String> hm = new HashMap<String,String>();
//            hm.put("txt", "App Name : " + mNames[i]);
//            hm.put("cur","creator : " + mAnimals[i]);
//            hm.put("flag", Integer.toString(flags[i]) );
//            aList.add(hm);}
//        String[] from = { "flag","txt","cur" };
//
//        int[] to = { R.id.flag,R.id.txt,R.id.cur,R.id.textView2};

        //View v = inflater.inflate(R.layout.fragment_top_rated, container,false);
        list = (ListView)view.findViewById(R.id.listView1);
        list.setVisibility(View.INVISIBLE);
//        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
//        list.setAdapter(adapter);
        btnSync= view.findViewById(R.id.btnlistSync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadAllCommande();
                list.setVisibility(View.VISIBLE);
            }
        });

            return view;
    }
    private void ReadAllCommande() {
        String userEmail="",phone="";
        if (sharedpreferences.contains(Email)) {
            userEmail=(sharedpreferences.getString(Email, "MyEmail"));

        } if (sharedpreferences.contains(Number)) {
            phone=(sharedpreferences.getString(Number, "000"));
        }
        final String []value={"achat20","achat40","achat50","achat100","achat200","achat300","achat400","achat500"} ;

        if ((userEmail!=null) ||(phone!=""))
        {
            int i=0;
            String path=userEmail+"--"+phone+"--"+value[i];
            for(i=0;i<8;i++){
                iFor=i;
                // Toast.makeText(getContext(),"" +simpleV,Toast.LENGTH_SHORT).show();


                if(path=="ch@gmail.com--0998623313--achat20"){
                    int u=0;
                }
                    DocumentReference user = db.collection("Achats").document(path);
//                if (user.get().isSuccessful()) {
                    int t;
                    // do something with the data
                    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                        @Override

                        public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                            if (task.isSuccessful()) {

                                DocumentSnapshot doc = task.getResult();

//                            StringBuilder fields = new StringBuilder("");
//
//                            fields.append("Name: ").append(doc.get("Name"));
//
//                            fields.append("\nEmail: ").append(doc.get("Email"));
//
//                            fields.append("\nPhone: ").append(doc.get("Phone"));

                                //titre.add(doc.get("Etat").toString());
                                mNames[iFor]=value[iFor];
                                mAnimals[iFor]=doc.get("Etat").toString();
                                Toast.makeText(getContext(),"--"+doc.get("Etat").toString(),Toast.LENGTH_SHORT).show();
                                titre.add(doc.get("Etat").toString());


                            }

                        }

                    })

                            .addOnFailureListener(new OnFailureListener() {

                                @Override

                                public void onFailure(@NonNull Exception e) {

                                }

                            });

//                } else {
//                    Toast.makeText(getContext(),"no data",Toast.LENGTH_SHORT).show();
//                }

            };

        }

        // imageViewSecondary.setImageResource(imageSecondary);
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<10;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", "App Name : " + mNames[i]);
            hm.put("cur","creator : " + mAnimals[i]);
            hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);}
        String[] from = { "flag","txt","cur" };

        int[] to = { R.id.flag,R.id.txt,R.id.cur,R.id.textView2};
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }
}
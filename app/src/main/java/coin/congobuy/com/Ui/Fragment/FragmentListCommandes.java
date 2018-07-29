package coin.congobuy.com.Ui.Fragment;

import coin.congobuy.com.PhoneState.AppStatus;
import coin.congobuy.com.R;

/**
 * Created by Andymub on 04/07/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.ContentValues.TAG;

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
    private String[]mNames2 =new String[7];
    public final List<String> titreContenu = new ArrayList<String>();
    public final List<String> mTitre = new ArrayList<String>();

    public ImageButton btnSync;
    public SimpleAdapter adapter;
    public TextView txt01,txt02,txt11,txt12,txt21,txt22,txt31,txt32,txt41,txt42,txt51,txt52,txt61,txt62,txt71,txt72;
    public ImageView flag0,flag1,flag2,flag3,flag4,flag5,flag6,flag7;


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
        txt01=view.findViewById(R.id.txtV01);
        txt02=view.findViewById(R.id.txtV02);
        flag0=view.findViewById(R.id.flag0);

        txt11=view.findViewById(R.id.txtV11);
        txt12=view.findViewById(R.id.txtV12);
        flag1=view.findViewById(R.id.flag1);

        txt21=view.findViewById(R.id.txtV21);
        txt22=view.findViewById(R.id.txtV22);
        flag2=view.findViewById(R.id.flag2);

        txt31=view.findViewById(R.id.txtV31);
        txt32=view.findViewById(R.id.txtV32);
        flag3=view.findViewById(R.id.flag3);

        txt41=view.findViewById(R.id.txtV41);
        txt42=view.findViewById(R.id.txtV42);
        flag4=view.findViewById(R.id.flag4);

        txt51=view.findViewById(R.id.txtV51);
        txt52=view.findViewById(R.id.txtV52);
        flag5=view.findViewById(R.id.flag5);

        txt61=view.findViewById(R.id.txtV61);
        txt62=view.findViewById(R.id.txtV62);
        flag6=view.findViewById(R.id.flag6);

        txt71=view.findViewById(R.id.txtV71);
        txt72=view.findViewById(R.id.txtV72);
        flag7=view.findViewById(R.id.flag7);
        initiateTxt();
        tvLabel.setText(title);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgMain);
        imageView.setImageResource(imageMain);

        ///ImageView imageViewSecondary = (ImageView) view.findViewById(R.id.imgSecondary);
        // imageViewSecondary.setImageResource(imageSecondary);

        btnSync = view.findViewById(R.id.btnlistSync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (AppStatus.getInstance(getContext()).isOnline()) {
                    /**
                     * Internet is available, Toast It!
                     */
                    //ReadAllCommande();
//                LoadFeedData loadFeedData=new LoadFeedData();
//                loadFeedData.execute();
                    DownloadXmlTask downloadXmlTask=new DownloadXmlTask();
                    downloadXmlTask.execute();
                    if (sharedpreferences.contains(Number))
                    {}
                    else
                    {Toast.makeText(getContext(),"Identifiez-vous avant tout Achat" ,Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getContext(), "WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * Internet is NOT available, Toast It!
                     */
                    Toast.makeText(getContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();

                }


            }
        });

        return view;
    }

    private void ReadAllCommande() {
        String userEmail = "", phone = "";
        if (sharedpreferences.contains(Email)) {
            userEmail = (sharedpreferences.getString(Email, "MyEmail"));

        }
        if (sharedpreferences.contains(Number)) {
            phone = (sharedpreferences.getString(Number, "000"));
        }
        //Toast.makeText(getContext(),"Identifiez-vous avant tout Achat" ,Toast.LENGTH_SHORT).show();
        final String[] value = {"achat20", "achat40", "achat50", "achat100", "achat200", "achat300", "achat400", "achat500"};

        if ((userEmail != null) && (phone != "")) {
            int i = 0;

            for (i = 0; i <8; i++) {
                final String path = userEmail + "--" + phone + "--" + value[i];


                DocumentReference user = db.collection("Achats").document(path);
//                if (user.get().isSuccessful()) { Toast.makeText(getContext(), "-Cest bon-".toString(), Toast.LENGTH_SHORT).show();
// }
//                int t;
                // do something with the data
                final int I = i;
                user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                    @Override

                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            // Toast.makeText(getContext(), "-Cest bon-".toString(), Toast.LENGTH_SHORT).show();

                        if (task.isSuccessful()) {

                            DocumentSnapshot doc = task.getResult();
                            //String ee= task.getResult().getData().toString();
                            titreContenu.add(doc.get("Etat").toString());
                            Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                            Object data=doc.getData().get(path);
                            mTitre.add(path);
                           // Toast.makeText(getContext(), "--" + doc.get("Etat").toString(), Toast.LENGTH_SHORT).show();
//                            mNames2[iFor]=doc.get("Etat").toString();
                            if(flag0.getVisibility()==View.INVISIBLE){
                                txt01.setText(GetCommandePrice(path));
                                txt01.setTextColor(getResources().getColor(R.color.white));
                                txt02.setText(doc.get("Etat").toString());
                                flag0.setVisibility(View.VISIBLE);

                            }else if (flag1.getVisibility()==View.INVISIBLE)
                            {
                                txt11.setText(GetCommandePrice(path));
                                txt12.setText(doc.get("Etat").toString());
                                flag1.setVisibility(View.VISIBLE);

                            }else if (flag2.getVisibility()==View.INVISIBLE)
                            {
                                txt21.setText(GetCommandePrice(path));
                                txt22.setText(doc.get("Etat").toString());
                                flag2.setVisibility(View.VISIBLE);

                            }else if (flag3.getVisibility()==View.INVISIBLE)
                            {
                                txt31.setText(GetCommandePrice(path));
                                txt32.setText(doc.get("Etat").toString());
                                flag3.setVisibility(View.VISIBLE);

                            }else if (flag4.getVisibility()==View.INVISIBLE)
                            {
                                txt41.setText(GetCommandePrice(path));
                                txt42.setText(doc.get("Etat").toString());
                                flag4.setVisibility(View.VISIBLE);

                            }else if (flag5.getVisibility()==View.INVISIBLE)
                            {
                                txt51.setText(GetCommandePrice(path));
                                txt52.setText(doc.get("Etat").toString());
                                flag5.setVisibility(View.VISIBLE);

                            }else if (flag6.getVisibility()==View.INVISIBLE)
                            {
                                txt61.setText(GetCommandePrice(path));
                                txt62.setText(doc.get("Etat").toString());
                                flag6.setVisibility(View.VISIBLE);

                            }else if (flag7.getVisibility()==View.INVISIBLE)
                            {
                                txt71.setText(GetCommandePrice(path));
                                txt72.setText(doc.get("Etat").toString());
                                flag7.setVisibility(View.VISIBLE);

                            }

                           // txt01.setText(path);
//                            txt01.setText(GetCommandePrice(mTitre.get(0)));
//                            txt02.setText(titreContenu.get(0));
//                            if(mTitre.size())
//                            {}
//                            else
//                            setTextINtxtview(GetCommandePrice(mTitre.get(I)),titreContenu.get(I));





                        }
                    } else {



//                            if((flag7.getVisibility()==View.INVISIBLE)||flag6.getVisibility()==View.INVISIBLE||flag5.getVisibility()==View.INVISIBLE||
//                                    flag4.getVisibility()==View.INVISIBLE||flag3.getVisibility()==View.INVISIBLE||flag2.getVisibility()==View.INVISIBLE||
//                                    flag1.getVisibility()==View.INVISIBLE||flag0.getVisibility()==View.INVISIBLE)
//                            {txt01.setText("Auccune commande en cours");
//                            txt01.setTextColor(getResources().getColor(R.color.bg_login));
//                            txt02.setText("...");
//                            flag0.setVisibility(View.VISIBLE);}
                }

                    }


                })

                        .addOnFailureListener(new OnFailureListener() {

                            @Override

                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Aucune commande en cours...",Toast.LENGTH_SHORT).show();
                                txt01.setText("Auccune commande en cours");
                            txt01.setTextColor(getResources().getColor(R.color.yellow));
                            txt02.setText("...");
                            flag0.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_shopping));
                            flag0.setVisibility(View.VISIBLE);

                            }

                        });
            }



        }
        else{

        }

//        // imageViewSecondary.setImageResource(imageSecondary);
//        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//        for (int i = 0; i < 10; i++) {
//            HashMap<String, String> hm = new HashMap<String, String>();
//            hm.put("txt", "App Name : " + mNames[i]);
//            hm.put("cur", "creator : " + mAnimals[i]);
//            hm.put("flag", Integer.toString(flags[i]));
//            aList.add(hm);
//        }
//        String[] from = {"flag", "txt", "cur"};
//
//        int[] to = {R.id.flag, R.id.txt, R.id.cur, R.id.textView2};
//        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
//        //adapter.notifyDataSetChanged();
//        list.setAdapter(adapter);
    }

    public String GetCommandePrice(String dataPrince) {
        String[] splitedData =dataPrince.split("--");
        String s= splitedData[2].replace("achat","");
        int f= Integer.parseInt(s);
        String price="";
        if(f==20){
            price="20$";
        }else if(f==40){
            price="40$";
        }else if(f==50){
            price="50$";
        }else if(f==100){
            price="100$";
        }else if(f==200){
            price="200$";
        }else if(f==300){
            price="300$";
        }else if(f==400){
            price="400$";
        }else if(f==500){
            price="500$";
        }
        return price;
    }

    public void setTextINtxtview (String titre, String contenu){
       if (txt01.getText().equals("")){
            txt01.setText(titre);
            txt02.setText(contenu);
            flag0.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"on est la",Toast.LENGTH_LONG).show();
            return;

        }else if (txt11.getText().equals("")){
            txt11.setText(titre);
            txt12.setText(contenu);
            flag1.setVisibility(View.VISIBLE);
            return;

        }else if (txt21.getText().equals("")){
            txt21.setText(titre);
            txt22.setText(contenu);
            flag2.setVisibility(View.VISIBLE);
            return;

        }else if (txt31.getText().equals("")){
            txt31.setText(titre);
            txt32.setText(contenu);
            flag3.setVisibility(View.VISIBLE);
            return;

        }else if (txt41.getText().equals("")){
            txt41.setText(titre);
            txt42.setText(contenu);
            flag4.setVisibility(View.VISIBLE);
            return;

        }else if (txt51.getText().equals("")){
            txt51.setText(titre);
            txt52.setText(contenu);
            flag5.setVisibility(View.VISIBLE);
            return;

        }else if (txt61.getText().equals("")){
            txt61.setText(titre);
            txt62.setText(contenu);
            flag6.setVisibility(View.VISIBLE);
            return;

        }else if (txt71.getText().equals("")){
            txt71.setText(titre);
            txt72.setText(contenu);
            flag7.setVisibility(View.VISIBLE);
            return;

        }




    }

    public void initiateTxt(){

        txt01.setText("");
        txt02.setText("");
        flag0.setVisibility(View.INVISIBLE);
        txt11.setText("");
        txt12.setText("");
        flag1.setVisibility(View.INVISIBLE);
        txt21.setText("");
        txt22.setText("");
        flag2.setVisibility(View.INVISIBLE);
        txt31.setText("");
        txt32.setText("");
        flag3.setVisibility(View.INVISIBLE);
        txt41.setText("");
        txt42.setText("");
        flag4.setVisibility(View.INVISIBLE);
        txt51.setText("");
        txt52.setText("");
        flag5.setVisibility(View.INVISIBLE);
        txt61.setText("");
        txt62.setText("");
        flag6.setVisibility(View.INVISIBLE);
        txt71.setText("");
        txt72.setText("");
        flag7.setVisibility(View.INVISIBLE);


    }
    private class DownloadXmlTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
   //         ReadAllCommande();
//            Toast.makeText(getContext(),"onPreExecute",Toast.LENGTH_SHORT).show();
//            //do something
//            // imageViewSecondary.setImageResource(imageSecondary);
//            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//            for (int i = 0; i < 10; i++) {
//                HashMap<String, String> hm = new HashMap<String, String>();
//                hm.put("txt", "App Name : " + mNames[i]);
//                hm.put("cur", "creator : " + mAnimals[i]);
//                hm.put("flag", Integer.toString(flags[i]));
//                aList.add(hm);
//            }
//            String[] from = {"flag", "txt", "cur"};
//
//            int[] to = {R.id.flag, R.id.txt, R.id.cur, R.id.textView2};
//            SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
//            //adapter.notifyDataSetChanged();
//            list.setAdapter(adapter);
//            list.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... esult) {
            ReadAllCommande();
            for (int i=0; i<mTitre.size(); i++){
                //setTextINtxtview(GetCommandePrice(mTitre.get(i)),titreContenu.get(i));
                int t=0;
            }
           // ReadAllCommande();
            return "here";
        }

        @Override
        protected void onPostExecute(String result) {
           // ReadAllCommande();
           // Toast.makeText(getContext(),"onPostExecute",Toast.LENGTH_SHORT).show();
            //do something

            //doing your download from internet

        }
    }
    public void after (){
//        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//
//        for (int i = 0; i < 10; i++) {
//            HashMap<String, String> hm = new HashMap<String, String>();
//            hm.put("txt", "App Name : " + mNames[i]);
//            hm.put("cur", "creator : " + mAnimals[i]);
//            hm.put("flag", Integer.toString(flags[i]));
//            aList.add(hm);
//        }
//        String[] from = {"flag", "txt", "cur"};
//        int[] to = {R.id.flag, R.id.txt, R.id.cur, R.id.textView2};
//        adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
//        //adapter.notifyDataSetChanged();
//        list.setAdapter(adapter);
//        list.setVisibility(View.VISIBLE);
    }
}
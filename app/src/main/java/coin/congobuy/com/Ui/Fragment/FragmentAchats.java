package coin.congobuy.com.Ui.Fragment;

/**
 * Created by Andymub on 04/07/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import coin.congobuy.com.R;
import coin.congobuy.com.Ui.MainActivity;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

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

                }
               else if (position==0){//20
                   value=(value*1);
                    editText_price.setText(value.toString());

                }
                else if (position==2){//50
                    value=(value*2.5);
                    editText_price.setText(value.toString());

                }
                else if (position==3){//100
                    value=(value*5);
                    editText_price.setText(value.toString());

                }else if (position==4){//200
                    value=(value*10);
                    editText_price.setText(value.toString());

                }else if (position==5){//300
                    value=(value*15);
                    editText_price.setText(value.toString());

                }else if (position==6){//400
                    value=(value*20);
                    editText_price.setText(value.toString());

                }
                else if (position==7){//400
                    value=(value*25);
                    editText_price.setText(value.toString());

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
                String simpleV="";


                if (sharedpreferences.contains(Email)) {
                    simpleV=(sharedpreferences.getString(Email, "MyEmail"));

                } if (sharedpreferences.contains(Number)) {
                    simpleV=simpleV+"_"+(sharedpreferences.getString(Number, "000"));
                }


                Toast.makeText(getContext(),"" +simpleV,Toast.LENGTH_SHORT).show();


            }
        });


        ImageView imageView = (ImageView) view.findViewById(R.id.imgMain);
        imageView.setImageResource(image);


        return view;

    }


    @Override
    public void onClick(View v) {

    }
}
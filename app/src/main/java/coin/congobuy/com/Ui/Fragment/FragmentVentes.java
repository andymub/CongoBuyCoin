package coin.congobuy.com.Ui.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import coin.congobuy.com.R;

/**
 * Created by Andymub on 04/07/2018.
 */

public class FragmentVentes extends Fragment {
    private String title;
    private int image;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "CBCpref";
    public static final String Number = "numKey";
    public static final String Email = "emailKey";
    public Button btnVendre;

    public static FragmentVentes newInstance(String title, int resImage) {
        FragmentVentes fragment = new FragmentVentes();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vente, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.txtMainv);
        tvLabel.setText(title);
        btnVendre= view.findViewById(R.id.btnvendre);
        // this = your fragment
        sharedpreferences = this.getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        btnVendre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleV="";


                if (sharedpreferences.contains(Email)) {
                    simpleV=(sharedpreferences.getString(Email, "MyEmail"));

                } if (sharedpreferences.contains(Number)) {
                    simpleV=simpleV+"/"+(sharedpreferences.getString(Number, "000"));
                }


                Toast.makeText(getContext(),"La Vente du Bitcoin sera disponible le 25 Octobre 2018",Toast.LENGTH_LONG).show();
            }
        });

        ImageView imageView = (ImageView) view.findViewById(R.id.imgMainv);
        imageView.setImageResource(image);
        return view;
    }
}
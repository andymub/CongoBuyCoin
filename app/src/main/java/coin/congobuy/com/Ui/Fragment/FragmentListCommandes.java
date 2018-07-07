package coin.congobuy.com.Ui.Fragment;

import coin.congobuy.com.R;

/**
 * Created by Andymub on 04/07/2018.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentListCommandes extends Fragment {
    private String title;
    private int imageMain;
    private int imageSecondary;


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
        View view = inflater.inflate(R.layout.fragment_liste_commande, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.txtMain);
        tvLabel.setText(title);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgMain);
        imageView.setImageResource(imageMain);

        ImageView imageViewSecondary = (ImageView) view.findViewById(R.id.imgSecondary);
        imageViewSecondary.setImageResource(imageSecondary);
        return view;
    }
}
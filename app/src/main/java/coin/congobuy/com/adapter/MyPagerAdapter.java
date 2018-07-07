package coin.congobuy.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coin.congobuy.com.R;
import coin.congobuy.com.Ui.Fragment.FragmentAchats;
import coin.congobuy.com.Ui.Fragment.FragmentListCommandes;
import coin.congobuy.com.Ui.Fragment.FragmentVentes;

/**
 * Created by Andymub on 04/07/2018.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages.
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for a particular page.
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentAchats.newInstance("Passer une commande",  R.drawable.ic_commande);
            case 1:
                return FragmentVentes.newInstance("Vendre sa Crypto", R.drawable.ic_sell);
            case 2:
                return FragmentListCommandes.newInstance("Mes commandes en cours", R.drawable.ic_commandes_encours, R.drawable.image_bg);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        String tab;
        if (position==0)
        {
         tab="ACHAT" ;
        }
        else if (position==1){
            tab="VENTE" ;
        }
        else {
            tab="MES COMMANDES" ;
        }
        return tab;
    }

}
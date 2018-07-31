package coin.congobuy.com.Ui.Notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import coin.congobuy.com.R;

public class ResultActivity extends AppCompatActivity {
    private TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String notificationMessage = intent.getStringExtra("notificationMessage");
        textView = findViewById(R.id.textView1);
        textView.setText(notificationMessage);
    }
}

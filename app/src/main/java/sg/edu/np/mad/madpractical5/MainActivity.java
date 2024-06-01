package sg.edu.np.mad.madpractical5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    MyDBHandler dbHandler;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHandler = new MyDBHandler(this, null, null, 1);
        int randomNumber = getIntent().getIntExtra("RANDOM_NUMBER", 0);

        // Initialize or retrieve a User object from the database
        String userName = "User" + randomNumber;
        user = dbHandler.findUser(userName);
        if (user == null) {
            user = new User(userName, "Description for " + userName, randomNumber, false);
            dbHandler.addUser(user);
        }

        // Get the TextViews and Buttons from the layout
        TextView tvName = findViewById(R.id.textView2);
        TextView tvDescription = findViewById(R.id.textView3);
        Button btnFollow = findViewById(R.id.button);
        Button btnMsg = findViewById(R.id.button2);

        // Set the TextViews with the User's name, description, and default button message
        tvName.setText(user.getName() + " " + randomNumber);
        tvDescription.setText(user.getDescription());
        btnFollow.setText(user.getFollowed() ? "Unfollow" : "Follow");

        btnFollow.setOnClickListener(view -> {
            String msg;
            if (!user.getFollowed()) {
                btnFollow.setText("Unfollow");
                msg = "Followed";
            } else {
                btnFollow.setText("Follow");
                msg = "Unfollowed";
            }
            user.setFollowed(!user.getFollowed());
            dbHandler.updateUserFollowStatus(user.getId(), user.getFollowed());

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }
}

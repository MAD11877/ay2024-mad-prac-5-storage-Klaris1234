package sg.edu.np.mad.madpractical5;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<User> userList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            int name = new Random().nextInt(100000);
            int description = new Random().nextInt(100000);
            boolean followed = new Random().nextBoolean();

            User user = new User("John Doe", "MAD Developer", 1, false);
            user.setName("Name" + name);
            user.setDescription("Description" + description);
            user.setFollowed(followed);
            userList.add(user);
        }

        UserAdapter adapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(adapter);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile");
        builder.setMessage("MADness");

        builder.setNegativeButton("CLOSE", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("VIEW", (dialog, which) -> {
            int randomNumber = generateRandomNumber();
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            intent.putExtra("RANDOM_NUMBER", randomNumber);
            startActivity(intent);
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100000);
    }
}



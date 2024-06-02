package sg.edu.np.mad.madpractical5;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private ArrayList<User> UserList;
    private ListActivity activity;

    private static final int VIEW_TYPE_REGULAR = 0;
    private static final int VIEW_TYPE_SPECIAL = 1;

    public UserAdapter(ArrayList<User> userList, ListActivity activity) {
        this.UserList = userList;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        User user = UserList.get(position);
        if (user.getName().endsWith("7")) {
            return VIEW_TYPE_SPECIAL;
        } else {
            return VIEW_TYPE_REGULAR;
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_activity_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = UserList.get(position);

        holder.name.setText(user.getName());
        holder.description.setText(user.getDescription());

        if (getItemViewType(position) == VIEW_TYPE_SPECIAL) {
            holder.largeImage.setVisibility(View.VISIBLE);
        } else {
            holder.largeImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Profile")
                        .setMessage(user.getName())
                        .setCancelable(true)
                        .setPositiveButton("View", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Random random = new Random();
                                int randomNum = random.nextInt(999999);
                                Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                                mainActivityIntent.putExtra("RANDOM_NUMBER", randomNum);
                                mainActivityIntent.putExtra("name", user.getName());
                                mainActivityIntent.putExtra("description", user.getDescription());
                                mainActivityIntent.putExtra("followed", user.getFollowed());
                                mainActivityIntent.putExtra("id", user.getId());
                                v.getContext().startActivity(mainActivityIntent);
                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }
}

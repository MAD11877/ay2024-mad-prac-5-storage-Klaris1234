package sg.edu.np.mad.madpractical5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Random;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_FOLLOWED = "followed";



    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_FOLLOWED + " INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert 20 random users
        for (int i = 0; i < 20; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, "User" + (i + 1));
            values.put(COLUMN_DESCRIPTION, "Description for User" + (i + 1));
            values.put(COLUMN_FOLLOWED, new Random().nextBoolean() ? 1 : 0); // 1 for true, 0 for false

            db.insert(TABLE_USERS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to add a new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_DESCRIPTION, user.getDescription());
        values.put(COLUMN_FOLLOWED, user.getFollowed() ? 1 : 0);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Method to find a user by name
    public User findUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setDescription(cursor.getString(2));
            user.setFollowed(cursor.getInt(3) == 1);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Method to delete a user by name
    public boolean deleteUser(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            db.delete(TABLE_USERS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    // Method to get all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Method to update user follow status
    public void updateUserFollowStatus(int id, boolean followed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLLOWED, followed ? 1 : 0);

        db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

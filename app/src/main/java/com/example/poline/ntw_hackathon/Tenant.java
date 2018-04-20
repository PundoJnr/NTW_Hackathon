package com.example.poline.ntw_hackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Tenant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);

        /*public static class Post {

  public String author;
  public String title;

  public Post(String author, String title) {
    // ...
  }

}

// Get a reference to our posts
final FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

// Attach a listener to read the data at our posts reference
ref.addValueEventListener(new ValueEventListener() {
  @Override
  public void onDataChange(DataSnapshot dataSnapshot) {
    Post post = dataSnapshot.getValue(Post.class);
    System.out.println(post);
  }

  @Override
  public void onCancelled(DatabaseError databaseError) {
    System.out.println("The read failed: " + databaseError.getCode());
  }
});*/

        //firebasetutorials
        /*myRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        String value = dataSnapshot.getValue(String.class);
        Log.d(TAG, "Value is: " + value);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
    }
});*/
    }
}

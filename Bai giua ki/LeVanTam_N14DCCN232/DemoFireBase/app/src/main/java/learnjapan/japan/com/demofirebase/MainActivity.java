package learnjapan.japan.com.demofirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<String> list;
    private TextView txt;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Ghi Data
        Item item = new Item();
        item.setAmHan("AAA 3");
        item.setCachDocHira("BBB");
        item.setNghiaTiengViet("CCC");
        item.setTuTiengNhat("DDD");
        mDatabase.child("Moji").child("Soumatome").child("Soumatome Bai 2").push().setValue(item);


        list = new ArrayList<>();
        txt = (TextView) findViewById(R.id.txt);
        sb = new StringBuilder();


        mDatabase.child("Moji").child("Soumatome").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getKey());
                txt.append(dataSnapshot.getKey().toString() + "\n");
                sb.append(dataSnapshot.getKey() + "\n");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("TutorialList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getKey());
                txt.append(dataSnapshot.getKey().toString() + "\n");
                sb.append(dataSnapshot.getKey() + "\n");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Doc Data
        /*mDatabase.child("Moji").child("Soumatome").child("Soumatome Bai 1").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item i = dataSnapshot.getValue(Item.class);
                sb.append(i.toString() + "\n");
                list.add(i.toString());
                txt.append(i.toString() + "\n");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();



    }
}

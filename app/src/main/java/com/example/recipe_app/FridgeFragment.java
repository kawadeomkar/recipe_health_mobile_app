package com.example.recipe_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FridgeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText itemET;
    private Button btn;
    private Button saveBtn;
    private ListView itemsList;
    private DocumentReference docRef;
    private View view;
    private String email;
    private List<String> ingredients;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    final String TAG = "FridgeFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        view = inflater.inflate(R.layout.fragment_fridge, container, false);

        Bundle bundle = this.getArguments();
        email = bundle.getString("email");
        itemET = view.findViewById(R.id.item_edit_text);
        btn = view.findViewById(R.id.add_btn);
        saveBtn = view.findViewById(R.id.save_btn);
        itemsList = view.findViewById(R.id.ing_list);

        //ingredients = FileHelper.readData(getActivity());

        loadIngredients();

        btn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        itemsList.setOnItemClickListener(this);

        return view;
    }

    // load ingredients from firebase given email
    public void loadIngredients()
    {
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("users").document(email)
                .collection("activities").document("ingredients");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ingredients = (List<String>)(document.getData().get("ingredients"));
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                                ingredients);
                        itemsList.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // add button
            case R.id.add_btn:
                String itemEntered = itemET.getText().toString();
                adapter.add(itemEntered);
                itemET.setText("");
                Toast.makeText(getActivity(), "Ingredient Added", Toast.LENGTH_SHORT).show();
                break;
            // save button
            case R.id.save_btn:
                Map<String, Object> ingMap = new HashMap<>();

                ingMap.put("ingredients", ingredients);
                db.collection("users").document(email).collection("activities")
                        .document("ingredients").set(ingMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(), "Ingredient Saved", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ingredients.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Ingredient Deleted", Toast.LENGTH_SHORT).show();
    }

}

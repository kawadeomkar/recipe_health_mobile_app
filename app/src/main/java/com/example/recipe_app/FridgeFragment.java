package com.example.recipe_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class FridgeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText itemET;
    private Button btn;
    private Button saveBtn;
    private ListView itemsList;
    private DocumentReference docRef;
    private View view;

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    final String TAG = "FridgeFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        view = inflater.inflate(R.layout.fragment_fridge, container, false);

        itemET = view.findViewById(R.id.item_edit_text);
        btn = view.findViewById(R.id.add_btn);
        saveBtn = view.findViewById(R.id.save_btn);

        itemsList = view.findViewById(R.id.ing_list);

        items = FileHelper.readData(getActivity());

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);

        btn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


        itemsList.setOnItemClickListener(this);


        loadIngredients();
        return view;
    }

    public void loadIngredients()
    {
        String email = "";
        Bundle bundle = this.getArguments();

        db = FirebaseFirestore.getInstance();
        docRef = db.collection("users").document(email)
                .collection("activities").document("ingredients");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("here");
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        System.out.println("test");
                        System.out.println(document.getData().get("ingredients"));
//                        itemsList = (ListView)(document.getData().get("ingredients"));
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
            case R.id.add_btn:
                System.out.println("2");
                String itemEntered = itemET.getText().toString();
                adapter.add(itemEntered);
                itemET.setText("");
                FileHelper.writeData(items, getActivity());
                Toast.makeText(getActivity(), "Ingredient Added", Toast.LENGTH_SHORT).show();
                break;

            case R.id.save_btn:
                String email = "";
                Bundle bundle = this.getArguments();
                email = bundle.getString("email");
                Map<String, ArrayList<String>> ingMap = new HashMap<>();



                ingMap.put("ingredients", items);

                db.collection("users").document(email).collection("activities").document("ingredients").set(ingMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(), "Ingredient Saved", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    {
                                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        FileHelper.writeData(items, getActivity());
        Toast.makeText(getActivity(), "Ingredient Deleted", Toast.LENGTH_SHORT).show();
    }

}

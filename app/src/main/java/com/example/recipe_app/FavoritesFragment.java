package com.example.recipe_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FavoritesFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private String email;
    private List<Favorite> favoriteList;
    private final String TAG = "FAVORITES_FRAGMENT";
    private FavoriteComplexAdapter favoriteComplexAdapter;
    private ListView favoriteListView;
    private Button saveButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        // get bundle arguments
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email = bundle.getString("email");
        } else {
            Log.d(TAG, "BUNDLE DOES NOT EXIST");
        }

        // instantiate firestore and docref
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("users").document(email)
                .collection("activities").document("favorite_recipes");

        favoriteListView = (ListView) view.findViewById(R.id.lv_fav_list);
        saveButton = (Button) view.findViewById(R.id.btn_fav_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFavorites();
            }
        });

        retrieveFavorites();

        return view;
    }

    // save deleted favorites to db
    private void saveFavorites() {
        Map<String, Map<String, String>> firestoreFavFormat = new HashMap<>();

        for (int i=0; i<favoriteList.size(); ++i) {
            Map<String, String> recipeInfo = new HashMap<>();
            recipeInfo.put("Image", favoriteList.get(i).getImage());
            recipeInfo.put("Title", favoriteList.get(i).getTitle());
            firestoreFavFormat.put(favoriteList.get(i).getRecipeID(), recipeInfo);
        }
        docRef.set(firestoreFavFormat).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(getContext(), "Changes saved!",
                         Toast.LENGTH_SHORT).show();
             }
        }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getContext(), "Could not save, error: " + e.getMessage(),
                         Toast.LENGTH_SHORT).show();
             }
        });
    }

    // retrieve ingredients from firestore with email
    private void retrieveFavorites() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        parseDbData(data);
                        handleFavoriteFragmentAdapter();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Failed with ", task.getException());
                }
            }
        });

    }

    // handle the favoriteComplexAdapter, use favoriteList to display all favorites to user
    private void handleFavoriteFragmentAdapter() {
        Context context = getContext();
        favoriteComplexAdapter = new FavoriteComplexAdapter(context, favoriteList);
        favoriteListView.setAdapter(favoriteComplexAdapter);
        favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putString("email", email);
                args.putString("recipeId", favoriteList.get(position).getRecipeID());
                startActivity(new Intent(getActivity(), RecipeInformation.class)
                        .putExtras(args));
            }
        });
    }

    // parse databse input into list<favorite>
    private void parseDbData(Map<String, Object> data) {
        favoriteList = new ArrayList<>();

        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Map<String, String>> recipe = (Map.Entry) it.next();

            Favorite fav = new Favorite();

            fav.setRecipeID((recipe.getKey()));
            fav.setImage(recipe.getValue().get("Image"));
            fav.setTitle(recipe.getValue().get("Title"));
            favoriteList.add(fav);
        }

    }
}

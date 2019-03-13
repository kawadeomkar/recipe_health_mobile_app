package com.example.recipe_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private String email;
    private List<String> ingredients;
    private List<RecipeTemp> recipeTempList;
    private int numberRecipesToShow;
    private RecipeComplexAdapter recipeComplexAdapter;
    private RequestQueue requestQueue;
    private ListView recipeList;
    final String TAG = "RecipeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // get bundle arguments
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email = bundle.getString("email");
        } else {
            Log.d(TAG, "BUNDLE DOES NOT EXIST");
        }

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        recipeList = (ListView) view.findViewById (R.id.lv_recipe_frag);

        retrieveIngredients();

        return view;
    }


    // create listview and display recipes
    private void handleRecipeFragmentAdapter() {
        Context context = getActivity().getApplicationContext();
        recipeComplexAdapter = new RecipeComplexAdapter(context, recipeTempList);
        recipeList.setAdapter(recipeComplexAdapter);
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // todo: move to new activity for selected recipe
            }
        });
    }

    // call api with given ingredients
    private void retrieveRecipesWithIngredients() {
        Log.d("RECIPEFRAGMENT", "MADE IT TO THE API CALL");
        final SpoonAPI spoon = new SpoonAPI();
        final String header = spoon.getHeaderKey();
        // hardcoded value for now
        numberRecipesToShow = 10;
        String url = spoon.getRecipeComplexURL(ingredients, numberRecipesToShow);
        requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("RECIPEFRAGMENT", "ABOUT TO DO THE API CALL");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spoon.getRecipeComplexHelper(response);
                        Log.d("RECIPEFRAGMENT", "THIS IS THE SPOON RESULT: "
                                + spoon.getRecipeComplex());
                        recipeTempList = spoon.getRecipeComplex();
                        handleRecipeFragmentAdapter();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })  {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("X-RapidAPI-Key", header);
                return headers;
            }
        };
        requestQueue.add(req);
    }

    // retrieve ingredients from firestore with email
    private void retrieveIngredients() {
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
                        retrieveRecipesWithIngredients();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Failed with ", task.getException());
                }
            }
        });

    }
}

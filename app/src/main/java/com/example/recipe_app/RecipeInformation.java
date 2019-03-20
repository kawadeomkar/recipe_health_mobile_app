package com.example.recipe_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeInformation extends AppCompatActivity implements View.OnClickListener {


    private RecipeFull recipeFull;
    private String recipeId;
    private String email;
    private RequestQueue requestQueue;
    private TextView tv_carbs;
    private TextView tv_protein;
    private TextView tv_calories;
    private TextView tv_fat;
    private TextView tv_cookingInMins;
    private TextView tv_instructions;
    private TextView tv_ingredients;
    private TextView tv_servings;
    private TextView tv_title;
    private ImageView iv_recipeImage;
    private Button btn_exit;
    private FloatingActionButton favoriteFab;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private static final String recipeID = "RecipeID";
    private static final String title = "Title";
    private static final String image = "Image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipeId = getIntent().getExtras().getString("recipeId");
        email = getIntent().getExtras().getString("email");
        handleAPICall();

        favoriteFab = findViewById(R.id.fab);
        btn_exit = findViewById(R.id.btn_recipeInfo_exit);
        btn_exit.setOnClickListener(this);
        favoriteFab.setOnClickListener(this);
    }

    // when a user clicks on the favorite fab, it will store information about the recipe to the
    // db (title, image, recipeID)
    private void saveFavoriteRecipeToDb() {
        Map<String, Object> user = new HashMap<>();
        Map<String, String> description = new HashMap<>();
        description.put(title, recipeFull.getTitle());
        description.put(image, recipeFull.getImage());

        user.put(recipeId, description);

        db = FirebaseFirestore.getInstance();
        db.collection("users").document(email).collection("activities")
                .document("favorite_recipes").update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RecipeInformation.this, "Recipe favorited!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RecipeInformation.this, "Could not save: "
                                + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        if (recipeFull.getCuisines() != null) {
            final RecipeFull recipeFinal = recipeFull;
            final DocumentReference docRefAccount = db.collection("users").document(email)
                    .collection("activities").document("account_information");
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(docRefAccount);
                    List<String> newLearningRandom = (List<String>) snapshot.get("learning_random");

                    List<String> copy = recipeFull.getCuisines();
                    for (int i=0; i <copy.size(); ++i) {
                        newLearningRandom.add(copy.get(i));
                    }
                    transaction.update(docRefAccount, "learning_random", newLearningRandom);

                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("RecipeInformation.java", "Transaction success!");
                }
            });
        }

    }

    // override onclicks
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // fab was clicked, favorite it
            case R.id.fab:
                saveFavoriteRecipeToDb();
                break;
            // exit button
            case R.id.btn_recipeInfo_exit:
                finish();
                break;
            case R.id.btn_eat:
                changeCalories();
        }
    }

    // display recipe to user by setting the text
    private void displayRecipeFull() {
        tv_carbs = findViewById(R.id.tv_recipeInfo_carbs);
        tv_protein = findViewById(R.id.tv_recipeInfo_protein);
        tv_calories = findViewById(R.id.tv_recipeInfo_calories);
        tv_fat = findViewById(R.id.tv_recipeInfo_fat);
        tv_cookingInMins = findViewById(R.id.tv_recipeInfo_cookingInMins);
        tv_instructions = findViewById(R.id.tv_recipeInfo_instructions);
        tv_ingredients = findViewById(R.id.tv_recipeInfo_ingredients);
        tv_servings = findViewById(R.id.tv_recipeInfo_servings);
        tv_title = findViewById(R.id.tv_recipeInfo_title);
        iv_recipeImage = findViewById(R.id.iv_recipeInfo_recipeImage);

        tv_carbs.setText("Carbs: " + recipeFull.getNutrition().getCarbohydrates());
        tv_protein.setText("Protein: " + recipeFull.getNutrition().getProtein());
        tv_calories.setText("Calories: " + recipeFull.getNutrition().getCalories());
        tv_fat.setText("Fat: " + recipeFull.getNutrition().getFat());
        tv_cookingInMins.setText("Cooking time: " + recipeFull.getCookingMinutes());
        tv_instructions.setText("Instructions: " + recipeFull.getInstructions());

        List<Ingredient> ingredients_list = recipeFull.getIngredients_list();
        String tv_string = "";
        for (int i=0; i<ingredients_list.size(); ++i) {
            Ingredient temp = ingredients_list.get(i);
            tv_string += temp.getName() + ": " + temp.getAmount() + " " + temp.getUnit() + " ";
        }
        tv_ingredients.setText(tv_string);
        tv_servings.setText("Servings: " + Integer.toString(recipeFull.getServings()));
        tv_title.setText(recipeFull.getTitle());

        Picasso.with(getApplicationContext()).load(recipeFull.getImage())
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(iv_recipeImage);
    }


    // handle API call to receive full information about recipe given id
    public void handleAPICall() {
        final SpoonAPI spoon = new SpoonAPI();

        String url = spoon.getRecipeIDURL(recipeId);

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spoon.getRecipeByIDHelper(response);
                        recipeFull = spoon.getRecipeFull();
                        displayRecipeFull();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

    }) {
            /**
             * Passing some request headers
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("X-RapidAPI-Key", spoon.getHeaderKey());
                return headers;
            }
        };
        requestQueue.add(req);
    }

    private void changeCalories() {
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef;

        docRef = db.collection("users").document(email)
                .collection("activities").document("account_information");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the data back
                        Map<String, Object> doc = document.getData();

                        int allowedCalories = (int) Float.parseFloat(doc.get("caloriesLeft").toString());

                        Map<String, Object> userMap = new HashMap<>();
                        int newCalories = allowedCalories - recipeFull.getNutrition().getCalories();
                        if (newCalories < 0) {
                            Toast.makeText(RecipeInformation.this, "You should not eat this," +
                                            " you are over your calories by " +  allowedCalories * -1,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecipeInformation.this, "You have " + newCalories + " left today!",
                                    Toast.LENGTH_SHORT).show();
                            userMap.put("caloriesLeft", Integer.toString(newCalories));

                            db.collection("users").document(email).collection("activities")
                                    .document("account_information").update(userMap);
                        }

                    }
                    else {
                        Log.d("Register.java", "No such doc");
                    }
                }
                else {
                    Log.d("Register.java", "get failed with " + task.getException());
                }
            }
        });

    }

}

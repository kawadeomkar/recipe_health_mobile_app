# Recipe Suggestion App
This Android application obtains user information to suggest personalized recipes.
Using the user's age, weight, height, and weight goal the app generates a Total Daily Energy Expenditure (calories) to guide the user into eating healthy foods.
All of the user's information is stored in Google Firebase.
Calls to the Spoonacular API allow the application to grab recipes from the Spoonacular database.


## Java Files
* Files are stored in **app/src/main/java/com/example/recipe_app/**

### AccountFragment.java
* Retrieves, saves, and displays account information in Firebase (age, weight, height, gender, weight goal, actiivty level, dietary restrictions)
* Also stores user's calories left for the day, as well as a step counter


### Favorite.java
* Favorite class to store favorite recipe information (recipeID, title, image)

### FavoriteComplexAdapter.java
* Favorite adapter to allow population of AdapterView

### FavoritesFragment.java
* Retrieves, saves, and displays user's favorites list using Firebase.

### FridgeFragment.java
* Retrieves, saves, and displays the user's ingredients using Firebase.

### HomePage.java
* Bottom navigation bar (recipes, ingredients, favorites, account page)

### Ingredient.java
* Ingredient class to store amount, units, etc.

### Login.java
* Login page
* Uses Firebase authentication to validate the user's login credentials.

### MainActivity
* Main activity to start application

### Nutrition.java
* Nutrition information (calories, fat, carbs, sugar, cholesterol, etc.)

### RecipeComplexAdapter.java
* Recipe adapter to allow population of AdapterView.

### RecipeFragment.java
* Calls API with ingredients stored in Firebase.
* Displays recipes.

### RecipeFull.java
* Full recipe information.

### RecipeInformation.java
* Displays full recipe information.
* Allows for favoriting recipes.

### RecipeTemp.java
* Set and get methods for recipes.

### Register.java
* Asks for name, email, and password. Must be a valid email, and password must be at least 6 characters.
* Saves registration information to Firebase.

### RegistrationInfo.java
* Asks for age, weight, height, gender, weight goal, activity level, and dietary restrictions in order to calculte the user's TDEE.
* Sends user's personal information to Firebase.

### RegistrationIngredients.java
* Allows user to input ingredients.
* Saves ingredients to Firebase.

### SpoonAPI.java
* Spoonacular API used to make API calls to obtain a list of recipes

### StepCounter.java
* Simple pedometer/step counter 

## UI/Layout Files
* Files are stored in **app/src/main/res/layout**
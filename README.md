# Foodie

## App Description:

The Foodie app aims to allow the user to easily access and save cooking recipes chosen by the user.


* Starting page which leads the user to the main page
* a page to choose a category for cooking recipes
* a specific category page showing recipes only from the chosen category and leads to the recipe page
* a recipe in which the user can find any needed information
* Ability to save recipes
* Ability to remove an already saved recipe
* Choose how many recipes to show on the page
* ability to see local cuisine
* Ability to delete all user/recipe data
* Ability to get current location(country)
* Ability to choose a specific diet (vegetarian, vegan, or no diet)

## App Design

### HomePage(the Navigation begins from it)
![HomePage](https://user-images.githubusercontent.com/80050357/205911764-10f3bf9f-ce18-46ec-95a4-34ce41dddd85.PNG)

The "HomePage" would be loaded every time the app is launched, we have a bottom nav bar that is disabled so the user can't use it while on the home page,
there is also the "Foodie" which is a TextView Object and it serves as a Title for the APP, the only other thing on this page is the Continue button which sends
the user to the Categories Page or the Settings Page depending on whether the user uses the app for the first time or not

### SettingsPage
![Settings](https://user-images.githubusercontent.com/80050357/205911594-cf28cdb5-8bf8-49cd-9170-a80e38057366.PNG)

The next page is the "SettingsPage" which is always loaded when a user uses the app for the first time, there can set how many recipes he wants to be loaded in the RecipesPage, the Location which is used to show Local cuisine recipes (they can be accessed from the Categories Page) also the Location can be extracted from the user's phone by using the GPS which would automatically fill the field but at the same time, the Location can be changed so users can access different cuisines.
In the settings page as well there is a button to clear all the data that is stored in the ROOM database for both the User Data and the Recipes and it sends the user again to the starting page
The last thing on the page is the SAVE ALL BUTTON which saves all the data that is entered on the page.
To get out of the Page the User can use the bottom navigation bar which can lead him to either the Categories Page or his saved Recipes

### CategoriesPage
![Categories](https://user-images.githubusercontent.com/80050357/205914091-a7b000a3-1546-46f4-b732-8bb35417aec1.PNG)

It can be said that the Categories Page is the main page of the app, this is the page that the user would be sent to, every time he clicks the Continue button from the home page (after the first time), this Page is used as a "hub" and it would send the user to the Recipes that he wants to Explore, there are a few different categories and they can be all accessed by Clicking the image that is over the category name

### RecipesPage
![RecipesPage](https://user-images.githubusercontent.com/80050357/205914978-552c09e8-66a9-49af-8c30-b70597c89bcc.PNG)

On this page up to 100 recipes are being loaded ( the number is chosen by the user on the settings page) in a RecyclerView, the grid that is being used is GridLayout which allows having 2 columns only and it allows as well the possibility to have only 1x Recipe if an uneven number is chosen.
When the Page is opened the category name is used to make a GET request from the API to get the JSON file which would give us all the information that we need currently for the Recipes and all that DATA is being stored in the ROOM database, so that after the first time that the CATEGORY is loaded it wouldn't need to make a GET requests from the API also depending on the user's choosing in the settings page a diet can be added to the GET request like(Vegetarian, No Diet or Vegan)

### FoodPage
![FoodPage](https://user-images.githubusercontent.com/80050357/205916275-2b01a385-f9e8-46a0-8cda-b1b8762f31ff.PNG)

The Food page shows detailed information about the recipe, a ScrollView is used since the information can't find on the page.
The way it works is, the first time a Recipe is opened a new GET request is used for the API to get us detailed information for the recipe after that the data is stored in the ROOM database so in the future a GET request wouldn't be necessary
On this page is added a button in the form of a heart, when the Recipe is being loaded it checks if the Recipe is added to MyRecipes (to the saved recipes) and if it is like the full heart is used if not it is just a border and when the button is clicked it adds or removes  the recipe from MyRecipes (and changes the border)

### MyRecipesPage
![MyRecipesPage](https://user-images.githubusercontent.com/80050357/205917179-a6d4a5d8-51d2-411e-9d18-8b76282b1489.PNG)

In the MyRecipes page are being listed the recipes that are added(liked (from the Hearth Button in FoodPage)) by the user, as in RecipePage a Recycler View is being used to show all the Recipes that are being added to the database
The ROOM database that the APP is using stores 2x Different Varbiales Recipe and User
The Recipe stores all the information that a recipe would need - (id(for the API), name, imageURL, cooking and prep time, serving size, instruction, ingredients if it is saved or not (in MyRecipes), the Category for the Recipe and if the Recipe is being loaded for the first time
The User stores all the information that is needed for a user (id(autogenerated), location (used for the cuisine), number of recipes per page, diet, and if he uses the app for the first time

# Reflective Statement
Having just finished with all the coding I would say that it took way longer than I expected and it was an educating experience, having completed this CW the least I would say is 
that I improved my usage and understanding of APIs and doing HTTP requests. I think that the recyclerView proved to be very useful and helpful and in the future, if I get to do another app I would use it again, I would say that I wasted some time the FireDatbase since I thought that I needed it as well but after I implemented the ROOM database I started understanding that it is quite useless for me, probably if we had a bigger project to work on I would use it because obviously, the data that can be stored on the mobile device is limited

# Testing:
![testing](https://user-images.githubusercontent.com/80050357/205959962-0749d30a-2483-4963-8699-bdb681540bef.PNG)
[Testing.csv](https://github.com/WorkAtRGU/cm3110-coursework-VKalushev/files/10167898/Testing.csv)


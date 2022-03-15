# Recipe-Backend

## Table of Contents

- [Description](#description)
- [How To Run](#how-to-run)
- [Objectives](#objectives)
- [Usage](#usage)
- [License](#license)

## Description

We all were in a situation when we wanted to cook something special but couldn't remember a recipe.

The program is a multi-user web service based on Spring Boot that allows storing, retrieving, updating, and deleting recipes.

## How To Run

Enter the project main directory.

```sh
$ ./gradlew run
```

## Objectives
* POST ```/api/recipe/new``` receives a recipe as a JSON object and returns a JSON object with one ``id`` field;
* GET ```/api/recipe/{id}``` returns a recipe with a specified id as a JSON object.
* DELETE ```/api/recipe/{id}``` deletes a recipe with a specified id.
* PUT ``/api/recipe/{id}`` receives a recipe as a JSON object and updates a recipe with a specified ``id``. Also, update the `date` field too. The server should return the `204 (No Content)` status code. If a recipe with a specified id does not exist, the server should return `404 (Not found)`. The server should respond with 400 (Bad Request) if a recipe doesn't follow the restrictions indicated above (all fields are required, string fields can't be blank, arrays should have at least one item);
* GET `/api/recipe/search` takes one of the two mutually exclusive query parameters:
`category` – if this parameter is specified, it returns a JSON array of all recipes of the specified category. Search is case-insensitive, sort the recipes by date (newer first);
`name` – if this parameter is specified, it returns a JSON array of all recipes with the names that **contain** the specified parameter. Search is case-insensitive, sort the recipes by date (newer first).
If no recipes are found, the program should return an empty JSON array. If 0 parameters were passed, or more than 1, the server should return `400 (Bad Request)`. The same response should follow if the specified parameters are not valid. If everything is correct, it should return `200 (Ok)`.
* Store all recipes permanently in a database: after a server restart, all added recipes should be available to a user;
* Implement a new DELETE ```/api/recipe/{id}``` endpoint. It deletes a recipe with a specified ```{id}```. The server should respond with the ``204 (No Content)`` status code. If a recipe with a specified id does not exist, the server should return ``404 (Not found)``;
* The service should accept only valid recipes – all fields are obligatory, ``name`` and ``description`` shouldn't be blank, and JSON arrays should contain at least one item. If a recipe doesn't meet these requirements, the service should respond with the ``400 (Bad Request)`` status code.
* ``category`` represents a category of a recipe. The field has the same restrictions as ``name`` and ``description``. It shouldn't be blank;
* ``date`` stores the date when the recipe has been added (or the last update). You can use any date/time format, for example ``2021-09-05T18:34:48.227624`` (the default ``LocalDateTime`` format), but the field should have at least 8 characters.
* New endpoint POST `/api/register` receives a JSON object with two fields: `email` (string), and `password` (string). If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with `200 (Ok)`. If a user is already in the database, respond with the `400 (Bad Request)` status code. Both fields are **required** and must be **valid**: `email` should contain `@` and `.` symbols, `password` should contain at least 8 characters and shouldn't be blank. If the fields do not meet these restrictions, the service should respond with `400 (Bad Request)`. Also, do not forget to use an encoder before storing a password in a database. `BCryptPasswordEncoder` is a good choice.
* Include the Spring Boot Security dependency and configure access to the endpoints – all implemented endpoints (except `/api/register`) should be available only to the registered and then authenticated and authorized via HTTP Basic auth users. Otherwise, the server should respond with the `401 (Unauthorized)` status code.
* Add additional restrictions – only an author of a recipe can delete or update a recipe. If a user is not the author of a recipe, but they try to carry out the actions mentioned above, the service should respond with the `403 (Forbidden)` status code.
## Usage

**Example 1**: POST ```/api/recipe/new``` request

```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
Response:
```json
{
   "id": 1
}
```
Further GET ```/api/recipe/1``` response:
```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2022-03-15T15:14:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```

**Example 2**: PUT ```/api/recipe/1``` request
```json
{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}
```

Further response for the GET ```/api/recipe/1``` request:

```json
{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "date": "2022-03-15T15:19:55.234795",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}
```

**Example 3**: A database with several recipes

```sh
{
   "name": "Iced Tea Without Sugar",
   "category": "beverage",
   "date": "2019-07-06T17:12:32.546987",
   ....
},
{
   "name": "vegan avocado ice cream",
   "category": "DESSERT",
   "date": "2020-01-06T13:10:53.011342",
   ....
},
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2021-09-06T14:11:51.006787",
   ....
},
{
   "name": "Vegan Chocolate Ice Cream",
   "category": "dessert",
   "date": "2021-04-06T14:10:54.009345",
   ....
},
{
   "name": "warming ginger tea",
   "category": "beverage",
   "date": "2020-08-06T14:11:42.456321",
   ....
}
```

Response for the GET ```/api/recipe/search/?category=dessert``` request:
```sh
[
   {
      "name": "Vegan Chocolate Ice Cream",
      "category": "dessert",
      "date": "2021-04-06T14:10:54.009345",
      ....
   },
   {
      "name": "vegan avocado ice cream",
      "category": "DESSERT",
      "date": "2020-01-06T13:10:53.011342",
      ....
   },
]
```
Response for the GET ```/api/recipe/search/?name=tea``` request:
```sh
[
   {
      "name": "Fresh Mint Tea",
      "category": "beverage",
      "date": "2021-09-06T14:11:51.006787",
      ....
   },
   {
      "name": "warming ginger tea",
      "category": "beverage",
      "date": "2020-08-06T14:11:42.456321",
      ....
   },
   {
      "name": "Iced Tea Without Sugar",
      "category": "beverage",
      "date": "2019-07-06T17:12:32.546987",
      ....
   },
]
```
Search is case-insensitive, the recipes are sorted by date.

**Example 4**: POST `/api/recipe/new` request without authentication
```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
Status code: ```401 (Unauthorized)```

**Example 5**: POST `/api/register` request without authentication
```json
{
   "email": "Cook_Programmer@somewhere.com",
   "password": "RecipeInBinary"
}
```
Status code: `200 (Ok)`

Further POST `/api/recipe/new` request with basic authentication; email (login): Cook_Programmer@somewhere.com, and password: RecipeInBinary
```json
{
   "name": "Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
Response:
```json
{
   "id": 1
}
```

Further PUT `/api/recipe/1` request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary
```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
Status code: 204 (No Content)

Further GET `/api/recipe/1` request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary

Response:
```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```

**Example 6**: POST `/api/register` request without authentication
```json
{
   "email": "CamelCaseRecipe@somewhere.com",
   "password": "C00k1es."
}
```
Status code: `200 (Ok)`

Further response for the GET `/api/recipe/1` request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.
```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```

Further PUT `/api/recipe/1` request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.
```json
{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}
```
Status code: `403 (Forbidden)`

Further DELETE `/api/recipe/1` request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.

Status code: `403 (Forbidden)`
## License

[MIT](LICENSE) © OXL
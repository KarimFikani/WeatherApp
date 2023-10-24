# WeatherApp

## Requirements:
* Search Screen that allows users to enter a US city - DONE
* Display the information that users would be interested in seeing - DONE
* Cache images - DONE
* Auto-load the last city searched upon app launch - DONE
* Ask the User for location access, If the User gives permission to access the location, then retrieve weather data by default - DONE

## Must have that are used:
* Kotlin - Used Kotlin in the whole codebase.
* Java - Used Java in unit tests
* Used MVVM as the architecture for the app
* Retrofit for the networking library
* Junit and Mockito for unit tests
* Kotlin coroutines for the communication between viewModel and view
* RxJava for the RxBus
* Hilt for dependency injection

## Project structure:
* core package - holds all the common functionalities for the app
* search package - holds all the UI and business logic for the search page
* weather package - holds all the UI and business logic for the main screean that displays the weather info and the location permission logic

## Time breakdown:
* 20 min - Understand the requirements and setup the plan for execution
* 10 min - Used Postman to test the APIs and understand the response
* 20 min - Setup the project with all the dependencies needed
* 1.15 hrs - Implement the search page, and display search results along with testing and covering corner cases
* 1 hr - Implement the main weather page and display weather info to the user
* 25 min - Implement location permission logic with testing
* 20 min - Implement unit tests for one of the mappers
* 30 min - Final QA pass and code cleanup and documentation (comments and video recording and readme)

## If I had more time, I would have:
* Written more unit tests to cover all viewModels, mappers and repositories
* Moved Search package into its own module. Similarly for the core package.
* Handled configuration changes
* Made the UI look pretty
* made search page more responsive by displaying results as the user types the location

## Showcase:
| Location permission NOT grated  | Location permission granted |
| ------------- | ------------- |
| ![](https://github.com/KarimFikani/WeatherApp/blob/main/video_1.gif) | ![](https://github.com/KarimFikani/WeatherApp/blob/main/video_2.gif) |

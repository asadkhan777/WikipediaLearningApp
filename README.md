## WikipediaLearningApp

# Screenshots

<p>
<a href="https://github.com/asadkhan777/WikipediaLearningApp/blob/develop/app/release/app-release.apk?raw=true">
<img src="https://github.com/asadkhan777/WikipediaLearningApp/blob/develop/screenshots/Screenshot_2019-08-14-07-52-29-173_com.example.wikipedialearningapp.png" width="350" title="Screenshot1">
<img src="https://github.com/asadkhan777/WikipediaLearningApp/blob/develop/screenshots/Screenshot_2019-08-14-04-42-17-251_com.example.wikipedialearningapp.png" width="350" title="Screenshot2">
</a>
</p>

# Scope & Purpose

- This app was taken up as an assignment as part of the Kings Learning (Enguru) Tech interview process
- Requirement & Spec document  [here](https://docs.google.com/document/d/1brNfPeOeLGkBmBN5j8ot9VJXLrKW2XQUNawM2uUBFuc/edit)
- Wikipedia web service documentation [here](https://www.mediawiki.org/wiki/API:Main_page)

# Current Implementation & Future scope

- Currently the App is structured such that it follows clean architecture system combined with the MVVM pattern
- Notable library integrations include:
      - Kotlin Std Lib
      - Dagger2
      - Retrofit2
      - RxJava2
      - Glide
      - Google Architecture Components

- The App utilizes the Wikipedia API to search for related articles pertaining to user's interest
- The most significant API used would be the query API, providing search results for the search term provided by the user via SearchView in the UI
- This search feature uses various tools like RxJava, Gson & Retrofit to fetch data over the network is an asynchronous way
- Swipe to refresh functionality is also implemented on the search results page
- The listing page loads results in batches of 10, lazily loading more and more results as user scrolls down
- Currently it uses Chrome custom tabs library to open the Wikipedia article inside the app itself.
- Due to the wikipedia API being slightly 'unconventional', it didn't seem technically feasible to parse raw html for each & every article being fetched from the network
- For offline (cache) functionality, chrome custom tabs allow an option to save the file locally for future use without network access.
- There is room for improvement in this area, eg. Taking article contents in a mobile viewing friendly format & save those contents in local database using pageid
- Such a cache could be set up using RoomDB library to store contents for offline usage
- Due to time & technical constraints, this has been left for future revisions

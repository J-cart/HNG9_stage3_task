

# HNG9_Stage3_task

<h1 align="center">Countries App</h1>
  This is a app that displays countries and their details

  ## Architecture 
**Countries app** is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overrall architecture of **Countries app** is composed of two layers; the UI layer and the data layer. Each layer has dedicated components and they have each different responsibilities, as defined below:

**Countries** was built with [Guide to app architecture](https://developer.android.com/topic/architecture), so it would be a great sample to show how the architecture works in real-world projects.



            
 - Design: [Figma](https://www.figma.com/file/v9AXj4VZNnx26fTthrPbhX/Explore?node-id=33%3A1390) was referenced.

  ## Tech stack and Libraries 
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - View Binding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Architecture
  - MVVM Architecture (View - View Binding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [Moshi](https://github.com/square/moshi/): A modern JSON library for Kotlin and Java.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [Coil](https://github.com/coil-kt/coil): Loading images from network.
- **API**
  - [Countries Api](https://restcountries.com/): Get information about countries via a RESTful API

## Download
You can download the latest APK from [Releases](), also for Appetize.io [App]()


## Features to be added
- [ ] 
- [ ] 
- [ ]

## Side notes

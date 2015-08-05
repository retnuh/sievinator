# The Sievinator
A small REST service showing off some of the features for the [Friboo](https://github.com/zalando-stups/friboo) project.  [Friboo](https://github.com/zalando-stups/friboo) is an opinionated framework that leverages [Swagger](http://swagger.io/),  [swagger1st](https://github.com/sarnowski/swagger1st), and Stuart Sierra's [Component](https://github.com/stuartsierra/component) library.

The project shows an evolution of steps, in different branches.

The branch [1-bare-minimum](https://github.com/retnuh/sievinator/tree/1-bare-minimum) shows a "bare minumum" example with 1 api endpoint and 2 components, with no additional components for storage.

The branch [2-more-endpoints](https://github.com/retnuh/sievinator/tree/2-more-endpoints) shows more endpoints, including ones with multiple args and some that change server-side state.

The branch [3-fs-storage-component](https://github.com/retnuh/sievinator/tree/3-fs-storage-component) adds a whole new component to the system to show off some of the [Component](https://github.com/stuartsierra/component) library.

Finally, the [master](https://github.com/retnuh/sievinator) branch includes the complete application, which includes multiple endpoints, POST endpoints,  en example of explicit error handling, and two different storage components (the file system one, and a sqlite one), which can be changed at run-time.

## Running stuff
Everything can be run from the repl or using `lein run`.

# The Sievinator
A small REST service showing off some of the features for the [Friboo](https://github.com/zalando-stups/friboo) project.  [Friboo](https://github.com/zalando-stups/friboo) is an opinionated framework that leverages [Swagger](http://swagger.io/),  [swagger1st](https://github.com/sarnowski/swagger1st), and Stuart Sierra's [Component](https://github.com/stuartsierra/component) library.

The project shows an evolution of steps, in different branches.  The
project itself provides a REST wrapper around what was originally a
small prime number sieve (for a seperate project) but grew to be a bit more all-singing all
dancing, including allowing you to get factors for numbers, test for
primality, etc.  The main point was to expose the functionality of
*SOMETHING*, where the something itself was reasonably self contained
and not too likely to distract from exploring the bits that Friboo (& co.) give.

For those interested in the sieve itself, I encourage you to look at
[Sieve of Eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes) and the
[Sieve of Sundaram](https://en.wikipedia.org/wiki/Sieve_of_Sundaram).
I think what is implemented here is technically a variant on the
latter, but I don't think I had read about it until after I had
written my code, but I could be wrong.

The branch [1-bare-minimum](https://github.com/retnuh/sievinator/tree/1-bare-minimum) shows a "bare minumum" example with 1 api endpoint and 2 components, with no additional components for storage.

The branch [2-more-endpoints](https://github.com/retnuh/sievinator/tree/2-more-endpoints) shows more endpoints, including ones with multiple args and some that change server-side state.

The branch [3-fs-storage-component](https://github.com/retnuh/sievinator/tree/3-fs-storage-component) adds a whole new component to the system to show off some of the [Component](https://github.com/stuartsierra/component) library.

Finally, the [master](https://github.com/retnuh/sievinator) branch includes the complete application, which includes multiple endpoints, POST endpoints,  en example of explicit error handling, and two different storage components (the file system one, and a sqlite one), which can be changed at run-time.

## Running stuff
Everything can be run from the repl or using `lein run`.

The default port for the server is `8080`.

The UI is accessible by pointing your browser at:
`http://localhost:8080/ui/`.

a JSON form of the swagger definition itself is available at:
`http://localhost:8080/swagger.json`.

Some fun examples with the excellent
[HTTPie](https://github.com/jkbrzt/httpie):

```
$ http -f :8080/sieve/block-size size=200
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 3
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:30:03 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

100

$ http -f :8080/sieve/block-size size=201
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 3
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:30:11 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

200

$ http -f :8080/sieve/block-size 
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 3
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:30:16 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

201

$ http -f :8080/sieve/highest-seen
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 3
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:33:56 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

201

$ http -f :8080/primes/nth/250
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 4
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:34:12 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

1583

$ http -f :8080/sieve/highest-seen
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 4
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:34:16 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

1801

$ http -f :8080/numbers/is-prime/1801
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 4
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:35:00 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

true

$ http -f :8080/numbers/is-prime/1799
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 5
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:35:05 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

false

$ http -f :8080/numbers/factors/1799
HTTP/1.1 200 OK
Access-Control-Allow-Headers:
Access-Control-Allow-Methods: GET, POST, DELETE, PUT, PATCH, OPTIONS
Access-Control-Allow-Origin: *
Access-Control-Max-Age: 3600
Content-Length: 7
Content-Type: application/json
Date: Wed, 05 Aug 2015 15:35:12 GMT
Server: Jetty(9.2.10.v20150310)
Strict-Transport-Security: max-age=10886400

[
    7, 
    257
]

```


## (UN)License
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>


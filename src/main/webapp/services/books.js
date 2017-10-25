'use strict';

angular.module('books')
    .service('books', function ($http) {
        return {
            booksList: function (success) {
                return $http.get("/rest/book").then(success);
            },
            save: function (book, success) {
                return $http.post("/rest/book", book).then(success);
            },
            filterBooks: function (filter, success) {
                return $http.get("/rest/book?filter=" + filter).then(success);
            },
            delete: function (id, success) {
                return $http.delete("/rest/book/" + id).then(success);
            },
        };
    });

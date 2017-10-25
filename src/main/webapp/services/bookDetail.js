'use strict';

angular.module('bookDetail')
    .service('bookDetail', function ($http) {
        return {
        	getBook: function (id, success) {
                return $http.get("/rest/book/" + id).then(success);
            },
            saveBook: function (newBook, success) {
                return $http.post("/rest/book", newBook).then(success);
            }
        };
    });

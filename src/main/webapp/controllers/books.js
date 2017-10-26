'use strict';

angular.module('books')
    .controller('BooksCtrl', function ($scope, books) {

        $scope.loadBooks = function() {
            books.booksList(function (booksList) {
                $scope.booksList = booksList.data;
            }).finally(function () {
                $scope.dataLoading = false;
            });
        }

        $scope.save = function() {
            books.save($scope.form, function() {
                $scope.loadBooks();
            });
        }
        
        $scope.deleteBook = function(id) {
        	books.delete(id, $scope.loadBooks);
		}
        
        $scope.filter = function() {
        	books.filterBooks($scope.filterContent, function (booksList) {
                $scope.booksList = booksList.data;
            });
        }
        $scope.dataLoading = true;
        $scope.bookForm = {};
        $scope.filterContent = "";
        $scope.loadBooks();
    });

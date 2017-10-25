'use strict';

angular.module('bookDetail')
    .controller('BookDetailCtrl', function ($scope, $routeParams, $location, bookDetail) {
    	
    	$scope.submitForm = function() {
    		if ($scope.bookForm.$valid) {
    			bookDetail.saveBook($scope.bookDetail, function() {
    				$scope.goBack();
                });
                console.log('Form OK');
            } else {
                console.log('Form KO');
            }
        }
    	
    	$scope.goBack = function() {
    		$scope.bookDetail = {};
    		$location.path("/books");
    	}
    	
    	
    	$scope.loadSingleBook = function(id) {
    		bookDetail.getBook(id, function (bookDetail) {
                $scope.bookDetail = bookDetail.data;
            });
        }
    	
    	$scope.isEditingMode = function() {
    		return $routeParams.bookParam != 'new';
    	}
    	
    	var bookParam = $routeParams.bookParam;
    	if(bookParam == 'new'){
    		$scope.bookDetail = {};
    	}else{
    		$scope.loadSingleBook($routeParams.bookParam);
    	}
    });

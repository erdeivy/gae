'use strict';
angular
    .module('test', ['ngRoute', 'books', 'bookDetail'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/list.html',
                controller: 'ListCtrl',
                controllerAs: 'list'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
angular
    .module('books', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
                .when('/books', {
                templateUrl: 'views/books.html',
                controller: 'BooksCtrl',
                controllerAs: 'books'
            })
    })
    .directive('ngConfirmClick', [
    function(){
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Are you sure?";
                var clickAction = attr.confirmedClick;
                element.bind('click',function (event) {
                    if ( window.confirm(msg) ) {
                        scope.$eval(clickAction)
                    }
                });
            }
        };
	}]);
angular
.module('bookDetail', ['ngRoute'])
.config(function ($routeProvider) {
    $routeProvider
        .when('/books/:bookParam', {
            templateUrl: 'views/bookDetail.html',
            controller: 'BookDetailCtrl',
            controllerAs: 'bookDetail'
        })
});
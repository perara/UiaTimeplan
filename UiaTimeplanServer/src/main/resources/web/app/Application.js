/**
 * Created by PerArne on 17.07.2014.
 */


var sampleApp = angular.module('uiatimeplan', [
    'ngResource',
    'ngRoute',
    'ngCookies',
    'mobile-angular-ui'
]);
sampleApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'app/views/List.html',
                controller: 'ListController'
            }).
            when('/course/:course', {
                templateUrl: 'app/views/Course.html',
                controller: 'CourseController'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);


///////////////////////////////////////
///
/// Resources
///
////////////////////////////////////////
sampleApp.factory('CourseListResource', function($resource) {
    return $resource('/api/course/list', {}, {
        'get': {method: 'GET', isArray: true}
    });
});

sampleApp.factory('CourseResource', function($resource) {
    return $resource('/api/course/:id', { id: '@id'}, {
        'get': {method: 'GET', isArray: false}
    });
});

sampleApp.factory('CourseItemsResource', function($resource) {
    return $resource('/api/course/items/:id', { id: '@id'}, {
        'get': {method: 'GET', isArray: false}
    });
});


sampleApp.controller("MainController", function($scope, $cookies){

    // Create array in cookie storage if not exist
    console.log($cookies.favourites)
    if(!$cookies.favourites) {
        $cookies.favourites = "";
    }


});



sampleApp.controller("MenuController", function($scope, CourseResource, $cookies){

    var favourites = $cookies.favourites.split(";");
    $scope.courses = [];

    angular.forEach(favourites, function(value, key) {
        if(!!value) {
            $scope.courses.push(CourseResource.get({id: value}));
        }
    });


});


sampleApp.controller("ListController", function($scope, CourseListResource){
    $scope.courses = CourseListResource.get();
    $scope.showSubjects = false;
});


sampleApp.controller("CourseController", function($scope, $routeParams, $cookies, CourseResource, CourseItemsResource ){
    var courseID = $routeParams.course;
    $scope.course = CourseResource.get({id : courseID})
    $scope.courseItems = CourseItemsResource.get({id : courseID})

    var d = new Date();
    d.setHours(0,0,0,0);
    $scope.now = d.getTime();


    $scope.isFavourite = ($cookies.favourites.indexOf(courseID) > -1) ? true : false;

    $scope.addFavourite = function(){
        if(!($cookies.favourites.indexOf(courseID) > -1))
        {
            $cookies.favourites =   $cookies.favourites + ";" + courseID
            $scope.isFavourite = true;
            window.location.reload();
        }
    }

    $scope.removeFavourite = function(){
        if($cookies.favourites.indexOf(courseID) > -1)
        {
            $cookies.favourites =   $cookies.favourites.replace(";"+courseID, "");
            $scope.isFavourite = false;
            window.location.reload();
        }
    }



});

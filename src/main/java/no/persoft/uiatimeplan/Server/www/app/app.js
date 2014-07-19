/**
 * Created by PerArne on 17.07.2014.
 */


var sampleApp = angular.module('uiatimeplan', [
    'ngRoute'
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
                redirectTo: '/google'
            });
    }]);




sampleApp.controller("ListController", function($scope, $http, $location){

    /* Fetch Courselist */
    $http({method: 'GET', url: '/api/course/list'}).
        success(function(data, status, headers, config) {

            var cnt = 0;
            $scope.courses = []
            $.each(data, function(key, val){
                $scope.courses.push({
                    'id': cnt++,
                    'name': val
                })
            });
        }).
        error(function(data, status, headers, config) {
            alert("The API failed, contact the Per")
            alert(data)
        });

    $scope.navigate = function(course){
        $location.path("/course/"+course);
    }

});

sampleApp.directive('lastrepeat', function() {
    return function(scope, element, attrs) {
        if (scope.$last) setTimeout(function(){
            scope.$emit('onRepeatLast', element, attrs);
        }, 1);
    };
})

sampleApp.controller("CourseController", function($scope,$http, $routeParams){
    $scope.course = $routeParams.course;

    /* Fetch Course Data */
    $http({method: 'GET', url: '/api/course/course/'+$scope.course}).
        success(function(data, status, headers, config) {
            $scope.courseItems = data.courseItems

            // Create a list with all dates
            dates = {}
            $.each($scope.courseItems, function(key,val){
                var dateItem = val.fromDate.split(" ")[0]; // Split the dateTime to date

                    // Hash map functionality of dictionary prevents duplicates <3
                    dates[dateItem] = ""
            })

            // Now transform all of the Dict keys to a array(
            $scope.dates = Object.keys(dates)

            // Sort the Date Array First to last
            $scope.dates.sort(function(x,y){
                return new Date(x) - new Date(y)
            })


        }).
        error(function(data, status, headers, config) {
            alert("The API failed, contact the Per")
            alert(data)
        });


    $scope.$on('onRepeatLast', function(scope, element, attrs){
        var now = new Date();
        var closestDate = Infinity;
        var closestElement = undefined
        $.each($(".course-item"), function(key, item) {
            var date = new Date($(item).attr('id'));

            // +1 = Future
            // 0 = Now
            // -1 = Past

            // Find the difference between now and then
            var diff = date - now;

            // Find the smallest number, (Closest to now), exclude the past
            if (diff < closestDate && diff >= 0) {
                closestDate = diff;
                closestElement = $(item);
            }

            // If Past
            if(diff < 0)
                $(item).addClass("past-course")

            // If Future
            if(diff > 0)
                $(item).addClass('future-course')
        });

        // Remove classes for PAST and Future, Then add for current
        closestElement.removeClass("future-course")
        closestElement.removeClass("past-course")
        closestElement.addClass('current-course')

    });



    /***
     * @param string - The string to split
     * @param char - Which character to split on
     * @param nb - Which index to return
     * @returns {*}
     */
    $scope.splitstring = function(string, char , nb) {
        return  string.split(char)[nb]
    }


    /**
     * A function which finds the closest next date
     * @param startDate the date you want to test
     * @param dates - The date you want to test against
     * @returns {*} The closest date
     */
    function nextDate( startDate, dates ) {
        var startTime = +startDate;
        var nearestDate, nearestDiff = Infinity;
        for( var i = 0, n = dates.length;  i < n;  ++i ) {

            var diff = +new Date(dates[i]) - startTime;
            if( diff > 0  &&  diff < nearestDiff ) {
                nearestDiff = diff;
                nearestDate = dates[i];
                console.log(":D")
            }
        }
        return nearestDate;
    }



});

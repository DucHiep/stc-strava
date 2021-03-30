var app = angular.module("strava", ['ui.router','ngStorage']);

app.constant('urls', {
    BASE: 'http://localhost:8080/',
    RUN_SERVICE_API : 'http://localhost:8080//api/v1/run',
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise('/');

        $stateProvider

            .state('/', {
                url: '/',
                templateUrl: 'templates/home',
                controller: 'HomeController',
                controllerAs: 'vm',
            });
    }])
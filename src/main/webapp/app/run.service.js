'use strict';

angular.module('strava').factory('runService',
    [ '$http', 'urls',
        function ($localStorage, $http, urls) {

            var factory = {
                loadAllRun: loadAllRun,
            };

            return factory;

            function loadAllRun(){
                return $http.get(urls.RUN_SERVICE_API);
            }
        }
])
'use strict';

angular.module('strava').controller('RunController',
    ['runService', '$scope',  function( runService, $scope) {
        var self = this;
        self.runs=[];

        self.getAllRun = getAllRun();

        function getAllRun(){
             runService.getAllRun()
                 .then( function (response) {
                     self.runs = response.data;
                 })
                 .catch(function () {
                     console.error("Error by get all run")
                 })
        }

    }
    ]);
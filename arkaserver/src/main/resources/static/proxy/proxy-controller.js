//var app = angular.module('myApp', []);

angular.module('myApp').controller('ProxyController', function($scope, $http, $state) {

//     $scope.changeTab = function(tabName) {
//            $location.path(tabName);
////             $scope.currentTab = tabName;
//        };

    $scope.changeTab = function(tabName) {
        console.log('Changing tab to:', tabName);
        $state.go(tabName);
    };


    $scope.createNewProxy = function() {
        var endpoint = '/api/start';
        var proxyPort = 5555;
        var params = {
            proxyPort: proxyPort
        };

        $http({
            method: 'GET',
            url: endpoint,
            params: params,
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(function(response) {
            console.log('Odpowiedź z serwera:', response.data);
        })
        .catch(function(error) {
            console.error('Błąd podczas wysyłania żądania:', error);
        });
    };

    $scope.stopProxy = function () {
        var endpoint = '/api/stop';
        var proxyPort = 5555;
        var params = {
            proxyPort: proxyPort
        };
        $http({
            method: 'GET',
            url: endpoint,
            params: params,
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(function (response) {
            console.log('Odpowiedź z serwera:', response.data);
        })
        .catch(function (error) {
            console.error('Błąd podczas wysyłania żądania:', error);
        });
    };

});

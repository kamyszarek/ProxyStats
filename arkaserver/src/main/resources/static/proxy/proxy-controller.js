angular.module('myApp').controller('ProxyController', function($scope, $http, $state) {
    var $this = this;

    $scope.changeTab = function(tabName) {
        console.log('Changing tab to:', tabName);
        $state.go(tabName);
    };

    $scope.proxyConfigData = {};
    $this.proxiesNames = [];

    $this.showProxy = function() {
        if (!$this.selectedProxyName) {
            console.error('No proxyName selected.');
            return;
        }
        $http({
            method: 'GET',
            url: '/api/proxy/read/' + $this.selectedProxyName
        })
        .then(function(response) {
            $this.proxyConfigData = response.data;
        })
        .catch(function(error) {
            console.error('Błąd podczas odpytywania endpointu:', error);
        });
    };

    $this.startProxy = function () {
        var endpoint = '/api/proxy/start';
        var proxyPort = 5555;
        var params = {
            proxyPort: proxyPort,
            proxyName: $this.selectedProxyName
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

    $this.stopProxy = function () {
        var endpoint = '/api/proxy/stop';
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

    $this.getProxiesList = function() {
       $http({
           method: 'GET',
           url: '/api/proxy/proxies-list'
       })
       .then(function(response) {
           $this.proxiesNames = response.data.map(proxy => proxy.proxyName);
       })
       .catch(function(error) {
           console.error('Error fetching proxies list:', error);
       });
    };

    $this.getProxiesList();

});

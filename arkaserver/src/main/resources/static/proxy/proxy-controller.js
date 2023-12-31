angular.module('myApp').controller('ProxyController', function($scope, $http, $state) {
    var $this = this;

    $this.proxyConfigData = {};
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
            console.error('An error occured during reading the proxy config:', error);
        });
    };

    $this.startProxy = function () {
        var endpoint = '/api/proxy/start';
        var params = {
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
            $this.proxyConfigData.enable = true;
            console.log('Server response:', response.data);
        })
        .catch(function (error) {
            console.error('Request could not be finalized: ', error);
        });
    };

    $this.stopProxy = function () {
        var endpoint = '/api/proxy/stop';
        var params = {
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
            $this.proxyConfigData.enable = false;
            console.log('Server response:', response.data);
        })
        .catch(function (error) {
            console.error('Request could not be finalized: ', error);
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
           console.error('Error fetching proxies list. Make sure you have proxy configured.');
       });
    };

    $this.getProxiesList();

});

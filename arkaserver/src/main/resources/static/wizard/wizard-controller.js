
angular.module('myApp')
    .controller('WizardController', function($scope, $http) {

     $scope.proxyData = {
            proxyName: "",
            proxyPort: null,
            dbUrl: "",
            dbPort: null,
            dbSchema: "",
            dbUsername: "",
            dbPassword: ""
        };

        $scope.submitProxy = function() {
            $http.post('/api/proxy/create', $scope.proxyData)
                .then(function(response) {
                    console.log(response.data);
                })
                .catch(function(error) {
                    console.error('Error creating proxy configuration:', error);
                });
        };

});

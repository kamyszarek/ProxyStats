angular.module('myApp')
    .controller('WizardController', function($http) {
    var $this = this;

     $this.proxyData = {
            proxyName: "",
            proxyPort: null,
            dbUrl: "",
            dbPort: null,
            dbSchema: "",
            dbUsername: "",
            dbPassword: ""
        };

        $this.submitProxy = function() {
            $http.post('/api/proxy/create', $this.proxyData, {
                transformResponse: [function(data) {
                    return data;
                }]
            })
            .then(function(response) {
                console.log(response.data);
            })
            .catch(function(error) {
                console.error('Error creating proxy configuration:', error);
            });
        }

});

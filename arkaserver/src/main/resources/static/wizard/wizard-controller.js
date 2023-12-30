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

        $http.post('/api/proxy/create', $this.proxyData, {
            transformResponse: [function(data) {
                // Zwracaj tekst jako czysty tekst, nie próbuj przetwarzać go jako JSON
                return data;
            }]
        })
        .then(function(response) {
            console.log(response.data); // Odczytaj tekst z odpowiedzi
        })
        .catch(function(error) {
            console.error('Error creating proxy configuration:', error);
        });


});

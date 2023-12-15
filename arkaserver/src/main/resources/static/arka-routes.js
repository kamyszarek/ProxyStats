var app = angular.module('myApp', ['ui.router']);

app.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('homepage', {
            url: '/homepage',
            templateUrl: 'homepage/homepage.html',
            controller: 'HomeController'
        })
        .state('proxy', {
            url: '/proxy',
            templateUrl: 'proxy/proxy.html',
            controller: 'ProxyController'
        })
        .state('wizard', {
            url: '/wizard',
            templateUrl: 'wizard/wizard.html',
            controller: 'WizardController'
        });

        $urlRouterProvider.otherwise('/');
});
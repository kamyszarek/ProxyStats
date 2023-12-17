var app = angular.module('myApp', ['ui.router']);

app.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('dashboard', {
            url: '/dashboard',
            templateUrl: 'dashboard/dashboard.html',
            controller: 'DashboardController'
        })
        .state('homepage', {
            url: '/homepage',
            templateUrl: 'homepage/homepage.html',
            controller: 'HomeController'
        })
        .state('proxy', {
            url: '/proxy',
            templateUrl: 'proxy/proxy.html',
            controller: 'ProxyController',
            controllerAs: 'proxyCtrl'
        })
        .state('wizard', {
            url: '/wizard',
            templateUrl: 'wizard/wizard.html',
            controller: 'WizardController'
        });

        $urlRouterProvider.otherwise('/');
});
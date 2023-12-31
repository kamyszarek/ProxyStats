angular.module('myApp').controller('MainController', function($scope, $state) {
    var $this = this;
    $scope.currentTab = '';

    $scope.changeTab = function(tabName) {
        if (tabName === '') {
            tabName = 'homepage';
        }
        $scope.currentTab = tabName;
        console.log('Changing tab to:', tabName);
        $state.go(tabName);
    };

    $scope.changeTab($scope.currentTab);

});

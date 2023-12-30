angular.module('myApp').controller('DashboardController', function ($http, $scope, $interval) {
    var $this = this;
    var intervalPromise;
    let proxyChartOne = '';

    intervalPromise = $interval(updateChart, 1000);

    $this.stopInterval = function() {
        if (angular.isDefined(intervalPromise)) {
            $interval.cancel(intervalPromise);
            intervalPromise = undefined;
        }
    };

    $scope.$on('$destroy', function() {
        $this.stopInterval();
    });

    function fetchRandomNumber() {
        return $http.get('/api/proxy/random-number')
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                console.error('Error fetching random number:', error);
            });
    }

    var data = {
        labels: Array.from({ length: 21 }, (_, i) => i),
        datasets: [{
            label: 'SELECT',
            backgroundColor: 'rgba(75,192,192,0.4)',
            borderColor: 'rgba(75,192,192,1)',
            borderWidth: 3,
            data: Array.from({ length: 21 }, () => 0)
        },
        {
            label: 'INSERT',
            backgroundColor: 'rgba(255, 206, 86, 0.4)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 3,
            data: Array.from({ length: 21 }, () => 0)
        },
        {
            label: 'UPDATE',
            backgroundColor: 'rgba(54, 162, 235, 0.4)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 3,
            data: Array.from({ length: 21 }, () => 0)
        },
        {
            label: 'DELETE',
            backgroundColor: 'rgba(255, 99, 132, 0.4)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 3,
            data: Array.from({ length: 21 }, () => 0)
        }
        ]
    };

    var options = {
        scales: {
            x: {
                reverse: true,
                type: 'linear',
                position: 'bottom'
            },
            y: {
                beginAtZero: true,
                max: 10,
                stepSize: 1
            }
        },
        animation: {
            duration: 1
        }
    };

    var ctx = document.getElementById('randomNumberChart').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });

    function updateChart() {
        fetch('/api/proxy/queries-count')
            .then(response => response.json())
            .then(responseData => {
             var proxyName = proxyChartOne;
             var proxyData = responseData[proxyName];
                var selectCount = proxyData.select || 0;
                var insertCount = proxyData.insert || 0;
                var updateCount = proxyData.update || 0;
                var deleteCount = proxyData.delete || 0;

                if (myChart.data.datasets[0].data.length >= 21) {
                    for (var i = 20; i > 0; i--) {
                        myChart.data.datasets[0].data[i] = myChart.data.datasets[0].data[i - 1];
                        myChart.data.datasets[1].data[i] = myChart.data.datasets[1].data[i - 1];
                        myChart.data.datasets[2].data[i] = myChart.data.datasets[2].data[i - 1];
                        myChart.data.datasets[3].data[i] = myChart.data.datasets[3].data[i - 1];
                    }
                    myChart.data.datasets[0].data[0] = selectCount;
                    myChart.data.datasets[1].data[0] = insertCount;
                    myChart.data.datasets[2].data[0] = updateCount;
                    myChart.data.datasets[3].data[0] = deleteCount;
                } else {
                    myChart.data.datasets[0].data.push(selectCount);
                    myChart.data.datasets[1].data.push(insertCount);
                    myChart.data.datasets[2].data.push(updateCount);
                    myChart.data.datasets[3].data.push(deleteCount);
                }
                myChart.options.scales.y.max = Math.max(...data.datasets.map(dataset => Math.max(...dataset.data))) + 10;
                myChart.update();
            })
            .catch(error => console.error('Error fetching data:', error));
        }

    $this.fetchActiveProxiesList = function() {
       return $http({
           method: 'GET',
           url: '/api/proxy/active-proxies-list'
       })
       .then(function(response) {
           $this.activeProxiesNames = response.data;
           $this.selectedProxyName = $this.activeProxiesNames[0]
           proxyChartOne =  $this.selectedProxyName;

       })
       .catch(function(error) {
           console.error('Error fetching active proxies list:', error);
       });
    };

    $this.generateNewData = function() {
       return $http({
           method: 'GET',
           url: '/api/proxy/random-number'
       })
       .then(function(response) {

       })
       .catch(function(error) {
           console.error('Error fetching random number:', error);
       });
    };

    $this.getActiveProxiesList = function() {
       return $this.activeProxiesNames;
    };

    $this.changeProxyChartOne = function() {
       proxyChartOne = $this.selectedProxyName;
    };


    $this.fetchActiveProxiesList();

});

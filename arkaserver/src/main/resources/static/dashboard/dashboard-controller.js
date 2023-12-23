angular.module('myApp').controller('DashboardController', function ($http, $scope, $interval) {
    var vm = this;

//    vm.randomNumbers = [];
//    var chartInitialized = false;
//    var ctx = document.getElementById('randomNumberChart').getContext('2d');
//    var labels = generateTimeLabels();
//    var chart;
//
//    // Funkcja do odpytywania endpointu co sekundę
//    function fetchRandomNumber() {
//        $http.get('/api/proxy/random-number')
//            .then(function (response) {
//                vm.randomNumbers.push(response.data);
//                if (!chartInitialized) {
//                    initializeChart();
//                    chartInitialized = true;
//                } else {
//                    updateChart();
//                }
//            })
//            .catch(function (error) {
//                console.error('Error fetching random number:', error);
//            });
//    }
//
//    // Uruchomienie odpytywania co sekundę
//    var intervalPromise = $interval(fetchRandomNumber, 1000);
//
//    // Zatrzymaj odpytywanie, gdy kontroler zostanie zniszczony
//    $scope.$on('$destroy', function () {
//        $interval.cancel(intervalPromise);
//    });
//
//    // Funkcja do inicjalizacji wykresu
//    function initializeChart() {
//        chart = new Chart(ctx, {
//            type: 'line',
//            data: {
//                labels: labels,
//                datasets: [{
//                    label: 'Random Number',
//                    borderColor: 'blue',
//                    data: vm.randomNumbers,
//                    fill: false
//                }]
//            },
//            options: {
//                scales: {
//                    x: {
//                        type: 'linear',
//                        position: 'bottom',
//                        ticks: {
//                            callback: function (value, index, values) {
//                                if (index % 1 === 0) {
//                                    return moment().subtract(30, 'seconds').add(index, 'seconds').format('HH:mm:ss');
//                                }
//                                return '';
//                            }
//                        }
//                    },
//                    y: {
//                        type: 'linear',
//                        position: 'left'
//                    }
//                }
//            }
//        });
//    }
//
//    // Funkcja do aktualizacji wykresu
//    function updateChart() {
//        // Dodaj nową wartość do danych
//        chart.data.datasets[0].data = vm.randomNumbers;
//
//        // Aktualizuj etykiety czasu
//        chart.data.labels = labels;
//
//        // Przerysuj wykres
//        chart.update();
//    }
//
//    // Funkcja do generowania etykiet czasu z ostatnich 30 sekund
//    function generateTimeLabels() {
//        var labels = [];
//        for (var i = 30; i >= 0; i--) {
//            labels.push(moment().subtract(i, 'seconds').format('HH:mm:ss'));
//        }
//        return labels;
//    }


//    let numbers = [];
//
//  // Funkcja do generowania losowych liczb
//     function getRandomInt(min, max) {
//         return Math.floor(Math.random() * (max - min + 1)) + min;
//     }
//
//     function getNumbers() {
//        numbers = [];
//        for (var i = 0; i <= 20; i++) {
//            numbers.push(getRandomInt(1, 20));
//        }
//        return numbers;
//     }
//
//     // Dane do wykresu
//     var data = {
//         labels: [], // Oś X
//         datasets: [{
//             label: 'Random Numbers',
//             backgroundColor: 'rgba(75,192,192,0.4)',
//             borderColor: 'rgba(75,192,192,1)',
//             borderWidth: 1,
//             data: [] // Oś Y
//         }]
//     };
//
//     // Generowanie losowych danych
//     for (var i = 0; i <= 20; i++) {
//         data.labels.push(i);
//         data.datasets[0].data.push(getRandomInt(1, 20));
//     }
//
//     // Ustawienia wykresu
//     var options = {
//         scales: {
//             x: {
//                 type: 'linear',
//                 position: 'bottom'
//             }
//         }
//     };
//
//
//     $interval(updateChart, 1000)
//
//    // Funkcja do generowania nowych danych i aktualizacji wykresu
//   function updateChart() {
//
//         var existingChart = Chart.getChart("randomNumberChart");
//         if (existingChart) {
//             existingChart.destroy();
//         }
//
//        data.datasets[0].data = getNumbers();
////        for (var i = 0; i <= 20; i++) {
////            data.datasets[0].data.push(getRandomInt(1, 20));
////        }
//
//        var ctx = document.getElementById('randomNumberChart').getContext('2d');
//        myChart = new Chart(ctx, {
//            type: 'line',
//            data: data,
//            options: options
//        });
//    };


    function fetchRandomNumber() {
        return $http.get('/api/proxy/random-number')
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                console.error('Error fetching random number:', error);
            });
    }

    // Dane do wykresu
    var data = {
        labels: Array.from({ length: 20 }, (_, i) => i + 1), // Oś X (stałe 20 ostatnich indeksów)
        datasets: [{
            label: 'Random Numbers',
            backgroundColor: 'rgba(75,192,192,0.4)',
            borderColor: 'rgba(75,192,192,1)',
            borderWidth: 3,
            data: Array.from({ length: 20 }, () => 0) // Początkowo 20 elementów równych zero
        },
        {
            label: 'Random Numbers 2',
            backgroundColor: 'rgba(255, 99, 132, 0.4)', // Kolor dla drugiej linii
            borderColor: 'rgba(255, 99, 132, 1)', // Kolor dla drugiej linii
            borderWidth: 3,
            data: Array.from({ length: 20 }, () => 2) // Początkowo 20 elementów równych 2 (o 2 więcej niż pierwsza linia)
        }
        ]
    };

    // Ustawienia wykresu
    var options = {
        scales: {
            x: {
                type: 'linear',
                position: 'bottom'
            },
            y: {
                beginAtZero: true, // Początek skali od zera
                max: 20, // Maksymalna wartość na osi Y
                stepSize: 1 // Krok skali
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

    setInterval(updateChart, 1000);

//    function updateChart() {
//        fetchRandomNumber().then(function (newNumber) {
//            if (data.datasets[0].data.length >= 20) {
//                for (var i = 0; i < 19; i++) {
//                    data.datasets[0].data[i] = data.datasets[0].data[i + 1];
//                    data.datasets[1].data[i] = data.datasets[1].data[i + 1];
//                }
//                data.datasets[0].data[19] = newNumber;
//                data.datasets[1].data[19] = newNumber + 2;
//            } else {
//                data.datasets[0].data.push(newNumber);
//                data.datasets[1].data.push(newNumber + 2);
//            }
//
//            myChart.update();
//        });
//    }

    function updateChart() {
        fetch('/api/proxy/queries-count') // Assuming your endpoint is '/remote-queries-count'
            .then(response => response.json())
            .then(data => {
                var selectCount = data.select || 0; // Assuming the key in the response is 'select'
                var insertCount = data.insert || 0; // Assuming the key in the response is 'insert'

                if (myChart.data.datasets[0].data.length >= 20) {
                    for (var i = 0; i < 19; i++) {
                        myChart.data.datasets[0].data[i] = myChart.data.datasets[0].data[i + 1];
                        myChart.data.datasets[1].data[i] = myChart.data.datasets[1].data[i + 1];
                    }
                    myChart.data.datasets[0].data[19] = selectCount;
                    myChart.data.datasets[1].data[19] = insertCount;
                } else {
                    myChart.data.datasets[0].data.push(selectCount);
                    myChart.data.datasets[1].data.push(insertCount);
                }

                myChart.update();
            })
            .catch(error => console.error('Error fetching data:', error));
        }



});
